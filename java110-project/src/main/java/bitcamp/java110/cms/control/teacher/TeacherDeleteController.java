package bitcamp.java110.cms.control.teacher;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.TeacherDao;

@Component
public class TeacherDeleteController{

 
    TeacherDao teacherDao;
    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
    
    @RequestMapping("teacher/delete")
    public void delete(Scanner keyIn) {
        System.out.print("삭제할 강사의 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());

        if (teacherDao.deleteByNo(no) > 0 ) {
            System.out.println("삭제 되었습니다.");
        }else {
            System.out.println("번호에 해당하는 강사정보가 없습니다.");
        }
    }





}
