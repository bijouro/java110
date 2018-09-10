package bitcamp.java110.cms.control;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.domain.Teacher;

@Component("teacher")
public class TeacherController{

    //public String name = "2";
    private List<Teacher> teachers = new ArrayList<>();

    @RequestMapping
    public void teacher(Scanner keyIn) {
        while(true) {     
            System.out.print("강사관리 > ");
            String command = keyIn.nextLine();
            if(command.toLowerCase().equals("list")) {
                printTeachers();
            }else if(command.toLowerCase().equals("add")){
                inputTeachers(keyIn);
            }else if(command.toLowerCase().equals("delete")){
                deleteTeacher(keyIn);
            }else if(command.toLowerCase().equals("detail")){
                detailTeacher(keyIn);
            }else if(command.toLowerCase().equals("quit")) {
                break;
            }                    
            else {
                System.out.println("유호하지 않은 명령입니다.");
            }
        }
    }

    private void printTeachers() {

        for(int i = 0 ; i < teachers.size() ; i++) {
            Teacher s = teachers.get(i);
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



    private void inputTeachers(Scanner keyIn) {
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

            teachers.add(m);            

            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    }



    private void deleteTeacher(Scanner keyIn) {
        System.out.print("삭제할 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());

        if( no < 0 || no >= teachers.size()) {
            System.out.println("존재하지 않는 번호입니다.");
            return;
        }

        teachers.remove(no);

        System.out.println("삭제 되었습니다.");
    }

    private void detailTeacher(Scanner keyIn) {
        System.out.print("조회할 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());

        if( no < 0 || no >= teachers.size()) {
            System.out.println("존재하지 않는 번호입니다.");
            return;
        }
        Teacher teacher = teachers.get(no);

        System.out.printf("이름 : %s\n", teacher.getName());
        System.out.printf("이메일 : %s\n", teacher.getEmail());
        System.out.printf("패스워드 : %s\n", teacher.getPassword());
        System.out.printf("전화 : %s\n", teacher.getTel());
        System.out.printf("시급 : %d\n", teacher.getPay());
        System.out.printf("강의과목 : %s\n", teacher.getSubjects());

    }


}
