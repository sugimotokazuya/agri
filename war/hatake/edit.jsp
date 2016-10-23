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
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>畑登録</h1>
			<h2>圃場番号</h2>
			<input type="text" ${f:text("no")} class="w50 num ${f:errorClass('no', 'err')}"/>
			<br/><span class="${f:errorClass('no', 'err')}">${f:h(errors.no)}</span>
			<h2>畑名</h2>
			<input type="text" ${f:text("name")} class="w300 ${f:errorClass('name', 'err')}"/>
			<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>

			<h2>短縮名</h2>
			<input type="text" ${f:text("shortName")} class="w200 ${f:errorClass('shortName', 'err')}"/>
			<br/><span class="${f:errorClass('shortName', 'err')}">${f:h(errors.shortName)}</span>
			
			<h2>面積(a)</h2>
			<input type="text" type="text" ${f:text("area")} class="w50 num ${f:errorClass('area', 'err')}"/>
			<br/><span class="${f:errorClass('area', 'err')}">${f:h(errors.area)}</span>
			
			<h2>所在地</h2>
			<input type="text" ${f:text("address")} class="w300 ${f:errorClass('address', 'err')}"/>
			<br/><span class="${f:errorClass('address', 'err')}">${f:h(errors.address)}</span>
			
			<h2>有機開始年月日</h2>
			<input type="text" ${f:text("year")} class="w50 num ${f:errorClass('year', 'err')}"/>年
			<input type="text" ${f:text("month")} class="w25 num ${f:errorClass('month', 'err')}"/>月
			<input type="text" ${f:text("day")} class="w25 num ${f:errorClass('day', 'err')}"/>日
			<div>
				<span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
				<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
				<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
			</div>
			
			<h2>状態</h2>
			<input id="s0" type="radio" ${f:radio("delete", "false")}/><label for="s0">表示</label>
			<input id="s1" type="radio" ${f:radio("delete", "true")}/><label for="s1">非表示</label>
				
			<hr/>
			<button type="submit">畑変更</button>
			<button type="button" onclick="window.location.href='/hatake';">畑管理へ</button>
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
