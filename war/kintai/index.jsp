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
	<style type="text/css">
		div.chikoku {
			background-color:#f2dae8;
		}
		table#kinmu a {
			color:black;
		}
		table#kinmu a:hover {
			color:#d7847e;
		}
		div.shinsei {
			font-size:8pt;
		}
	</style>
</head>

<body>
<form action="${f:url('./')}" method="post">
	<jsp:include page="/header2.jsp"/>
	<jsp:include page="/menu.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>勤怠</h1>
			<button type="button" onclick="window.location.href='${f:url('/kintai/shift')}';">シフト管理</button>
			<c:if test="${loginUser.authKintai}">
				<button type="button" onclick="window.location.href='${f:url('/kintai/teiji')}';">勤務時間の定時管理</button>
				<button type="button" onclick="window.location.href='${f:url('/kintai/shonin')}';">修正・残業申請一覧</button>
			</c:if>
			<button type="button" onclick="window.location.href='${f:url('/kintai/kinmuhyo')}';">勤務表一覧</button>
			<c:set var="url">/kintai/teishutsu/${f:h(userRef)}</c:set>
			<button type="button" onclick="window.location.href='${f:url(url)}';">勤務表提出</button>
			<button type="button" onclick="window.location.href='${f:url('/')}';">トップへ戻る</button>
			<br/>
			<c:if test="${loginUser.authKintai}">
				ユーザー：
				<select name="userRef">
					<c:forEach var="e" items="${userList}">
						<option ${f:select("userRef", f:h(e.key))}>${f:h(e.name)}</option>
					</c:forEach>
				</select>
			</c:if>	
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
			照会月：
			<select name="month">
				<option ${f:select("month", "0")}>1月</option>
				<option ${f:select("month", "1")}>2月</option>
				<option ${f:select("month", "2")}>3月</option>
				<option ${f:select("month", "3")}>4月</option>
				<option ${f:select("month", "4")}>5月</option>
				<option ${f:select("month", "5")}>6月</option>
				<option ${f:select("month", "6")}>7月</option>
				<option ${f:select("month", "7")}>8月</option>
				<option ${f:select("month", "8")}>9月</option>
				<option ${f:select("month", "9")}>10月</option>
				<option ${f:select("month", "10")}>11月</option>
				<option ${f:select("month", "11")}>12月</option>
			</select>
			<button type="submit">照会</button>
			
			<h2>勤務表　-　${f:h(year)}年${f:h(month+1)}月　${f:h(user.name)}</h2>
			<div>※打刻の修正を行いたい場合は、修正したい箇所をクリック（タップ）してください。</div>
			<div>※カッコ内の時間は修正申請中の時間です。</div>
			<table id="kinmu">
				<thead>
					<tr>
						<th>日付</th>
						<th>出勤</th>
						<th>外出</th>
						<th>戻り</th>
						<th>退勤</th>
						<th>残業</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${dayList}" varStatus="vs">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<c:set var="dakoku" value="${map[e]}"/>
							<c:set var="teiji" value="${teijiMap[e]}"/>
							<c:set var="date" value="${dateMap[e]}"/>
							<c:set var="ss2Map" value="${ssMap[e]}"/>
							<td><fmt:formatDate value="${date}" pattern="dd日（E）"/></td>
							<td>
								<div<c:if test="${dakoku.start!=null && teiji.startTimeInt<dakoku.startInt}"> class="chikoku"</c:if>>
									<c:set var="url">/kintai/shinsei/start/${f:h(user.key)}/${f:h(year)}/${f:h(month)}/${f:h(e)}</c:set>
									<a href="${f:url(url)}">
										<fmt:formatDate value="${dakoku.start}" pattern="HH時mm分"/>
										<c:if test="${dakoku.start==null}">_ _時_ _分</c:if>
									</a>
									<c:if test="${ss2Map!=null && ss2Map['1']!=null}">
										<div class="shinsei">
											（<fmt:formatDate value="${ss2Map['1'].time}" pattern="HH時mm分"/>）
										</div>
									</c:if>
								</div>
							</td>
							<td>
								<c:set var="url">/kintai/shinsei/out/${f:h(user.key)}/${f:h(year)}/${f:h(month)}/${f:h(e)}</c:set>
								<c:if test="${dakoku.start!=null}">
									<a href="${f:url(url)}">
										<fmt:formatDate value="${dakoku.out}" pattern="HH時mm分"/>
										<c:if test="${dakoku.out==null}">_ _時_ _分</c:if>
									</a>
								</c:if>
								<c:if test="${ss2Map!=null && ss2Map['2']!=null}">
									<div class="shinsei">
										（<fmt:formatDate value="${ss2Map['2'].time}" pattern="HH時mm分"/>）
									</div>
								</c:if>
							</td>
							<td>
								<div<c:if test="${dakoku.out!=null && dakoku.in==null}"> class="chikoku"</c:if></div>
									<c:set var="url">/kintai/shinsei/in/${f:h(user.key)}/${f:h(year)}/${f:h(month)}/${f:h(e)}</c:set>
									<c:if test="${dakoku.out!=null}">
										<a href="${f:url(url)}">
										<fmt:formatDate value="${dakoku.in}" pattern="HH時mm分"/>
										<c:if test="${dakoku.in==null}">_ _時_ _分</c:if>
									</a>
									</c:if>
									<c:if test="${ss2Map!=null && ss2Map['3']!=null}">
										<div class="shinsei">
											（<fmt:formatDate value="${ss2Map['3'].time}" pattern="HH時mm分"/>）
										</div>
									</c:if>
								</div>
							</td>
							<td>
								<div<c:if test="${(dakoku.end!=null && dakoku.endInt<teiji.endTimeInt)
										|| (dakoku.start!=null && dakoku.end==null)}"> class="chikoku"</c:if></div>
									<c:set var="url">/kintai/shinsei/end/${f:h(user.key)}/${f:h(year)}/${f:h(month)}/${f:h(e)}</c:set>
									<c:if test="${dakoku.start!=null}">
										<a href="${f:url(url)}">
											<fmt:formatDate value="${dakoku.end}" pattern="HH時mm分"/>
											<c:if test="${dakoku.end==null}">_ _時_ _分</c:if>
										</a>
									</c:if>
									<c:if test="${ss2Map!=null && ss2Map['4']!=null}">
										<div class="shinsei">
											（<fmt:formatDate value="${ss2Map['4'].time}" pattern="HH時mm分"/>）
										</div>
									</c:if>
								</div>
							</td>
							<td>
								<c:if test="${dakoku!=null}">
									<c:choose>
										<c:when test="${dakoku.zangyo}">
											<c:set var="url">/kintai/zangyoDelete/${f:h(dakoku.key)}/${f:h(dakoku.version)}</c:set>
											<a href="${f:url(url)}" onclick="return confirm('残業を取り消して良いですか?')">残業</a>
										</c:when>
										<c:when test="${ss2Map!=null && ss2Map['5']!=null}">
											<c:set var="url">/kintai/shinsei/zangyo/${f:h(user.key)}/${f:h(year)}/${f:h(month)}/${f:h(e)}</c:set>
											<a href="${f:url(url)}">申請中</a>
										</c:when>
										<c:otherwise>
											<c:set var="url">/kintai/shinsei/zangyo/${f:h(user.key)}/${f:h(year)}/${f:h(month)}/${f:h(e)}</c:set>
											<a href="${f:url(url)}">申請</a>
										</c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<%--
							<c:if test="${loginUser.authSagyoItemEdit}">
								<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<td><a href="${f:url(editUrl)}">変更</a></td>
								<td><a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a></td>
							</c:if>
							--%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<h2>修正申請一覧</h2>
			<table>
				<thead>
					<tr>
						<th>日付</th>
						<th>種別</th>
						<th>修正前</th>
						<th>修正値</th>
						<th>理由</th>
						<th>状態</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="e" items="${ssList}" varStatus="vs">
						<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
							<td><fmt:formatDate value="${e.date}" pattern="dd日（E）"/></td>
							<td>${f:h(e.statusStr)}</td>
							<td><fmt:formatDate value="${e.beforeTime}" pattern="HH時mm分"/></td>
							<td><fmt:formatDate value="${e.time}" pattern="HH時mm分"/></td>
							<td>${f:h(e.riyu)}</td>
							<td>${f:h(e.shoninStr)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<jsp:include page="/footer2.jsp"/>
		</div>
		
		<jsp:include page="/side.jsp"/>
		
	</div>
	<jsp:include page="/footer.jsp"/>
</form>
</body>
</html>
