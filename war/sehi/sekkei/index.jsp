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
<style type="text/css">
td input {
font-size:medium;
}
</style>
<title>施肥設計</title>
<script type="text/javascript" src="${f:url('/prototype.js')}"></script> 
<script type="text/javascript">
var area = ${f:h(sokutei.hatakeRef.model.area)};
var hidu = 1.2;
function calc() {
$('caMin').innerHTML=Math.round($F('cec') * 11.216); 
$('caMax').innerHTML=Math.round($F('cec') * 11.216 * 1.5);
$('mgMin').innerHTML=Math.round($F('cec') * 2.015);
$('mgMax').innerHTML=Math.round($F('cec') * 2.015 * 1.5);
$('kMin').innerHTML=Math.round($F('cec') * 1.6956);
$('kMax').innerHTML=Math.round($F('cec') * 1.6956 * 1.5);
var nSum = 0;var pSum = 0;var caSum = 0;var mgSum = 0; var kSum = 0;var feSum = 0; var mnSum = 0;var bSum = 0;
for(i=0;i<hiryo.length;i++) {
	var nkeisu = Number($('nkeisu_' + hiryo[i]['key']).value);
	nSum += Number(hiryo[i]['seibunN']) * nkeisu / 10000 * Number($F('sehiryo' + i));
	pSum += Number(hiryo[i]['seibunP']) / 100 * Number($F('sehiryo' + i));
	caSum += Number(hiryo[i]['seibunCa']) / 100 * Number($F('sehiryo' + i));
	mgSum += Number(hiryo[i]['seibunMg']) / 100 * Number($F('sehiryo' + i));
	kSum += Number(hiryo[i]['seibunK']) / 100 * Number($F('sehiryo' + i));
	feSum += Number(hiryo[i]['seibunFe']) / 100 * Number($F('sehiryo' + i));
	mnSum += Number(hiryo[i]['seibunMn']) / 100 * Number($F('sehiryo' + i));
	bSum += Number(hiryo[i]['seibunB']) / 100 * Number($F('sehiryo' + i));
	$('juryo_' + hiryo[i]['key']).innerHTML = Number($F('sehiryo' + i)) * area / 10;
	$('fukuro_' + hiryo[i]['key']).innerHTML = (Number($F('sehiryo' + i)) * area / 10 / hiryo[i]['fukuro']).toFixed(1);
}
$('nSum').innerHTML = nSum.toFixed(1);
$('pSum').innerHTML = pSum.toFixed(1);
$('caSum').innerHTML = caSum.toFixed(1);
$('mgSum').innerHTML = mgSum.toFixed(1);
$('kSum').innerHTML = kSum.toFixed(1);
$('feSum').innerHTML = feSum.toFixed(1);
$('mnSum').innerHTML = mnSum.toFixed(1);
$('bSum').innerHTML = bSum.toFixed(1);
$('nh4n10').innerHTML = (nSum / hidu + Number($('seibunNh4n').innerHTML)).toFixed(1);
$('nh4n30').innerHTML = ((nSum / hidu / 3 + Number($('seibunNh4n').innerHTML))).toFixed(1);
$('nh4n20').innerHTML = ((Number($('nh4n10').innerHTML) + Number($('nh4n30').innerHTML)) / 2).toFixed(1);
$('p10').innerHTML = (pSum / hidu + Number($('seibunP').innerHTML)).toFixed(1);
$('p30').innerHTML = ((pSum / hidu / 3 + Number($('seibunP').innerHTML))).toFixed(1);
$('p20').innerHTML = ((Number($('p10').innerHTML) + Number($('p30').innerHTML)) / 2).toFixed(1);
$('ca10').innerHTML = (caSum / hidu + Number($('seibunCa').innerHTML)).toFixed(1);
$('ca30').innerHTML = ((caSum / hidu / 3 + Number($('seibunCa').innerHTML))).toFixed(1);
$('ca20').innerHTML = ((Number($('ca10').innerHTML) + Number($('ca30').innerHTML)) / 2).toFixed(1);
$('mg10').innerHTML = (mgSum / hidu + Number($('seibunMg').innerHTML)).toFixed(1);
$('mg30').innerHTML = ((mgSum / hidu / 3 + Number($('seibunMg').innerHTML))).toFixed(1);
$('mg20').innerHTML = ((Number($('mg10').innerHTML) + Number($('mg30').innerHTML)) / 2).toFixed(1);
$('k10').innerHTML = (kSum / hidu + Number($('seibunK').innerHTML)).toFixed(1);
$('k30').innerHTML = ((kSum / hidu / 3 + Number($('seibunK').innerHTML))).toFixed(1);
$('k20').innerHTML = ((Number($('k10').innerHTML) + Number($('k30').innerHTML)) / 2).toFixed(1);
$('fe10').innerHTML = (feSum * 10 / hidu + Number($('seibunFe').innerHTML)).toFixed(1);
$('fe30').innerHTML = ((feSum * 10 / hidu / 3 + Number($('seibunFe').innerHTML))).toFixed(1);
$('fe20').innerHTML = ((Number($('fe10').innerHTML) + Number($('fe30').innerHTML)) / 2).toFixed(1);
$('mn10').innerHTML = (mnSum * 10 / hidu + Number($('seibunMn').innerHTML)).toFixed(1);
$('mn30').innerHTML = ((mnSum * 10 / hidu / 3 + Number($('seibunMn').innerHTML))).toFixed(1);
$('mn20').innerHTML = ((Number($('mn10').innerHTML) + Number($('mn30').innerHTML)) / 2).toFixed(1);
$('b10').innerHTML = (bSum * 10 / hidu).toFixed(1);
$('b30').innerHTML = ((bSum * 10 / hidu / 3)).toFixed(1);
$('b20').innerHTML = ((Number($('b10').innerHTML) + Number($('b30').innerHTML)) / 2).toFixed(1);
var ca10 = Number($('ca10').innerHTML);
var ca20 = Number($('ca20').innerHTML);
var ca30 = Number($('ca30').innerHTML);
var k10 = Number($('k10').innerHTML);
var k20 = Number($('k20').innerHTML);
var k30 = Number($('k30').innerHTML);
var caMin = Number($('caMin').innerHTML);
var caMax = Number($('caMax').innerHTML);
var mgMin = Number($('mgMin').innerHTML);
var mgMax = Number($('mgMax').innerHTML);
var kMin = Number($('kMin').innerHTML);
var kMax = Number($('kMax').innerHTML);

var ph10 = (6 + ((ca10 + k10 + Number($('seibunMg').innerHTML))
	 - (caMin + mgMin + kMin)) / ((caMax + mgMax + kMax)
	 - (caMin + mgMin + kMin))).toFixed(1);
var ph20 = (6 + ((ca20 + k20 + Number($('seibunMg').innerHTML))
	 - (caMin + mgMin + kMin)) / ((caMax + mgMax + kMax)
	 - (caMin + mgMin + kMin))).toFixed(1);
var ph30 = (6 + ((ca30 + k30 + Number($('seibunMg').innerHTML))
	 - (caMin + mgMin + kMin)) / ((caMax + mgMax + kMax)
	 - (caMin + mgMin + kMin))).toFixed(1);
$('ph10').innerHTML = ph10;
$('ph20').innerHTML = ph20;
$('ph30').innerHTML = ph30;
var phMin = $('phMin').innerHTML;
var phMax = $('phMax').innerHTML;
var nMin = $('nMin').innerHTML;
var nMax = $('nMax').innerHTML;
var pMin = $('pMin').innerHTML;
var pMax = $('pMax').innerHTML;
var feMin = $('feMin').innerHTML;
var feMax = $('feMax').innerHTML;
var mnMin = $('mnMin').innerHTML;
var mnMax = $('mnMax').innerHTML;
var bMin = $('bMin').innerHTML;
var bMax = $('bMax').innerHTML;

vCheck($('ph10'), phMin, phMax);
vCheck($('ph20'), phMin, phMax);
vCheck($('ph30'), phMin, phMax);
vCheck($('nh4n10'), nMin, nMax);
vCheck($('nh4n20'), nMin, nMax);
vCheck($('nh4n30'), nMin, nMax);
vCheck($('p10'), pMin, pMax);
vCheck($('p20'), pMin, pMax);
vCheck($('p30'), pMin, pMax);
vCheck($('ca10'), caMin, caMax);
vCheck($('ca20'), caMin, caMax);
vCheck($('ca30'), caMin, caMax);
vCheck($('mg10'), mgMin, mgMax);
vCheck($('mg20'), mgMin, mgMax);
vCheck($('mg30'), mgMin, mgMax);
vCheck($('k10'), kMin, kMax);
vCheck($('k20'), kMin, kMax);
vCheck($('k30'), kMin, kMax);
vCheck($('fe10'), feMin, feMax);
vCheck($('fe20'), feMin, feMax);
vCheck($('fe30'), feMin, feMax);
vCheck($('mn10'), mnMin, mnMax);
vCheck($('mn20'), mnMin, mnMax);
vCheck($('mn30'), mnMin, mnMax);
vCheck($('b10'), bMin, bMax);
vCheck($('b20'), bMin, bMax);
vCheck($('b30'), bMin, bMax);
}
function vCheck(obj, min, max) {
	var minColor = "white";
	var maxColor = "white";
	var normalColor = "black";
	var minBackColor = "lightskyblue";
	var maxBackColor = "tomato";
	var normalBackColor = "white";
	var v = Number(obj.innerHTML);
	if(min > v) {
		obj.style.backgroundColor = minBackColor;
		obj.style.fontWeight = "bold";
		obj.style.color = minColor;
	} else if(max < v) {
		obj.style.backgroundColor = maxBackColor;
		obj.style.fontWeight = "bold";
		obj.style.color = maxColor;
	} else {
		obj.style.backgroundColor = normalBackColor;
		obj.style.fontWeight = "normal";
		obj.style.color = normalColor;
	}
}
</script>
</head>
<body onload="calc();var periodicalAleter = new PeriodicalExecuter(calc,0.5);">
<jsp:include page="/header.jsp"/>
<div class="title"><span style="float:left;margin-right:50px;">施肥設計</span>
<table>
<tr><th>畑</th><td>${f:h(sokutei.hatakeRef.model.name)}</td>
<th>作物名</th><td>${f:h(sokutei.hinmoku)}</td>
<th>面積(a)</th><td class="num">${f:h(sokutei.hatakeRef.model.area)}</td>
<th>日付</th><td><fmt:formatDate value="${sokutei.date}" pattern="yyyy/MM/dd"/></td>
</tr>
</table></div>
<hr/>
<div style="float:left; margin-right:10px;"">
<table>
<thead>
<tr><th>診断項目</th><th>測定値</th><th>下限値</th><th>上限値</th><th>10cm</th><th>20cm</th><th>30cm</th><th>成分量</th></tr>
</thead>
<tbody>
<tr>
<th>CEC</th>
<td class="num"><input type="hidden" id="cec" value="${f:h((sokutei.seibunCa+sokutei.seibunMg+sokutei.seibunK)/(-30.87+7.63*sokutei.phH2o))}"/><fmt:formatNumber value="${f:h((sokutei.seibunCa+sokutei.seibunMg+sokutei.seibunK)/(-30.87+7.63*sokutei.phH2o))}" pattern="##0.0" /></td>
<td class="num">20</td><td class="num">30</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
</tr>
<tr>
<th>pH(H2O)</th><td class="num">${f:h(sokutei.phH2o)}</td><td class="num" id="phMin">6</td><td class="num" id="phMax">7</td>
<td id="ph10" class="num"></td><td class="num" id="ph20"></td><td id="ph30" class="num"></td><td>&nbsp;</td>
</tr>
<tr>
<th>pH(KCl)</th><td class="num">${f:h(sokutei.phKcl)}</td><td class="num">5</td><td class="num">6</td>
<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
</tr>
<tr>
<th>NH4N</th><td class="num" id="seibunNh4n">${f:h(sokutei.seibunNh4n)}</td><td class="num" id="nMin">0.8</td><td class="num" id="nMax">9</td>
<td id="nh4n10" class="num"></td><td id="nh4n20" class="num"></td><td id="nh4n30" class="num"></td><td rowspan="2" id="nSum" class="num"></td>
</tr>
<tr>
<th>NO3N</th><td class="num">${f:h(sokutei.seibunNo3n)}</td><td class="num">0.8</td><td class="num">15</td>
<td id="no3n10" class="num"></td><td id="no3n20" class="num"></td><td id="no3n30" class="num"></td>
</tr>
<tr>
<th>P2O</th><td class="num" id="seibunP">${f:h(sokutei.seibunP)}</td><td class="num" id="pMin">20</td><td class="num" id="pMax">60</td>
<td id="p10" class="num"></td><td id="p20" class="num"></td><td id="p30" class="num"></td><td id="pSum" class="num"></td>
</tr>
<tr>
<th>CaO</th><td class="num" id="seibunCa">${f:h(sokutei.seibunCa)}</td><td id="caMin" class="num"></td><td id="caMax" class="num"></td>
<td id="ca10" class="num"></td><td id="ca20" class="num"></td><td id="ca30" class="num"></td><td id="caSum" class="num"></td>
</tr>
<tr>
<th>MgO</th><td class="num" id="seibunMg">${f:h(sokutei.seibunMg)}</td><td id="mgMin" class="num"></td><td id="mgMax" class="num"></td>
<td id="mg10" class="num"></td><td id="mg20" class="num"></td><td id="mg30" class="num"></td><td id="mgSum" class="num"></td>
</tr>
<tr>
<th>K2O</th><td class="num" id="seibunK">${f:h(sokutei.seibunK)}</td><td id="kMin" class="num"></td><td id="kMax" class="num"></td>
<td id="k10" class="num"></td><td id="k20" class="num"></td><td id="k30" class="num"></td><td id="kSum" class="num"></td>
</tr>
<tr>
<th>Fe</th><td class="num" id="seibunFe">${f:h(sokutei.seibunFe)}</td><td class="num" id="feMin">10</td><td class="num" id="feMax">30</td>
<td id="fe10" class="num"></td><td id="fe20" class="num"></td><td id="fe30" class="num"></td><td id="feSum" class="num"></td>
</tr>
<tr>
<th>Mn</th><td class="num" id="seibunMn">${f:h(sokutei.seibunMn)}</td><td class="num" id="mnMin">10</td><td class="num" id="mnMax">30</td>
<td id="mn10" class="num"></td><td id="mn20" class="num"></td><td id="mn30" class="num"></td><td id="mnSum" class="num"></td>
</tr>
<tr>
<th>B</th><td class="num" id="seibunB">&nbsp;</td><td class="num" id="bMin">0.8</td><td class="num" id="bMax">3.6</td>
<td id="b10" class="num"></td><td id="b20" class="num"></td><td id="b30" class="num"></td><td id="bSum" class="num"></td>
</tr>
<tr>
<th>NaCl</th><td class="num">${f:h(sokutei.seibunEnbun)}</td><td class="num"></td><td class="num"></td>
<td id="enbun10" class="num"></td><td id="enbun20" class="num"></td><td id="enbun30" class="num"></td><td id="enbunSum" class="num"></td>
</tr>
</tbody>
</table>
</div>
<form action="${f:url('save')}" method="post">
<c:if test="${fn:length(hiryoList)==0}">
<span class="err">【肥料】を登録してください。</span>
</c:if>
<table>
<thead>
<tr><th>肥料名</th><th>N</th><th>N係数</th><th>P</th><th>K</th><th>Ca</th><th>Mg</th><th>B</th><th>Mn</th><th>Fe</th><th>kg/袋</th><th>施肥量(kg/反)</th><th>重量</th><th>袋数</th></tr>
</thead>
<tbody>
<c:forEach var="e" varStatus="i" items="${hiryoList}">
<tr>
<td>${f:h(e.name)}</td>
<td class="num">${f:h(e.seibunN)}</td>
<td class="num">
<c:set var="nkeisuKey" value="nkeisu_${f:h(e.key)}_${e.version}"/>
<input ${f:text(nkeisuKey)} maxlength="3" onchange="calc();" style="width:55px;height:30px;text-align:right;" type="text" id="nkeisu_${f:h(e.key)}_${e.version}" class="sehi ${f:errorClass(nkeisuKey, 'err')}"/>
</td>
<td class="num">${f:h(e.seibunP)}</td>
<td class="num">${f:h(e.seibunK)}</td>
<td class="num">${f:h(e.seibunCa)}</td>
<td class="num">${f:h(e.seibunMg)}</td>
<td class="num">${f:h(e.seibunB)}</td>
<td class="num">${f:h(e.seibunMn)}</td>
<td class="num">${f:h(e.seibunFe)}</td>
<td class="num">${f:h(e.weight)}</td>
<td>
<c:set var="sehiryoKey" value="sehiryo_${f:h(e.key)}_${e.version}"/>
<input maxlength="4" ${f:text(sehiryoKey)} onchange="calc();" style="width:80px;height:30px;text-align:right;" type="text" id="sehiryo${i.index}" name="sehiryo_${f:h(e.key)}_${e.version}" class="sehi ${f:errorClass(sehiryoKey, 'err')}"/>
</td>
<td class="num" id="juryo_${f:h(e.key)}_${e.version}"></td>
<td class="num" id="fukuro_${f:h(e.key)}_${e.version}"></td>
</tr>
</c:forEach>
<script type="text/javascript">
var hiryo = [];
<c:forEach var="e" varStatus="i" items="${hiryoList}">
hiryo[${i.index}] = { seibunN: ${f:h(e.seibunN)}, seibunP: ${f:h(e.seibunP)}
, seibunCa: ${f:h(e.seibunCa)}, seibunMg: ${f:h(e.seibunMg)}, seibunK: ${f:h(e.seibunK)}
, seibunFe: ${f:h(e.seibunFe)}, seibunMn: ${f:h(e.seibunMn)}, seibunB: ${f:h(e.seibunB)}
, fukuro: ${f:h(e.weight)}, key: '${f:h(e.key)}_${e.version}' };
</c:forEach>
</script>
</tbody>
</table>
<hr/>
<c:if test="${loginUser.authSehiEdit}">
<input class="w50" type="submit" value="設計保存"/>
</c:if>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('../sokutei')}';"/>
<input type="hidden" ${f:hidden("key")}/>
<input type="hidden" ${f:hidden("version")}/>
</form>
</body>
</html>
