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
			<h1>使用資材変更 - ${f:h(sagyoItem.name)}</h1>
			<h2>使用資材名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>単位</h2>
			<input type="text" ${f:text("tanni")} class="w50 ${f:errorClass('tanni', 'err')}"/>
			<br/><span class="${f:errorClass('tanni', 'err')}">${f:h(errors.tanni)}</span>
			
			<h2>入手先（有機JAS認定書類で使用）</h2>
			<input type="text" ${f:text("nyushusaki")} class="w300 ${f:errorClass('nyushusaki', 'err')}"/>
			<br/><span class="${f:errorClass('nyushusaki', 'err')}">${f:h(errors.nyushusaki)}</span><br/>
			
			
			<h2>製造者名（有機JAS認定書類で使用）</h2>
			<input type="text" ${f:text("seizosha")} class="w300 ${f:errorClass('seizosha', 'err')}"/>
			<br/><span class="${f:errorClass('seizosha', 'err')}">${f:h(errors.seizosha)}</span><br/>
			
			<h2>該当する別表資材名（有機JAS認定書類で使用）</h2>
			<input type="text" ${f:text("beppyoName")} class="w300 ${f:errorClass('beppyoName', 'err')}"/>
			<br/><span class="${f:errorClass('beppyoName', 'err')}">${f:h(errors.beppyoName)}</span><br/>
			
			<h2>別表区分（有機JAS認定書類で使用）</h2>
			<input type="text" ${f:text("beppyoKubun")} class="w300 ${f:errorClass('beppyoKubun', 'err')}"/>
			<br/><span class="${f:errorClass('beppyoKubun', 'err')}">${f:h(errors.beppyoKubun)}</span><br/>
			
			<h2>資材使用の理由（有機JAS認定書類で使用）</h2>
			<input type="text" ${f:text("riyu")} class="w300 ${f:errorClass('riyu', 'err')}"/>
			<br/><span class="${f:errorClass('riyu', 'err')}">${f:h(errors.beppyoKubun)}</span><br/>
			
			<hr/>
			<button type="submit">使用資材変更</button>
			<button type="button" onclick="window.location.href='/sagyo/shizai/?key=${f:h(sagyoItem.key)}&version=${sagyoItem.version}';">使用資材管理へ戻る</button>
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
