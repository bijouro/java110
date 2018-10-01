/* 
 * 인클루드(include)
 *  - 
 *  
 */
package bitcamp.java110.ex09;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ex09/servlet06")
public class Servlet06 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet( HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        // 이전 서블릿에서 호출한 setContentType()이 그대로 적용되기 때문에
        // include servlet에서는 setContentType를 할 필요가 없다.
        
//        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<h1>servlet06</h1>");        
    }
}

