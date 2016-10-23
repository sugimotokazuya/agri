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
<title>出荷形態変更</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>出荷形態変更</h1>
		
		<form action="${f:url('update')}" method="post">
			<input type="hidden" ${f:hidden("key")}/>
			<input type="hidden" ${f:hidden("version")}/>
			<h2>取引先：${f:h(torihikisakiRef.model.name)}</h2>
			<h2>品目：${f:h(hinmokuRef.model.name)}</h2>
			<h2>出荷形態</h2>
			<input type="text" ${f:text("hoso")} class="w75 ${f:errorClass('hoso', 'err')}"/>（例：4kg箱, 200g袋）
			<br/>
			<span class="${f:errorClass('hoso', 'err')}">${f:h(errors.hoso)}</span>
			
			<h2>重量</h2>
			<input type="text" ${f:text("juryo")} class="w50 num ${f:errorClass('price', 'err')}"/> g
			<br/>
			<span class="${f:errorClass('juryo', 'err')}">${f:h(errors.juryo)}</span>
			
			<h2>価格</h2>
			<input type="text" ${f:text("price")} class="w50 num ${f:errorClass('price', 'err')}"/> 円
			<br/>
			<span class="${f:errorClass('price', 'err')}">${f:h(errors.price)}</span>
			<hr/>
			<button type="submit">出荷形態変更</button>
			<button type="button" onclick="window.location.href='${f:url('./')}';">戻る</button>
		</form>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>