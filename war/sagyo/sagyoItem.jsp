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
<form action="${f:url('./sagyoItem')}" method="post">
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
			<h1>作業項目別で作業を表示</h1>
			<select name="itemRef">
				<option value="">選択してください</option>
				<c:forEach var="e" items="${siList}">
					<option ${f:select("itemRef", f:h(e.key))}>${f:h(e.name)}</option>
				</c:forEach>
			</select>
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			<button type="submit">照会</button>
			<button type="submit" target="_blank" name="pdf">使用資材管理リスト(PDF)</button>
			<button type="button" onclick="window.location.href='${f:url('/sagyo')}';">作業日誌トップへ</button>
			<h2>作業項目：${f:h(si.name)} （${f:h(year)}年）</h2>
	
			<div>集計</div>
			<table>
				<thead>
					<tr><th>ほ場</th><th>作付</th><th>面積</th><th>集計値</th><th>反当</th></tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${map}">
						<tr>
							<td>${f:h(e.key.hatakeRef.model.name)}</td>
							<td>${f:h(e.key.name)}</td>
							<td class="num">${f:h(e.key.area)} a</td>
							<td class="num"><fmt:formatNumber value="${e.value}" pattern="###,###,##0.0"/> ${f:h(si.tanni)}</td>
							<td class="num"><fmt:formatNumber value="${e.value/e.key.area*10}" pattern="###,###,##0"/> ${f:h(si.tanni)}/反</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
			<div>明細</div>
			<table>
				<thead>
					<tr>
						<th>日付</th><th>ほ場</th><th>作付</th><th>使用量</th><th>使用資材</th><th>備考</th><th>画像</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${list}" varStatus="vs">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<td><fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></td>
							<td>${f:h(e.sakudukeRef.model.hatakeRef.model.name)}</td>
							<td>${f:h(e.sakudukeRef.model.name)}</td>
							
							<td>${f:h(e.amount)} ${f:h(e.sagyoItemRef.model.tanni)}</td>
							<td>
								<c:forEach var="s" items="${e.shiyoShizaiListRef.modelList}">
									${f:h(s.shizaiRef.model.name)}(${f:h(s.amount)} ${f:h(s.shizaiRef.model.tanni)})<br/>
								</c:forEach>
							</td>
							<td>${f:br(f:h(e.biko))}</td>
							<td>
								<c:forEach var="g" items="${e.gazoListRef.modelList}">
									<c:set var="imgUrl1" value="/gazo/viewThumb/${f:h(g.key)}"/>
									<c:set var="imgUrl2" value="/gazo/view/${f:h(g.key)}"/>
									<i onclick="loadImg('${f:url(imgUrl1)}','${f:url(imgUrl2)}');" class="fa fa-file-picture-o"></i>
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
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
