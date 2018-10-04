package bitcamp.java110.cms.servlet.student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.StudentDao;
import bitcamp.java110.cms.domain.Student;

@WebServlet("/student/detail")
public class StudentDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
  
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {

        int no = Integer.parseInt(request.getParameter("no"));
        
        StudentDao studentDao = (StudentDao)this.getServletContext()
                .getAttribute("studentDao");
        Student s = studentDao.findByNo(no);
        
        // JSP 페이지에서 사용할 수 있도록 ServletRequest 보관소에 저장한다.
        request.setAttribute("student", s);
        
        response.setContentType("text/html;charset=UTF-8");
        
        // JSP로 실행을 위임한다.
        RequestDispatcher rd = request.getRequestDispatcher("/student/detail.jsp");
        rd.include(request, response);
    }

}
