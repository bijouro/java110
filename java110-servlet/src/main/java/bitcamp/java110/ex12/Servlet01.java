<<<<<<< HEAD
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

=======
// JSP ì‚¬ìš© ì „ - ë¡œê·¸ì¸ í¼ ì¶œë ¥í•˜ê¸°
// => ê°œë°œìê°€ ì§ì ‘ HTML ì¶œë ¥ ì½”ë“œë¥¼ ì‘ì„±í•´ì•¼ í•œë‹¤.
//
// JSP 
// => ê°œë°œìë¥¼ ëŒ€ì‹ í•˜ì—¬ ì„œë¸”ë¦¿ í´ë˜ìŠ¤ë¥¼ ì •ì˜í•˜ê³ ,
//    ìë°” ì¶œë ¥ ì½”ë“œë¥¼ ì‘ì„±í•œë‹¤.
// => êµ¬ë™ ì›ë¦¬
//    hello.jsp ===> [JSP ì—”ì§„] ===> hello_jsp.java ìƒì„±
//    - ìƒì„±ëœ ìë°” í´ë˜ìŠ¤ëŠ” HttpServlet í´ë˜ìŠ¤ì˜ í•˜ìœ„ í´ë˜ìŠ¤ì´ë‹¤.
//    - í´ë˜ìŠ¤ ì´ë¦„ì€ JSP ì—”ì§„ì— ë”°ë¼ ë‹¤ë¥¼ ìˆ˜ ìˆë‹¤.
//    - JSP íŒŒì¼ì„ ì§ì ‘ ì‹¤í–‰í•˜ëŠ” ê²ƒì´ ì•„ë‹ˆë‹¤.
//
>>>>>>> b1
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
public class Servlet01 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) 
                    throws ServletException, IOException {
        
        // ì¿ í‚¤ ë°ì´í„°ì— email ì´ ìˆë‹¤ë©´ êº¼ë‚¸ë‹¤.
        String email = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    email = cookie.getValue();
                    break;
                }
            }
        }
        
<<<<<<< HEAD
        if(email == "") {
            // email ì¿ í‚¤ê°? ?—†?‹¤ë©?
            // ?‹¤?Œ ?š”ì²??•  ?•Œ ?´ë©”ì¼ ì¿ í‚¤ë¥? ë°›ì„ ?ˆ˜ ?ˆ?„ë¡?
            // ?…Œ?Š¤?Š¸?š© ì¿ í‚¤ë¥? ?›¹ë¸Œë¼?š°???—ê²? ë³´ë‚¸?‹¤.
            Cookie cookie = new Cookie("email","hongkildong");
=======
        if (email == "") { // email ì¿ í‚¤ê°€ ì—†ë‹¤ë©´,
            // ë‹¤ìŒ ìš”ì²­í•  ë•Œ ì´ë©”ì¼ ì¿ í‚¤ë¥¼ ë°›ì„ ìˆ˜ ìˆë„ë¡ 
            // í…ŒìŠ¤íŠ¸ ìš© ì¿ í‚¤ë¥¼ ì›¹ë¸Œë¼ìš°ì €ì—ê²Œ ë³´ë‚¸ë‹¤.
            Cookie cookie = new Cookie("email", "hongkildong");
>>>>>>> b1
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
        out.println("th {");
        out.println("    text-align: right;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>ë¡œê·¸?¸</h1>");
        out.println("<form action='login' method='post'>");
        out.println("<table>");
        out.println("<tr>");
<<<<<<< HEAD
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
=======
        out.println("    <th></th>");
        out.println("    <td>");
        out.println("        <input type='radio' name='type' value='student' checked>í•™ìƒ"); 
        out.println("        <input type='radio' name='type' value='teacher'>ê°•ì‚¬");
        out.println("        <input type='radio' name='type' value='manager'>ë§¤ë‹ˆì €");
        out.println("    </td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("    <th>ì´ë©”ì¼</th>");
        out.printf("    <td><input type='email' name='email' value='%s'></td>\n", email);
        out.println("</tr>");
        out.println("<tr>");
        out.println("    <th>ì•”í˜¸</th>");
        out.println("    <td><input type='password' name='password'></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("    <th></th>");
        out.println("    <td><input type='checkbox' name='save'>ì´ë©”ì¼ ì €ì¥</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("    <th></th>");
        out.println("    <td><button>ë¡œê·¸ì¸</button></td>");
>>>>>>> b1
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
}













