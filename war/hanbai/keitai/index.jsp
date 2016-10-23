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
<title>出荷形態管理</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>出荷形態管理</h1>
		<c:if test="${loginUser.authShukkaEdit}">
			<button type="button" onclick="window.location.href='${f:url('create')}';">出荷形態登録</button>
		</c:if>
		<button type="button" onclick="window.location.href='${f:url('../')}';">戻る</button>
		<c:if test="${!empty(errorStr)}">
			<span class="err">${f:h(errorStr)}</span>
			<hr/>
		</c:if>
		<form action="${f:url('./')}" method="post">
			取引先：
			<select id="torihikisakiRef" name="torihikisakiRef">
				<option value="">すべての取引先</option>
				<c:forEach var="e" items="${tList}">
					<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
		</form>
		<table>
			<thead>
				<tr>
					<th>取引先</th><th>品目</th><th>出荷形態</th><th>重量</th><th>価格</th>
					<c:if test="${loginUser.authShukkaEdit}">
						<th></th><th></th><th></th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${shukkaKeitaiList}" varStatus="vs">
					<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<td>${f:h(e.torihikisakiRef.model.name)}</td>
						<td>${f:h(e.hinmokuRef.model.name)}</td>
						<td>${f:h(e.hoso)}</td>
						<td class="num"><fmt:formatNumber value="${e.juryo}" pattern="###,###,##0"/>g</td>
						<td class="num"><fmt:formatNumber value="${e.price}" pattern="###,###,##0"/>円</td>
						<c:if test="${loginUser.authShukkaEdit}">
							<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
							<c:set var="togoUrl" value="togo/${f:h(e.key)}/${e.version}"/>
							<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
							<td><a href="${f:url(editUrl)}">変更</a></td>
							<td><a href="${f:url(togoUrl)}">他の出荷形態を統合</a></td>
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
