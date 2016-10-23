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
function deleteGazo(url, id) {
  $("#" + id).css('display','none');
  httpSend(url);
}

function useGazo(url, id) {
	var useGazo1 = document.getElementById("useGazo1");
	var target = document.getElementById(id);
	useGazo1.appendChild(target);
	$("#sakujo_" + id).css('display','none');
	$("#kaijo_" + id).css('display','inline');
	$("#use_" + id).css('display','none');
	httpSend(url);
}

function kaijoGazo(url, id) {
	var gazo1 = document.getElementById("gazo1");
	var target = document.getElementById(id);
	gazo1.appendChild(target);
	$("#sakujo_" + id).css('display','inline');
	$("#kaijo_" + id).css('display','none');
	$("#use_" + id).css('display','inline');
	httpSend(url);
}

function httpSend(url) {
  var xhr = new XMLHttpRequest();
  xhr.open('POST', url, true);
  xhr.responseType = 'text';
  xhr.onload = function(e) {
    if (this.status == 200) {
      $.unblockUI();
    }
  };
  $.blockUI({ 
            message: '<div class="executing">処理中です．．．</div>', 
            timeout: 10000 
        }); 
  xhr.send();
}

</script>
</head>
<body>

<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>
			作業変更[<span class="strong">${f:h(sagyo.sagyoItemRef.model.name)}</span>]
			${f:h(sagyo.sakudukeRef.model.hatakeRef.model.name)} / ${f:h(sagyo.sakudukeRef.model.name)}
			<span class="small">
				[担当者：${f:h(sagyo.sakudukeRef.model.tantoRef.model.name)} / 
				開始日：<fmt:formatDate value="${sagyo.sakudukeRef.model.startDate}" pattern="yyyy-MM-dd"/>]
			</span>
		</h1>
		<form action="${f:url('update')}" method="post">
			<input type="hidden" ${f:hidden("key")}/>
			<input type="hidden" ${f:hidden("version")}/>
			<input type="hidden" ${f:hidden("page")}/>

			<h2>日付</h2>
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/>年
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/>月
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/>日
			<div>
				<span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
				<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
				<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
			</div>
			
			<h2>使用量</h2>
			<input type="text" ${f:text("amount")} class="w50 num ${f:errorClass('amount', 'err')}"/>
			${f:h(sagyoItem.tanni)}
			<div><span class="${f:errorClass('amount', 'err')}">${f:h(errors.amount)}</span></div>

			<h2>備考</h2>
			<textarea name="biko" class="area500x70 ${f:errorClass('biko', 'err')}">${f:h(biko)}</textarea>
			<div><span class="${f:errorClass('biko', 'err')}">${f:h(errors.biko)}</span></div>

			<h2>作業者</h2>
			<table>
				<thead>
				<tr>
					<th>作業者</th><th>作業時間</th>
				</tr>
				</thead>
				<tbody>
					<c:forEach begin="1" end="5" varStatus="s">
						<c:set var="user">user${s.index}</c:set>
						<c:set var="minutes">minutes${s.index}</c:set>
						<c:if test="${s.index==2}"><div id="hideLines"></c:if>
						<tr id="userLine${s.index}" class="${s.index == 1?'':'hide'}">
							<td class="middle">
								<select name="user${s.index}">
									<c:if test="${s.index > 1}"><option value="">なし</option></c:if>
									<c:forEach var="e" items="${userList}">
										<option ${f:select(user, f:h(e.key))}>${f:h(e.name)}</option>
									</c:forEach>
								</select>
							</td>
							<td class="middle">
								<input type="text" ${f:text(minutes)} class="w50 num ${f:errorClass(minutes, 'err')}"/> 分
								<div><span class="${f:errorClass(minutes, 'err')}">${f:	h(errors[minutes])}</span></div>
							</td>
						</tr>
					</c:forEach>
					</div>
				</tbody>
			</table>
			<button type="button" onclick="$('#userLine2').slideToggle();$('#userLine3').slideToggle();$('#userLine4').slideToggle();$('#userLine5').slideToggle();">行を追加／非表示</button>

			<h2>画像の選択<span class="small">（画像の選択・解除は「作業変更ボタン」を押さなくても更新されます）</span></h2>
			<h3>選択されている画像</h3>
			
			<div class="gazoBox" id="useGazo1">
			
				<c:forEach var="e" items="${useGazoList}">
					<c:set var="gazoThumb" value="/gazo/viewThumb/${f:h(e.key)}"/>
					<c:set var="gazo" value="/gazo/view/${f:h(e.key)}"/>
					
					<div class="gazo" id="${f:h(e.key)}">
						<div class="gazoThumb">
							<a href="${f:url(gazo)}" target="_blank">
								<img src="${f:url(gazoThumb)}" />
							</a>
						</div>
						<div class="gazoRedButtons">
							<div>受信日：<fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></div>
							<c:set var="kaijoGazo" value="/gazo/kaijo/${f:h(e.key)}"/>
							<div class="gazoButton deleteButton" style="display:none;" id="sakujo_${f:h(e.key)}" onclick="deleteGazo('${f:url(deleteGazo)}','${f:h(e.key)}');">
								<i class="fa fa-times"></i>削除する
							</div>
							<div class="gazoButton kaijoButton" id="kaijo_${f:h(e.key)}" onclick="kaijoGazo('${f:url(kaijoGazo)}','${f:h(e.key)}');">
								<i class="fa fa-times"></i>選択解除
							</div>
						</div>
						<div class="gazoGreenButtons">
							<c:set var="useGazo" value="/gazo/useGazo/${f:h(e.key)}/${f:h(sagyo.key)}"/>
							<div class="gazoButton selectButton" style="display:none;" id="use_${f:h(e.key)}" onclick="useGazo('${f:url(useGazo)}','${f:h(e.key)}');">
								<i class="fa fa-check-square-o"></i>選択する
							</div>
						</div>
					</div>
				</c:forEach>
			</div>

			<h3>保存されている画像</h3>
			
			<div class="gazoBox" id="gazo1">
				<c:forEach var="e" items="${gazoList}">
					<c:set var="gazoThumb" value="/gazo/viewThumb/${f:h(e.key)}"/>
					<c:set var="gazo" value="/gazo/view/${f:h(e.key)}"/>
					
					<div class="gazo" id="${f:h(e.key)}">
						<div class="gazoThumb">
							<a href="${f:url(gazo)}" target="_blank">
								<img src="${f:url(gazoThumb)}" />
							</a>
						</div>
						<div class="gazoRedButtons">
							<div>受信日：<fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></div>
							<c:set var="deleteGazo" value="/gazo/delete/${f:h(e.key)}"/>
							<div class="gazoButton deleteButton" id="sakujo_${f:h(e.key)}" onclick="deleteGazo('${f:url(deleteGazo)}','${f:h(e.key)}');">
								<i class="fa fa-times"></i>削除する
							</div>
							<div class="gazoButton kaijoButton" style="display:none;" id="kaijo_${f:h(e.key)}" onclick="kaijoGazo('${f:url(kaijoGazo)}','${f:h(e.key)}');">
								<i class="fa fa-times"></i>選択解除
							</div>
						</div>
						<div class="gazoGreenButtons">
							<c:set var="useGazo" value="/gazo/useGazo/${f:h(e.key)}/${f:h(sagyo.key)}"/>
							<div class="gazoButton selectButton" id="use_${f:h(e.key)}" onclick="useGazo('${f:url(useGazo)}','${f:h(e.key)}');">
								<i class="fa fa-check-square-o"></i>選択する
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
			<h2>使用機械・器具</h2>
			<table>
				<thead>
					<tr><th>使用機械・器具</th></tr>
				</thead>
				<c:forEach begin="1" end="5" varStatus="s">
					<c:set var="kikai">kikai${s.index}</c:set>
					<tr id="kikaiLine${s.index}" style="display:${s.index == 1?'inline':'none'};">
						<td class="middle">
							<select name="kikai${s.index}">
								<option value="">なし</option>
								<c:forEach var="e" items="${sagyoItem.kikaiListRef.modelList}">
									<option ${f:select(kikai, f:h(e.key))}>${f:h(e.name)}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</c:forEach>
			</table>
			<button type="button" onclick="$('#kikaiLine2').slideToggle();$('#kikaiLine3').slideToggle();$('#kikaiLine4').slideToggle();$('#kikaiLine5').slideToggle();">行を追加／非表示</button>
			<h2>種苗及び使用資材</h2>

			<table>
				<thead>
					<tr><th>種苗及び使用資材名</th><th>数量</th></tr>
				</thead>
				<c:forEach begin="1" end="5" varStatus="s">
					<c:set var="shizai">shizai${s.index}</c:set>
					<c:set var="amount">amount${s.index}</c:set>
					<tr id="line${s.index}" class="${s.index == 1?'':'hide'}">
						<td class="middle">
							<select name="shizai${s.index}">
								<option value="">なし</option>
								<c:forEach var="e" items="${sagyoItem.shizaiListRef.modelList}">
									<option ${f:select(shizai, f:h(e.key))}>${f:h(e.name)}(単位：${f:h(e.tanni)})</option>
								</c:forEach>
							</select>
						</td>
						<td class="middle">
							<input type="text" ${f:text(amount)} class="w50 num ${f:errorClass(amount, 'err')}"/>
							<br/><span class="${f:errorClass(amount, 'err')}">${f:h(errors[amount])}</span>
						</td>
					</tr>
				</c:forEach>
			</table>
			<button type="button" onclick="$('#line2').slideToggle();$('#line3').slideToggle();$('#line4').slideToggle();$('#line5').slideToggle();">行を追加／非表示</button>

			<hr/>
			<button type="submit">作業変更</button>
			<input type="hidden" name="key" value="${f:h(sagyoItem.key)}"/>
			<input type="hidden" name="version" value="${f:h(sagyoItem.version)}"/>
			<button type="button" onclick="window.location.href='/sagyo';">作業日誌トップへ</button>
		</form>
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
