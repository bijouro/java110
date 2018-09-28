package bitcamp.java110.cms.servlet.manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import bitcamp.java110.cms.dao.ManagerDao;
import bitcamp.java110.cms.dao.impl.ManagerMysqlDao;
import bitcamp.java110.cms.util.DataSource;

@WebServlet("/manager/delete")
public class ManagerDeleteServlet extends HttpServlet { 

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        int no = Integer.parseInt(request.getParameter("no"));

        // 삭제 결과를 출력하고 1초가 경과한 후에 목록 페이지를 요청하도록 
        // refresh 명령을 설정한다.
        //   - 응답헤더로 웹 브라우저에게 알린다.
        
        ManagerDao managerDao = (ManagerDao)this.getServletContext()
                .getAttribute("managerDao");
        
        try {
            managerDao.delete(no);
            response.sendRedirect("list");
            
        }catch(Exception e) {
            
            e.printStackTrace();
            response.setHeader("Refresh", "1;url=list");
            response.setContentType("text/html;charset=UTF-8");
            
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>매니저 관리</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>매니저 삭제 오류</h1>");
            out.printf("<p>%s</p>", e.getMessage());
            out.println("<p>잠시 기다리시면 목록 페이지로 자동으로 이동합니다.</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}