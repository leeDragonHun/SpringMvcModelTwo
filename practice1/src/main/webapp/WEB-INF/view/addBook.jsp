<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 등록</title>
</head>
<body>
	<h1>도서 등록</h1>
	<form action="${pageContext.request.contextPath}/addBook" method="post" 
	enctype="multipart/form-data"><!-- file이 포함된 form을 전송할 때 사용 -->
		<table border="1">
			<tr>
				<td>제목</td>
				<td colspan="2">
					<input type="text" name="bookTitle">
				</td>
			</tr>
			<tr>
				<td>작가</td>
				<td colspan="2">
					<input type="text" name="bookWriter">
				</td>
			</tr>
			<tr>
				<td>표지</td>
				<td>
					<input type="file" name="imgName">
				</td>
				<td>
					<button type="submit">등록</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>