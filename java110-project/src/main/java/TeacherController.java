import java.util.Scanner;

public class TeacherController {

    static Teacher[] teachers = new Teacher[100];
    static int teacherIndex = 0;
    static Scanner keyIn;
    
    static class Teacher extends Member{
        
        protected String tel;
        protected int pay;
        protected String subjects;
        
        public String getTel() {
            return tel;
        }
        public void setTel(String tel) {
            this.tel = tel;
        }
        public int getPay() {
            return pay;
        }
        public void setPay(int pay) {
            this.pay = pay;
        }
        public String getSubjects() {
            return subjects;
        }
        public void setSubjects(String subjects) {
            this.subjects = subjects;
        }

        
        
        
    }
    
    static void serviceTeacherMenu() {
        while(true) {     
            System.out.print("강사관리 > ");
            String command = keyIn.nextLine();
            if(command.toLowerCase().equals("list")) {
                printTeachers();
            }else if(command.toLowerCase().equals("add")){
                inputTeachers();
            }else if(command.toLowerCase().equals("quit")) {
                break;
            }                    
            else {
                System.out.println("유호하지 않은 명령입니다.");
            }
        }
    }
    
    static void printTeachers() {
        //for(int i = 0 ; i < index ; i++) {
        int count = 0;
        for(Teacher s : teachers) {
            if(count++ == teacherIndex)
                break;
            System.out.printf("%s, %s, %s, %s, %d, [%s] \n", 
                    s.getName(), 
                    s.getEmail(), 
                    s.getPassword(),
                    s.getTel(),
                    s.getPay(),
                    s.getSubjects()                    
                    );            
        }
    }
    

    
    static void inputTeachers() {
        while(true) {
            Teacher m = new Teacher();
            
            System.out.print("이름: ");
            m.setName(keyIn.nextLine());
            
            System.out.print("이메일: ");
            m.setEmail(keyIn.nextLine());
            
            System.out.print("패스워드: ");
            m.setPassword(keyIn.nextLine());
            
            System.out.print("전화번호: ");
            m.setTel(keyIn.nextLine());
            
            System.out.print("시급 : ");
            m.setPay(Integer.parseInt(keyIn.nextLine()));
            
            System.out.print("강의과목(예: 자바, C, C++) : ");
            m.setSubjects(keyIn.nextLine());
            
            teachers[teacherIndex++] = m;
            
                       
            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    }
}
