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

            if(App.studentDao.insert(m) > 0) {
                System.out.println("저장하였습니다.");
            }else{
                System.out.println("같은 이메일의 학생이 존재합니다.");
            };

            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    } 

    {  // 인스턴스 블럭 생성자 보다 먼저 실행      
        Student s = new Student();
        s.setName("A");
        s.setEmail("a@test.com");
        App.studentDao.insert(s);

        s = new Student();
        s.setName("B");
        s.setEmail("B@test.com");
        App.studentDao.insert(s);

        s = new Student();
        s.setName("C");
        s.setEmail("C@test.com");
        App.studentDao.insert(s);

        s = new Student();
        s.setName("D");
        s.setEmail("D@test.com");
        App.studentDao.insert(s);

        s = new Student();
        s.setName("E");
        s.setEmail("E@test.com");
        App.studentDao.insert(s);        
    }


}
