package bitcamp.java110.cms.control.manager;

import java.util.Scanner;

import bitcamp.java110.cms.App;
import bitcamp.java110.cms.annotation.Component;
import bitcamp.java110.cms.annotation.RequestMapping;

@Component
public class ManagerDeleteController {
    @RequestMapping("manager/delete") 
    public void delete(Scanner keyIn) {
        System.out.print("삭제할 번호 : ");
        int no = Integer.parseInt(keyIn.nextLine());

        if( no < 0 || no >= App.managers.size()) {
            System.out.println("존재하지 않는 번호입니다.");
            return;
        }

        App.managers.remove(no);

        System.out.println("삭제 되었습니다.");
    }
    
}
