package bitcamp.java110.cms.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import bitcamp.java110.cms.dao.impl.ManagerMysqlDao;
import bitcamp.java110.cms.dao.impl.StudentMysqlDao;
import bitcamp.java110.cms.dao.impl.TeacherMysqlDao;
import bitcamp.java110.cms.util.DataSource;



public class InitServlet extends HttpServlet{

    private static final long serialVersionUID = 1L;
    
    @Override
    public void init() throws ServletException {
        
        System.out.println("InitServlet.init() 실행!!");
        ServletContext sc = this.getServletContext();
        
        try {
        
        //DAO가 사용할 DB ConnectionPool 객체 준비
        DataSource dataSource = new DataSource(
                sc.getInitParameter("jdbc.driver"),
                sc.getInitParameter("jdbc.url"),
                sc.getInitParameter("jdbc.username"),
                sc.getInitParameter("jdbc.password"));
        
        // DAO 객체 생성 및 DB Connection Pool 주입하기
        
        ManagerMysqlDao managerDao = new ManagerMysqlDao();
        managerDao.setDataSource(dataSource);
        
        StudentMysqlDao studentDao = new StudentMysqlDao();
        studentDao.setDataSource(dataSource);
        
        TeacherMysqlDao teacherDao = new TeacherMysqlDao();
        teacherDao.setDataSource(dataSource);
        
        //Servlet에서 DAO를 이용할 수 있도록 ServletContext 보관소에 저장하기
        sc.setAttribute("managerDao", managerDao);
        sc.setAttribute("studentDao", studentDao);
        sc.setAttribute("teacherDao", teacherDao);
        }catch(Exception e) {
            throw new ServletException(e);
        }
    }
    
    // service(), doGet(), doPost() 등의 메서드를 구현하지 않나?
    //  > 이 Servlet은 client request를 처리하기 위해 만든 servlet이 아니다.
    //  > client request를 처리하는 다른 servlet들이 사용할 공용 자원을 준비하는 일을 한다.
    //  > 그래서 service(),doGet(),doPost()를 구현하지 않는다.
    //  > Client가 요청하지 않아도 이 servlet object가 생성되고
    //  > init()가 실행되어야 하기 때문에 loadOnStartup 배치 속성을 추가한다.
    
}
