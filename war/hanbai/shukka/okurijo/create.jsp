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

var oMap = new Array();
var haitatsubiMap = new Array();
var kiboujikanMap = new Array();
<c:forEach var="e" items="${oMap}">
	oMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.name)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
	haitatsubiMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.haitatsubi)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
	kiboujikanMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.kiboujikan)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
</c:forEach>

function initTorihikisaki(index) {
	var tKey = $('#torihikisaki' + index).val();
	if(tKey=="") {
		$('#kuchisu' + index).val('');
		$('#haitatsubi' + index).val('1');
		$('#kiboujikan' + index).val('0');
		$('#okurisaki' + index).empty();
		$('#okurisaki' + index).append('<option value="">選択してください</option>');
		return;
	}
	var oList = oMap[tKey];
	$('#okurisaki' + index).empty();
	for(var key in oList) {
		var okurisaki = oList[key];
		var appendStr = '<option style="" value="' + key + '">' + okurisaki + '</option>';
		$('#okurisaki' + index).append(appendStr);
	}
	$('#kuchisu' + index).val('1');
	initOkurisaki(index);
}

function initOkurisaki(index) {
	var oKey = $('#okurisaki' + index).val();
	if(oKey=="") {
		$('#kuchisu' + index).val('1');
		$('#haitatsubi' + index).val('1');
		$('#kiboujikan' + index).val('0');
		return;
	}
	var tKey = $('#torihikisaki' + index).val();
	$('#haitatsubi' + index).val(haitatsubiMap[tKey][oKey]);
	$('#kiboujikan' + index).val(kiboujikanMap[tKey][oKey]);
}

</script>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>送り状作成 - 発送日[${year}年${month}月${day}日]</h1>
		<form action="${f:url('pdf')}" method="post">
			<input type="hidden" id="recordCount" ${f:hidden("recordCount")}/>
			<input type="hidden" id="year" ${f:hidden("year")}/>
			<input type="hidden" id="month" ${f:hidden("month")}/>
			<input type="hidden" id="day" ${f:hidden("day")}/>
			<table id="itemTable">
				<thead>
					<tr>
						<th>取引先</th><th>送り先</th><th>配達日</th><th>希望時間</th><th>口数</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach begin="1" end="${recordCount}" step="1" varStatus="s">
						<tr id="itemTr${s.index}">
							<td>
								<select onchange="initTorihikisaki(${s.index});" id="torihikisaki${s.index}" name="torihikisaki${s.index}" style="width:100%" class="w60">
									<option value="">選択してください</option>
									<c:forEach var="e" items="${tList}">
										<c:set var="torihikisaki">torihikisaki${f:h(s.index)}</c:set>
										<option style="" ${f:select(torihikisaki, f:h(e.key))}>${f:h(e.name)}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<select onchange="initOkurisaki(${s.index});" id="okurisaki${s.index}" name="okurisaki${s.index}" style="width:100%" class="w60">
									<option value="">選択してください</option>
									<c:set var="okurisaki">okurisaki${f:h(s.index)}</c:set>
									<c:forEach var="o" items="${oListArray[s.index-1]}">
										<option style="" ${f:select(okurisaki, f:h(o.key))}>${f:h(o.name)}</option>
									</c:forEach>
								</select>
							</td>

							<c:set var="haitatsubi">haitatsubi${f:h(s.index)}</c:set>
							<td>
								<select id="haitatsubi${s.index}" name="haitatsubi${s.index}" style="width:100%">
									<c:set var="haitatsubi">haitatsubi${s.index}</c:set>
									<option ${f:select(haitatsubi, 1)}>１日後</option>
									<option ${f:select(haitatsubi, 2)}>２日後</option>
								</select>
							</td>
							<td>
								<select id="kiboujikan${s.index}" name="kiboujikan${s.index}" style="width:100%">
									<c:set var="kiboujikan">kiboujikan${s.index}</c:set>
									<option ${f:select(kiboujikan, 0)}>午前中</option>
									<option ${f:select(kiboujikan, 1)}>12時-14時</option>
									<option ${f:select(kiboujikan, 2)}>14時-16時</option>
									<option ${f:select(kiboujikan, 3)}>16時-18時</option>
									<option ${f:select(kiboujikan, 4)}>18時-20時</option>
									<option ${f:select(kiboujikan, 5)}>20時-21時</option>
									<option ${f:select(kiboujikan, 6)}>希望しない</option>
								</select>
							</td>
							<td>
								<select id="kuchisu${s.index}" name="kuchisu${s.index}">
								<c:forEach begin="1" end="9" step="1" varStatus="status">
									<c:set var="k">kuchisu${s.index}</c:set>
									<option ${f:select(k, status.index)}>${status.index}口</option>
								</c:forEach>
								</select>
							</td>
						</tr>
					</c:forEach>
					
				</tbody>
			</table>
			
			<hr/>
			<button type="submit">送り状印刷</button>
			<button type="button" onclick="window.location.href='${f:url('/hanbai')}';">戻る</button>
		</form>
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
