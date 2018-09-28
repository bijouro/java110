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

@WebServlet("/teacher/list")
public class TeacherListServlet extends HttpServlet { 
    
    private static final long serialVersionUID = 1L;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        TeacherDao teacherDao = (TeacherDao)this.getServletContext()
                .getAttribute("teacherDao");
        
        
        List<Teacher> list = teacherDao.findAll();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>강사 관리</title>");

        out.println("<style>");
        out.println("table, th, td {");
        out.println(" border: 1px solid gray;");
        out.println("};");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");
        out.println("<h1>강사 목록</h1>");
        
        out.println("<p><a href='form.html'>추가</a></p>");
        
        out.println("<table>");
        out.println("<thead>");
        out.println("   <tr>");
        out.println("       <th>번호</th><th>이름</th><th>이메일</th><th>최종학력</th><th>재직여부</th>");
        out.println("   </tr>");
        out.println("</thead>");
        out.println("<tbody>");
        
        
        for (Teacher t : list) {
            out.println("   <tr>");
            out.printf("<td>%d</td><td><a href='detail?no=%d'>%s</a></td><td>%s</td><td>%d</td><td>%s</td>",
                    t.getNo(),
                    t.getNo(),
                    t.getName(), 
                    t.getEmail(), 
                    t.getPay(),
                    t.getSubjects());
            out.println("   </tr>");
        }
        out.println("</tbody>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
