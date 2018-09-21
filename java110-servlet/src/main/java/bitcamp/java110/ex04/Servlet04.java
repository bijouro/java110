/* 클라이언트가 보낸 데이터 읽기 - 멀티파트 데이터 읽기
 *  
 */
package bitcamp.java110.ex04;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

// Servlet API 4.0부터 제공하는 멀티파트 처리기를 이용하기
//  서블릿 선언부에 멀티파트 데이터 처리를 지정함


@MultipartConfig(maxFileSize=10_000_000)
@WebServlet("/ex04/servlet04")
public class Servlet04 extends GenericServlet{

    private static final long serialVersionUID = 1L;

    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {


        // 멀티파트 데이터는 HttpServletRequest의 getParts(), getPart)
        // 매서드를 사용해서 꺼낸다.

        HttpServletRequest  httpReq = (HttpServletRequest)req;

        String file1name = "";
        Part part = httpReq.getPart("file1");
        if(part.getSize() > 0 ) {
            file1name = UUID.randomUUID().toString();
            part.write(this.getServletContext().getRealPath("/upload/" + file1name));
        }
        
        String file2name = "";
        part = httpReq.getPart("file2");
        if(part.getSize() > 0 ) {
            file2name = UUID.randomUUID().toString();
            part.write(this.getServletContext().getRealPath("/upload/" + file2name));
        }
        res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();

        out.printf("name=%s \n", req.getParameter("name"));
        out.printf("age=%d \n", Integer.parseInt(req.getParameter("age")));
        out.printf("working=%b \n", Boolean.parseBoolean(req.getParameter("working")));
        out.printf("file1=%s \n", file1name);
        out.printf("file2=%s \n", file2name);

    }
}

// 멀티파트 POST 요청

