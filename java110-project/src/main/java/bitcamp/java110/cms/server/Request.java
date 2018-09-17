package bitcamp.java110.cms.server;

import java.util.HashMap;
import java.util.Map;

public class Request {

    String command;
    String appPath;
    String queryString;
    Map<String, String> paramMap = new HashMap<>();
    
    public Request(String command) {
        this.command = command;
        
        // 명령어에서 Query String을 분리한다.
        // ex) manager/detail?no=10
        
        String[] values = command.split("\\?");
        this.appPath = values[0];
        
        if(values.length >= 2) {
            queryString = values[1];
            parseQueryString(queryString);
        }
    }
    
    private void parseQueryString(String qs) {
        // TODO Auto-generated method stub
        String[] values = qs.split("&");
        for(String value : values) {
            String[] kv = value.split("=");
            paramMap.put(kv[0], kv[1]);
        }
    }
    
    public String getParameter(String name) {
        return this.paramMap.get(name);
    }
    
    public String getCommand() {
        return command;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getAppPath() {
        return appPath;
    }
    
    public static void main(String[] args) {
        String str = "manager/detail?name=aaa&email=aaa@test&password=1111";
        Request req = new Request(str);
        
        System.out.println(req.getParameter("name"));
        System.out.println(req.getParameter("email"));
        System.out.println(req.getParameter("password"));
        
/*        assertEquals(req.getCommand(), "manager/detail?no=20");
        assertEquals(req.getAppPath(), "manager/detail");
        assertEquals(req.getQueryString(), "no=20");*/
        
    }
    
}
