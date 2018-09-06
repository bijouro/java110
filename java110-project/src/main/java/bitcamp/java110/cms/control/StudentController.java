package bitcamp.java110.cms.control;
import java.util.Scanner;

import bitcamp.java110.cms.domain.Student;
import bitcamp.java110.cms.util.LinkedList;

public class StudentController {
    
    //private  ArrayList<Student> students = new ArrayList<>();    
    private  LinkedList<Student> students = new LinkedList<>();
    public  Scanner keyIn;
    
    public StudentController(Scanner keyIn) {
        this.keyIn = keyIn;
    }
    
    public void serviceStudentMenu() {
        while(true) {     
            System.out.print("학생관리 > ");
            String command = keyIn.nextLine();
            if(command.toLowerCase().equals("list")) {
                printStudents();
            }else if(command.toLowerCase().equals("add")){
                inputStudents();
            }else if(command.toLowerCase().equals("delete")){
                deleteStudent();
            }else if(command.toLowerCase().equals("detail")){
                detailStudent();
            }else if(command.toLowerCase().equals("quit")) {
                break;
            }                    
            else {
                System.out.println("유호하지 않은 명령입니다.");
            }
        }
    }

     
    private void printStudents() {

        for(int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%d : %s, %s, %s, %s, %b, %s \n",
                    i,
                    s.getName(), 
                    s.getEmail(), 
                    s.getPassword(),
                    s.getSchool(),
                    s.isWorking(),
                    s.getTel()
                    );
        }
    }
    
    private void inputStudents() {
        while(true) {
            Student m = new Student();
            
            System.out.print("이름: ");
            m.setName(keyIn.nextLine());
            
            System.out.print("이메일: ");
            m.setEmail(keyIn.nextLine());
            
            System.out.print("패스워드: ");
            m.setPassword(keyIn.nextLine());
            
            System.out.print("최종학력: ");
            m.setSchool(keyIn.nextLine());
            
            System.out.print("재직여부(true/false) : ");
            m.setWorking(Boolean.parseBoolean(keyIn.nextLine()));
            
            System.out.print("전화 : ");
            m.setTel(keyIn.nextLine());
            
            students.add(m);
                       
            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    } 


    private void deleteStudent() {
        System.out.print("삭제할 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());
        
        if( no < 0 || no >= students.size()) {
            System.out.println("존재하지 않는 번호입니다.");
            return;
        }
        students.remove(no);
        
        System.out.println("삭제 되었습니다.");
    }
    
    private void detailStudent() {
        System.out.print("조회할 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());
        
        if( no < 0 || no >= students.size()) {
            System.out.println("존재하지 않는 번호입니다.");
            return;
        }
        
        Student student = students.get(no);
        
        
        System.out.printf("이름 : %s\n", student.getName());
        System.out.printf("이메일 : %s\n", student.getEmail());
        System.out.printf("패스워드 : %s\n", student.getPassword());
        System.out.printf("최종학력 : %s\n", student.getSchool());
        System.out.printf("전화 : %s\n", student.getTel());
        System.out.printf("재직여부 : %b\n", student.isWorking());
        
    }
    
    {  // 인스턴스 블럭 생성자 보다 먼저 실행      
        Student s = new Student();
        s.setName("A");
        students.add(s);
        
        s = new Student();
        s.setName("B");
        students.add(s);
        
        s = new Student();
        s.setName("C");
        students.add(s);
        
        s = new Student();
        s.setName("D");
        students.add(s);
        
        s = new Student();
        s.setName("E");
        students.add(s);        
    }
}
