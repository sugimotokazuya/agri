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
</head>

<body>
<form action="${f:url('update')}" method="post">
	<input type="hidden" name="key" value="${f:h(key)}"/>
	<input type="hidden" name="version" value="${f:h(version)}"/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>送り先変更 - ${f:h(torihikisaki.name)}</h1>
			<h2>送り先名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>郵便番号</h2>
			<input type="text" ${f:text("yubin1")} class="w50 ${f:errorClass('yubin1', 'err')}"/> -
			<input type="text" ${f:text("yubin2")} class="w50 ${f:errorClass('yubin2', 'err')}"/>
			<br/><span class="${f:errorClass('yubin1', 'err')}">${f:h(errors.yubin1)}</span>
			<br/><span class="${f:errorClass('yubin2', 'err')}">${f:h(errors.yubin2)}</span>
			
			<h2>住所１</h2>
			<input type="text" ${f:text("address1")} class="w300 ${f:errorClass('address1', 'err')}"/>
			<br/><span class="${f:errorClass('address1', 'err')}">${f:h(errors.address1)}</span><br/>
			
			<h2>住所２</h2>
			<input type="text" ${f:text("address2")} class="w300 ${f:errorClass('address2', 'err')}"/>
			<br/><span class="${f:errorClass('address2', 'err')}">${f:h(errors.address2)}</span><br/>
			
			<h2>住所３</h2>
			<input type="text" ${f:text("address3")} class="w300 ${f:errorClass('address3', 'err')}"/>
			<br/><span class="${f:errorClass('address3', 'err')}">${f:h(errors.address3)}</span><br/>
			
			<h2>電話番号</h2>
			<input type="text" ${f:text("tel")} class="w200 ${f:errorClass('tel', 'err')}"/>
			<br/><span class="${f:errorClass('tel', 'err')}">${f:h(errors.tel)}</span><br/>

			<h2>送料</h2>
			<input type="text" ${f:text("soryo")} class="w50 ${f:errorClass('soryo', 'err')}"/>(四国:453円, 関西:504円, 関東:576円, 東海555円)
			<br/><span class="${f:errorClass('soryo', 'err')}">${f:h(errors.soryo)}</span><br/>

			<h2>配達希望日（何日後着か）</h2>
			<input type="text" ${f:text("haitatsubi")} class="num w25 ${f:errorClass('haitatsubi', 'err')}"/>日後
			<br/><span class="${f:errorClass('haitatsubi', 'err')}">${f:h(errors.haitatsubi)}</span><br/>
			
			<h2>配達希望時間</h2>
			<input id="n0" type="radio" ${f:radio("kiboujikan", "0")}/><label for="n0">午前中</label>　
			<input id="n1" type="radio" ${f:radio("kiboujikan", "1")}/><label for="n1">12時〜14時</label>
			<input id="n2" type="radio" ${f:radio("kiboujikan", "2")}/><label for="n2">14時〜16時</label>
			<input id="n3" type="radio" ${f:radio("kiboujikan", "3")}/><label for="n3">16時〜18時</label>
			<input id="n4" type="radio" ${f:radio("kiboujikan", "4")}/><label for="n4">18時〜20時</label>
			<input id="n5" type="radio" ${f:radio("kiboujikan", "5")}/><label for="n5">20時〜21時</label>
			<input id="n6" type="radio" ${f:radio("kiboujikan", "6")}/><label for="n6">希望しない</label>
			<br/><span class="${f:errorClass('kiboujikan', 'err')}">${f:h(errors.kiboujikan)}</span>
			
			
			<h2>依頼主名</h2>
			<input type="text" ${f:text("iraiName")} class="w300 ${f:errorClass('iraiName', 'err')}"/>
			<br/><span class="${f:errorClass('iraiName', 'err')}">${f:h(errors.iraiName)}</span>

			<h2>依頼主-郵便番号</h2>
			<input type="text" ${f:text("iraiYubin1")} class="w50 ${f:errorClass('iraiYubin1', 'err')}"/> -
			<input type="text" ${f:text("iraiYubin2")} class="w50 ${f:errorClass('iraiYubin2', 'err')}"/>
			<br/><span class="${f:errorClass('iraiYubin1', 'err')}">${f:h(errors.iraiYubin1)}</span>
			<br/><span class="${f:errorClass('iraiYubin2', 'err')}">${f:h(errors.iraiYubin2)}</span>
			
			<h2>依頼主-住所</h2>
			<input type="text" ${f:text("iraiAddress1")} class="w300 ${f:errorClass('iraiAddress1', 'err')}"/>
			<span class="${f:errorClass('iraiAddress1', 'err')}">${f:h(errors.iraiAddress1)}</span><br/>
			<input type="text" ${f:text("iraiAddress2")} class="w300 ${f:errorClass('iraiAddress2', 'err')}"/>
			<span class="${f:errorClass('iraiAddress2', 'err')}">${f:h(errors.iraiAddress2)}</span><br/>
			
			<h2>依頼主-電話番号</h2>
			<input type="text" ${f:text("iraiTel")} class="w200 ${f:errorClass('iraiTel', 'err')}"/>
			<br/><span class="${f:errorClass('iraiTel', 'err')}">${f:h(errors.iraiTel)}</span><br/>
			
			
			<hr/>
			<button type="submit">変更</button>
			<button type="button" onclick="window.location.href='/torihikisaki/okurisaki?key=${f:h(torihikisaki.key)}&version=${torihikisaki.version}';">戻る</button>
			<button type="button" onclick="window.location.href='/torihikisaki';">取引先管理へ戻る</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
