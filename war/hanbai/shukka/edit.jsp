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

function addRow() {
	var itemCount = $("#itemCount").val();
	var nextCount = Number(itemCount) + 1;
	$('#itemTr' + itemCount).after('<tr id="itemTr' + nextCount + '"><td>'
		+ '<select onchange="initHoso(' + nextCount + ');" id="hinmoku' + nextCount
		+ '" name="hinmoku' + nextCount + '">'
		+ '<c:forEach var="e" items="${hList}"><option style="" value="${f:h(e.key)}">${f:h(e.name)}</option></c:forEach>'
		+ '</select></td><td>'
		+ '<input id="tanka' + nextCount + '" type="text" name="tanka' + nextCount + '" class="w50 num"/></td>'
		+ '<td><input id="suryo' + nextCount + '" type="text" name="suryo' + nextCount + '" class="w50 num"/></td>'
		+ '<td><select onchange="initPrice(' + nextCount + ');" id="shukkaKeitai' + nextCount
		+ '" name="shukkaKeitai' + nextCount + '" style="width:100%" class="w60"></td>'
		+ '<td><input id="kakuduke' + nextCount + '" type="text" name="kakuduke' + nextCount + '" class="w50 num"/></td>'
		+ '<td><input id="biko' + nextCount + '" type="text" name="biko' + nextCount + '" class="w200"/></td>'
		+ '<td><div id="kakaku' + nextCount + '" style="text-align:right;"></div></td></tr>');
						
	itemCount++;
	initHoso(itemCount);
	$("#itemCount").val(itemCount);
}

var skMap = new Array();
var pMap = new Array();
<c:forEach var="e" items="${skMap}">
skMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.hoso)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
pMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.price)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
</c:forEach>

var tSoryoMap = new Array();
<c:forEach var="e" items="${tList}">
	tSoryoMap['${f:h(e.key)}'] = ${e.soryo};
</c:forEach>

var oMap = new Array();
var soryoMap = new Array();
<c:forEach var="e" items="${oMap}">
	oMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.name)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
	soryoMap['${f:h(e.key)}'] = {<c:forEach var="e2" items="${e.value}" varStatus="vs">'${f:h(e2.key)}':"${f:h(e2.soryo)}"<c:if test="${!vs.last}">,</c:if></c:forEach>};
</c:forEach>

function initHosoAll() {
	for(i=1;i<=$("#itemCount").val();i++) {
		initHoso(i);
	}
	initOkurisaki();
	$('#soryo').val(tSoryoMap[$('#torihikisakiRef').val()]);
}
function initOkurisaki() {
	$('#okurisaki').val('');
	initFirstOkurisaki();
}
function initFirstOkurisaki() {
	var oKey = $('#torihikisakiRef').val();
	var oList = oMap[oKey];
	$('#okurisakiSelect').empty();
	$('#okurisakiSelect').append('<option value="">選択してください</option>');
	for(var key in oList) {
		var okurisaki = oList[key];
		var appendStr;
		if(okurisaki == $('#okurisaki').val()) {
			appendStr = '<option selected style="" value="' + key + '">' + okurisaki + '</option>';
		} else {
			appendStr = '<option style="" value="' + key + '">' + okurisaki + '</option>';
		}
		$('#okurisakiSelect').append(appendStr);
	}
}


function selectOkurisaki() {
	var okurisaki = $('[id=okurisakiSelect] option:selected');
	if(okurisaki.val()!='') {
		$('#okurisaki').val(okurisaki.text());
		var soryoList = soryoMap[$('#torihikisakiRef').val()];
		for(var key in soryoList) {
			if(key == okurisaki.val()) {
				$('#soryo').val(soryoList[key] * $('[id=kuchisu] option:selected').val());
			}
		}
	} else {
		$('#okurisaki').val('');
		$('#soryo').val(tSoryoMap[$('#torihikisakiRef').val()] * $('[id=kuchisu] option:selected').val());
	}
}

function initHoso(i) {
	var hoso1Key = $('#torihikisakiRef').val() + ':' + $('#hinmoku' + i).val();
	var skList = skMap[hoso1Key];
	$('#shukkaKeitai' + i).empty();
	for(var key in skList) {
		var hoso = skList[key];
		var appendStr = '<option style="" value="' + key + '">' + hoso + '</option>';
		$('#shukkaKeitai' + i).append(appendStr);
	}
	initPrice(i);
}

function initPrice(i) {
	var key1 = $('#torihikisakiRef').val() + ':' + $('#hinmoku' + i).val();
	var target = $('#shukkaKeitai' + i).val();
	var pList = pMap[key1];
	for(var key in pList) {
		if(target == key) {
			$('#tanka' + i).val(pList[key]);
		}
	}
}


function calc() {

	var sum = 0;
	var focusId = $(':focus').attr('id');
	for(i=1;i<=$("#itemCount").val();i++) {
		$('#kakaku' + i).text(String($('#tanka' + i).val() * $('#suryo' + i).val()).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + '円');
		sum += $('#tanka' + i).val() * $('#suryo' + i).val();
		if(('suryo' + i) == focusId && $('#kakuduke').val() == '') {
			$('#kakuduke' + i).val($('#suryo' + i).val());
		}
	}
	$('#shokei').text(String(sum).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + '円');
	var taxTemp = Math.floor(sum*${taxConst}/100);
	if($('#taxKomi:checked').val()) taxTemp = 0;
	$('#tax').val(taxTemp);
	$('#taxDisp').text(String($('#tax').val()).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + '円');
	$('#gokei').text(String(sum+taxTemp+Number($('#soryo').val())).replace( /(\d)(?=(\d\d\d)+(?!\d))/g, '$1,') + '円');
}

</script>
</head>
<body onload="initFirstOkurisaki();setInterval('calc()',500);">
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>出荷変更</h1>
		<form action="${f:url('update')}" method="post">
			<input type="hidden" ${f:hidden("key")}/>
			<input type="hidden" ${f:hidden("version")}/>
			<input type="hidden" id="tax" ${f:hidden("tax")}/>
			<input type="hidden" id="itemCount" ${f:hidden("itemCount")}/>
			
			<h2>出荷情報</h2>
			<table>
				<tr>
					<th>出荷日</th>
					<td colspan="3">
						<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/><span>年</span>
						<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/><span>月</span>
						<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/><span>日</span>
						<div>
							<span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
							<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
							<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
						</div>
					</td>
				</tr>
				<tr>
					<th>取引先</th>
					<td colspan="3">
						<select onchange="initHosoAll();" id="torihikisakiRef" name="torihikisaki">
							<c:forEach var="e" items="${tList}">
								<option ${f:select("torihikisaki", f:h(e.key))}>${f:h(e.name)}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>送り先</th>
					<td colspan="3">
						<select onchange="selectOkurisaki();" id="okurisakiSelect" name="okurisakiSelect">
						</select>
						<select onchange="selectOkurisaki();" id="kuchisu" name="kuchisu">
							<c:forEach begin="1" end="9" step="1" varStatus="status">
								<option ${f:select("kuchisu", status.index)}>${status.index}口</option>
							</c:forEach>
						</select>
						<input style="" id="okurisaki" type="text" ${f:text("okurisaki")} class="w300 ${f:errorClass('okurisaki', 'err')}"/>
						<span style=""> 様</span>
					</td>
				</tr>
				<tr>
					<th>備考</th>
					<td colspan="3">
						<input style="" type="text" ${f:text("biko")} class="w300 ${f:errorClass('biko', 'err')}"/>
					</td>
				</tr>
				<tr>
					<th>格付担当者</th>
					<td>
						<select name="kakudukeTantoRef">
							<c:forEach var="e" items="${kakudukeTantoList}">
								<option ${f:select("kakudukeTantoRef", f:h(e.key))}>${f:h(e.name)}</option>
							</c:forEach>
						</select>
					</td>
					<th>格付枚数</th>
					<td>
						<input id="kakuduke" type="text" ${f:text("kakuduke")} class="num w50 ${f:errorClass('kakuduke', 'err')}"/>
					</td>
				</tr>
			</table>
			
			<h2>商品</h2>
			<table id="itemTable">
				<thead>
					<tr>
						<th>品目</th><th>単価（円）</th><th>数量</th><th>包装形態</th><th>格付枚数</th><th>備考</th><th>価格</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach begin="1" end="${itemCount}" step="1" varStatus="s">
						<tr id="itemTr${s.index}">
							<td>
							<select onchange="initHoso(${s.index});" id="hinmoku${s.index}" name="hinmoku${s.index}" style="width:100%" class="w60">
							<c:forEach var="e" items="${hList}">
								<c:set var="hinmoku">hinmoku${f:h(s.index)}</c:set>
								<option style="" ${f:select(hinmoku, f:h(e.key))}>${f:h(e.name)}</option>
							</c:forEach>
							</select>
							</td>
							<c:set var="tanka">tanka${f:h(s.index)}</c:set>
							<td><input id="tanka${s.index}" type="text" ${f:text(tanka)} class="w50 num ${f:errorClass(tanka, 'err')}"/></td>
							<c:set var="suryo">suryo${f:h(s.index)}</c:set>
							<td><input id="suryo${s.index}" type="text" ${f:text(suryo)} class="w50 num ${f:errorClass(suryo, 'err')}"/></td>
							<td>
								<select onchange="initPrice(${s.index});" id="shukkaKeitai${s.index}" name="shukkaKeitai${s.index}" style="width:100%" class="w60">
									<c:set var="skList">skList${f:h(s.index)}</c:set>
									<c:set var="shukkaKeitai">shukkaKeitai${f:h(s.index)}</c:set>
									<c:forEach var="sk" items="${skListArray[s.index-1]}">
										<option style="" ${f:select(shukkaKeitai, f:h(sk.key))}>${f:h(sk.hoso)}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<c:set var="kakuduke">kakuduke${f:h(s.index)}</c:set>
								<input id="kakuduke${s.index}" type="text" ${f:text(kakuduke)} class="w50 num ${f:errorClass(kakuduke, 'err')}"/>
							</td>
							<td>
								<c:set var="biko">biko${f:h(s.index)}</c:set>
								<input id="biko${s.index}" type="text" ${f:text(biko)} class="w200 ${f:errorClass(biko, 'err')}"/>
							</td>
							<td><div id="kakaku${s.index}" style="width:100%;text-align:right;"></div></td>
						</tr>
					</c:forEach>
					<thead>
					<tr>
					<th colspan="2"><button type="button" onclick="addRow();">行を追加</button></th>
					<th colspan="4">小計</th>
					<th><div id="shokei" style="width:100%;text-align:right;"></div></th>
					</tr>
					<tr>
					<th colspan="6">消費税（税込み<input type="checkbox" id="taxKomi" ${f:checkbox("taxKomi")}/>）</th><th><div id="taxDisp" style="width:100%;text-align:right;"></div></th>
					</tr>
					<tr>
					<th colspan="6">送料(四国:453円, 関西:504円, 関東:576円, 東海555円)</th><th>
					<input type="text" id="soryo" ${f:text("soryo")} class="w50 num ${f:errorClass('soryo', 'err')}"/>
					<span style="">円</span>
					<br/>
					<span class="${f:errorClass('soryo', 'err')}">${f:h(errors.soryo)}</span>
					</th>
					</tr>
					<tr>
					<th colspan="6">合計</th><th><div id="gokei" style="width:100%;text-align:right;ds"></div></th>
					</tr>
					</thead>
				</tbody>
			</table>
			
			<hr/>
			<button type="submit">変更</button>
			<button type="button" onclick="window.location.href='${f:url('/hanbai')}';">戻る</button>
		</form>
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
