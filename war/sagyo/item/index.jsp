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
<form action="${f:url('./')}" method="post">
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>作業項目管理</h1>
			<button type="button" onclick="window.location.href='${f:url('create')}';">作業項目を登録</button>
			<button type="button" onclick="window.location.href='${f:url('/sagyo')}';">作業日誌トップへ</button>
			
			<table>
				<thead>
					<tr>
						<th>作業名</th><th>単位</th><th>状態</th><th></th><th></th>
						<c:if test="${loginUser.authSagyoItemEdit}">
							<th></th><th></th>
						</c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${sagyoItemList}">
						<tr>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.tanni)}</td>
							<td><c:if test="${e.delete==false}">表示</c:if><c:if test="${e.delete==true}">非表示</c:if></td>
						
							<c:if test="${loginUser.authSagyoItemEdit}">
								<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(editUrl)}">変更</a></td>
								<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
							</c:if>
							<td><a href="../shizai/index?key=${f:h(e.key)}&version=${e.version}">使用資材</a></td>
							<td><a href="../kikai/index?key=${f:h(e.key)}&version=${e.version}">使用機械</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
