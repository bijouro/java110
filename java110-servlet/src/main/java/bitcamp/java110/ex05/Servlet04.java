/* 
 * GET / POST 구분하기 II
 */
package bitcamp.java110.ex05;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// MyHttpServlet를 직접 상속 받는것 보다 
// MyHttpServlet2 클래스를 상속받으면
// GET/POST 요청을 구분하여 처리할 수 있다.

@WebServlet("/ex05/servlet04")
public class Servlet04 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("GET 요청입니다.");
    
    }
 
}
