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
<link rel="stylesheet" type="text/css" href="/css/global2.css"/>
<title>請求書印刷</title>
<script type="text/javascript" src="${f:url('/prototype.js')}"></script> 
<script type="text/javascript">

</script>
<style type="text/css">
table {background-color:#ffffff;}
th { font-size:xx-small;}
div.print table td { padding:0.5%;font-size:xx-small;}
div.print table td.num { text-align:right;}
@media print {
div#hanko {
	position:fixed;
}
}
</style>
</head>
<body onload="" style="background-color:#ffffff;margin:5%;">

<table style="width:100%;">
	<tr>
		<td style="width:70%;"></td>
		<td style="width:30%;padding-left:5%;">
			<!--
				<div id="hanko" style="text-align:right;z-index:1;"><img style="width:80px;height:80px;" src="${f:url('/hanko.png')}" /></div>
			-->
		</td>
	</tr>
</table>
<div style="text-align:center;font-size:xx-large;">請求書</div>
<table class="noborder" style="width:100%;" border="0">
<tr>
	<td style="width:70%;">
		<div style="font-size:large;">
			${f:h(s.torihikisakiRef.model.name)}
			 様
		</div>
		<div style="margin-top:18px;">${f:h(s.torihikisakiRef.model.biko)}</div>
		<div style="margin-top:18px;">下記の通りご請求申し上げます。</div>
		<div style="font-size:x-large;margin-top:18px;">
			ご請求金額：<fmt:formatNumber value="${s.seikyuKingaku}" pattern="###,###,##0"/>円
		</div>
	</td>
	<td style="width:30%;font-size:x-small;">
		<div style="position:relative;z-index:2;">
		<div style="font-size:x-large;">${f:h(loginYago.name)}</div>
		<div>${f:br(f:h(loginYago.toiawase))}</div>
		<div style="font-size:small;">
		<div style="margin-top:20px;">
			発行日：<fmt:formatDate value="${s.date}" pattern="yyyy年MM月dd日"/>
		</div>
		<div>
			支払期限：<fmt:formatDate value="${s.limitDate}" pattern="yyyy年MM月dd日"/>
		</div>
		</div>
		</div>
	</td>
</tr>
<tr>
	<td colspan="2">
		<div>下記いずれかの銀行口座までお振り込みくださいますようお願い申し上げます。</div>
		<table style="width:100%;">
			<tr>
				<td style="font-size:small;">${f:br(f:h(loginYago.furikomisaki1))}</td>
				<td style="font-size:small;">${f:br(f:h(loginYago.furikomisaki2))}</td>
				<td style="font-size:small;">${f:br(f:h(loginYago.furikomisaki3))}</td>
			</tr>
		</table>
	</td>
</tr>
</table>
<hr/>
明細
<c:forEach var="e0" items="${list}" varStatus="vs">
<c:if test="${vs.index>0}"><hr style="border:0px solid black;page-break-after: always;" /></c:if>
<div class="print">
<table style="width:100%;font-size:xx-small;" border="1" frame="box">
<tr>
	<th style="width:9%">出荷日</th>
	<th style="width:23%">送り先</th>
	<th style="width:27%">商品名【備考】</th>
	<th style="width:3%;font-size:x-small;">単価（円）</th>
	<th style="width:2%;font-size:x-small;">数量</th>
	<th style="width:8%;font-size:x-small;">形態</th>
	<th style="width:9%;font-size:x-small;">金額（円）</th>
	<th style="width:5%;font-size:x-small;">消費税（円）</th>
	<th style="width:5%;font-size:x-small;">送料（円）</th>
	<th style="width:9%;font-size:x-small;">合計（円）</th>
</tr>
<c:forEach var="e" items="${e0}">
	<tr>
		<td rowspan="${e.size}" style="vertical-align:top;">
			<fmt:formatDate value="${e.shukka.date}" pattern="MM月dd日"/>
		</td>
		<td rowspan="${e.size}" style="font-size:4pt;">
			${f:h(e.shukka.okurisaki)}<c:if test="${!empty e.shukka.okurisaki}">様</c:if>
		</td>
		<td>
			${f:h(e.recList[0].hinmokuRef.model.name)}
			<c:if test="${!empty e.recList[0].biko}"><br/>【${f:h(e.recList[0].biko)}】</c:if>
			</td>
		<td class="num"><fmt:formatNumber value="${e.recList[0].tanka}" pattern="###,###,##0"/></td>
		<td class="num" style="font-size:xx-small;"><fmt:formatNumber value="${e.recList[0].suryo}" pattern="###,###,##0.#"/></td>
		<td>${f:h(e.recList[0].shukkaKeitaiRef.model.hoso)}</td>
		<td class="num"><fmt:formatNumber value="${e.recList[0].tanka*e.recList[0].suryo}" pattern="###,###,##0.#"/></td>
		<td class="num" rowspan="${e.size}"><fmt:formatNumber value="${e.shukka.tax}" pattern="###,###,##0"/></td>
		<td class="num" rowspan="${e.size}"><fmt:formatNumber value="${e.shukka.soryo}" pattern="###,###,##0"/></td>
		<td class="num" rowspan="${e.size}"><fmt:formatNumber value="${e.gokei}" pattern="###,###,##0"/></td>
	</tr>
	<c:forEach var="e2" begin="1" items="${e.recList}">
		<tr>
			<td>
				${f:h(e2.hinmokuRef.model.name)}
				<c:if test="${!empty e2.biko}">【${f:h(e2.biko)}】</c:if>
			</td>
			<td class="num"><fmt:formatNumber value="${e2.tanka}" pattern="###,###,##0"/></td>
			<td class="num"><fmt:formatNumber value="${e2.suryo}" pattern="###,###,##0.#"/></td>
			<td>${f:h(e2.shukkaKeitaiRef.model.hoso)}</td>
			<td class="num"><fmt:formatNumber value="${e2.tanka*e2.suryo}" pattern="###,###,##0"/></td>
		</tr>
	</c:forEach>
</c:forEach>
<c:if test="${vs.last}">
	<c:forEach var="t" items="${empList}">
		<tr><td>　</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	</c:forEach>
	<tr>
		<th colspan="6">合計</th>
		<th class="num" style="text-align:right;font-size:xx-small;font-weight:normal;"><fmt:formatNumber value="${sumKingaku}" pattern="###,###,##0"/></th>
		<th class="num" style="text-align:right;font-size:xx-small;font-weight:normal;"><fmt:formatNumber value="${sumTax}" pattern="###,###,##0"/></th>
		<th class="num" style="text-align:right;font-size:xx-small;font-weight:normal;"><fmt:formatNumber value="${sumSoryo}" pattern="###,###,##0"/></th>
		<th class="num" style="text-align:right;font-size:xx-small;font-weight:normal;"><fmt:formatNumber value="${sumGokei}" pattern="###,###,##0"/></th>
	</tr>
</c:if>
</table>
</div>
<div style="text-align:right;font-size:small;">
${vs.count} / ${pageCount} ページ
</div>
</c:forEach>
</body>
</html>
