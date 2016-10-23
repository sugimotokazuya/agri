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
			<h1>ユーザー管理</h1>
			<button type="button" onclick="window.location.href='${f:url('create')}';">ユーザー登録</button>
			<button type="button" onclick="window.location.href='${f:url('../')}';">TOPへ戻る</button>
			
			<table>
				<thead>
					<tr>
						<tr><th>名前</th><th>email</th><th>mobile email</th><th>社員タイプ</th><th>表示</th>
						<c:if test="${loginUser.authUserEdit}">
							<th></th><th></th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${userList}">
						<tr>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.emailStr)}</td>
							<td>${f:h(e.email2Str)}</td>
							<td><c:if test="${e.employeeType==0}">アルバイト</c:if><c:if test="${e.employeeType==1}">正規社員</c:if></td>
							<td><c:if test="${e.delete==false}">表示</c:if><c:if test="${e.delete==true}">非表示</c:if></td>
							<c:if test="${loginUser.authUserEdit}">
								<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(editUrl)}">変更</a></td>
								<c:if test="${e!=owner && e!=loginUser}">
									<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
									</c:if>
									<c:if test="${e==owner || e==loginUser}">
									<td>削除不可</td>
								</c:if>
							</c:if>
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
