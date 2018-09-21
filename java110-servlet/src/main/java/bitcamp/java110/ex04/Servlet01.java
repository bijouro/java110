/* 클라이언트로 출력하기
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

@WebServlet("/ex04/servlet01")
public class Servlet01 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        
        // GET 요청으로 값을 서버에 전달하는 방법
        // http://localhost:8888/ex04/servlet01?name=aaa&age=1&working=true
        
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


// GET 요청

// URL에 붙인다....
//  GET 요청으로 서버에 값을 보내기 위해서는 Request-URI에 포함해야 한다.
//  GET http://localhost:8888/ex04/servlet01?name=%EB%B0%B1%EB%91%90%EC%82%B0&age=23&working=true

//  Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//  Accept-Encoding: gzip, deflate, br
//  Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6
//  Cache-Control: no-cache
//  Connection: keep-alive
//  Cookie: JSESSIONID=BD5F6431FF4D226A07A3A06206CF3534
//  Host: localhost:8888
//  Pragma: no-cache
//  Upgrade-Insecure-Requests: 1
//  User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36

// GET 요청의 특징
//  - URL에 데이터가 포함된다.
//      즐겨찾기에 데이터를 포함하여 URL을 저장할 수 있다.
//      그래서 링크를 누르면 결과ㅣ 화면을 바로 확인할 수 있다.
//      보통조회화면을 요청할 때 GET 방식을 사용한다.

//  - 보안
//      URL은 브라우저 캐시에 자동으로 보관되기 때문에
//      노출되지 말아야 할 데이터를 전송할 때는 적합하지 않다.
//      예를 들면 로그인, 회원가입 등에는 적합하지 않다.

//  - 대용량 데이터
//      대부분의 웹 서버는 Request-URI 또는 헤더를 포함한 메타 데이터의 크기를
//      8KB 미만으로 설정하고 있다
//      GET 요청으로는 대용량의 데이터를 전송할 수 없다.

//  - 바이너리 데이터 전송
//      Request-URI에 포함할 수 있는것은 TEXT이기 떄문에 BINARY 데이터 전송에 부적합하다.
//      물론 Base64 인코딩을 이용하여 바이너리 데이터를 텍스트 데이터로 변환한 다음에 보낼수는 있다.
//      하지만, 결국 대용량의 데이터인 경우에는 마찬가지로 크기 제한때문에 불가능하다.

//  - 적용부분
//      게시물 조회, 상품 조회 등 보내는 데이터가 노출되어도 무방한 경우
//      
