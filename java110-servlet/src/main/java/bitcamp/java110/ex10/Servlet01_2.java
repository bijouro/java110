/* 
 * 쿠키(cookie) - 사용 범위 지정하가ㅣ
 *  > 쿠키의 사용 범위를 지정하면
 *     해당 범위에 있는 URL을 요청할때 웹브라우저가 쿠키를 보낸다.
 *    
 */ 

package bitcamp.java110.ex10;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ex10/servlet01_2")
public class Servlet01_2 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet( HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        // > 현재 경로에 한정
        //   이 쿠키는 같은 경로 (/ex10/*)
        Cookie c1 = new Cookie("name","honggildong");
        
        // > 웹 애플리케이션 전체 범위로 확장
        //   이 쿠키는 전체 서블릿 경로 (/*)        
        Cookie c2 = new Cookie("age","12");
        c2.setPath("/");

        // > 현재 경로보다 더 좁히기
        //  이 쿠키는 특정 경로 (/ex10/a/b/*) 
        Cookie c3 = new Cookie("working","true");
        c3.setPath("/ex10/a/b");
        
        
        // 2) 응답 헤더에 쿠키를 포함하기
        res.addCookie(c1);
        res.addCookie(c2);
        res.addCookie(c3);

        /* HTTP 응답 프로토콜 예)
            HTTP/1.1 200
            Set-Cookie: name=honggildong
            Set-Cookie: age=12; Path=/  << 경로정보 추가 됨
            Set-Cookie: working=true; Path=/ex10/a/b  << 경로정보 추가 됨
            Content-Type: text/html;charset=UTF-8
            Content-Length: 140
            Date: Mon, 01 Oct 2018 03:17:43 GMT
         */

        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>ex10</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>쿠키 보내기</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}

