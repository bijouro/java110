package bitcamp.java110.cms;

import java.lang.reflect.Method;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.context.ApplicationContext;
import bitcamp.java110.cms.context.RequestMappingHandlerMapping;
import bitcamp.java110.cms.context.RequestMappingHandlerMapping.RequestMappingHandler;

public class App {

    static Scanner keyIn = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        ApplicationContext iocContainer = new ApplicationContext("bitcamp.java110.cms");

        RequestMappingHandlerMapping requestHandlerMap = new RequestMappingHandlerMapping();
        // Ioc 컨테이너에 보관된 객체의 이름 목록을 가져온다.

        String[] names = iocContainer.getBeanDefinitionNames();
        for (String name : names) {
            Object obj = iocContainer.getBean(name);
            // 객체에서 @RequestMapping이 붙은 매서드를 찾아 저장한다.
            requestHandlerMap.addMapping(obj);
        }

        while (true) {
            String menu = prompt();

            if (menu.equals("exit")) {
                System.out.println("안녕히 가세요!");
                break;
            }
            RequestMappingHandler mapping = requestHandlerMap.getMapping(menu);
            if (mapping == null) {
                System.out.println("해당 메뉴가 없습니다.");
                continue;
            }
            try {
                mapping.getMethod().invoke(mapping.getInstance(), keyIn);    
            }catch(Exception e) {
                System.out.println("실행 오류");
                System.out.println(e.getCause()); 

            }
            
        }
        keyIn.close();
    }

    @SuppressWarnings("unused")
    private static Method findRequestMapping(Class<?> clazz) {

        // 클래스의 메서드 목록을 꺼낸다 .

        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {

            // 매서드에서 @RequestMapping 정보를 추출한다.
            RequestMapping anno = m.getAnnotation(RequestMapping.class);
            if (anno != null) {
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
