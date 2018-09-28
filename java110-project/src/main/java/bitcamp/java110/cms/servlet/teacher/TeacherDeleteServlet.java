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
            response.setHeader("Refresh", "1;url=list");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>강사 관리</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>강사 삭제 실패</h1>");
            out.printf("<p>잠시 기다리시면 목록 페이지로 자동 이동합니다.</p>\n");
            out.println("</body>");
            out.println("</html>");
            out.println("</body>");
            out.println("</html>");
        }
        
    }
    
}
