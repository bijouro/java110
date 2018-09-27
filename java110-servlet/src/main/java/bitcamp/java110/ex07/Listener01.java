package bitcamp.java110.ex07;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

public class Listener01 implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Web Application 이 시작될 때 호출된다.  
        System.out.println("ex07.Listener01.contextInitialized() 호출 됨!");
        
        // ServletContext 보관소를 알아 낸다.
        ServletContext sc = sce.getServletContext();
        
        sc.setAttribute("aaa", "홍길동");
        
    }
     
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Web Aplicaiton이 종료될 때 호출된다.
        System.out.println("ex07.Listener01.contextDestoryed() 호출됨!!");
        
        
    }
    
}
