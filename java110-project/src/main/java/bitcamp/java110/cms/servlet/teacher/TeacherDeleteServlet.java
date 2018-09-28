package bitcamp.java110.cms.servlet.teacher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.TeacherDao;
import bitcamp.java110.cms.dao.impl.TeacherMysqlDao;
import bitcamp.java110.cms.domain.Teacher;
import bitcamp.java110.cms.util.DataSource;

@WebServlet("/teacher/delete")
public class TeacherDeleteServlet  extends HttpServlet { 
    
    private static final long serialVersionUID = 1L;
    

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        int no = Integer.parseInt(request.getParameter("no"));
        
        
        TeacherDao teacherDao = (TeacherDao)this.getServletContext()
                .getAttribute("teacherDao");
        
        try {
            teacherDao.delete(no);
            response.sendRedirect("list");
        }catch(Exception e) {
            request.setAttribute("error", e);
            request.setAttribute("message", "강사 삭제 오류!");
            request.setAttribute("refresh", "3;url=list");
            request.getRequestDispatcher("/error").forward(request, response);
        }
        
    }
    
}
