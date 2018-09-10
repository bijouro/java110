package bitcamp.java110.cms;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.context.ApplicationContext;
import bitcamp.java110.cms.context.RequestMappingHandlerMapping;
import bitcamp.java110.cms.context.RequestMappingHandlerMapping.RequestMappingHandler;
import bitcamp.java110.cms.dao.StudentDao;
import bitcamp.java110.cms.domain.Manager;
import bitcamp.java110.cms.domain.Teacher;

public class App {

    public static StudentDao studentDao = new StudentDao();
    //public static ManagerDao managerDao = new ManagerDao();
    //public static TeacherDao teacherDao = new TeacherDao();
    public static ArrayList<Manager> managers = new ArrayList<>();
    public static ArrayList<Teacher> teachers = new ArrayList<>();
    
    static Scanner keyIn = new Scanner(System.in);

    public static void main(String[] args) throws Exception{

        ApplicationContext iocContainer = 
                new ApplicationContext("bitcamp.java110.cms.control");

 
        RequestMappingHandlerMapping requestHandlerMap = new RequestMappingHandlerMapping();
        // Ioc 컨테이너에 보관된 객체의 이름 목록을 가져온다.    


        String[] names = iocContainer.getBeanDefinitionNames();
        for(String name : names) {
            Object obj = iocContainer.getBean(name);            
            // 객체에서 @RequestMapping이 붙은 매서드를 찾아 저장한다.
            requestHandlerMap.addMapping(obj);            
        }
 
        while(true) {
            String menu = prompt();

            if(menu.equals("exit")) {
                System.out.println("안녕히 가세요!");
                break;
            }
            RequestMappingHandler mapping = requestHandlerMap.getMapping(menu);
            if ( mapping == null) {
                System.out.println("해당 메뉴가 없습니다.");
                continue;
            }
            mapping.getMethod().invoke(mapping.getInstance(), keyIn);
        }
        keyIn.close();
    }    

    private static Method findRequestMapping(Class<?> clazz) {

        // 클래스의 메서드 목록을 꺼낸다.

        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods) {

            // 매서드에서 @RequestMapping 정보를 추출한다.
            RequestMapping anno = m.getAnnotation(RequestMapping.class);
            if (anno != null){
                // 메서드를 찾으면 리턴한다.
                return m;
            }
        }

        return null;
    }

    private static String prompt() {
        // 사용자로부터 메뉴를 입력받기

        System.out.print("메뉴 > ");
        return keyIn.nextLine();

    }
}
