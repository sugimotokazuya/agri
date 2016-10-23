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
			<h1>取引先管理</h1>
			<button type="button" onclick="window.location.href='${f:url('create')}';">取引先登録</button>
			<button type="button" onclick="window.location.href='${f:url('../')}';">TOPへ戻る</button>
			<c:if test="${!empty(errorStr)}">
				<span class="err">${f:h(errorStr)}</span>
			</c:if>
			<form action="${f:url('./')}" method="post">
				<input id="all1" type="radio" ${f:radio("all", true)} onchange="this.form.submit();"/>
				<label for="all1">非表示も表示する</label>
				<input id="all2" type="radio" ${f:radio("all", false)} onchange="this.form.submit();"/>
				<label for="all2">非表示は表示しない</label>
			</form>
			<table>
				<thead>
					<tr>
						<th>取引先名</th><th>区分</th><th>備考</th><th>納品書</th><th>納品書の<br/>一括プリント</th><th>表示</th>
						<c:if test="${loginUser.authTorihikisakiEdit}">
							<th></th><th></th><th></th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${torihikisakiList}">
						<tr>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.statusName)}</td>
							<td>${f:h(e.biko)}</td>
							<td>${f:h(e.nohinName)}</td>
							<td>${f:h(e.ikkatsuPrintStr)}</td>
							<td><c:if test="${e.delete==false}">表示</c:if><c:if test="${e.delete==true}">非表示</c:if></td>
							<c:if test="${loginUser.authTorihikisakiEdit}">
								<td><a href="/torihikisaki/okurisaki/index?key=${f:h(e.key)}&version=${e.version}">送り先</a></td>
								<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(editUrl)}">変更</a></td>
								<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
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
