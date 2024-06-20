<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 상세보기</title>
</head>
<body>
	<h1>도서 상세보기</h1>
	<form method="post" action="${pageContext.request.contextPath}/bookDelete">
		<table border="1">
			<c:forEach var="l" items="${list }">
				<tr>
					<th>제목</th>
					<td>
						${l.bookTitle }
					</td>
				</tr>
				<tr>
					<th>작가</th>
					<td>
						${l.bookWriter }
					</td>
				</tr>
				<tr>
					<th>사진</th>
					<td>
						<img src="${pageContext.request.contextPath}/upload/${l.imgName }">
					</td>
				</tr>
				<!-- 도서번호와, 이미지파일의 이름을 hidden으로 같이 보낸다.(삭제 버튼 눌렀을 때) -->
				<input type="hidden" name="imgName" value="${l.imgName }">
				<input type="hidden" name="bookNo" value="${l.bookNo }">
				<a href="${pageContext.request.contextPath}/bookUpdate?bookNo=${l.bookNo }">수정</a>
				<button type="submit">삭제</button>
			</c:forEach>
		</table>
	</form>
</body>
</html>