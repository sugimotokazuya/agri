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
<form action="${f:url('create')}" method="post">
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>使用機械管理 - ${f:h(sagyoItem.name)}</h1>
			<c:if test="${loginUser.authSagyoItemEdit}">
				<button type="submit">使用機械登録</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/sagyo/item')}';">作業項目管理へ戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/sagyo')}';">作業日誌トップへ</button>
			
			<table>
				<thead>
					<tr>
						<th>名前</th>
						<th>汚染防止措置</th>
						<c:if test="${loginUser.authSagyoItemEdit}"><th></th><th></th></c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${kikaiList}">
						<tr>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.osenboushisochi)}</td>
							
							<c:if test="${loginUser.authSagyoItemEdit}">
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
</form>
</body>
</html>
