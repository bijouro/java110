/* Servlet 만들기
 *  - javax.servlet.Servlet 인터페이스 구현
 */
package bitcamp.java110.ex02;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

// Servlet을 만들었으면 Servlet 컨테이너에 등록한다.
// Servlet을 등록할때 URL을 지정해야 클라이언트가 호출할 수 있다.
// URL은 '/' 로 시작해야 한다.
// 클라이언트는 다음과 같이 요청해야 한다.
//  1) Web application 경로를 root(/) 로 설정했을 경우
//      http://localhost:8888/ex01/servlet01
//  2) Web application 경로를 web01로 설정했을 경우
//      http://localhost:8888/web01/ex01/servlet01
//  3) Web application 경로를 별도로 지정하지 않았을 경우
//      http://localhost:8888/java110-servlet/ex01/servlet01
//      Web Application 이름 자리에 프로젝트 이름을 사용한다.

// Servlet을 새로 만든 경우
// Servlet 컨테이너를 다시 시작해야 적용된다.
// 이미 만든 Servlet에 대해 변경했을 경우 10초 이내에 자동으로 로딩된다.


@WebServlet("/ex02/servlet01")
public class Servlet01 implements Servlet{

    ServletConfig config;
    
    public Servlet01(){
        System.out.println("Servlet01 생성자 호출 됨");
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        // Servlet 객체를 만든 후 생성자 다음으로 호출되기 때문에
        // 이 매서드에서는 Servlet이 작업할 때 사용할 자원을 준비하는 일을 한다.
        // 예를 들면 DB Connection, 외부 서버 Socket 연결 등의 작업.
        // 또는 Servlet에서 사용할 Service 객체나 DAO 객체를 준비하는 일을 한다.
        
        System.out.println("init() 호출 됨");
        this.config = config;
    }

    @Override
    public ServletConfig getServletConfig() {
        // Servlet 실행중 Servlet 정보가 필요 할때
        // 이 매서드를 호출하여 ServletConfig 객체를 리턴받아 사용한다.
        // 이 매서드는 init()에서 파라미터로 받은 ServletConfig를 리턴해야한다.
        
        
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        // 클라이언트가 이 Servlet의 실행을 요청할때마다 Servlet 컨테이너가 호출 한다.
        // 클라이언트가 요청한 작업을 수행한다.
        // 게시물 목록 출력, 로그인 처리, 회원가입 처리 등...
        
        System.out.println("service() 호출됨");   
    }

    @Override
    public String getServletInfo() {
        // Servlet을 실행중에 직접 호출하는 경우는 없다.
        // 주로 서블릿 컨테이너의 관리 메뉴에서 Servlet 정보를 출력할때 
        // 이매서드의 리턴값을 받아서 출력한다.
        
        return "Servlet01";
    }

    @Override
    public void destroy() {
        // Servlet 객체가 제거되기 직전에 호출되는 메서드이다.
        // 이 매서드에서는 init()에서 준비한 자원을 해제시키는 작업을 한다.
        // DB Connection, Socket 등의 연결 닫기 
        System.out.println("destroy() 호출됨");   
    }


}

/*
  Servlet 구동 절차
   - 클라이언트가 요청한 URL에 등답할 Servlet을 찾는다.

   - 해당 Servlet의 인스턴스가 생성되었다면
      - service()를 호출한다.

   - 해당 Servlet 객체가 아직 생성되지 않았다면
      - Servlet 객체 생성 및 생성자 호출
      - init() 호출
      - service() 호출

   - Servlet 컨테이너를 종료하거나 Web Applicaiton의 실행을 멈추면 
      - 생성된 모든 Servlet들에 대해 destory()를 호출한다.
      - 생성된 모든 Servlet 객체를 Garbage로 만든다.

 주의!!
    - Servlet 객체는 클래스당 한개만 생성된다.
    - 요청할때마다 생성되는게 아니다.
    - 클라이언트 마다 객체가 생성되는것이 아니라 한 객체를 사용하는것이기 때문에
          객체의 인스턴스 변수를 공유하게 된다.
          
    - 클라이언트 마다 구분해서 값을 저장해야 한다면 Servlet의 인스턴스 변수를 사용해서는 안된다.
    
 */


