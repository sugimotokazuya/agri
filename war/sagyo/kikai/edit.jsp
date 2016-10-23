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
			<h1>使用機械変更 - ${f:h(sagyoItem.name)}</h1>
			<h2>使用機械名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>汚染防止措置（有機JAS認定書類で使用）</h2>
			<input type="text" ${f:text("osenboushisochi")} class="w300 ${f:errorClass('osenboushisochi', 'err')}"/>
			<br/><span class="${f:errorClass('osenboushisochi', 'err')}">${f:h(errors.osenboushisochi)}</span><br/>
			
			<hr/>
			<button type="submit">使用機械変更</button>
			<button type="button" onclick="window.location.href='/sagyo/kikai/?key=${f:h(sagyoItem.key)}&version=${sagyoItem.version}';">使用資材管理へ戻る</button>
			<button type="button" onclick="window.location.href='/sagyo/item';">作業項目管理へ戻る</button>
			<button type="button" onclick="window.location.href='/sagyo';">作業日誌トップへ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
