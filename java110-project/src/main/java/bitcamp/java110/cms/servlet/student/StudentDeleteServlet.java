package bitcamp.java110.cms.servlet.student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.StudentDao;
import bitcamp.java110.cms.dao.impl.StudentMysqlDao;
import bitcamp.java110.cms.domain.Student;
import bitcamp.java110.cms.util.DataSource;

@WebServlet("/student/delete")
public class StudentDeleteServlet extends HttpServlet { 
    
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        int no = Integer.parseInt(request.getParameter("no"));
        StudentDao studentDao = (StudentDao)this.getServletContext()
                .getAttribute("studentDao");
        
        try {
            studentDao.delete(no);
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
            out.println("<title>학생 관리</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>학생 삭제 실패</h1>");
            out.printf("<p>잠시 기다리시면 목록 페이지로 자동 이동합니다.</p>\n");
            out.println("</body>");
            out.println("</html>");
            out.println("</body>");
            out.println("</html>");
            
            
        }
    }
    
}
