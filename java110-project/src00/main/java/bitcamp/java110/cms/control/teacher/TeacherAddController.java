package bitcamp.java110.cms.control.teacher;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.TeacherDao;
import bitcamp.java110.cms.domain.Teacher;

@Component
public class TeacherAddController{

    TeacherDao teacherDao;
    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
 
    @RequestMapping("teacher/add")
    public void add(Scanner keyIn) {
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

            teacherDao.insert(m);                      

            System.out.print("계속 하시겠습니까? (Y/n) ");
            String answer = keyIn.nextLine();
            if(answer.toLowerCase().equals("n"))
                break;
        }
    }




}
