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
			<h1>送り先 - ${f:h(torihikisaki.name)}</h1>
			<c:if test="${loginUser.authTorihikisakiEdit}">
				<button type="submit">送り先を登録</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/torihikisaki')}';">取引先へ戻る</button>
			
			<table>
				<thead>
					<tr>
						<th>名前</th>
						<th>郵便番号</th>
						<th>住所１</th>
						<th>住所２</th>
						<th>住所３</th>
						<th>電話番号</th>
						<th>送料</th>
						<th>配達希望日</th>
						<th>配達希望時間</th>
						<c:if test="${loginUser.authTorihikisakiEdit}"><th></th><th></th></c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}">
						<tr>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.yubin1)}-${f:h(e.yubin2)}</td>
							<td>${f:h(e.address1)}</td>
							<td>${f:h(e.address2)}</td>
							<td>${f:h(e.address3)}</td>
							<td>${f:h(e.tel)}</td>
							<td><fmt:formatNumber value="${e.soryo}" pattern="###,##0"/></td>
							<td class="num">${f:h(e.haitatsubi)}日後</td>
							<td>${f:h(e.kiboujikanStr)}</td>
							<c:if test="${loginUser.authTorihikisakiEdit}">
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
