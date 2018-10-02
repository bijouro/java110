// 세센의 원리 - 세션ID와 쿠키

package bitcamp.java110.ex11;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ex11/servlet21")
public class Servlet21 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        // 세션 얻기
        //   > 요청 프로토콜에서 세션아이디를 검사한다.
        //   
        //   > 있다면,
        //      - 웹브라우저가 제시한 세션 아이디에 해당하는 세션 객체를 찾는다.
        //      - 있다면, 유효하다면 그 세션 객체를 리턴한다.
        //      - 웹브라우저가 세션아이디를 갖고 있기 때문에 
        //        응답할 때 프로토콜에 쿠키로 다시 세션 아이디를 보내지 않는다.
        //        
        //   > 없으면
        //      - 새로 HttpSession 객체를 생성하여 리턴한다.
        //      - 응답할 때 응답 프로토콜의 쿠키 정보로 새로 생성한 세션 아이디를 리턴한다.
        //      - 웹 브라우저는 서버로부터 받은 쿠키데이터인 세션ID를 서버에 요청할때 마다 보낼것이다.
        
        //   > 세션ID를 응답 프로토콜로 보내는 예
        //      - 톰캣 서버에서는 JSESSIONID라는 쿠키 이름으로 세션 아이디를 보낸다.
        //      - WAS 마다 세션ID의 쿠키이름이 다를 수 있다.
        
        
        HttpSession session = request.getSession();
        
        session.setAttribute("name", "홍길동");
        
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
        out.println("<h1>1페이지</h1>");
        out.println("<form action='servlet22' method='post'>");
        out.println("이름 : <input type='text' name='name'><br>");
        out.println("<button>다음</button>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
    
}
