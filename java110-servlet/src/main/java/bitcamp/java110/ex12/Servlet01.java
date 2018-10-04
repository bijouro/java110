// JSP ?¬?©?   - λ‘κ·Έ?Έ ?Ό μΆλ ₯?κΈ?
//    > κ°λ°?κ°? μ§μ  HTML μΆλ ₯ μ½λλ₯? ??±?΄?Ό ??€.
//
// JSP
//    > κ°λ°?λ₯? ??? ??¬ ?λΈλ¦Ώ ?΄??€λ₯? ? ??κ³? 
//      ?λ°? μΆλ ₯μ½λλ₯? ??±??€.

//    > κ΅¬λ?λ¦?
//      hello.jsp => [JSP ?μ§?] => hello_jsp.java ??±
//      - ??±? ?λ°? ?΄??€? HttpServlet ?΄??€? ?? ?΄??€ ?΄?€.
//      - ?΄??€ ?΄λ¦μ? JSP ?μ§μ ?°?Ό ?¬λ₯? ? ??€.
//      - JSP ??Ό? μ§μ  ?€??? κ²μ΄ ???€.

package bitcamp.java110.ex12;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ex12/servlet01")

public class Servlet01 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        String email = "";
        Cookie[] cookies = request.getCookies();
        
        if(cookies != null) {
            for(Cookie cookie: cookies) {
                
                if(cookie.getName().equals("email")) {
                    email = cookie.getValue();
                    break;
                }
                
            }
        }
        
        if(email == "") {
            // email μΏ ν€κ°? ??€λ©?
            // ?€? ?μ²??  ? ?΄λ©μΌ μΏ ν€λ₯? λ°μ ? ??λ‘?
            // ??€?Έ?© μΏ ν€λ₯? ?ΉλΈλΌ?°???κ²? λ³΄λΈ?€.
            Cookie cookie = new Cookie("email","hongkildong");
            cookie.setPath("/");
            response.addCookie(cookie);            
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>λ‘κ·Έ?Έ</title>");
        out.println("<style>");
        out.println("th{");
        out.println("text-align: right;");
        out.println("}");
         
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>λ‘κ·Έ?Έ</h1>");
        out.println("<form action='login' method='post'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td>");
        out.println("<input type='radio' name='type' value='student' checked>??");
        out.println("<input type='radio' name='type' value='teacher'>κ°μ¬");
        out.println("<input type='radio' name='type' value='manager'>λ§€λ??");
        out.println("</td>");
                
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>?΄λ©μΌ</th>");
        out.printf("<td><input type='email' name='email' value='%s'></td>\n",email); 
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>??Έ</th>");
        out.println("<td><input type='password' name='password'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td><input type='checkbox' name='save'>?΄λ©μΌ???₯</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td><button>λ‘κ·Έ?Έ</button></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        
        
    }
    
}
