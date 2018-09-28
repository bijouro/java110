package bitcamp.java110.cms.servlet.manager;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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
            // 오류 내용을 처리하는 서블릿으로 실행을 위임한다.
            RequestDispatcher rd = request.getRequestDispatcher("/error");
            
            // 위임하기 전에 작업을 수행하는데 필요한 정보를 ServletRequest 보관소에 담아 전달한다.
            request.setAttribute("error", e);
            request.setAttribute("message", "매니저 등록 오류!");
            request.setAttribute("refresh", "3;url=list");
            rd.forward(request, response);
            
            
        }
    }

}