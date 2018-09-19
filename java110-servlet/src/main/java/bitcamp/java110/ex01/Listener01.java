// 리스너 만들기
// - 리스너 (Listener) ? 특정 상황에 놓이면 실행되는 객체
// - javax.servlet.XxxListener


package bitcamp.java110.ex01;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// 톰캣 서버가 실행되거나 종료되는 상황일때 실행되는 리스너 만들기
// 리스너를 만들었으면 Servlet Container에 등록해야 하낟.

@WebListener
public class Listener01 implements ServletContextListener{
    
    public Listener01() {
        System.out.println("Listener01 생성자 호출");
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Web Application이 시작될때 이 매서드가 호출된다.
        // 이 Web Application에 소속된 서블릿들이 공통으로 사용하는 자원은 이 매스더에서 준비한다.
        // 예를 들면 IoC 컨테이너, DAO, DB Connection 등
        
        System.out.println("Listener01 - contextInitialized() 호출");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Web Application이 종료될때 이 매서드가 호출된다.
        // contextInitialized() 에서 준비했던 자원을 해제시킨다.
        
        System.out.println("Listener01 - contextDestroyed() 호출");
    }
}
