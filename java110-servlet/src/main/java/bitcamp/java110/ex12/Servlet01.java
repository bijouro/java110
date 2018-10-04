// JSP ?‚¬?š©? „  - ë¡œê·¸?¸ ?¼ ì¶œë ¥?•˜ê¸?
//    > ê°œë°œ?ê°? ì§ì ‘ HTML ì¶œë ¥ ì½”ë“œë¥? ?‘?„±?•´?•¼ ?•œ?‹¤.
//
// JSP
//    > ê°œë°œ?ë¥? ???‹ ?•˜?—¬ ?„œë¸”ë¦¿ ?´?˜?Š¤ë¥? ? •?˜?•˜ê³? 
//      ?ë°? ì¶œë ¥ì½”ë“œë¥? ?‘?„±?•œ?‹¤.

//    > êµ¬ë™?›ë¦?
//      hello.jsp => [JSP ?—”ì§?] => hello_jsp.java ?ƒ?„±
//      - ?ƒ?„±?œ ?ë°? ?´?˜?Š¤?Š” HttpServlet ?´?˜?Š¤?˜ ?•˜?œ„ ?´?˜?Š¤ ?´?‹¤.
//      - ?´?˜?Š¤ ?´ë¦„ì? JSP ?—”ì§„ì— ?”°?¼ ?‹¬ë¥? ?ˆ˜ ?ˆ?‹¤.
//      - JSP ?ŒŒ?¼?„ ì§ì ‘ ?‹¤?–‰?•˜?Š” ê²ƒì´ ?•„?‹ˆ?‹¤.

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
            // email ì¿ í‚¤ê°? ?—†?‹¤ë©?
            // ?‹¤?Œ ?š”ì²??•  ?•Œ ?´ë©”ì¼ ì¿ í‚¤ë¥? ë°›ì„ ?ˆ˜ ?ˆ?„ë¡?
            // ?…Œ?Š¤?Š¸?š© ì¿ í‚¤ë¥? ?›¹ë¸Œë¼?š°???—ê²? ë³´ë‚¸?‹¤.
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
        out.println("<title>ë¡œê·¸?¸</title>");
        out.println("<style>");
        out.println("th{");
        out.println("text-align: right;");
        out.println("}");
         
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>ë¡œê·¸?¸</h1>");
        out.println("<form action='login' method='post'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td>");
        out.println("<input type='radio' name='type' value='student' checked>?•™?ƒ");
        out.println("<input type='radio' name='type' value='teacher'>ê°•ì‚¬");
        out.println("<input type='radio' name='type' value='manager'>ë§¤ë‹ˆ??");
        out.println("</td>");
                
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>?´ë©”ì¼</th>");
        out.printf("<td><input type='email' name='email' value='%s'></td>\n",email); 
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th>?•”?˜¸</th>");
        out.println("<td><input type='password' name='password'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td><input type='checkbox' name='save'>?´ë©”ì¼???¥</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("<td><button>ë¡œê·¸?¸</button></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
        
        
    }
    
}
