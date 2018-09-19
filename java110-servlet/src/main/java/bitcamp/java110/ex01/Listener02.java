// 리스너 만들기
// - 리스너 (Listener) ? 특정 상황에 놓이면 실행되는 객체
// - javax.servlet.XxxListener


package bitcamp.java110.ex01;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

// 클라이언트로 부터 요청이 들어오면 실행되는 리스너

//@WebListener
public class Listener02 implements ServletRequestListener{

    public Listener02() {
        System.out.println("Listener02 생성자 호출");
    } 
    
    @Override
    public void requestInitialized(ServletRequestEvent sre) {

        // 클라이언트 요청이 들어왔을때 호출된다.  
        // 예를들면, 클라이언트의 IP Address를 기록할 수 있다.
        System.out.println("Listener02 - requestInitialized() 호출");
        HttpServletRequest httpRequest = 
                (HttpServletRequest)sre.getServletRequest();
        System.out.printf("IP : %s\n", httpRequest.getRemoteAddr());
        System.out.printf("PATH : %s\n", httpRequest.getServletPath());
        
        
    }
    
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        
        // 클라이언트에게 응답을 완료 했을때 호출된다.
        System.out.println("Listener02 - requestDestroyed() 호출");
        
    }
}
