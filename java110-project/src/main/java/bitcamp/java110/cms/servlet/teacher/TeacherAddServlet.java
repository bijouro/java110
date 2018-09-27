package bitcamp.java110.cms.servlet.teacher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.TeacherDao;
import bitcamp.java110.cms.domain.Teacher;

@WebServlet("/teacher/add")
public class TeacherAddServlet extends HttpServlet { 
    
    private static final long serialVersionUID = 1L;
    

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
            Teacher t = new Teacher();
            
            t.setName(request.getParameter("name"));
            t.setEmail(request.getParameter("email"));
            t.setPassword(request.getParameter("password"));
            t.setTel(request.getParameter("tel"));
            t.setPay(Integer.parseInt(request.getParameter("pay")));
            t.setSubjects(request.getParameter("subject"));
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            TeacherDao teacherDao = (TeacherDao)this.getServletContext()
                    .getAttribute("teacherDao");
            
            
            if (teacherDao.insert(t) > 0) {
                out.println("저장하였습니다.");
            } else {
                out.println("같은 이메일의 강사가 존재합니다.");
            }
    }
    
}
