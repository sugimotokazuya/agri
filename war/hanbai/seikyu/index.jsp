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
<title>請求データ</title> 
<script type="text/javascript">

</script>

</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>請求データ</h1>
		
		<button type="button" onclick="window.location.href='${f:url('../')}';">戻る</button>
		
		<form id="seikyuForm" action="${f:url('seikyu/create')}" method="post">
		</form>
		<form action="${f:url('./')}" method="post">
			
			照会年：
			<select name="year">
				<option value="0">すべての年</option>
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			
			取引先：
			<select id="torihikisakiRef" name="torihikisakiRef">
				<option value="">すべての取引先</option>
				<c:forEach var="e" items="${tList}">
					<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
		</form>

		<table id="subjects">
			<thead>
				<tr>
					<th>発行日</th>
					<th>支払期限</th>
					<th>取引先</th>
					<th>請求金額</th>
					<th>状態</th>
					<th>請求書印刷</th>
					<c:if test="${loginUser.authSeikyuEdit}">
						<th>状態変更</th>
						<th>変更</th>
						<th>削除</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${list}" varStatus="vs">
					<tr id="row-${f:h(e.key)}" <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<td><fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></td>
						<td><fmt:formatDate value="${e.limitDate}" pattern="yyyy-MM-dd"/></td>
						<td>${f:h(e.torihikisakiName)} 様</td>
						<td style="text-align:right;"><fmt:formatNumber value="${e.seikyuKingaku}" pattern="###,###,##0"/>円</td>
						<td <c:if test="${e.kaishu==0}">class="alert"</c:if>>${f:h(e.kaishuStr)}</td>
						<td style="text-align:center;">
							<c:set var="url" value="print/${f:h(e.key)}/${e.version}"/>
							<a href="${f:url(url)}" target="_blank">請求書印刷</a>
						</td>
						<c:if test="${loginUser.authSeikyuEdit}">
							<td style="text-align:center;">
								<c:if test="${e.kaishu==0}">
									<c:set var="url" value="kaishuOn/${f:h(e.key)}/${e.version}"/>
									<a href="${f:url(url)}">回収済に変更</a>
								</c:if>
								<c:if test="${e.kaishu==1}">
									<c:set var="url" value="kaishuOff/${f:h(e.key)}/${e.version}"/>
									<a href="${f:url(url)}">未回収に変更</a>
								</c:if>
							</td>
							<td style="text-align:center;">
								<c:set var="url" value="edit/${f:h(e.key)}/${e.version}"/>
								<a href="${f:url(url)}">変更</a></div>
							</td>
							<td style="text-align:center;">
								<c:set var="url" value="delete/${f:h(e.key)}/${e.version}"/>
								<a href="${f:url(url)}" onclick="return confirm('削除して良いですか?')">削除</a>
							</td>
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
