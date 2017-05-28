<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import = "model.*" %>
<%@ page import = "java.util.*" %>

<%
	ArrayList<Info> infoList = (ArrayList<Info>) session.getAttribute("infoList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>結果｜GoodPickerSearch</title>
</head>
<body>

<table>
	<tr>
		<th>ユーザID</th>
		<th>ユーザ名</th>
		<th>follower</th>
		<th>following</th>
		<th>URL</th>
	</tr>
<%
	for (Info info : infoList){
%>
		<tr>
			<td><%= info.getId() %></td>
			<td><%= info.getName() %></td>
			<td><%= info.getFollower() %></td>
			<td><%= info.getFollowing() %></td>
			<td><a href="<%= info.getUrl() %>"><%= info.getUrl() %></a></td>
		</tr>
<% } %>
</table>

</body>
</html>