package bitcamp.java110.cms.control.manager;

import java.util.Scanner;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.ManagerDao;

@Component
public class ManagerDeleteController {
    
    ManagerDao managerDao;
    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }
    
    
    @RequestMapping("manager/delete") 
    public void delete(Scanner keyIn) {

        System.out.print("삭제할 학생의 이메일 : ");
        String email = keyIn.nextLine();

        if (managerDao.delete(email) > 0 ) {
            System.out.println("삭제 되었습니다.");
        }else {
            System.out.println("이메일에 해당하는 학생정보가 없습니다.");
        }

    }
    
}
