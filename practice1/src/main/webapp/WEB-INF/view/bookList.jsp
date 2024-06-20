<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- prefix는 접두사이다. 이 라이브러리를 쓸 때 붙여준다. -->
<!-- JSTL에서 제어구조와 반복구조 사용 c:forEach와 같은.. -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- JSTL에서 다국어, 날짜&시간 형식화와 같은 포맷팅 작업을 위한 태그들을 제공 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!-- JSTL에서 fn:length 같은 함수태그를 제공 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>도서 목록</title>
</head>
<body>
	<h3>도서 목록</h3>
	<div>현재페이지 : ${currentPage }</div>
	<table border="1">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작가</th>
			<th>표지</th>
		</tr>
		<c:forEach var="b" items="${list }">
			<tr>
				<td>${b.bookNo }</td>
				<td><a href="${pageContext.request.contextPath}/bookInfo?bookNo=${b.bookNo }">${b.bookTitle }</a></td>
				<td>${b.bookWriter }</td>
				<td>
					<img src="${pageContext.request.contextPath}/upload/${b.imgName }"
					width="100" height="141">
				</td>
			</tr>
		</c:forEach>
	</table>
	<c:if test="${currentPage > 1}"><!-- 현재페이지가 1보다 크면 이전과 처음 버튼 보이게 -->
		<a href="${pageContext.request.contextPath}/bookList?currentPage=1">처음</a>
		<a href="${pageContext.request.contextPath}/bookList?currentPage=${currentPage-1}">이전</a>
	</c:if>
	<c:if test="${currentPage < lastPage}"><!-- 현재페이지가 마지막페이지보다 작으면 다음과 마지막 버튼 보이게 -->
		<a href="${pageContext.request.contextPath}/bookList?currentPage=${currentPage+1}">다음</a>
		<a href="${pageContext.request.contextPath}/bookList?currentPage=${lastPage}">마지막</a>
	</c:if>
	<a href="${pageContext.request.contextPath}/addBook">도서 등록</a>
</body>
</html>