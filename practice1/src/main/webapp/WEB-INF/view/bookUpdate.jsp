<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 수정</title>
</head>
<body>
	<h1>도서 수정</h1>
	<form action="${pageContext.request.contextPath}/bookUpdateAction" method="post" 
	enctype="multipart/form-data"><!-- enctype="multipart/form-data" file이 포함된 form을 전송할 때 사용 -->
		<c:forEach var="l" items="${list }">
			<table border="1">
				<tr>
					<td>제목 수정</td>
					<td colspan="2">
						<input type="text" name="bookTitle" value="${l.bookTitle }">
					</td>
				</tr>
				<tr>
					<td>작가 수정</td>
					<td colspan="2">
						<input type="text" name="bookWriter" value="${l.bookWriter }">
					</td>
				</tr>
				<tr>
					<td>현재표지</td>
					<td>
						<img src="${pageContext.request.contextPath}/upload/${l.imgName }">
					</td>
				</tr>
				<tr>
					<td>표지 수정</td>
					<td>
						<input type="file" name="NewImgName">
					</td>
					<td>
						<input type="hidden" name="imgName" value="${l.imgName }">
						<input type="hidden" name="imgNo" value="${l.imgNo }">
						<input type="hidden" name="bookNo" value="${l.bookNo }">
						<button type="submit">수정</button>
					</td>
				</tr>
			</table>
		</c:forEach>
	</form>
</body>
</html>