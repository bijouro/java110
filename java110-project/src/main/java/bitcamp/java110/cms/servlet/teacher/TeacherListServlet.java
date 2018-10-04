package bitcamp.java110.cms.servlet.teacher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.TeacherDao;
import bitcamp.java110.cms.domain.Teacher;

@WebServlet("/teacher/list")
public class TeacherListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        
        TeacherDao teacherDao = (TeacherDao)this.getServletContext()
                .getAttribute("teacherDao");
        
        List<Teacher> list = teacherDao.findAll();
        
        response.setContentType("text/html;charset=UTF-8");
        
        // JSP가 사용할 수 있도록 ServletRequest 보관소에 저장한다.
        request.setAttribute("list", list);
        
        // JSP로 실행을 위임한다.
        RequestDispatcher rd = request.getRequestDispatcher("/teacher/list.jsp");
        rd.include(request, response);
    }
}
