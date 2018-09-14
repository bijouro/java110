package bitcamp.java110.cms.control.teacher;
import java.util.List;
import java.util.Scanner;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.TeacherDao;
import bitcamp.java110.cms.domain.Teacher;

@Component
public class TeacherListController{

    TeacherDao teacherDao;
    @Autowired
    public void setTeacherDao(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
    
 
    @RequestMapping("teacher/list")
    public void list(Scanner keyIn) {


        List<Teacher> list = teacherDao.findAll();
        
        for(Teacher s : list) {
            System.out.printf("%d, %s, %s, %s, %s, %d, [%s] \n",
                    s.getNo(),
                    s.getName(), 
                    s.getEmail(), 
                    s.getPassword(),
                    s.getTel(),
                    s.getPay(),
                    s.getSubjects()                    
                    );            
        }
    }
}
