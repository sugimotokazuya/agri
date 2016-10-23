<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<div class="side">
	<h1><i class="fa fa-clock-o"></i> <fmt:formatDate value="${today}" pattern="yyyy-MM-dd（E）"/></h1>
	<p id="time"></p>
	
	<c:if test="${loginUser.authMailView}">
		<h1><i class="fa fa-envelope"></i> 売上速報<span class="small">（最新５件）</span></h1>
		<c:if test="${fn:length(onList)==0}">
			<p>現在売上速報はありません。</p>
		</c:if>
		<ul>
			<c:forEach var="e" items="${onList}">
				<c:set var="url">/mail/sokuho/${f:h(e.key)}/${e.version}</c:set>
				<li>
					<a href="${f:url(url)}">
						<i class="fa fa-envelope-o"></i>
						<fmt:formatDate value="${e.date}" pattern="yyyy年MM月dd日 HH時mm分"/>
					</a>
				</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<h1><i class="fa fa-info"></i> お知らせ</h1>
	<c:if test="${fn:length(oList)==0}">
		<p>現在お知らせはありません。</p>
	</c:if>
	<dl>
		<c:forEach var="e" items="${oList}">
			<dt>
				<div class="date"><fmt:formatDate value="${e.date}" pattern="yyyy-MM-dd"/></div>
				${f:h(e.header)}
			</dt>
			<dd>${f:br(f:h(e.body))}</dd>
		</c:forEach>
	</dl>
</div>