<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<html>
<!DOCTYPE html>
<html lang="ja">
<head>
<jsp:include page="/head.jsp"/>

<script type="text/javascript">

var minBackColor = "lightskyblue";
var maxBackColor = "tomato";
var normalBackColor = "#e2e2e2";
var suryos = [];
var shopSize = ${shopSize};
var hKeys = [];
<c:forEach var="e" items="${recList}">
	suryos.push(${e.suryo});
	hKeys.push('${f:h(e.key)}');
</c:forEach>

function calc() {
	for(var i = 0; i < hKeys.length; ++i) {
		var sum = 0;
		for(var j = 0; j < shopSize; ++j) {
			sum += Number($("#"+hKeys[i] + '_' + j).val());
		}
		var targetTd = $('#inputTd_' + hKeys[i]).val();
		$('.input_' + hKeys[i]).text(sum);
		if(suryos[i] == sum) {
			$('.th_' + hKeys[i]).css("backgroundColor",normalBackColor);
		} else if(suryos[i] < sum) {
			$('.th_' + hKeys[i]).css("backgroundColor",maxBackColor);
		} else {
			$('.th_' + hKeys[i]).css("backgroundColor",minBackColor);
		}
		
	}
}




(function($){ 
    
    $(function(){
        // スクロールが発生した時に処理を行う
        $(window).scroll(function () {
 			showTh();
        });
 
 		function showTh() {
			var $table = $(".chokubaiTable");          // テーブルの要素を取得
            var $thead = $table.children("thead");  // thead取得
            var toffset = $table.offset();          // テーブルの位置情報取得
            // テーブル位置+テーブル縦幅 < スクロール位置 < テーブル位置
            if(toffset.top + $table.height()< $(window).scrollTop()
              || toffset.top > $(window).scrollTop()){
                // クローンテーブルが存在する場合は消す
                var $clone = $("#clonetable");
                if($clone.length > 0){
                    $clone.css("display", "none");
                }
            }
            // テーブル位置 < スクロール位置 < テーブル位置+テーブル縦幅
            else if(toffset.top < $(window).scrollTop()){
                // クローンテーブルが存在するか確認
                var $clone = $("#clonetable");
                if($clone.length == 0){
                    // 存在しない場合は、theadのクローンを作成
                    $clone= $thead.clone(true);
                    // idをclonetableとする
                    $clone.attr("id", "clonetable");
                    // body部に要素を追加
                    $clone.appendTo("body");
                    // theadのCSSをコピーする
                    StyleCopy($clone, $thead);
                    // theadの子要素(tr)分ループさせる
                    for(var i = 0; i < $thead.children("tr").length; i++)
                    {
                        // i番目のtrを取得
                        var $theadtr = $thead.children("tr").eq(i);
                        var $clonetr = $clone.children("tr").eq(i);
                        // trの子要素(th)分ループさせる
                        for (var j = 0; j < $theadtr.eq(i).children("th").length; j++){
                            // j番目のthを取得
                            var $theadth = $theadtr.eq(i).children("th").eq(j);
                            var $cloneth = $clonetr.eq(i).children("th").eq(j);
                            // thのCSSをコピーする
                            StyleCopy($cloneth, $theadth);
                        }
                    }
                }
 
                // コピーしたtheadの表示形式をtableに変更
                $clone.css("display", "table");         
                // positionをブラウザに対し絶対値とする
                $clone.css("position", "fixed");        
                $clone.css("border-collapse", "collapse");
                // positionの位置を設定(left = 元のテーブルのleftとする)
                $clone.css("left", toffset.left - $(window).scrollLeft());
                // positionの位置を設定(topをブラウザの一番上とする)
                $clone.css("top", "0px");
                // 表示順番を一番優先させる
                $clone.css("z-index", 99);
            }
		}
 
        // CSSのコピー
        function StyleCopy($copyTo, $copyFrom){
            $copyTo.css("font-size", 
                        $copyFrom.css("font-size"));
            
            $copyTo.css("width", 
                        $copyFrom.css("width"));
            $copyTo.css("height", 
                        $copyFrom.css("height"));
 
            $copyTo.css("padding-top", 
                        $copyFrom.css("padding-top"));
            $copyTo.css("padding-left", 
                        $copyFrom.css("padding-left"));
            $copyTo.css("padding-bottom", 
                        $copyFrom.css("padding-bottom"));
            $copyTo.css("padding-right", 
                        $copyFrom.css("padding-right"));
 
            $copyTo.css("background", 
                        $copyFrom.css("background"));
            $copyTo.css("background-color", 
                        $copyFrom.css("background-color"));
            $copyTo.css("vertical-align", 
                        $copyFrom.css("vertical-align"));
 
            $copyTo.css("border-top-width", 
                        $copyFrom.css("border-top-width"));
            $copyTo.css("border-top-color", 
                        $copyFrom.css("border-top-color"));
            $copyTo.css("border-top-style", 
                        $copyFrom.css("border-top-style"));
 
            $copyTo.css("border-left-width", 
                        $copyFrom.css("border-left-width"));
            $copyTo.css("border-left-color", 
                        $copyFrom.css("border-left-color"));
            $copyTo.css("border-left-style", 
                        $copyFrom.css("border-left-style"));
 
            $copyTo.css("border-right-width", 
                        $copyFrom.css("border-right-width"));
            $copyTo.css("border-right-color", 
                        $copyFrom.css("border-right-color"));
            $copyTo.css("border-right-style", 
                        $copyFrom.css("border-right-style"));
 
            $copyTo.css("border-bottom-width", 
                        $copyFrom.css("border-bottom-width"));
            $copyTo.css("border-bottom-color", 
                        $copyFrom.css("border-bottom-color"));
            $copyTo.css("border-bottom-style", 
                        $copyFrom.css("border-bottom-style"));
        }
    });
})(jQuery);

</script>
<title>直売店振り分け</title>
</head>
<body onload="setInterval('calc()',500);">
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>直売店振り分け（<fmt:formatDate value="${shukka.date}" pattern="yyyy年MM月dd日"/>出荷分）</h1>

		<form action="${f:url('chokubaiInsert')}" method="post">
			<c:if test="${numError}">
				<span class="err">入力値と合計が一致しません。</span>
			</c:if>
			
			<div style="font-size:small;">
				<c:forEach var="e" items="${linkList}" varStatus="vs">
					<c:if test="${vs.index%4!=0}"> / </c:if>
					<c:if test="${vs.index%4==0}"><br/></c:if>
					<c:set var="url">/mail/sokuhoIFrame/${f:h(e.key)}/${e.version}</c:set>
					<button type="button" onclick="window.open('${f:url(url)}','<fmt:formatDate value='${e.date}' pattern='yyyy年MM月dd日HH時'/>' ,'width=500px,height=500px,top=200px,left=${f:h(200+recListSize*100)}px');"><fmt:formatDate value='${e.date}' pattern='yyyy年MM月dd日HH時'/></button>
				</c:forEach>
			</div>
			
			<table style="margin-right:30px;" class="chokubaiTable">
			<thead>
				<tr>
					<th>直売店舗</th>
					<c:forEach var="e" items="${recList}" varStatus="vs">
						<th class="th_${f:h(e.key)}">
							<div>${f:h(e.hinmokuRef.model.shortName)}</div>
							<div>
								<span class="input_${f:h(e.key)}"></span> /
								<fmt:formatNumber value="${e.suryo}" pattern="###,###,##0"/>
							</div>
						</th>
					</c:forEach>
				</tr>
				
			</thead>
			<tbody>
				<c:forEach var="e" items="${chokubaiList}" varStatus="vs">
					<tr>
						<td>${f:h(e.name)}</td>
						<c:forEach var="h" items="${recList}">
							<td class="num">
								<c:set var="k">${f:h(h.key)}_${f:h(e.key)}</c:set>
								<c:set var="k2">${f:h(h.key)}_${vs.index}</c:set>
								<input id="${k2}" style="width:50px;font-size:large;" type="text" ${f:text(k)} class="num ${f:errorClass(k, 'err')}" maxlength="4" />
								<br/>
								<div style="width:100%;">
									<c:set var="uc">${dto1.map[e.key].ucMap[h.hinmokuRef.key] == null ? 0 : dto1.map[e.key].ucMap[h.hinmokuRef.key]}</c:set>
									<c:set var="sc">${dto1.map[e.key].scMap[h.hinmokuRef.key] == null ? 0 : dto1.map[e.key].scMap[h.hinmokuRef.key]}</c:set>
									<c:choose>
										<c:when test="${(uc*1)>=(sc*0.9) && uc > 0}"><c:set var="color">tomato</c:set></c:when>
										<c:when test="${(sc*1)>(uc*2)}"><c:set var="color">lightskyblue</c:set></c:when>
										<c:otherwise><c:set var="color">white</c:set></c:otherwise>
									</c:choose>
									今<span style="width:50%;text-align:center;background-color:${f:h(color)}">${f:h(uc)}/${f:h(sc)}</span> |
									<c:set var="uc">${dto2.map[e.key].ucMap[h.hinmokuRef.key] == null ? 0 : dto2.map[e.key].ucMap[h.hinmokuRef.key]}</c:set>
									<c:set var="sc">${dto2.map[e.key].scMap[h.hinmokuRef.key] == null ? 0 : dto2.map[e.key].scMap[h.hinmokuRef.key]}</c:set>
									<c:choose>
										<c:when test="${(uc*1)>=(sc*0.9) && uc > 0}"><c:set var="color">tomato</c:set></c:when>
										<c:when test="${(sc*1)>(uc*2)}"><c:set var="color">lightskyblue</c:set></c:when>
										<c:otherwise><c:set var="color">white</c:set></c:otherwise>
									</c:choose>
									昨<span style="width:50%;text-align:center;background-color:${f:h(color)}">${f:h(uc)}/${f:h(sc)}</span>
								</div>
								<div style="width:100%;">
									<c:set var="uc">${dto7.map[e.key].ucMap[h.hinmokuRef.key] == null ? 0 : dto7.map[e.key].ucMap[h.hinmokuRef.key]}</c:set>
									<c:set var="sc">${dto7.map[e.key].scMap[h.hinmokuRef.key] == null ? 0 : dto7.map[e.key].scMap[h.hinmokuRef.key]}</c:set>
									<c:choose>
										<c:when test="${(uc*1)>=(sc*0.9) && uc > 0}"><c:set var="color">tomato</c:set></c:when>
										<c:when test="${(sc*1)>(uc*2)}"><c:set var="color">lightskyblue</c:set></c:when>
										<c:otherwise><c:set var="color">white</c:set></c:otherwise>
									</c:choose>
									週<span style="width:50%;text-align:center;background-color:${f:h(color)}">${f:h(uc)}/${f:h(sc)}(<fmt:formatNumber value="${uc/sc}" pattern="#0%"/>)</span>
								</div>
							</td>
						</c:forEach>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<hr/>
			<c:if test="${loginUser.authShukkaEdit}">
				<input type="hidden" ${f:hidden("key")}/>
				<button type="submit">登録</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('../')}';">戻る</button>
		</form>
		
		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
