<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" /> 
<link rel="stylesheet" type="text/css" href="/css/global.css"/>
<title>仕訳入力</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">仕訳入力</div>
<form action="${f:url('insert')}" method="post">
日付：
<input type="number" ${f:text("year")} class="year ${f:errorClass('year', 'err')}"/>年
<input type="number" ${f:text("month")} class="month ${f:errorClass('month', 'err')}"/>月
<input type="number" ${f:text("day")} class="day ${f:errorClass('day', 'err')}"/>日
<br/> <span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
<hr/>
借方科目：${f:h(karikataKamoku.name)}<br/>
貸方科目：${f:h(kashikataKamoku.name)}<hr/>
金額：
<input type="number" ${f:text("kingaku")} class="num ${f:errorClass('kingaku', 'err')}"/>
<br/><span class="${f:errorClass('kingaku', 'err')}">${f:h(errors.kingaku)}</span><hr/>
摘要：
<select name="tekiyo">
<c:forEach var="e" items="${tekiyoList}">
<option ${f:select("tekiyo", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select><br/>
<input class="w100" type="text" ${f:text("tekiyoText")} class="${f:errorClass('tekiyoText', 'err')}"/>
<span class="${f:errorClass('tekiyoText', 'err')}">${f:h(errors.tekiyoText)}</span><hr/>
<input type="hidden" name="karikataKamoku" value="${f:h(karikataKamoku.key)}"/>
<input type="hidden" name="kashikataKamoku" value="${f:h(kashikataKamoku.key)}"/>
<input class="w50" type="submit" value="仕訳入力"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/>
</form>
</body>
</html>
