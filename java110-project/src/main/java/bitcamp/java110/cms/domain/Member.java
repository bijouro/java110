package bitcamp.java110.cms.domain;

import java.io.Serializable;

public class Member implements Serializable{

    //여러 속성의 값을 관리하기 쉽도록 사용자 정의 데이터 타입을 만들어 사용한다.

    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected String email;
    
    // Serialize 대상에서 제외 transient
    transient protected String password;

    // 인스턴스의 메모리를 다루는 operator = setter/getter=accessor=property=message
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

 
}
