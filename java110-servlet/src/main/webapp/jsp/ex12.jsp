<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8" trimDirectiveWhitespaces="true" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP</title>
</head>
<body>
 <h1>JSP 액션 태그 - jsp:useBean</h1>
 <pre>
   > JSP 액션 태그?
     >> JSP 페이지에서 사용할 수 있는 특별한 태그이다.
     >> 특정 자바 코드를 생성한다.
  
   > 보관소에 저장된 객체를 꺼낼때 사용한다.
  
   > 문법 
     &lt;jsp:useBean% 
       scope="보관소이름" 
       id="보관할때 사용한 이름" 
       class="저장된 객체의 타입"        
     %>
   > scope : application(Servlet) 
             session(HttpSession)
             request(HttpServletRequest)
             page(PageContext)  
      scope에 지정하는 값은 빌트인 객체의 변수명이 아니다.
      scope를 지정하지 않으면 기본값은 "page" 이다.
      
    > class :
       >> 보관된 객체의 타입을 가리킨다.
       >> fully-qualified class name 이다. (패키지 명을 포함한 클래스 명이어야 한다.)
          page의 import 속성에 패키지를 지정하는것과 상관없이 반드시 패키지명을 지정해야 한다.
          
       >> 보관소에서 꺼낸 객체를 저장히기 위한 변수의 타입으로 사용된다.
       >> 만약 보관소에 지정된 이름의 객체가 없다면 이 속성에 지정된 객체를 임의로 만들것이다.
       
 </pre>
<% 
// ServletContext 보관소
application.setAttribute("name","홍길동");

//HttpSession 보관소
session.setAttribute("name","임꺽정");

// ServletRequest 보관소
request.setAttribute("name","유관순");

// PageContext 보관소
pageContext.setAttribute("name","안중근");
%>
<jsp:useBean  id="name" class="java.lang.String"/>
 
<p>이름 : <%=name%></p> 

</body>
</html>