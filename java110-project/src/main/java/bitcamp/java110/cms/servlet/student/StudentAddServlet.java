package bitcamp.java110.cms.servlet.student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bitcamp.java110.cms.dao.impl.StudentMysqlDao;
import bitcamp.java110.cms.domain.Student;
import bitcamp.java110.cms.util.DataSource;

@WebServlet("/student/add")
public class StudentAddServlet extends HttpServlet { 
    
    private static final long serialVersionUID = 1L;
    StudentMysqlDao studentDao;
    
    @Override
    public void init() throws ServletException {
        DataSource dataSource = new DataSource();
        studentDao = new StudentMysqlDao();
        studentDao.setDataSource(dataSource);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
            Student s = new Student();
            
            
            s.setName(request.getParameter("name"));      
            s.setEmail(request.getParameter("email"));
            s.setPassword(request.getParameter("password"));
            s.setSchool(request.getParameter("school"));
            s.setWorking(Boolean.parseBoolean(request.getParameter("working")));
            s.setTel(request.getParameter("tel"));            
            
            studentDao.insert(s);
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("등록하였습니다.");
    }
    
}
