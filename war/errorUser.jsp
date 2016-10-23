<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" /> 
<link rel="shortcut icon" href="${f:url('/shinkoku/favicon.ico')}"/> 
<link rel="stylesheet" type="text/css" href="/css/global.css"/>
<title>あぐりクラウドβ</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="title">あぐりクラウドβ</div>
<div>
お使いのGoogleアカウントは、ご利用登録されていません。
</div>
<input class="w50" style="font-size:x-large;margin-top:10px;" type="button" value="ログアウト" onclick="window.location.href='${f:url('/logout')}';"/>
</body>
</html>
