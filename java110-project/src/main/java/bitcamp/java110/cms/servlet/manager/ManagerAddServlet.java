package bitcamp.java110.cms.servlet.manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.ManagerDao;
import bitcamp.java110.cms.domain.Manager;

@WebServlet("/manager/add")
public class ManagerAddServlet extends HttpServlet { 

    private static final long serialVersionUID = 1L;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        Manager m = new Manager();
        request.setCharacterEncoding("UTF-8");        
        
        m.setName(request.getParameter("name"));
        m.setEmail(request.getParameter("email"));
        m.setPassword(request.getParameter("password"));
        m.setTel(request.getParameter("tel"));
        m.setPosition(request.getParameter("position"));
        
        // 등록 결과를 출력하고 1초가 경과한 후에 목록 페이지를 요청하도록 
        // refresh 명령을 설정한다.
        //   - 응답헤더로 웹 브라우저에게 알린다.
        
        ManagerDao managerDao = (ManagerDao)this.getServletContext()
                .getAttribute("managerDao");
        
        
        try {
            managerDao.insert(m);
            // 오류 없이 등록에 성공 했으면 목록 페이지를 다시 요청한다.
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
            out.println("<h1>매니저 등록 결과</h1>");
            out.printf("<p>잠시 기다리시면 목록 페이지로 자동 이동합니다.</p>\n");
            out.println("</body>");
            out.println("</html>");
        }
    }

}