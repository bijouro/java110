// 필터 만들기
// javax.servlet.Filter 인터페이스 구현
//

package bitcamp.java110.ex01;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

// 필터를 만들었으면 어떤 요청에 대해 실행할 것인지 등록해야 한다.
// URL은 반드시 "/" 로 시작해야 한다.

//@WebFilter("/ex01/*")
public class Filter02 implements Filter{

    public Filter02() {
        System.out.println("Filter02 생성자 호출 됨");
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터가 생성된 후 생성자 호출 후 이 매서드가 실행된다.
        // 필터가 작업하는 동안 사용할 자원을 준비한다.
        System.out.println("Filter02 - init()  호출 됨");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 클라이언트가 요청한 서블릿을 실행하기 전에 
        // 해당 URL에 등록한 필터를 실행한다.
        // 이때 이 매서드가 호출된다.
        // 서블릿을 수행하기 전,후에 수행해야 할 작업이 있다면 이 매서드에서 실행한다.
        
        // 서블릿을 실행하기 전에 하는일 : 
        //   클라이언트가 암호화해서 보낸 데이터를 Servlet이 사용할수 있도록암호 해제하기
        //   클라이언트가 압축해서 보낸 데이터를 Servlet이 사용할수 있도록 압축 해제
        //   클라이언트의 요청에 대해 기록을 남기기
        //   클라이언트가 특별한 형식으로 보낸 데이터를 Parsing하여 Servlet이 사용하기 쉽게 변환
        //   클라이언트가 요청한 Servlet의 실행권한이 있는지 체크한다.
        //   클라이언트가 로그인한 사용자인지 체크한다.
        System.out.println("Filter02 - doFilter() -- before");
        
        //   다음 필터가 있다면 실행하고 없으면 Servlet을 호출하게 한다.
        chain.doFilter(request, response);
        // 위 매서드를 호출하지 않으면 다음 필터나 Servlet을 호출하지 않는다.. 필수!!!!
        // 최종적으로 Servlet의 service() 호출이 끝나면 위 매서드는 리턴된다.
        
        // 서블릿을 실행한 후에 하는일 :
        //   클라이언트로 보낼 데이터를 압축하기
        //   클라이언트로 보낼 데이터를 암호화하기
        //   클라이언트로 보낼 데이터를 특정 형식으로 변환하기
        System.out.println("Filter02 - doFilter() -- after");
    }

    
    @Override
    public void destroy() {
        
        // Tomcat server가 종료 되거나 Web Application이 멈출 때
        // 생성된 모든 필터에 대해 이 매서드를 호출한다. From Servlet Container
        // init()에서 준비된 자원을 해제 시킨다.
        System.out.println("Filter02 - destory()  호출 됨");
        Filter.super.destroy();
    }
}


// 필터가 구동되는 절차
//   - Tomcat Server를 실행하거나 Web Application을 시작하면 필터 객체를 생성한다.
//      - 생성자 호출 -> init() 호출
//   - 클라이언트 요청이 들어 오면 ,
//      - 필터에 해당하는 URL이라면, doFilter() 호출
//      - 필터에 해당하지 않는 URL이라면 이 필터는 동작되지 않는다.
//   - Tomcat Server를 종료하거나 Web Application을 멈추면
//      - destory() 호출




