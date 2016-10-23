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
<title>納品書印刷</title>
<script type="text/javascript" src="${f:url('/prototype.js')}"></script> 
<script type="text/javascript">
</script>
<style type="text/css">
table {background-color:#ffffff;}
td { padding:1%;font-size:normal;}
div.print table td {font-size:xx-small;}
div.print table td.num {text-align:right;}
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
		<td style="width:30%;padding-left:2%;">
			<!--<div id="hanko" style="text-align:right;z-index:1;"><img style="width:80px;height:80px;" src="${f:url('/hanko.png')}" /></div>-->
		</td>
	</tr>
</table>
<div style="text-align:center;font-size:xx-large;">納品書</div>

<table class="noborder" style="width:100%;" border="0">
<tr>
	<td style="width:70%;">
		<div style="font-size:large;">
			<c:if test="${s.okurisaki=='' || s.torihikisakiRef.model.nohin==5}">${f:h(s.torihikisakiRef.model.name)}</c:if>
			<c:if test="${s.okurisaki!='' && s.torihikisakiRef.model.nohin!=5}">${f:h(s.okurisaki)}</c:if>
			 様
		</div>
		<div style="font-size:large;">
			<c:if test="${s.okurisaki!='' && s.torihikisakiRef.model.nohin==5}">
				（送り先：${f:h(s.okurisaki)} 様）
			</c:if>
		</div>
		<div style="margin-top:20px;">ご注文ありがとうございました。</div>
		<div style="margin-top:20px;">${f:h(s.torihikisakiRef.model.biko)}</div>
	</td>
	<td style="width:35%;font-size:x-small;">
		<div style="position:relative;z-index:2;">
		<div style="font-size:x-large;">${f:h(loginYago.name)}</div>
		<div>${f:br(f:h(loginYago.toiawase))}</div>
		</div>
	</td>
</tr>
</table>
<hr style="margin:20pt 0pt;" />

<div class="print">
<table style="width:100%;" border="1">
<tr>
	<th style="width:15%">出荷日</th><th style="width:17%">商品名</th>
	<th style="width:24%">備考</th><th style="width:13%">単価（円）</th>
	<th style="width:10%">数量</th><th style="width:8%">形態</th><th style="width:13%">金額（円）</th>
</tr>
<c:set var="sumKingaku" value="0"/>
<tr>
	<td rowspan="${size}" style="vertical-align:top;"><fmt:formatDate value="${s.date}" pattern="yyyy年MM月dd日"/></td>
	<td>${f:h(recList[0].hinmokuRef.model.name)}</td>
	<td>${f:h(recList[0].biko)}</td>
	<td class="num"><fmt:formatNumber value="${recList[0].tanka}" pattern="###,###,##0"/></td>
	<td class="num"><fmt:formatNumber value="${recList[0].suryo}" pattern="###,###,##0.#"/></td>
	<td>${f:h(recList[0].shukkaKeitaiRef.model.hoso)}</div>
	<td class="num"><fmt:formatNumber value="${recList[0].tanka*recList[0].suryo}" pattern="###,###,##0"/></td>
	<c:set var="sumKingaku">${sumKingaku+recList[0].tanka*recList[0].suryo}</c:set>
</tr>
<c:forEach var="e" begin="1" items="${recList}">
		<td>${f:h(e.hinmokuRef.model.name)}</td>
		<td>${f:h(e.biko)}</td>
		<td class="num"><fmt:formatNumber value="${e.tanka}" pattern="###,###,##0"/></td>
		<td class="num"><fmt:formatNumber value="${e.suryo}" pattern="###,###,##0.#"/></td>
		<td>${f:h(e.shukkaKeitaiRef.model.hoso)}</td>
		<td class="num"><fmt:formatNumber value="${e.tanka*e.suryo}" pattern="###,###,##0"/></td>
		<c:set var="sumKingaku">${sumKingaku+e.tanka*e.suryo}</c:set>
	</tr>
</c:forEach>

<c:forEach var="e" items="${empList}">
	<tr>
		<td>　</td><td>　</td><td>　</td><td>　</td><td>　</td><td>　</td><td>　</td>
	</tr>
</c:forEach>
<tr>
<th colspan="6" align="right">金額　</td><td class="num"><fmt:formatNumber value="${sumKingaku}" pattern="###,###,##0"/>円</td>
</tr>
<tr>
<th colspan="6" align="right">消費税　</td><td class="num"><fmt:formatNumber value="${s.tax}" pattern="###,###,##0"/>円</td>
</tr>
<tr>
<th colspan="6" align="right">小計　</td><td class="num"><fmt:formatNumber value="${s.tax+sumKingaku}" pattern="###,###,##0"/>円</td>
</tr>
<tr>
<th colspan="6" align="right">送料　</td><td class="num"><fmt:formatNumber value="${s.soryo}" pattern="###,###,##0"/>円</td>
</tr>
<tr>
<th colspan="6" align="right">合計　</td><td class="num"><fmt:formatNumber value="${s.soryo+s.tax+sumKingaku}" pattern="###,###,##0"/>円</td>
</tr>
</table>
</div>
</body>
</html>
