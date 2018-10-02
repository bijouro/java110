<%@ page import="java.io.File,java.io.InputStream" %>
<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" 
    import="java.net.*,java.util.*"
    import="java.lang.ref.*" 
    trimDirectiveWhitespaces="true"
    autoFlush="false"
    buffer="10kb"
 
 %>
 <%@ page import="java.lang.reflect.Method" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP</title>
</head>
<body>
 <h1>지시부(Directive Element)</h1>
 <pre>
   > 생성된 Servlet Class에 특별한 코드를 추가 할때 사용한다
      &lt;%@ 지시어 속성=값 속성=값 %>
           각 속성별로 특별한 자바 코드를 생성한다.
   
   > 종류
      >> &lt;%@ page %>
      >> &lt;%@ include %>
      >> &lt;%@ taglib %>
 </pre>
 <h1>page 지시명령문</h1>
 <pre>
  > language 속성
     >> 기본값 : "java"
     >> 스크립트릿, 표현식에 작성할 수 있는 프로그래밍 언어를 지정한다.
     >> 원래는 다양한 언어를 사용할 수 있게 규칙을 정했지만 현업에서는 "java"만 사용한다.
     >> 그리고 규칙상으로도 "java" 값만 가질 수 있다.

  > contentType 속성
     >> 다음 java code를 _jspSErvice() 매서드 안에 생성한다.
        response.setContentType(속성값);
     
  > pageEncoding
     >> JSP 엔진에게 JSP 파일이 어떤 문자표를 사용하여 인코딩 했는지 알려준다.
      
  > import 속성
     >> Servlet class에 import 코드를 추가 한다.
     >> import 속성에 콤마(,)를 사용하여 여러 import 문장을 작성할 수 있다.
              예) import="java.net.*,java.util.*"
              
     >> import를 각각의 page 지시명령으로 작성할 수 있다.
              예) &lt;@ page import="java.net.file" %>
           &lt;@ page import="java.util.file" %>
           
   > trimDirectiveWhitespaces 속성
     >> true로 설정 하면 지시명령어 태그 뒤에 붙는 줄바꿈 코드는 출력하지 않는다.  
             
   > buffer 속성
     >> 출력 데이터를 임시 보관하는 버퍼의 크기를 설정한다.
     >> 기본 값은 8KB이다
     >> 만약 버퍼 크기를 초과하면 autoFlush의 설정에 따라 처리된다.
     
   > autoFlush 속성
     >> 버퍼가 꽉 찼을 때 클라이언트로 내보낼지 여부를 설정한다.
     >> 기본값은 true     
     >> ture 이면 버퍼를 초과하는 순간 클라이언트로 즉시 출력 된다.
     >> false 이면 버퍼를 초과하는 순간 예외가 발생한다
     
     
     
 </pre>
</body>
</html>
