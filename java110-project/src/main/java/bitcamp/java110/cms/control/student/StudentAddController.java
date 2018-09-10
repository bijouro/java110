package bitcamp.java110.cms.control.student;

import java.util.List;
import java.util.Scanner;

import bitcamp.java110.cms.App;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.domain.Student;

@Component
public class StudentAddController {


    @RequestMapping("student/add") 
    public void add(Scanner keyIn) {
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

            App.students.add(m);

            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    } 

    {  // 인스턴스 블럭 생성자 보다 먼저 실행      
        Student s = new Student();
        s.setName("A");
        App.students.add(s);

        s = new Student();
        s.setName("B");
        App.students.add(s);

        s = new Student();
        s.setName("C");
        App.students.add(s);

        s = new Student();
        s.setName("D");
        App.students.add(s);

        s = new Student();
        s.setName("E");
        App.students.add(s);        
    }


}
