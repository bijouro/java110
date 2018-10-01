/* 
 * 쿠키(cookie) - 사용 기간 지정하기
 *  > 쿠키의 사용 기간을 지정하지 않으면,
 *    웹브라우저는 메모리에 임시보관한다.
 *     그래서 웹브라우저를 닫으면 해당 쿠키는 버려진다.
 *  > 쿠키의 사용 기간을 지정하면,
 *    웹 브라우저는 별도의 파일에 보관한다.
 *    그래서 웹브라우저는 쿠키의 유효기간 동안에는 서버에서 쿠키를 제시한다.
 *    유효기간이 지나면 로컬에 저장된 쿠키를 삭제한다.
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

@WebServlet("/ex10/servlet01_3")
public class Servlet01_3 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet( HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        // > 유효기간 : 웹브라우저를 실행하는 동안만 유효
        
        // > 현재 경로에 한정
        //   이 쿠키는 같은 경로 (/ex10/*)
        Cookie c1 = new Cookie("name","honggildong");
        c1.setPath("/");
        // > 유효기간 : 쿠키를 받은 시점에서 30초 동안만 유효
        // > 웹 애플리케이션 전체 범위로 확장
        //   이 쿠키는 전체 서블릿 경로 (/*)        
        Cookie c2 = new Cookie("age","12");
        c2.setMaxAge(60);
        c2.setPath("/");

        // > 유효기간 : 쿠키를 받은 시점에서 1일 동안만 유효
        // > 현재 경로보다 더 좁히기
        //  이 쿠키는 특정 경로 (/ex10/a/b/*) 
        Cookie c3 = new Cookie("working","true");
        c3.setMaxAge(60*60*24);
        c3.setPath("/");
        
        
        // 2) 응답 헤더에 쿠키를 포함하기
        res.addCookie(c1);
        res.addCookie(c2);
        res.addCookie(c3);

        /* HTTP 응답 프로토콜 예)
         * 
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

