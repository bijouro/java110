package bitcamp.java110.ex13;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener01 implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("inventory", new Inventory());
        sce.getServletContext().setAttribute("inventory2", new Inventory2());
    }

}
