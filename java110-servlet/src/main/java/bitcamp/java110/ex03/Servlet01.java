/* 클라이언트로 출력하기
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

@WebServlet("/ex03/servlet01")
public class Servlet01 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        out.println("Hello!!!");
        out.println("안녕하세요");
        out.println("こんにちは");
        out.println("你好");     
        
        // 문제점
        // 영어를 제외한 다른 언어의 문자는 ?로 출력된다
        //   - getWriter가 return 한 출력Stream은 
        //      기본적으로 Java Unicode를 ISO-8859-1에 따라 출력하기 때문이다.
        //   - ISO-8859-1에 정의되지 않은 문자는 ?로 대체되어 출력된다.
        
        

    }
}
