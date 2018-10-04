package bitcamp.java110.cms.control;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import bitcamp.java110.cms.annotation.RequestMapping;
import bitcamp.java110.cms.dao.ManagerDao;
import bitcamp.java110.cms.domain.Manager;

public class ManagerController { 

    ManagerDao managerDao;

    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @RequestMapping("manager/add")
    public void add(ServletRequest request, ServletResponse response) 
            throws Exception{
        Manager m = new Manager();
        
        m.setName(request.getParameter("name"));
        m.setEmail(request.getParameter("email"));
        m.setPassword(request.getParameter("password"));
        m.setTel(request.getParameter("tel"));
        m.setPosition(request.getParameter("position"));
        
        managerDao.insert(m);
        PrintWriter out = response.getWriter();
        out.println("?“±ë¡í•˜???Šµ?‹ˆ?‹¤.");
        
    }

    @RequestMapping("manager/delete")
    public void delete(ServletRequest request, ServletResponse response) 
            throws Exception{
        int no = Integer.parseInt(request.getParameter("no"));


        PrintWriter out = response.getWriter();
        
        if (managerDao.delete(no) > 0) {
            out.println("?‚­? œ?•˜???Šµ?‹ˆ?‹¤.");
        } else {
            out.println("?•´?‹¹ ë²ˆí˜¸?˜ ë§¤ë‹ˆ??ê°? ?—†?Šµ?‹ˆ?‹¤!");
        }
    }
    
    @RequestMapping("manager/detail")
    public void detail(ServletRequest request, ServletResponse response) 
            throws Exception{
        
        int no = Integer.parseInt(request.getParameter("no"));
        Manager m = managerDao.findByNo(no);
        
        PrintWriter out = response.getWriter();
        
        if (m == null) {
            System.out.println("?•´?‹¹ ë²ˆí˜¸?˜ ë§¤ë‹ˆ??ê°? ?—†?Šµ?‹ˆ?‹¤!");
            return;
        }
        
        out.printf("?´ë¦?: %s\n", m.getName());
        out.printf("?´ë©”ì¼: %s\n", m.getEmail());
        out.printf("?•”?˜¸: %s\n", m.getPassword());
        out.printf("ì§ìœ„: %s\n", m.getPosition());
        out.printf("? „?™”: %s\n", m.getTel());
    }
    
    @RequestMapping("manager/list")
    public void list(ServletRequest request, ServletResponse response) 
            throws Exception{
        PrintWriter out = response.getWriter();
        List<Manager> list = managerDao.findAll();
        for (Manager s : list) {
                out.printf("%d, %s, %s, %s\n",
                    s.getNo(),
                    s.getName(), 
                    s.getEmail(), 
                    s.getPosition());
        }
    }
}