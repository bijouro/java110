/* 
 * 서버측 객체 보관소
 *  - 서블릿이나 필터, 리스너를 실행하는 중에 필요한 값들을 보관소에 저장하고 꺼내 쓸 수 있다.
 *  - 보관소
 *      1) ServletContext 보관소
 *          - 생성시점 : Web Application이 시작 될 때 각각 하나의 보관소가 생성 됨 
 *          - 소멸시점 : Web Application 종료 시
 *          - 저장 대상 : 모든 서블릿들이 공유하는 객체를 주로 보관
 *                    예) DAO, 서비스 객체
 *                    
 *      2) HttpSesson 보관소
 *          - 생성시점 : 각 클라이언트에 대해 세션을 시작할 때 마다 
 *          - 소멸시점 : 각 클라이언트에 대해 세션을 종료할 때 마다
 *          - 저장 대상 : 클라이언트 전용 정보를 보관한다.
 *                    예) 로그인 정보, 페이지 사이에 공유하는 정보.                    
 *                     
 *      3) ServletRequest 보관소
 *          - 생성시점 : 요청이 들어 올 때 마다
 *          - 소멸시점 : 응답을 완료 할 때 마다
 *          - 저장대상 : 하나의 요청을 처리하는 서블릿들이 공유하는 정보
 *          
 *      4) PageContext 보관소
 *          - 생성시점 : JSP가 실행될 때 마다
 *          - 소멸시점 : JSP 실행을 마쳤을때 마다
 *          - 저장대상 : JSP와 핸들러가 공유 하는 정보 
 *          
 */
package bitcamp.java110.ex07;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ex07/servlet01")
public class Servlet01 extends HttpServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service( HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {

        req.setAttribute("bbb", "임꺽정");
        res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("/ex06/servlet01 실행!");
        
        // ServletContext 보관소에 저장된 값 꺼내기.
        //  먼저 ServletContext 객체를 알아 낸다. 
        
        ServletContext sc = this.getServletContext();
        out.printf("ServletContext: aaa=%s\n", sc.getAttribute("aaa"));
        out.printf("ServletContext: bbb=%s\n", sc.getAttribute("bbb"));
    }
}
