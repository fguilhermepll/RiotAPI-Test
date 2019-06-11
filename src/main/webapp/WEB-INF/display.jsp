<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Resultado da busca</title>
</head>
<body>
<%
	String sumName = (String)request.getAttribute("sumName");
	int sumIconId = (int)request.getAttribute("sumIconId");
	out.print(sumName + "<br>");
	out.print(sumIconId + "<br>");

	ArrayList<Boolean> winCondition = (ArrayList<Boolean>)request.getAttribute("winCondition");
	
	for(int i = 0; i < 10; i++){
		if(winCondition.get(i) == true){
			out.print("<br>" + "Vitória!");
		}else{
			out.print("<br>" + "Derrota!");
		}
	}
%>
</body>
</html>