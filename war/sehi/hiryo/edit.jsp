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
<title>肥料変更</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">肥料変更</div>
<form action="${f:url('update')}" method="post">
<input type="hidden" ${f:hidden("key")}/>
<input type="hidden" ${f:hidden("version")}/>
肥料名：
<input type="text" ${f:text("name")} class="${f:errorClass('name', 'err')}"/>
<br/><span class="${f:errorClass('name', 'err')}">${f:h(errors.name)}</span>
<hr/>
N：
<input type="text" maxlength="4" ${f:text("seibunN")} class="${f:errorClass('seibunN', 'err')}"/>
<br/><span class="${f:errorClass('seibunN', 'err')}">${f:h(errors.seibunN)}</span>
<hr/>
N係数：
<input type="text" maxlength="4" ${f:text("nkeisu")} class="${f:errorClass('nkeisu', 'err')}"/>
<br/><span class="${f:errorClass('nkeisu', 'err')}">${f:h(errors.nkeisu)}</span>
<hr/>
P：
<input type="text" maxlength="4" ${f:text("seibunP")} class="${f:errorClass('seibunP', 'err')}"/>
<br/><span class="${f:errorClass('seibunP', 'err')}">${f:h(errors.seibunP)}</span>
<hr/>
K：
<input type="text" maxlength="4" ${f:text("seibunK")} class="${f:errorClass('seibunK', 'err')}"/>
<br/>	<span class="${f:errorClass('seibunK', 'err')}">${f:h(errors.seibunK)}</span>
<hr/>
Ca：
<input type="text" maxlength="4" ${f:text("seibunCa")} class="${f:errorClass('seibunCa', 'err')}"/>
<br/><span class="${f:errorClass('seibunCa', 'err')}">${f:h(errors.seibunCa)}</span>
<hr/>
Mg：
<input type="text" maxlength="4" ${f:text("seibunMg")} class="${f:errorClass('seibunMg', 'err')}"/>
<br/><span class="${f:errorClass('seibunMg', 'err')}">${f:h(errors.seibunMg)}</span>
<hr/>
B：
<input type="text" maxlength="4" ${f:text("seibunB")} class="${f:errorClass('seibunB', 'err')}"/>
<br/><span class="${f:errorClass('seibunB', 'err')}">${f:h(errors.seibunB)}</span>
<hr/>
Mn：
<input type="text" maxlength="4" ${f:text("seibunMn")} class="${f:errorClass('seibunMn', 'err')}"/>
<br/><span class="${f:errorClass('seibunMn', 'err')}">${f:h(errors.seibunMn)}</span>
<hr/>
Fe：
<input type="text" maxlength="4" ${f:text("seibunFe")} class="${f:errorClass('seibunFe', 'err')}"/>
<br/><span class="${f:errorClass('seibunFe', 'err')}">${f:h(errors.seibunFe)}</span>
<hr/>
kg/袋：
<input type="text" maxlength="4" ${f:text("weight")} class="${f:errorClass('weight', 'err')}"/>
<br/><span class="${f:errorClass('weight', 'err')}">${f:h(errors.weight)}</span>
<hr/>
状態：
<input id="s0" type="radio" ${f:radio("delete", "false")}/><label for="s0">表示</label>
<input id="s1" type="radio" ${f:radio("delete", "true")}/><label for="s1">非表示</label>
<hr/>
<input class="w50" type="submit" value="肥料変更"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/>
</form>
</body>
</html>
