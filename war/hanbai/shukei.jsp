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
<title>出荷集計</title>
 
<script type="text/javascript">

</script>
</head>
<body>
	
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>出荷集計</h1>
		
		<button type="button" onclick="window.location.href='${f:url('../hanbai')}';">戻る</button>
		
		<form id="seikyuForm" action="${f:url('seikyu/create')}" method="post">
		</form>
		<form action="${f:url('./shukei')}" method="post">
			
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			
			取引先：
			<select id="torihikisakiRef" name="torihikisakiRef" style="">
				<option value="">すべての取引先</option>
				<c:forEach var="e" items="${tList}">
					<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
		</form>
		
		<table>
			<tr>
				<th>品目</th><th>金額</th><th>消費税</th>
			</tr>
			<c:set var="sumKingaku" value="0"/>
			<c:set var="sumTax" value="0"/>
			<c:forEach var="h" items="${hList}">
				<c:if test="${!empty map[h]}">
					<c:set var="sumKingaku" value="${sumKingaku+map[h].kingaku}"/>
					<c:set var="sumTax" value="${sumTax+map[h].tax}"/>
					<tr>
						<td>${f:h(h.name)}</td>
						<td class="num"><fmt:formatNumber value="${map[h].kingaku}" pattern="###,###,##0"/>円</td>
						<td class="num"><fmt:formatNumber value="${map[h].tax}" pattern="###,###,##0"/>円</td>
					</tr>
				</c:if>
			</c:forEach>
			<tr>
				<th rowspan="2">合計</th>
				<th><fmt:formatNumber value="${sumKingaku}" pattern="###,###,##0"/>円</th>
				<th><fmt:formatNumber value="${sumTax}" pattern="###,###,##0"/>円</th>
			</tr>
			<tr>
				<th colspan="2"><fmt:formatNumber value="${sumKingaku+sumTax}" pattern="###,###,##0"/>円</th>
			</tr>
			</table>
		
		<c:if test="${ukSum!=0}">
			<br/>直売所売上金額：<fmt:formatNumber value="${ukSum}" pattern="###,###,##0"/>円
		</c:if>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>

</body>
</html>
