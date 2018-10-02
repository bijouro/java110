<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP</title>
</head>
<body>
 <h1>JSP 구동 원리</h1>
 <pre>
1) 클라이언트가 JSP 실행을 요청
2) 서블릿 컨테이너
    > JSP 파일에 대응하는 Servlet Class를 찾는다.
    > 있으면,
      >> 현재 JSP 파일로 만든 Servlet Class인지 검사
      >> Servlet class를 만든 후 JSP가   변경된 적이 없다면 
         >>> 해당 servlet 실행
         
      >> Servlet class를 만든 후 JSP가 변경된 적이 있다면
         >>> Servlet class가 없는 경우와 동일하게 처리한다.
      
    > 없으면,
      >> JSP 파일을 가지고 Servlet class를 만든다.
         - $배치폴더/work/.../xxx.java1
      >> compile 한 후 Servlet container에 등록한다
         - $배치폴더/work/.../xxx.class
         
      >> Servlet Class를 실행 한다.
</pre>
 <h1>JSP로 Servlet Class 만들 때 규칙</h1>
 <pre>
> javax.servlet.jsp.HttpJspPage Interface를 구현해야 한다.
> HttpJspPage 상속 계층도
    Servlet
        |
        |-- JspPage
           |
           |-- HttpJspPage
              |
              |-- 자동 생성된 ServletClass

</pre>
<h1>Tomcat Server의 JSP 엔진은 Servlet Class를 어떻게 만들까</h1>
<pre>
> Class 계층도
    HttpServlet(상속), HttpJspPage(구현)
       |
       |--- org.apache.jasper.runtime.HttpJspBase
               | > init(ServletConfig){
               |   ...
               |   jspInit();
               |  }
               | > destroy(){
               |   ...
               |   jspDestroy();
               |  }
               | > service(HttpServletRequest, HttpServletResponse){
               |   _jspService();
               |  }
               |--- tomcat JSP엔진이 JSP를 가지고 만든 서블릿 클래스
                  > jspInit(){....}
                  > jspDestroy(){....};
                  > _jspService(){.....};
</pre>
</body>
</html>