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
<title>売上速報（<fmt:formatDate value="${on.date}" pattern="yyyy年MM月dd日HH時"/>）</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>売上速報（<fmt:formatDate value="${on.date}" pattern="yyyy年MM月dd日HH時"/>）</h1>

		<c:set var="url">notice/${year}/${month}</c:set>
		<button type="button" onclick="window.location.href='${f:url(url)}';">戻る</button>
		<button type="button" onclick="window.location.href='${f:url('/start')}';">TOPへ戻る</button>
		<hr/>
		<table>
			<thead>
				<tr>
					<th rowspan="2">直売店舗</th>
					<c:forEach var="h" items="${hinmokuList}">
					<th colspan="2">${f:h(h.shortName)}</th>
					</c:forEach>
					<th rowspan="2">出荷金額</th>
					<th rowspan="2">売上金額</th>
					</tr>
					<tr>
					<c:forEach var="h" items="${hinmokuList}">
						<th>出</th><th>売</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${chokubaiList}" varStatus="vs">
					<c:set var="sk" value="${map[e.key].sk.kingaku == null ? 0 : map[e.key].sk.kingaku}"/>
					<c:set var="uk" value="${map[e.key].uk.kingaku == null ? 0 : map[e.key].uk.kingaku}"/>
					<c:if test="${sk!=0 || uk!=0}">
						<tr>
							<td>
								<c:if test="${map[e.key].uk != null}">${f:h(map[e.key].uk.chokubaiRef.model.name)}</c:if>
								<c:if test="${map[e.key].uk == null}">${f:h(map[e.key].sk.chokubaiRef.model.name)}</c:if>
							</td>
							<c:forEach var="h" items="${hinmokuList}">
								<c:set var="sc" value="${map[e.key].scMap[h.key]==null ? 0 : map[e.key].scMap[h.key]}"/>
								<c:set var="uc" value="${map[e.key].ucMap[h.key]==null ? 0 : map[e.key].ucMap[h.key]}"/>
								<td class="num">${sc}</td>
									<c:choose>
										<c:when test="${(uc*1)>=(sc*0.9) && uc > 0}"><c:set var="color">tomato</c:set></c:when>
										<c:when test="${(sc*1)>(uc*2)}"><c:set var="color">lightskyblue</c:set></c:when>
										<c:otherwise><c:set var="color">white</c:set></c:otherwise>
									</c:choose>
								<td class="num" style="background-color:${f:h(color)};">${f:h(uc)}</td>
							</c:forEach>
							<td class="num<c:if test="${sk<=uk && uk!=0}"> max</c:if>">
								<fmt:formatNumber value="${sk}" pattern="###,###,##0"/>円
							</td>
							<td class="num<c:if test="${sk<=uk && uk!=0}"> max</c:if>">
								<fmt:formatNumber value="${uk}" pattern="###,###,##0"/>円
							</td>
						</tr>
					</c:if>
				</c:forEach>
				<tr>
				<th>合計</th>
					<c:forEach var="h" items="${hinmokuList}">
						<th style="num">${skSumMap[h.key]}</th>
						<th style="sum">${ukSumMap[h.key]}</th>
					</c:forEach>
					<th style="num"><fmt:formatNumber value="${skSum}" pattern="###,###,##0"/>円</th>
					<th style="num"><fmt:formatNumber value="${ukSum}" pattern="###,###,##0"/>円</th>
				</tr>
			</tbody>
		</table>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
