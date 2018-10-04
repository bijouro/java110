package bitcamp.java110.cms.servlet.teacher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.TeacherDao;
import bitcamp.java110.cms.domain.Teacher;

@WebServlet("/teacher/detail")
public class TeacherDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(
            HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {

        int no = Integer.parseInt(request.getParameter("no"));
        
        TeacherDao teacherDao = (TeacherDao)this.getServletContext()
                .getAttribute("teacherDao");
        
        Teacher t = teacherDao.findByNo(no);
        
        //JSP로 실행을 위임하기 전에 응답 콘텐츠의 타입을 설정한다.
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("teacher", t);
        // JSP 페이지를 인클루딩
        RequestDispatcher rd = request.getRequestDispatcher("/teacher/detail.jsp");
        rd.include(request, response);
        response.setContentType("text/html;charset=UTF-8");
    }

}
