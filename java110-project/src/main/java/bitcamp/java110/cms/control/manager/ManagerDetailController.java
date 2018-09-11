package bitcamp.java110.cms.control.manager;

import java.util.Scanner;

import bitcamp.java110.cms.annotation.Autowired;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.ManagerDao;
import bitcamp.java110.cms.domain.Manager;

@Component
public class ManagerDetailController {
    
    ManagerDao managerDao;
    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }
    
    
    @RequestMapping("manager/detail") 
    public void detail(Scanner keyIn) {
        
        System.out.print("조회할 학생의 이메일 : ");
        String email = keyIn.nextLine();

        Manager manager = managerDao.findByEmail(email);

        if(manager == null) {
            System.out.println("해당 이메일이 학생정보가 존재하지 않습니다.");
            return;
        } 

        System.out.printf("이름 : %s\n", manager.getName());
        System.out.printf("이메일 : %s\n", manager.getEmail());
        System.out.printf("패스워드 : %s\n", manager.getPassword());
        System.out.printf("전화 : %s\n", manager.getTel());
        System.out.printf("직위 : %s\n", manager.getPosition());

    }
    
}
