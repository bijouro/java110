package bitcamp.java110.cms.control;
import java.util.Scanner;
import bitcamp.java110.cms.domain.Member;


public class ManagerController {

    static Manager[] managers = new Manager[100];
    static int managerIndex = 0;
    
    public static Scanner keyIn;
    
    static class Manager extends Member{
        
        protected String tel;
        protected String position;
        public String getTel() {
            return tel;
        }
        public void setTel(String tel) {
            this.tel = tel;
        }
        public String getPosition() {
            return position;
        }
        public void setPosition(String position) {
            this.position = position;
        }
    }
    
    public static void serviceManagerMenu() {
        while(true) {     
            System.out.print(" 매니저 관리 > ");
            String command = keyIn.nextLine();
            if(command.toLowerCase().equals("list")) {
                printManagers();
            }else if(command.toLowerCase().equals("add")){
                inputManagers();
            }else if(command.toLowerCase().equals("quit")) {
                break;
            }                    
            else {
                System.out.println("유호하지 않은 명령입니다.");
            }
        }
    }
    
    private static void printManagers() {
        //for(int i = 0 ; i < index ; i++) {
        int count = 0;
        for(Manager s : managers) {
            if(count++ == managerIndex)
                break;
            System.out.printf("%s, %s, %s, %s,%s \n", 
                    s.getName(), 
                    s.getEmail(), 
                    s.getPassword(),
                    s.getTel(),
                    s.getPosition()                    
                    );            
        }
    }
    

    
    private static void inputManagers() {
        while(true) {
            Manager m = new Manager();
            
            System.out.print("이름: ");
            m.setName(keyIn.nextLine());
            
            System.out.print("이메일: ");
            m.setEmail(keyIn.nextLine());
            
            System.out.print("패스워드: ");
            m.setPassword(keyIn.nextLine());
            
            System.out.print("전화번호: ");
            m.setTel(keyIn.nextLine());
            
            System.out.print("직위 : ");
            m.setPosition(keyIn.nextLine());   
            
            
            managers[managerIndex++] = m;
            
                       
            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    }
}
