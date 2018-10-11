package bitcamp.java110.cms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class DataSource {

    String url;
    String username;
    String password;

    ArrayList<Connection> connections = new ArrayList<>();
    ThreadLocal<Connection> local = new ThreadLocal<>();
    Connection con;

    public DataSource(
            String driver, 
            String url, 
            String username, 
            String password) throws Exception {
        Class.forName(driver);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    
    public Connection getConnection() throws Exception{
        Connection con = local.get();
        if(con != null) {
            return con;
        }else {
            return getConnection(false);
        }
    }

    public Connection getConnection(boolean useTransaction) throws Exception {

        Connection con = null;

        while(connections.size() > 0) {
            con = connections.remove(0);
            if(!con.isClosed() && con.isValid(3)) {
                System.out.println("기존 커넥션 사용");
                break;
            }
            con = null;

        }

        if(con == null) {
            System.out.println("새 커넥션 사용!!!");
            return con = DriverManager.getConnection(url, username, password);
        }
        
        if(useTransaction) {
            con.setAutoCommit(false);
            local.set(con);
        }else {
            con.setAutoCommit(true);
        }
        
        return con;
        
    }

    public void returnConnection(Connection con) {
        returnConnection(con, false);
    }
    
    public void returnConnection(Connection con, boolean useTransaction) {
        
        if(useTransaction) {
            local.remove();
        }
        if(local.get() == null) {
            connections.add(con);
        }
    }
}
