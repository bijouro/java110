/* 클라이언트로 출력하기 - POST 요청
 *  
 */
package bitcamp.java110.ex04;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ex04/servlet02")
public class Servlet02 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        
        // POST 요청으로 값을 서버에 전달하는 방법
        //   HTTP 요청에서 message-body 영역에 데이터를 붙여 전달한다.
        
        // HTML의 form tag를 이용하여 전송한다.
        // http://localhost:8888/ex04/form.html 페이지에서 값을 입력한 후 보내기 버튼 클릭

        // 그러나 서블릿에서 값을 꺼내는 방법은 GET 과 같다.
        
        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        boolean working = Boolean.parseBoolean(req.getParameter("working"));
        
        res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.printf("name=%s", name);
        out.printf("age=%d", age);
        out.printf("working=%b\n", working);
        

    }
}

// POST 요청의 특징
//  Message-body 영역에 데이터를 포함시킨다.
//      URL에 데이터가 포함되어 있지 않다.
//      URL만 저장되기 때문에 데이터가 포함되지 않은 URL을 저장해도 소용없다.
//      데이터 조회 화면을 요청할 때는 POST 방식을 사용하지 않는다.
// - 보안
//      URL에 데이터가 포함되어 있지 않기 때문에
//      데이터가 노출되지 말아야 할 데이터를 전송할 때는 적합하지 않다.
//      예를 들면 로그인, 회원가입 등에는 적합하지 않다.

// 대용량 데이터
//      대부분의 웹 서버는 Request-URI 또는 헤더를 포함한 메타 데이터의 크기를
//      8KB 미만으로 설정하고 있다.
//      그러나 message-body 영역은 제한을 두지 않거나 큰 용량을 허락하고 있다.
//      그래서 대용량 데이터를 보낼 때는 POST 방식을 사용한다.

//  Binary Data 전송
//      message-body 부분에 첨부하기 때문에 바이너리 데이터를 전송할 수 있다.
//      단, Multi-Part 형식으로 보낸다.

// - 적용부분
//      로그인, 회원가입 처럼 데이터가 캐시에 보관되거나 노출되지 말아야 하는 경우
//      게시글 등록/변경 처럼 대용량의 데이터를 전송해야 하는 경우
//      파일 업로드 처럼 바이너리 데이터를 전송해야 하는 경우






