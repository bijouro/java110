import java.lang.reflect.Method;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.context.ApplicationContext;

public class App {


    static Scanner keyIn = new Scanner(System.in);

    public static void main(String[] args) throws Exception{

        /*   HashMap<String, Controller> requestHandlerMapping = new HashMap<>();

        requestHandlerMapping.put("1", 
                new StudentController(new LinkedList<Student>()));
        requestHandlerMapping.put("2", 
                new TeacherController(new ArrayList<Teacher>()));
        requestHandlerMapping.put("3", 
                new ManagerController(new ArrayList<Manager>()));*/

        ApplicationContext iocContainer = 
                new ApplicationContext("bitcamp.java110.cms.control");


        while(true) {
            String menu = prompt();

            if(menu.equals("exit")) {
                System.out.println("안녕히 가세요!");
                break;
            }

            Object controller = iocContainer.getBean(menu);
            if ( controller == null) {
                System.out.println("해당 메뉴가 없습니다.");
                continue;
            }
            
            Method method = findRequestMapping(controller.getClass());
            
            if(method == null) {
                System.out.println("해당 메뉴가 없습니다.");
                continue;
            }
            
            method.invoke(controller, keyIn);
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
/*        while(true) {
            System.out.print("메뉴 번호 > ");
            String menu = keyIn.nextLine();

            switch (menu){
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                
            default:
                System.out.println("메뉴 번호가 유호하지 않습니다.");
            }
        }*/

    }

}
