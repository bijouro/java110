<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>EL</title>
</head>
<body>
 <h1>EL - Map 객체에서 값 꺼내기 </h1>
 <%
 HashMap<String,Object> map = new HashMap<>();
 map.put("s01", "김구");
 map.put("s02", "안중근");
 map.put("s03", "윤봉길");
 
 pageContext.setAttribute("map",map);
 %>
 
 ${map["s01"]}<br>
 ${map['s02']}<br>
 ${map.s03}<br> 
 
 <pre>
 </pre>

<%-- 보관소가 아닌 로컬 변수는 EL에서 사용할 수 없다. --%>
${nameList[0]}<br>


</body>
</html>