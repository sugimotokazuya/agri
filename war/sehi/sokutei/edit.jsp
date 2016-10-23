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
<title>測定値変更</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="title">測定値変更</div>
<form action="${f:url('update')}" method="post">
<input type="hidden" ${f:hidden("key")}/>
<input type="hidden" ${f:hidden("version")}/>
日付：
<input type="number" ${f:text("year")} class="year ${f:errorClass('year', 'err')}"/>年
<input type="number" ${f:text("month")} class="month ${f:errorClass('month', 'err')}"/>月
<input type="number" ${f:text("day")} class="day ${f:errorClass('day', 'err')}"/>日
<br/> <span class="${f:errorClass('year', 'err')}">${f:h(errors.year)}</span>
<span class="${f:errorClass('month', 'err')}">${f:h(errors.month)}</span>
<span class="${f:errorClass('day', 'err')}">${f:h(errors.day)}</span>
<hr/>
畑：
<select name="hatake">
<c:forEach var="e" items="${hatakeList}">
<option ${f:select("hatake", f:h(e.key))}>${f:h(e.name)}</option>
</c:forEach>
</select><hr/>
品目：
<input type="text" ${f:text("hinmoku")} class="${f:errorClass('hinmoku', 'err')}"/>
<br/><span class="${f:errorClass('hinmoku', 'err')}">${f:h(errors.hinmoku)}</span>
<hr/>
pH(H2O)：
<input type="text" ${f:text("phH2o")} class="${f:errorClass('phH2o', 'err')}"/>
<br/><span class="${f:errorClass('phH2o', 'err')}">${f:h(errors.phH2o)}</span>
<hr/>
pH(KCl)：
<input type="text" ${f:text("phKcl")} class="${f:errorClass('phKcl', 'err')}"/>
<br/><span class="${f:errorClass('phKcl', 'err')}">${f:h(errors.phKcl)}</span>
<hr/>
NH4N：
<input type="text" ${f:text("seibunNh4n")} class="${f:errorClass('seibunNh4n', 'err')}"/>
<br/><span class="${f:errorClass('seibunNh4n', 'err')}">${f:h(errors.seibunNh4n)}</span>
<hr/>
NO3N：
<input type="text" ${f:text("seibunNo3n")} class="${f:errorClass('seibunNo3n', 'err')}"/>
<br/><span class="${f:errorClass('seibunNo3n', 'err')}">${f:h(errors.seibunNo3n)}</span>
<hr/>
P2O：
<input type="number" ${f:text("seibunP")} class="${f:errorClass('seibunP', 'err')}"/>
<br/><span class="${f:errorClass('seibunP', 'err')}">${f:h(errors.seibunP)}</span>
<hr/>
CaO：
<input type="number" ${f:text("seibunCa")} class="${f:errorClass('seibunCa', 'err')}"/>
<br/><span class="${f:errorClass('seibunCa', 'err')}">${f:h(errors.seibunCa)}</span>
<hr/>
MgO：
<input type="number" ${f:text("seibunMg")} class="${f:errorClass('seibunMg', 'err')}"/>
<br/><span class="${f:errorClass('seibunMg', 'err')}">${f:h(errors.seibunMg)}</span>
<hr/>
K2O：
<input type="number" ${f:text("seibunK")} class="${f:errorClass('seibunK', 'err')}"/>
<br/>	<span class="${f:errorClass('seibunK', 'err')}">${f:h(errors.seibunK)}</span>
<hr/>
Fe：
<input type="text" ${f:text("seibunFe")} class="${f:errorClass('seibunFe', 'err')}"/>
<br/><span class="${f:errorClass('seibunFe', 'err')}">${f:h(errors.seibunFe)}</span>
<hr/>
Mn：
<input type="text" ${f:text("seibunMn")} class="${f:errorClass('seibunMn', 'err')}"/>
<br/><span class="${f:errorClass('seibunMn', 'err')}">${f:h(errors.seibunMn)}</span>
<hr/>
NaCl：
<input type="text" ${f:text("seibunEnbun")} class="${f:errorClass('seibunEnbun', 'err')}"/>
<br/><span class="${f:errorClass('seibunEnbun', 'err')}">${f:h(errors.seibunEnbun)}</span>
<hr/><input class="w50" type="submit" value="測定値変更"/>
<input class="w50" type="button" value="戻る" onclick="window.location.href='${f:url('./')}';"/>
</form>
</body>
</html>
