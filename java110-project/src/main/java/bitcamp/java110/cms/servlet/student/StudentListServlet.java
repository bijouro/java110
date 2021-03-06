package bitcamp.java110.cms.servlet.student;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.domain.Student;
import bitcamp.java110.cms.service.StudentService;

@WebServlet("/student/list")
public class StudentListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
  
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        
        StudentService studentService = (StudentService)this.getServletContext()
                .getAttribute("studentService");
         
        List<Student> list = studentService.list();
        request.setAttribute("list", list);
        
        response.setContentType("text/html;charset=UTF-8");
        
        RequestDispatcher rd = request.getRequestDispatcher(
                "/student/list.jsp");
        rd.include(request, response);
        
    }
}
