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
			<h1>勤務表一覧</h1>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">TOPへ戻る</button>
			<br/>
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
			
			<table>
				<thead>
					<tr>
						<c:if test="${loginUser.authKintai}"><th>氏名</th></c:if>
						<th>年月</th>
						<th>状態</th>
						<th>却下理由</th>
						<th>勤務日数</th>
						<th>勤務時間</th>
						<th>残業時間</th>
						<th></th><th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="vs">
						<c:if test="${e.shonin!=0}">
							<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
								<c:if test="${loginUser.authKintai}"><td>${f:h(e.userRef.model.name)}</td></c:if>
								<td><fmt:formatDate value="${e.date}" pattern="yyyy年MM月"/></td>
								<td>${f:h(e.shoninStr)}</td>
								<td>${f:h(e.riyu)}</td>
								<td class="num">${f:h(e.kinmuDays)}日</td>
								<td class="num">${f:h(e.kinmuHours)}時間</td>
								<td class="num"><fmt:formatNumber value="${e.zangyoMinutes}" pattern="###,###,##0"/>分</td>
								<c:set var="url" value="view/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(url)}">勤務表閲覧</a></td>
								<td>
									<c:if test="${e.employeeType==0}">
										<c:set var="url" value="printA/${f:h(e.key)}/${e.version}"/>
										<a target="_blank" href="${f:url(url)}">アルバイト用印刷</a>
									</c:if>
									<c:if test="${e.employeeType==1}">
										<c:set var="url" value="print/${f:h(e.key)}/${e.version}"/>
										<a target="_blank" href="${f:url(url)}">社員用印刷</a>
									</c:if>
								</td>
							</tr>
						</c:if>
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
