<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Enumeration<String> en = request.getParameterNames();
while (en.hasMoreElements()) {
String name = en.nextElement();
System.out.println((name + "=[6]=" + request.getParameter(name)));
}
System.out.println("xxxxxxxxxxxxxxx");
%>


