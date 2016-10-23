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
<form action="${f:url('shinseiUpdate')}" method="post">
	<input type="hidden" ${f:hidden("userKey")}/>
	<input type="hidden" ${f:hidden("year")}/>
	<input type="hidden" ${f:hidden("month")}/>
	<input type="hidden" ${f:hidden("day")}/>
	<input type="hidden" ${f:hidden("status")}/>
	<input type="hidden" ${f:hidden("key")}/>
	<input type="hidden" ${f:hidden("version")}/>
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>修正申請　-　${f:h(year)}年${f:h(month+1)}月${f:h(day)}日　${f:h(user.name)}</h1>
			
			<h2>修正前</h2>
			<c:choose>
				<c:when test="${status=='start'}">
					出勤時間 <c:if test="${dakoku.start==null}">_ _時_ _分</c:if><fmt:formatDate value="${dakoku.start}" pattern="HH時mm分"/>
				</c:when>
				<c:when test="${status=='out'}">
					外出時間 <c:if test="${dakoku.out==null}">_ _時_ _分</c:if><fmt:formatDate value="${dakoku.out}" pattern="HH時mm分"/>
				</c:when>
				<c:when test="${status=='in'}">
					戻り時間 <c:if test="${dakoku.in==null}">_ _時_ _分</c:if><fmt:formatDate value="${dakoku.in}" pattern="HH時mm分"/>
				</c:when>
				<c:when test="${status=='end'}">
					退勤時間 <c:if test="${dakoku.end==null}">_ _時_ _分</c:if><fmt:formatDate value="${dakoku.end}" pattern="HH時mm分"/>
				</c:when>
			</c:choose>
			
			<h2>修正値</h2>
			<input type="text" ${f:text("time1")} class="w25 ${f:errorClass('time1', 'err')}"/>時
			<input type="text" ${f:text("time2")} class="w25 ${f:errorClass('time2', 'err')}"/>分
			<br/><span class="${f:errorClass('time1', 'err')}">${f:h(errors.time1)}</span>
			<br/><span class="${f:errorClass('time2', 'err')}">${f:h(errors.time2)}</span>
			
			<h2>修正理由</h2>
			<input type="text" ${f:text("riyu")} class="w300 ${f:errorClass('riyu', 'err')}"/>
			<br/>例：直帰のため
			<br/><span class="${f:errorClass('riyu', 'err')}">${f:h(errors.riyu)}</span>
			
			
			<hr/>
			<button type="submit">打刻修正を申請する</button>
			<c:if test="${!empty(key)}">
				<c:set var="url" value="/kintai/shinseiDelete/${f:h(key)}/${version}"/>
				<button type="button" onclick="if(confirm('取り消して良いですか?')) window.location.href='${f:url(url)}';">申請を取り消す</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/kintai')}';">戻る</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>

			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
