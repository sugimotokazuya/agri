<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<div class="footer1">
	<hr/>
	<div class="c">Copyright © あぐりクラウド</div>
	<div class="logout">
		<a href="${f:url('/logout')}">ログアウト</a>
	</div>
</div>
