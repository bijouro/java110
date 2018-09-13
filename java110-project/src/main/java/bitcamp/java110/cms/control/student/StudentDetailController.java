package bitcamp.java110.cms.control.student;

import java.util.Scanner;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.StudentDao;
import bitcamp.java110.cms.domain.Student;

@Component
public class StudentDetailController {

    StudentDao studentDao;
    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
     
    @RequestMapping("student/detail") 
    public void detail(Scanner keyIn) {
        System.out.print("조회할 학생 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());

        Student student = studentDao.findByNo(no);

        if(student == null) {
            System.out.println("해당 이메일이 학생정보가 존재하지 않습니다.");
            return;
        }
        System.out.printf("번호 : %d\n", student.getNo());
        System.out.printf("이름 : %s\n", student.getName());
        System.out.printf("이메일 : %s\n", student.getEmail());
        System.out.printf("패스워드 : %s\n", student.getPassword());
        System.out.printf("최종학력 : %s\n", student.getSchool());
        System.out.printf("전화 : %s\n", student.getTel());
        System.out.printf("재직여부 : %b\n", student.isWorking());

    }

}
