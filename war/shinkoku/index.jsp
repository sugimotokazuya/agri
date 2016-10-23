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
<link rel="shortcut icon" href="${f:url('/shinkoku/favicon.ico')}"/> 
<link rel="stylesheet" type="text/css" href="/css/global.css"/>
<title>申告アプリ</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<input class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="仕訳入力" onclick="window.location.href='${f:url('./shiwake/')}';"/>
<input class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="仕訳照会" onclick="window.location.href='${f:url('./shiwake/menu')}';"/>
<input class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="科目別摘要集計" onclick="window.location.href='${f:url('./shukei/')}';"/>
<input  class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="損益計算表" onclick="window.location.href='${f:url('./pl')}';"/>
<hr/>

<input class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="科目管理" onclick="window.location.href='${f:url('./kamoku/')}';"/>

<hr/>

<input class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="戻る" onclick="window.location.href='${f:url('../start')}';"/>

</body>
</html>
