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
<title>請求データ変更</title>

<script type="text/javascript">

</script>
</head>
<body onload="">
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>請求データ変更</h1>
		
		<form action="${f:url('update')}" method="post">
			<input type="hidden" name="key" value="${f:h(s.key)}"/>
			<input type="hidden" name="version" value="${f:h(s.version)}"/>
			
			<table>
				<tr>
					<th>取引先</th>
					<td>${f:h(s.torihikisakiRef.model.name)} 様</td>
				</tr>
				<tr>
					<th>請求金額</th>
					<td><fmt:formatNumber value="${s.seikyuKingaku}" pattern="###,###,##0"/> 円</td>
				</tr>
				<tr>
					<th>発行日</th>
					<td>
						<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/><span>年</span>
						<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/><span>月</span>
						<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/><span>日</span>
						<br/> <span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
						<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
						<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
					</td>
				</tr>
				<tr>
					<th>期限</th>
					<td>
						<input type="text" ${f:text("limitYear")} class="w50 num ${f:errorClass('limitYear', 'err')}"/>年
						<input type="text" ${f:text("limitMonth")} class="w25 num ${f:errorClass('limitMonth', 'err')}"/>月
						<input type="text" ${f:text("limitDay")} class="w25 num ${f:errorClass('limitDay', 'err')}"/>日
						<br/> <span class="${f:errorClass('limitYear', 'err')}">${f:h(errors.limitYear)}</span>
						<span class="${f:errorClass('limitMonth', 'err')}">${f:h(errors.limitMonth)}</span>
						<span class="${f:errorClass('limitDay', 'err')}">${f:h(errors.limitDay)}</span>
					</td>
				</tr>
			</table>
			<br/>
			
			<table>
				<tr>
					<th>日付</th><th>送り先</th>
					<th style="width:450px;">
						<div style="width:40%;float:left;text-align:center;">商品</div>
						<div style="width:20%;float:left;text-align:center;">単価</div>
						<div style="width:8%;float:left;text-align:center;">数量</div>
						<div style="width:12%;float:left;text-align:center;">形態</div>
						<div style="width:20%;float:right;text-align:center;">価格</div>
					</th>
					<th>小計</th><th>消費税</th><th>送料</th><th>合計</th>
				</tr>
				<c:forEach var="e" items="${list}" varStatus="vs">
					<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<input type="hidden" name="sKey" value="${f:h(e.shukka.key)}"/>
						<td><fmt:formatDate value="${e.shukka.date}" pattern="yyyy-MM-dd"/></td>
						<td>
							<c:if test="${!empty e.shukka.okurisaki}">${f:h(e.shukka.okurisaki)}様</c:if>
						</td>
						<td>
							
							<table style="border:0px;width:100%;background-color:transparent;">
								<c:forEach var="e2" items="${e.recList}" varStatus="s">
									<tr>
										<td style="border:0px;width:40%;background-color:transparent;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>">${f:h(e2.hinmokuRef.model.name)}</td>
										<td style="border:0px;width:20%;background-color:transparent;text-align:right;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>"><fmt:formatNumber value="${e2.tanka}" pattern="###,###,##0"/>円</td>
										<td style="border:0px;width:8%;background-color:transparent;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>">　<fmt:formatNumber value="${e2.suryo}" pattern="###,###,##0"/></td>
										<td style="border:0px;width:12%;background-color:transparent;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>">【${f:h(e2.shukkaKeitaiRef.model.hoso)}】</td>
										<td style="border:0px;width:20%;background-color:transparent;text-align:right;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>"><fmt:formatNumber value="${e2.tanka * e2.suryo}" pattern="###,###,##0"/>円</td>
									</tr>
								</c:forEach>
							</table>
						</td>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${e.shokei}" pattern="###,###,##0"/>円</div></td>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${e.shukka.tax}" pattern="###,###,##0"/>円</div></td>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${e.shukka.soryo}" pattern="###,###,##0"/>円</div></td>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${e.gokei}" pattern="###,###,##0"/>円</div></td>
					</tr>
				</c:forEach>
			</table>
			<hr/>
			<button type="submit">変更</button>
			<button type="button" onclick="window.location.href='${f:url('./')}';">戻る</button>
		</form>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>

</body>
</html>
