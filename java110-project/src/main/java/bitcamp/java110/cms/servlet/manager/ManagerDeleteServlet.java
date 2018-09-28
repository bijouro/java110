package bitcamp.java110.cms.servlet.manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
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

            request.setAttribute("error", e);
            request.setAttribute("message", "매니저 삭제 오류!");
            request.setAttribute("refresh", "3;url=list");
            request.getRequestDispatcher("/error").forward(request, response);
        }
    }
}