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
<script type="text/javascript">
</script>

</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>年度別出荷重量集計</h1>
		
		<button type="button" id="" onclick="window.location.href='${f:url('/hanbai')}';">戻る</button>
		<button type="button" id="" onclick="window.location.href='${f:url('/start')}';">TOPへ戻る</button>

		<form id="seikyuForm" action="${f:url('seikyu/create')}" method="post">
		</form>		
		
		<form action="${f:url('/hanbai/juryo')}" method="post">
			照会年度：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年度</option>
				</c:forEach>
			</select>
			
			<button type="submit">照会</button>
			
		</form>
		
		<table id="subjects">
			<thead>
				<tr>
					<th>品目</th><th>重量(kg)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="hinmoku" items="${hList}">
					<tr>
						<td>${f:h(hinmoku.name)}</td>
						<td class="num"><fmt:formatNumber value="${map[hinmoku]}" pattern="###,###,##0"/></td>
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





</body>
</html>
