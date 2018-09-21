/* 클라이언트로 출력하기 - 한글 꺠짐현상 
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

@WebServlet("/ex03/servlet02")
public class Servlet02 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res) 
            throws ServletException, IOException {
        
        // 출력스트림 객체를 꺼내기 전에 어떤 문자표를 사용하여 출력할 것인지 지정해야 한다.
        res.setContentType("text/plain;charset=UTF-8");
        // 서블릿 컨테이너는 이 설정에 따라 출력할 수 있는 출력스트림 객체를 리턴하고
        // 응답할때 Contgent-Type이라는 응답 헤더를 추가한다.
        
        // 웹브라우저는 Content-Type 응답 헤더의 정보에 따라 웹 서버가 보낸 컨텐츠를 처리한다.
        
        
        
        PrintWriter out = res.getWriter();
        out.println("Hello!!!");
        out.println("안녕하세요");
        out.println("こんにちは");
        out.println("你好");     
        

        // Content-Type
        // text/plain : MIME Type
        // charset-UTF-8    : 문자표 지정
        // Content-Type을 지정하지 않으면 ISO-8859-1 문자표가 기본으로 사용 된다.
        // Content-Type을 지정하면 응답 헤더에 이 정보를 추가한다.
        // 웹브라우저는 
        
        
        
        // MIME : Multi-purpose Internet Mail Extensions
        // 메일에 첨부하는 파일의 타입을 지정하기 위해 만들었다.
        // 메일 클라이언트는 이 타입 정보를 보고 첨부 파일을 읽을때 
        // 어떤 프로그램을 사용해야 하는지 결정한다.
        // 인터넷 분야에서 컨텐츠의 타입을 지정할때 이 MIME Type을 사용하게 되었따.
        // 웹브라우저는 서버에서 받은 컨텐츠를 어떻게 출력할 것인지 이 MIME Type를 보고 결정한다.
        // Character-Set : 컴퓨터에서 문자를 저장할 때 어떤 코드 값으로 저장할 것인지를 정의한 규칙이다
        //      ASCII   : 7bit, 미국표준,
        //      ISO-8859-1(ISO-Latin-1) : 8bit, 국제표준
        //      EUC-KR  : 16bit, 국제표준, 한글 2350자 정의
        //      조합형     : 16bit, 아래아한글 규칙. 초성(5bit),중성(5bit),종성(5bit)
        //      MS949(CP949)  : 16bit, MS OS규칙,EUC-KR(2350) + 8822 = 11722자 
        //      UNICODE     : 16bit. 국제표준, 한글 11722자
        //                    영어도 2바이트
        //      UTF-8       : 1~4바이트로 문자 표현, 국제표준, 유니코드 변형
        //                  : 한글은 규칙에 따라 3바이틀 표현
        //                  : 영어는 ISO-8859-1 코드 그대로 1바이트로 표현
        
        
    }
}
