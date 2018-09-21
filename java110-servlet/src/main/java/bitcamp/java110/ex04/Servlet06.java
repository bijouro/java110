/* 클라이언트로 출력하기 - POST 요청
 *  
 */
package bitcamp.java110.ex04;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/ex04/servlet06")
public class Servlet06 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        
        // http://localhost:8888/ex04/servlet06?name=ABC가각
        // URL 주소창에 파라미터 값으로 한글을 입력했다 하더라도
        // 개발 도구 창에서 서버에 요청한 HTTP 프로토콜을 보면
        // 영어는 그대로 보이지만 한글은 %XX%XX%XX 형식으로 변환되어 보여진다.
        
        // GET 요청에서 URL에 한글이 포함될 경우 POST 처럼 ChatacterEncoding 설정을 하지 않아도 된다.
        // Tomcat 7 이하에서는 server.xml에 설정을 해 줘야 한다.
        
/*        
        <Connector connectionTimeout="20000" 
                port="8888" 
                protocol="HTTP/1.1" 
                redirectPort="8443"
                URIEncding="UTF-8"
                />

            다음은 POST 요청으로 들어온 한글 데이터를 처리할 때 사용하는 것이다.
       GET 요청에 대해서는 setCharacterEncoding() 효력이 없다.
       
*/
                
                
        //req.setCharacterEncoding("UTF-8");
        
        String name = req.getParameter("name");
        
        res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.printf("name=%s \n", name);
    }
}

// URL 인코딩이란?
//  HTTP 프로토콜로 클라이언트와 서버가 데이터를 주고 받을 때,
//  7비트 장비를 경유하더라도 데이터가 깨지지 않게 하기 위한 방안이다.

//  7비트 장비란?
//      오래전에 구축된 일부 네트워크 장비(Gateway)가 7비트인 경우가 있다.
//      이 경우 8비트 데이터를 전송하면 나올 때 1비트가 손실될 수 있다.

// 해결책
//      8비트 문자를 ASCII 문자로 바꿔 보내고,
//      받은 쪽에서는 ASCII문자를 원래의 바이트로 바꿔 사용한다.
//      즉 8비트 데이터를 ASCII 문자화 시키는 방법. URL인코딩 
//      웹브라우저는 데이터를 보내기 전에 URL 인코딩한다.
//      원래 ASCII 문자는 그대로 두고 한글과 같은 7비트 초과하는 코드인 경우
//      URL Encoding을 진행한다.
//      웹 서버는 웹 브라우저로 부터 받은 URL Encoding값을 Decoding 하여
//      원래의 바이트 값으로 자동으로 변경한다.
//      따라서 개발자 개입할 필요는 없다.

//      인코딩,디코딩을 알아야 하는 이유
//      웹브라우저와 웹서버 사이에 데이터를 주고 받는 HTTP 프로토콜을 분석하다 보면
//      %xx%Xxx%xx 와 같은 데이터를 보게 된다.
//      이런 경우 당황하지 않고 정확히 이해 하기 위해.........................

//      개발자가 직접 소켓 프로그래밍을 통해 데이터를 보낼때는
//      URL 인코딩이 자동으로 처리되는 것이 아니기 때문에
//      개발자가 직접 java.net.URLEncoder를 사용하여 처리한다.

//      웹브라우저가 보낸 데이터를 웹서버를 통해 받지 않고 직접받는 경우가 있다.
//      이런 경우에도 개발자가 직접 URLDecoder를 사용하여 직접 디코딩 해야 한다.
