import java.util.Scanner;

import bitcamp.java110.cms.context.ApplicationContext;
import bitcamp.java110.cms.control.Controller;

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
            String menu = promptMenu();
            
            if(menu.equals("0")) {
                System.out.println("안녕히 가세요!");
                break;
            }
            
            Controller controller = (Controller)iocContainer.getBean(menu);
            if(controller != null) {
                controller.service(keyIn);
            }else {
                System.out.println("해당 메뉴가 없습니다.");
            }

        }
        
        keyIn.close();
    }    
 
    private static String promptMenu() {
        // 사용자로부터 메뉴를 입력받기
        System.out.println("[메뉴]");
        System.out.println("1. 학생관리");
        System.out.println("2. 강사관리");
        System.out.println("3. 매니저 관리");
        System.out.println("0. 종료");
        
        while(true) {
            System.out.print("메뉴 번호 > ");
            String menu = keyIn.nextLine();
            
            switch (menu){
            case "0":
            case "1":
            case "2":
            case "3":
                return menu;
            default:
                System.out.println("메뉴 번호가 유호하지 않습니다.");
            }
        }
        
    }
   
}
