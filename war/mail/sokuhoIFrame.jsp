<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
<link rel="stylesheet" type="text/css" href="/css/global.css"/>
<style>

</style>
<script type="text/javascript" src="${f:url('/prototype.js')}"></script>
<script type="text/javascript">
function iframeResize(){
	var PageHight = document.body.scrollHeight + 0; // ページの高さを取得
	window.parent.document.getElementById('disp').style.height = PageHight + 'px'; // iframeの高さを変更
}
window.onload = iframeResize;
</script>
<title>売上速報（<fmt:formatDate value="${on.date}" pattern="yyyy年MM月dd日HH時"/>）</title>
</head>
<body style="margin:20px;">
<fmt:setTimeZone value="Asia/Tokyo" />
売上速報（<fmt:formatDate value="${on.date}" pattern="yyyy年MM月dd日HH時"/>）

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
	<td class="num<c:if test="${sc>uc}"> min</c:if>">${uc}</td>
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

</body>
</html>
