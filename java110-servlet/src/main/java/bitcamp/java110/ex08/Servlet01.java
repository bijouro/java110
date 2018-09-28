/* 
 * 리프래시 
 *  - refresh란?
 *    클라이언트에서 서버로 자동으로 요청하게 만드는 기술
 *          
 */
package bitcamp.java110.ex08;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ex08/servlet01")
public class Servlet01 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet( HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {


        // 방법1
        //  > response header에 refresh 명령을 추가하기
        //  > 응답 내용을 출력한 후, 2초 후에 http://www.daum.net을 요청
        
     //  res.setHeader("Refresh", "2;url=http://www.daum.net");
        
        
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        
        // 방법 2
        //   > HTML의 <meta> tag에 refresh 명영을 설정할 수 있다
        out.println("<meta http-equiv='Refresh' content='5;url=http://www.naver.com'>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>ex08</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet01 실행</h1>");
        out.println("</body>");
        out.println("</html>");
        
        
    }
}

// 리프래시 응답 프로토콜
// 
