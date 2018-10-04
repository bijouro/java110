// JSP ?��?��?��  - 로그?�� ?�� 출력?���?
//    > 개발?���? 직접 HTML 출력 코드�? ?��?��?��?�� ?��?��.
//
// JSP
//    > 개발?���? ???��?��?�� ?��블릿 ?��?��?���? ?��?��?���? 
//      ?���? 출력코드�? ?��?��?��?��.

//    > 구동?���?
//      hello.jsp => [JSP ?���?] => hello_jsp.java ?��?��
//      - ?��?��?�� ?���? ?��?��?��?�� HttpServlet ?��?��?��?�� ?��?�� ?��?��?�� ?��?��.
//      - ?��?��?�� ?��름�? JSP ?��진에 ?��?�� ?���? ?�� ?��?��.
//      - JSP ?��?��?�� 직접 ?��?��?��?�� 것이 ?��?��?��.

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
            // email 쿠키�? ?��?���?
            // ?��?�� ?���??�� ?�� ?��메일 쿠키�? 받을 ?�� ?��?���?
            // ?��?��?��?�� 쿠키�? ?��브라?��???���? 보낸?��.
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
        out.println("<title>로그?��</title>");
        out.println("<style>");
        out.println("th{");
        out.println("text-align: right;");
        out.println("}");
         
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>로그?��</h1>");
        out.println("<form action='login' method='post'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td>");
        out.println("<input type='radio' name='type' value='student' checked>?��?��");
        out.println("<input type='radio' name='type' value='teacher'>강사");
        out.println("<input type='radio' name='type' value='manager'>매니??");
        out.println("</td>");
                
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>?��메일</th>");
        out.printf("<td><input type='email' name='email' value='%s'></td>\n",email); 
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>?��?��</th>");
        out.println("<td><input type='password' name='password'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td><input type='checkbox' name='save'>?��메일???��</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td><button>로그?��</button></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        
        
    }
    
}
