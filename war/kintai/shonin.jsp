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
			<h1>修正・残業申請一覧</h1>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">勤怠TOPへ戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>
			<br/>
			<table>
				<thead>
					<tr>
						<th>日付</th>
						<th>ユーザー</th>
						<th>種別</th>
						<th>修正前</th>
						<th>修正値</th>
						<th>理由</th>
						<th></th><th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="vs">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<td><fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></td>
							<td>${f:h(e.userRef.model.name)}</td>
							<td>${f:h(e.statusStr)}</td>
							<td>
								<c:if test="${e.beforeTime==null && e.status!=5}">_ _時_ _分</c:if>
								<fmt:formatDate value="${e.beforeTime}" pattern="HH時mm分"/>
							</td>
							<td><fmt:formatDate value="${e.time}" pattern="HH時mm分"/></td>
							<td>${f:h(e.riyu)}</td>
							<c:set var="shoninUrl" value="shoninUpdate/2/${f:h(e.key)}/${e.version}"/>
							<c:set var="kyakkaUrl" value="shoninUpdate/1/${f:h(e.key)}/${e.version}"/>
							<td><a href="${f:url(shoninUrl)}" onclick="return confirm('承認して良いですか?')">承認</a></td>
							<td><a href="${f:url(kyakkaUrl)}" onclick="return confirm('却下して良いですか?')">却下</a></td>
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
