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

<script>

DragArea.setAreaAndArray(document.getElementById("bodyId"), dragarray,dropfunc,"debugMsg");//初期設定の実行

function setPosition(){
	target = document.getElementById('bodyId');
	window.document.onmousemove = function(e){
		$("#x").val(getMousePosition(e).x);
		$("#y").val(getMousePosition(e).y);
	};
}
function getMousePosition(e) {
	
	var obj = new Object();
	if(e) {
		obj.x = e.pageX;
		obj.y = e.pageY;
	}
	else {
		obj.x = event.x + document.body.scrollLeft;
		obj.y = event.y + document.body.scrollTop;
	}
	return obj;
}

function GetLeft(oj){
    var px = 0;
    while(oj){
        px += oj.offsetLeft;
        oj = oj.offsetParent;
    }
    return px;
}
 

function loadImg(imgUrl1, imgUrl2) {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', imgUrl1, true);
  xhr.responseType = "arraybuffer";
  xhr.onload = function() {
    var bytes = new Uint8Array(this.response);
    var binaryData = "";
    for (var i = 0, len = bytes.byteLength; i < len; i++) {
      binaryData += String.fromCharCode(bytes[i]);
    }
    var bytes = new Uint8Array(this.response);
    if (bytes[0] === 0xff && bytes[1] === 0xd8 && bytes[bytes.byteLength-2] === 0xff && bytes[bytes.byteLength-1] === 0xd9) {
      imgSrc = "data:image/jpeg;base64,";
    }
    else if (bytes[0] === 0x89 && bytes[1] === 0x50 && bytes[2] === 0x4e && bytes[3] === 0x47) {
      imgSrc = "data:image/png;base64,";
    }
    else if (bytes[0] === 0x47 && bytes[1] === 0x49 && bytes[2] === 0x46 && bytes[3] === 0x38) {
      imgSrc = "data:image/gif;base64,";
    }
    else if (bytes[0] === 0x42 && bytes[1] === 0x4d) {
      imgSrc = "data:image/bmp;base64,";
    }
    else {
      imgSrc = "data:image/unknown;base64,";
    }
    
    var image = new Image();
    image.src = imgSrc + window.btoa(binaryData);
    image.onload = function() {
      try {
      	
        var canvas = document.getElementById('canvas');
        canvas.width = image.width;
        canvas.height = image.height;
    	var gazo = $('#gazo');
        //gazo.css('display', 'inline');
        if(window.innerWidth>767) {
        	gazo.css('left', $('#x').val() - image.width - 50 + "px");
        } else {
        	gazo.css('left', "0px");
        }
        gazo.css('top', $('#y').val() - 50 + "px");
        gazo.fadeIn();
        
        $('#button2').click(function() {
        	$('#gazo').fadeOut();
        });
        $('#button1').click(function() {
        	window.open(imgUrl2);
        });
        
        if (canvas.getContext) {
          var context = canvas.getContext('2d');
          context.drawImage(image, 0, 0, image.width, image.height);
        }

      } catch (exp) {
        // エラー処理
        return;
      }
    };
  };
  xhr.send();
}
</script>

</head>

<body id="bodyId" onload="setPosition();">
<form action="${f:url('view')}" method="post">
<input type="hidden" ${f:hidden("key")}/>
<div id="gazo">
	<div>
		<div class="button button1" id="button1" onclick="">
			<i class="fa fa-arrows"></i>原寸大表示</div>
			<div class="button button2" id="button2" onclick="">
			<i class="fa fa-times"></i>閉じる
		</div>
	</div>
	<canvas id="canvas">
	</canvas>
</div>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<input type="hidden" id="x"/>
		<input type="hidden" id="y"/>
		<h1>作付の作業を表示</h1>
		<button type="button" onclick="window.location.href='${f:url('/sagyo')}';">作業日誌トップへ</button>
		<button type="button" onclick="window.location.href='${f:url('/sagyo/sakuduke')}';">作付一覧へ</button>
		<h2>
			${f:h(sakuduke.hatakeRef.model.name)} / ${f:h(sakuduke.name)}
			<span class="small">
				[担当者：${f:h(sakuduke.tantoRef.model.name)} / 開始日：<fmt:formatDate value="${sakuduke.startDate}" pattern="yyyy-MM-dd"/>]
			</span>
		</h2>
		
		
		<select name="sagyoItem">
			<option value="">全ての作業項目</option>
			<c:forEach var="e" items="${siList}">
				<option ${f:select("sagyoItem", f:h(e.key))}>${f:h(e.name)}</option>
			</c:forEach>
		</select>
		<button type="submit" onclick="this.form.action='${f:url('view')}';noBlock=false;">照会</button>
		<button type="submit" onclick="this.form.action='${f:url('csv')}';noBlock=true;if(this.form.sagyoItem.selectedIndex==0){alert('作業項目を選択してください');return false;}">日付と量をCSV出力</button>

		<table>
			<thead>
				<tr>
					<th>作業項目</th><th>作業者（時間）</th><th>量</th><th>使用機械</th><th>使用資材</th><th>備考</th><th>画像</th><th></th><th></th>
				</tr>
			</thead>
			<tbody>
				<c:set var="minutes" value="0"/>
				<c:set var="amount" value="0"/>
				<c:forEach var="e2" items="${sakuduke.sagyoListRef.modelList}" varStatus="vs">
					<c:if test="${empty(siKey) || e2.sagyoItemRef.model.key==siKey}">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<td><fmt:formatDate value="${e2.date}" pattern="yyyy-MM-dd"/> ${f:h(e2.sagyoItemRef.model.name)}</td>
							<td>
								<c:forEach var="u" items="${e2.sagyoUserListRef.modelList}">
									${f:h(u.userRef.model.name)}<c:if test="${u.minutes!=null}">（${f:h(u.minutes)}分）</c:if><br/>
									<c:set var="minutes" value="${minutes+u.minutes}"/>
								</c:forEach>
							</td>
							<td class="num">
								${f:h(e2.amount)}<c:if test="${e2.amount!=null}">${f:h(e2.sagyoItemRef.model.tanni)}</c:if>
								<c:set var="amount" value="${amount+e2.amount}"/>
							</td>
							<td>
								<c:forEach var="k" items="${e2.shiyoKikaiListRef.modelList}">
									${f:h(k.kikaiRef.model.name)}<br/>
								</c:forEach>
							</td>
							<td>
								<c:forEach var="e3" items="${e2.shiyoShizaiListRef.modelList}">
									${f:h(e3.shizaiRef.model.name)} ${f:h(e3.amount)}${f:h(e3.shizaiRef.model.tanni)}<br/>
								</c:forEach>
							</td>
							<td>${f:br(f:h(e2.biko))}</td>
							<td>
								<c:forEach var="g" items="${e2.gazoListRef.modelList}">
									<c:set var="imgUrl1" value="/gazo/viewThumb/${f:h(g.key)}"/>
									<c:set var="imgUrl2" value="/gazo/view/${f:h(g.key)}"/>
									<i onclick="loadImg('${f:url(imgUrl1)}','${f:url(imgUrl2)}');" class="fa fa-file-picture-o"></i>
								</c:forEach>
							</td>
							<c:if test="${loginUser.authSagyoEdit}">
								<c:set var="editUrl" value="edit/${f:h(e2.key)}/${e2.version}/0"/>
								<c:set var="deleteUrl" value="delete/${f:h(e2.key)}/${e2.version}/0"/>
								<td><a href="${f:url(editUrl)}">変更</a></td>
								<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
							</c:if>
						</tr>
					</c:if>
				</c:forEach>
				<thead>
					<tr>
						<th>合計</th>
						<th><fmt:formatNumber value="${minutes/60}" pattern="###,##0.#"/>時間</th>
						<th>
							<c:if test="${!empty(siKey) && amount > 0}">
								<fmt:formatNumber value="${amount}" pattern="###,##0.0"/>
								${f:h(si.tanni)}
							</c:if>
						</th>
						<th colspan="2">
							<c:if test="${!empty(siKey) && amount > 0}">
								<fmt:formatNumber value="${amount/(sakuduke.area/10)}" pattern="###,###,##0.0"/>
								${f:h(si.tanni)}/反
							</c:if>
						</th>
						<th colspan="4"></th>
					</tr>
				</thead>
				
			</tbody>
		</table>
		
		<jsp:include page="/footer2.jsp"/>
	</div>
	
	<jsp:include page="/side.jsp"/>
	
</div>
<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
