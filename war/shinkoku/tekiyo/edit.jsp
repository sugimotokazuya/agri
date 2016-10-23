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
<title>摘要変更</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">摘要変更 - ${f:h(kamoku.name)}</div>
<form action="${f:url('update')}" method="post">
摘要名：
<input type="text" ${f:text("name")} class="${f:errorClass('name', 'err')}"/>
<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span><br/>
<c:if test="${kamoku.kubun==4}">
事業割合(%)：
<input type="text" ${f:text("wariai")} class="${f:errorClass('wariai', 'err')}"/>(0〜100)
<br/><span class="${f:errorClass('wariai', 'err')}">${f:h(errors.wariai)}</span>
</c:if>
<c:if test="${kamoku.kubun!=4}"><input type="hidden" name="wariai" value="100"/></c:if>
<hr/>
<input class="w50" type="submit" value="摘要変更"/>
<input type="hidden" name="key" value="${f:h(key)}"/>
<input type="hidden" name="version" value="${f:h(version)}"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='/shinkoku/tekiyo/?key=${f:h(kamoku.key)}&version=${kamoku.version}';"/>
</form>
</body>
</html>
