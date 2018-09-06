import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import bitcamp.java110.cms.control.ManagerController;
import bitcamp.java110.cms.control.StudentController;
import bitcamp.java110.cms.control.TeacherController;
import bitcamp.java110.cms.domain.Manager;
import bitcamp.java110.cms.domain.Student;
import bitcamp.java110.cms.domain.Teacher;

public class App {
     
   
    static Scanner keyIn = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        StudentController sc = new StudentController(keyIn, new LinkedList<Student>());
        TeacherController tc = new TeacherController(keyIn, new ArrayList<Teacher>());
        ManagerController mc = new ManagerController(keyIn, new ArrayList<Manager>());
        sc.keyIn = keyIn;
        tc.keyIn = keyIn;
        mc.keyIn = keyIn;
        
        while(true) {
            String menu = promptMenu();

            if ( menu.equals("1") ) {
                sc.serviceStudentMenu(); 
            }else if(menu.equals("2")) { 
                tc.serviceTeacherMenu();
            }else if(menu.equals("3"))
                mc.serviceManagerMenu();
            else if(menu.equals("0")) {
                System.out.println("안녕히 가세요!!!!");
                break;
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
