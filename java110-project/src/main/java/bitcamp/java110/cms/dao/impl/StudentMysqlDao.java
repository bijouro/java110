package bitcamp.java110.cms.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bitcamp.java110.cms.dao.DaoException;
import bitcamp.java110.cms.dao.StudentDao;
import bitcamp.java110.cms.domain.Student;
import bitcamp.java110.cms.util.DataSource;

@Component
public class StudentMysqlDao implements StudentDao {

    DataSource dataSource;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public int insert(Student student) {
        Connection con = null;
        Statement stmt = null;
        
        try {
            
            con = dataSource.getConnection();
            
            con.setAutoCommit(false);

            stmt = con.createStatement();
            String sql = "insert into p1_memb(name,email,pwd,tel,cdt)"
                    + " values('" + student.getName()
                    + "','" + student.getEmail()
                    + "',password('" + student.getPassword()
                    + "'),'" + student.getTel()
                    + "',now())";
            
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            
            ResultSet autogeneratedKeys = stmt.getGeneratedKeys();
            autogeneratedKeys.next();
            int no = autogeneratedKeys.getInt(1);
            autogeneratedKeys.close();
            
            String sql2 = "insert into p1_stud(sno,schl,work)"
                    + " values(" + no
                    + ",'" + student.getSchool()
                    + "','" + (student.isWorking()?'Y':'N')
                    + "')";
            stmt.executeUpdate(sql2);
            con.commit();
            return 1;
            
        } catch (Exception e) {
            try{con.rollback();}catch(Exception e2) {}
            throw new DaoException(e);
            
        } finally {
            try {stmt.close();} catch (Exception e) {}
        }
    }
    
    public List<Student> findAll() {
        
        ArrayList<Student> list = new ArrayList<>();
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = dataSource.getConnection();
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery(
                    "select" + 
                    " m.mno," +
                    " m.name," + 
                    " m.email," + 
                    " s.schl," +
                    " s.work" +
                    " from p1_stud s" + 
                    " inner join p1_memb m on s.sno = m.mno");
            
            while (rs.next()) {
                Student s = new Student();
                s.setNo(rs.getInt("mno"));
                s.setEmail(rs.getString("email"));
                s.setName(rs.getString("name"));
                s.setSchool(rs.getString("schl"));
                s.setWorking(rs.getString("work").equals("Y") ? true : false);
                
                list.add(s);
            }
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            try {rs.close();} catch (Exception e) {}
            try {stmt.close();} catch (Exception e) {}        }
        return list;
    }
    
    public Student findByEmail(String email) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = dataSource.getConnection();
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    "select" + 
                    " m.mno," +
                    " m.name," + 
                    " m.email," + 
                    " s.schl," +
                    " s.work" + 
                    " from p1_stud s" + 
                    " inner join p1_memb m on s.sno = m.mno" +
                    " where m.email='" + email + "'");
            
            if (rs.next()) {
                Student s = new Student();
                s.setNo(rs.getInt("mno"));
                s.setEmail(rs.getString("email"));
                s.setName(rs.getString("name"));
                s.setTel(rs.getString("tel"));
                s.setSchool(rs.getString("schl"));
                s.setWorking(rs.getString("work").equals("Y") ? true : false);
                
                return s;
            }
            return null;
            
        } catch (Exception e) {
            throw new DaoException(e);
            
        } finally {
            try {rs.close();} catch (Exception e) {}
            try {stmt.close();} catch (Exception e) {}
        }
    }
    
    public Student findByNo(int no) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            con = dataSource.getConnection();
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(
                    "select" + 
                    " m.mno," +
                    " m.name," + 
                    " m.email," + 
                    " m.tel," + 
                    " s.schl," +
                    " s.work" +  
                    " from p1_stud s" + 
                    " inner join p1_memb m on s.sno = m.mno" +
                    " where m.mno=" + no);
            
            if (rs.next()) {
                Student s = new Student();
                s.setNo(rs.getInt("mno"));
                s.setEmail(rs.getString("email"));
                s.setName(rs.getString("name"));
                s.setTel(rs.getString("tel"));
                s.setSchool(rs.getString("schl"));
                s.setWorking(rs.getString("work").equals("Y") ? true : false);
                
                return s;
            }
            return null;
            
        } catch (Exception e) {
            throw new DaoException(e);
            
        } finally {
            try {rs.close();} catch (Exception e) {}
            try {stmt.close();} catch (Exception e) {}
        }
    }
    
    public int delete(int no) {
        Connection con = null;
        Statement stmt = null;
        
        try {
            con = dataSource.getConnection();
            
            con.setAutoCommit(false);
            stmt = con.createStatement();
            
            String sql = "delete from p1_stud where sno=" + no ;
            int count = stmt.executeUpdate(sql);
            
            if (count == 0)
                return 0;
            
            String sql2 = "delete from p1_memb where mno=" + no;
            stmt.executeUpdate(sql2);
            con.commit();
            
            return 1;
            
        } catch (Exception e) {
            try{con.rollback();}catch(Exception e2) {}
            throw new DaoException(e);
            
        } finally {
            try {stmt.close();} catch (Exception e) {}
        }
    }
}









