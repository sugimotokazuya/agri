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
function selectRow() {
	$("input[type=checkbox]").each(function(index){
		var setval = $(this).val();
		$(this).val([setval]);
	});
}
function unSelectRow() {
	$("input[type=checkbox]").each(function(index){
		$(this).val([]);
	});
}

function seikyu() {
	
	var selectRows = $('[class="selectRow"]:checked').map(function(){
	  //$(this)でjQueryオブジェクトが取得できる。val()で値をvalue値を取得。
	  return $(this).val();
	}).get();
	if(selectRows.length == 0) {
		alert("データを選択してください。");
		return;
	}
	var sForm = document.getElementById('seikyuForm');
	for(var i=0;i<selectRows.length;++i) {
		var q = document.createElement("input");
		q.type="hidden";
		q.name="sKey";
		q.value=selectRows[i];
		sForm.appendChild(q);
	}
	sForm.submit();
}
function test() {

	var selected = selector.getSelected();
	if(selected.length == 0) {
		alert("データを選択してください。");
		return;
	}
	var sForm = $('seikyuForm');
	for(var i=0;i<selected.length;++i) {
		var q = document.createElement("input");
		q.type="hidden";
		q.name="sKey";
		q.value=selected[i];
		sForm.appendChild(q);
	}
	sForm.submit();
}

</script>

</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>出荷データ</h1>
		<c:if test="${!empty(errorStr)}">
			<span class="err">${f:h(errorStr)}</span>
			<hr/>
		</c:if>
		<c:if test="${loginUser.authShukkaEdit}">
			<button type="button" id="shukkaB" onclick="window.location.href='${f:url('./shukka/create')}';">出荷登録</button>
		</c:if>
		<c:if test="${loginUser.authSeikyuView}">
			<button type="button" id="" onclick="window.location.href='${f:url('./seikyu/')}';">請求確認</button>
		</c:if>
		<c:if test="${torihikisakiRef!=null && torihikisakiRef!='' && loginUser.authSeikyuEdit}">
			<button type="button" id="seikyuB" onclick="seikyu();">請求データ作成</button>
		</c:if>
		<c:if test="${loginUser.authShukkaView}">
			<button type="button" id="" onclick="window.location.href='${f:url('./keitai/')}';">出荷形態管理</button>
			<button type="button" id="" onclick="window.location.href='${f:url('./juryo')}';">出荷重量集計</button>
			<button type="button" id="" onclick="window.location.href='${f:url('./shukei')}';">出荷量集計</button>
			<button type="button" id="" onclick="window.location.href='${f:url('./kakuduke')}';">格付管理</button>
		</c:if>
		<button type="button" id="" onclick="window.location.href='${f:url('../start')}';">TOPへ戻る</button>

		<form id="seikyuForm" action="${f:url('seikyu/create')}" method="post">
		</form>		
		
		<form action="${f:url('./')}" method="post">
			照会年月：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			
			<select name="month">
				<option ${f:select("month", "0")}>1月</option>
				<option ${f:select("month", "1")}>2月</option>
				<option ${f:select("month", "2")}>3月</option>
				<option ${f:select("month", "3")}>4月</option>
				<option ${f:select("month", "4")}>5月</option>
				<option ${f:select("month", "5")}>6月</option>
				<option ${f:select("month", "6")}>7月</option>
				<option ${f:select("month", "7")}>8月</option>
				<option ${f:select("month", "8")}>9月</option>
				<option ${f:select("month", "9")}>10月</option>
				<option ${f:select("month", "10")}>11月</option>
				<option ${f:select("month", "11")}>12月</option>
				<option ${f:select("month", "12")}>全て</option>
			</select>
			
			取引先：
			<select id="torihikisakiRef" name="torihikisakiRef">
				<option value="">すべての取引先</option>
				<c:forEach var="e" items="${tList}">
					<option ${f:select("torihikisakiRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
			<div>（※ 取引先を選択すると請求データを作成することができます。）</div>
		</form>
		<c:if test="${torihikisakiRef!=''}">
			<button onclick="selectRow();">全て選択</button>
			<button onclick="unSelectRow();">全て解除</button>
		</c:if>
		<c:set var="url" value="pdfNohins/${todayYear}/${todayMonth}/${todayDay}"/>
		<button onclick="window.open('${f:url(url)}', '_blank');">本日の納品書を印刷</button>
		<c:set var="url" value="shukka/okurijo/create/${todayYear}/${todayMonth}/${todayDay}"/>
		<button onclick="window.open('${f:url(url)}', '_blank');">本日の送り状を印刷</button>
		<table id="subjects">
			<thead>
				<tr>
					<c:if test="${torihikisakiRef!=null && torihikisakiRef!=''}"><th>選<br/>択</th></c:if>
					<th>日付</th>
					<th>
						<div style="border-bottom:dashed 1px gray;">取引先</div>
						<div style="border-bottom:dashed 1px gray;">送り先</div>
						<div>備考</div>
					</th>
					<th>
						<div style="width:100%;text-align:center;">商品【備考】</div>
						<div style="width:25%;float:left;text-align:center;">単価（円）</div>
						<div style="width:50%;float:left;text-align:center;">数量【形態】</div>
						<div style="width:25%;float:right;text-align:center;">価格（円）</div>
					</th>
					<th>小計<br/>（円）</th><th>消費税<br/>（円）</th><th>送料<br/>（円）</th><th>合計<br/>（円）</th>
					<th>状態</th><th>格付<br/>枚数</th><th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="sumShokei" value="0"/>
				<c:set var="sumTax" value="0"/>
				<c:set var="sumSoryo" value="0"/>
				<c:set var="sumGokei" value="0"/>
				<c:forEach var="e" items="${list}" varStatus="vs">
					<tr id="row-${f:h(e.shukka.key)}" <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<c:if test="${torihikisakiRef!=null && torihikisakiRef!=''}">
							<td>
								<c:if test="${e.shukka.kaishu==1}">
									<input class="selectRow" type="checkbox" value="row-${f:h(e.shukka.key)}" />
								</c:if>
							</td>
						</c:if>
						<td>
							<fmt:formatDate value="${e.shukka.date}" pattern="MM"/>月<br/>
							<fmt:formatDate value="${e.shukka.date}" pattern="dd"/>日
						</td>
						<td>
							<div style="border-bottom:dashed 1px gray;<c:if test='${fn:length(e.shukka.torihikisakiName)>15}'>font-size:x-small;</c:if>">
								${f:h(e.shukka.torihikisakiName)} 様
								<c:if test="${empty e.shukka.okurisaki}">${e.shukka.kuchisu}口</c:if>
							</div>
							<div style="border-bottom:dashed 1px gray;<c:if test='${fn:length(e.shukka.okurisaki)>15}'>font-size:x-small;</c:if>">
								<c:if test="${!empty e.shukka.okurisaki}">
									${f:h(e.shukka.okurisaki)} 様 ${e.shukka.kuchisu}口
								</c:if>
								<c:if test="${empty e.shukka.okurisaki}">&nbsp;</c:if>
							</div>
							<div style="<c:if test='${fn:length(e.shukka.biko)>15}'>font-size:x-small;</c:if>">${f:h(e.shukka.biko)}</div>
						</td>
						
						<td>
							<table style="border:0px;width:310px;background-color:transparent;">
								<c:forEach var="e2" items="${e.recList}" varStatus="s">
									<tr>
										<td colspan="3" style="border:0px;">
											${f:h(e2.hinmokuName)}
											<c:if test="${!empty(e2.biko)}">【${f:h(e2.biko)}】</c:if>
										</td>
									</tr>
									<tr>
										<td class="num" style="border:0px;width:15%;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>">
											<fmt:formatNumber value="${e2.tanka}" pattern="###,###,##0"/>
										</td>
										<td class="num" style="border:0px;width:60%;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>">
											<fmt:formatNumber value="${e2.suryo}" pattern="###,###,##0.#"/>
											【${f:h(e2.hosoName)}】
										</td>
										<td class="num" style="border:0px;width:25%;<c:if test="${!s.last}">border-bottom:dashed 1px gray;</c:if>">
											<fmt:formatNumber value="${e2.tanka * e2.suryo}" pattern="###,###,##0"/>
										</td>
									</tr>
								</c:forEach>
							</table>
						</td>
						<c:set var="tmpTax" value="${e.shukka.tax}"/>
						<c:set var="tmpShokei" value="${e.shokei}"/>
						<c:if test="${tmpTax==0 && e.shokei>0}">
							<c:set var="tmpTax" value="${e.shokei-(e.shokei/(1+taxConst/100))}"/>
							<c:set var="tmpShokei" value="${e.shokei-tmpTax}"/>
						</c:if>
						<c:set var="sumShokei" value="${sumShokei+tmpShokei}"/>
						<c:set var="sumTax" value="${sumTax+tmpTax}"/>
						<c:set var="sumSoryo" value="${sumSoryo+e.shukka.soryo}"/>
						<c:set var="sumGokei" value="${sumGokei+e.gokei}"/>
						<c:set var="sumKakudukeMaisu" value="${sumKakudukeMaisu+e.shukka.kakudukeMaisu}"/>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${tmpShokei}" pattern="###,###,##0"/></div></td>
						<td>
							<div style="text-align:right;width:100%;">
								<fmt:formatNumber value="${tmpTax}" pattern="###,###,##0"/>
								<c:if test="${e.shukka.tax==0 && e.shokei>0}"><br/>（内税）</c:if>
							</div>
						</td>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${e.shukka.soryo}" pattern="###,###,##0"/></div></td>
						<td><div style="text-align:right;width:100%;"><fmt:formatNumber value="${e.gokei}" pattern="###,###,##0"/></div></td>
						<td>${f:h(e.shukka.kaishuStr)}</td>
						<td class="num"><fmt:formatNumber value="${e.shukka.kakudukeMaisu}" pattern="###,###,##0"/></td>
						<td style="text-align:center;">
							<c:if test="${e.shukka.torihikisakiRef.model!=null}">
								<c:set var="url" value="pdfNohin/${f:h(e.shukka.key)}/${e.shukka.version}"/>
								<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}" target="_blank">納品書</a></div>
								<!--
								<c:if test="${e.shukka.torihikisakiRef.model.nohin==1}">
									<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}" target="_blank">納品書</a></div>
								</c:if>
								<c:if test="${e.shukka.torihikisakiRef.model.nohin==2}">
									<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}" target="_blank">納品書<br/>（金額有）</a></div>
								</c:if>
								<c:if test="${e.shukka.torihikisakiRef.model.nohin==3}">
									<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}" target="_blank">納品書兼<br/>請求書</a></div>
								</c:if>
								<c:if test="${e.shukka.torihikisakiRef.model.nohin==4}">
									<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}" target="_blank">納品書（宛名は取引先）</a></div>
								</c:if>
								<c:if test="${e.shukka.torihikisakiRef.model.nohin==5}">
									<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}" target="_blank">納品書金額有り（宛名は取引先）</a></div>
								</c:if>
								-->
								<c:if test="${loginUser.authShukkaEdit}">
									<c:set var="url" value="shukka/edit/${f:h(e.shukka.key)}/${e.shukka.version}"/>
									<div style="border-bottom:dashed 1px gray;">
										<a href="${f:url(url)}">変更</a>
										<!-- 格付を変更したかったのでとりあえず
										<c:if test="${e.shukka.kaishu!=2}"><a href="${f:url(url)}">変更</a></c:if>
										<c:if test="${e.shukka.kaishu==2}">変更</c:if>
										-->
									</div>
								</c:if>
								<c:if test="${e.shukka.torihikisakiRef.model.status==0}">
									<c:set var="url" value="shukka/chokubaiEdit/${f:h(e.shukka.key)}"/>
									<div style="border-bottom:dashed 1px gray;"><a href="${f:url(url)}">直売振分</a></div>
								</c:if>
							</c:if>
							<c:if test="${loginUser.authShukkaEdit}">
								<c:set var="url" value="shukka/delete/${f:h(e.shukka.key)}/${e.shukka.version}"/>
								<c:if test="${e.shukka.kaishu!=2}">
									<a href="${f:url(url)}" onclick="return confirm('削除して良いですか?')">削除</a>
								</c:if>
								<c:if test="${e.shukka.kaishu==2}">削除</c:if>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<thead>
				<tr>
					<c:if test="${torihikisakiRef!=null && torihikisakiRef!=''}"><th>&nbsp;</th></c:if>
					<th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th>
					<th><fmt:formatNumber value="${sumShokei}" pattern="###,###,##0"/></th>
					<th><fmt:formatNumber value="${sumTax}" pattern="###,###,##0"/></th>
					<th><fmt:formatNumber value="${sumSoryo}" pattern="###,###,##0"/></th>
					<th><fmt:formatNumber value="${sumGokei}" pattern="###,###,##0"/></th>
					<th>&nbsp;</th>
					<th><fmt:formatNumber value="${sumKakudukeMaisu}" pattern="###,###,##0"/></th>
					<th>&nbsp;</th>
				</tr>
			</thead>
		</table>
		
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>





</body>
</html>
