<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.util.ArrayList;"%>
<%@ page import = "getstarted.Customer;"%>
<!DOCTYPE html>
<!-- 用jsp语句,将servlet传过来的list数据拿到,并放到一个list中 -->
<% 
  ArrayList list = (ArrayList) request.getAttribute("customers");
%>
<html>
<head>
<meta charset="utf-8">
<title>菜鸟教程(runoob.com)</title>
</head>
<body>
<p>
    <ul>
    <%  for(int i = 0;i<list.size();i++){
  	Customer customer =(Customer) list.get(i); %>
        <li> <%=customer.getName()%> ( <%=customer.getCity() %>)( <a href='myeditCustomer?id=<%=customer.getId()%>'> edit </a>)
    <% }%>
    </ul>
</p>       
</body> 
</html> 
