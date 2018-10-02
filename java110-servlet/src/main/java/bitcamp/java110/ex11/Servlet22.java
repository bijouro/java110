// 세션 사용 후 - HttpSession 보관소에 데이터 저장하기
package bitcamp.java110.ex11;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ex11/servlet22")
public class Servlet22 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 세션얻기
        //  > /ex11/servlet21을 먼저 실행한 후 이 서블릿을 실행하면,
        //    웹브라우저가 서버에 요청할 때 이전 서블릿에서 받은 세션 아이디를
        //    요청 프로토콜의 쿠키에 담아서 제출할 것이다.
        //    요청 프로토콜이 세션아이디 쿠키 정보
        
        // Cookie: JSESSIONID=36EFEAC6019D1B07754F3B1E899779F
        
        HttpSession session = request.getSession();
        String name = (String)session.getAttribute("name");
                
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        request.setCharacterEncoding("UTF-8");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>session</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>세션 데이터 꺼내기</h1>");
        out.printf("이름 : %s<br>\n", name);
        out.println("</body>");
        out.println("</html>");
    }
    
}
