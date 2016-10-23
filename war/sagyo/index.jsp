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
$(function () {
	
	//DragArea.setAreaAndArray(document.getElementById("bodyId"), dragarray,dropfunc,"debugMsg");//初期設定の実行
	
	$("div.new5").hide();
});

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
		<h1>作業日誌</h1>

		<c:if test="${loginUser.authSakudukeView}">
			<button class="linkButton" type="button" onclick="window.location.href='${f:url('./sakuduke/')}';">作付管理</button>
		</c:if>
		<c:if test="${loginUser.authSagyoItemView}">
			<button type="button" onclick="window.location.href='${f:url('./item/')}';">作業項目管理</button>
		</c:if>
		<button type="button" onclick="window.location.href='${f:url('./sagyoItem')}';">作業項目別表示</button>
		<button type="button" onclick="window.location.href='${f:url('./user')}';">作業者別表示</button>
		<button type="button" onclick="window.location.href='${f:url('/')}';">TOPへ戻る</button>
		<input type="hidden" id="x"/>
		<input type="hidden" id="y"/>
		
		<form action="${f:url('easyCreate')}" method="post">
			<select name="sagyoItemKey" id="sagyoItem">
				<option value="">作業項目を選択</option>
				<c:forEach var="e2" items="${sagyoItemList}">
					<option ${f:select("sagyoItemKey", f:h(e2.key))}>${f:h(e2.name)}</option>
				</c:forEach>
			</select>
			<button class="small" type="submit" onclick="if($('#sagyoItem').val()=='') {alert('作業項目を選択してください');return false;}">簡易一括入力</button>									
		</form>
		
		<h2>今日の作業一覧</h2>
		<table>
			<thead>
				<tr>
					<th>作付</th><th>作業</th><th>作業者（時間）</th><th>量</th><th>使用機械</th>
					<th>使用資材</th><th>備考</th><th>画像</th><th></th><th></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="e" items="${todayList}" varStatus="vs">
				<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
					<td>${f:h(e.sakudukeRef.model.hatakeRef.model.name)} / ${f:h(e.sakudukeRef.model.name)}</td>
					<td>${f:h(e.sagyoItemRef.model.name)}</td>
					<td>
						<c:forEach var="u" items="${e.sagyoUserListRef.modelList}">
							${f:h(u.userRef.model.name)}<c:if test="${u.minutes!=null}">（${f:h(u.minutes)}分）</c:if><br/>
						</c:forEach>
					</td>
					<td class="num">${f:h(e.amount)}<c:if test="${e.amount!=null}">${f:h(e.sagyoItemRef.model.tanni)}</c:if></td>
					<td>
						<c:forEach var="k" items="${e.shiyoKikaiListRef.modelList}">
							${f:h(k.kikaiRef.model.name)}<br/>
						</c:forEach>
					</td>
					<td>
						<c:forEach var="e2" items="${e.shiyoShizaiListRef.modelList}">
							${f:h(e2.shizaiRef.model.name)} ${f:h(e2.amount)}${f:h(e2.shizaiRef.model.tanni)}<br/>
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
						<c:if test="${loginUser.authSagyoEdit}">
						<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}/1"/>
						<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}/1"/>
						<td><a href="${f:url(editUrl)}">変更</a></td>
						<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
					</c:if>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<h2>昨日の作業一覧</h2>
		<table>
			<thead>
				<tr>
					<th>作付</th><th>作業</th><th>作業者（時間）</th><th>量</th><th>使用機械</th>
					<th>使用資材</th><th>備考</th><th>画像</th><th></th><th></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="e" items="${yesterdayList}" varStatus="vs">
				<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
					<td>${f:h(e.sakudukeRef.model.hatakeRef.model.name)} / ${f:h(e.sakudukeRef.model.name)}</td>
					<td>${f:h(e.sagyoItemRef.model.name)}</td>
					<td>
						<c:forEach var="u" items="${e.sagyoUserListRef.modelList}">
							${f:h(u.userRef.model.name)}<c:if test="${u.minutes!=null}">（${f:h(u.minutes)}分）</c:if><br/>
						</c:forEach>
					</td>
					<td class="num">${f:h(e.amount)}<c:if test="${e.amount!=null}">${f:h(e.sagyoItemRef.model.tanni)}</c:if></td>
					<td>
						<c:forEach var="k" items="${e.shiyoKikaiListRef.modelList}">
							${f:h(k.kikaiRef.model.name)}<br/>
						</c:forEach>
					</td>
					<td>
						<c:forEach var="e2" items="${e.shiyoShizaiListRef.modelList}">
							${f:h(e2.shizaiRef.model.name)} ${f:h(e2.amount)}${f:h(e2.shizaiRef.model.tanni)}<br/>
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
						<c:if test="${loginUser.authSagyoEdit}">
						<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}/1"/>
						<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}/1"/>
						<td><a href="${f:url(editUrl)}">変更</a></td>
						<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
					</c:if>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<h2>進行中の作付一覧</h2>
		
		<table>
				<thead>
					<tr>
						<th>開始日</th><th>畑</th><th>作付名</th><th>担当者</th><th>面積</th><th></th>
						<c:if test="${loginUser.authSagyoEdit}"><th></th></c:if>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${sakudukeList}" varStatus="vs">
						<tr class="<c:if test='${vs.count%2==0}'>odd</c:if><c:if test='${e.tantoRef.model==loginUser}'> red</c:if>">
							<td><fmt:formatDate value="${e.startDate}" pattern="yyyy-MM-dd"/></td>
							<td>${f:h(e.hatakeRef.model.name)}</td>
							<td>${f:h(e.name)}</td>
							<td>${f:h(e.tantoRef.model.name)}</td>
							
							<td class="num">${f:h(e.area)}a</td>
							<c:set var="sagyoUrl" value="/sagyo/view/${f:h(e.key)}"/>
							<td><a href="${f:url(sagyoUrl)}">作業一覧表示</a></td>
							<c:if test="${loginUser.authSagyoEdit}">
								<td>
									<form action="${f:url('create')}" method="post">
										<input type="hidden" name="key" value="${f:h(e.key)}"/>
										<input type="hidden" name="version" value="${f:h(e.version)}"/>
										<select name="sagyoItem" id="sagyoItem${vs.index}">
											<option value="">作業項目を選択</option>
											<c:forEach var="e2" items="${sagyoItemList}">
												<option ${f:select("sagyoItem", f:h(e2.key))}>${f:h(e2.name)}</option>
											</c:forEach>
										</select>
										<button class="small" type="submit" onclick="if($('#sagyoItem${vs.index}').val()=='') {alert('作業項目を選択してください');return false;}">作業入力</button>									
									</form>
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		<hr/>
		画像ファイルは以下のメールアドレスに添付して送信すると、作業に貼り付けることができます。<br/>
		photo${f:h(loginUser.key.id)}@agriapp.appspotmail.com
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
