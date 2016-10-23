<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<!DOCTYPE html>
<html lang="ja">
<head>
	<jsp:include page="/head.jsp"/>
</head>

<body>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>お知らせ管理</h1>
			<button type="button" onclick="window.location.href='${f:url('create')}';">お知らせ登録</button>
			<button type="button" onclick="window.location.href='${f:url('../')}';">TOPへ戻る</button>
			
			<table>
				<thead>
					<tr>
						<th>日付</th>
						<th>お知らせタイトル</th>
						<th>お知らせ</th><th></th><th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${oshiraseList}">
						<tr>
							<td><fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></td>
							<td>${f:h(e.header)}</td>
							<td>${f:br(f:h(e.body))}</td>
							<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
							<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
							<td><a href="${f:url(editUrl)}">変更</a></td>
							<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</body>
</html>
