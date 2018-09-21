/* 클라이언트로 출력하기 - HTML 출력하기
 *  
 */
package bitcamp.java110.ex03;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ex03/servlet03")
public class Servlet03 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Welcome!!</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>환영합니다!!!!</h1>");
        out.println("</body>");
        out.println("</html>");

    }
}
