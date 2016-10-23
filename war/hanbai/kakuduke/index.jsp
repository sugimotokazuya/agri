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
<title>格付管理</title>
</head>
<body>
<jsp:include page="/header2.jsp"/>
<jsp:include page="/menu.jsp"/>
<div class="contentBox">
	<div class="content">
		<h1>格付管理</h1>
		<c:if test="${loginUser.authShukkaEdit}">
			<button type="button" onclick="window.location.href='${f:url('create')}';">格付枚数登録</button>
		</c:if>
		<button type="button" onclick="window.location.href='${f:url('../')}';">戻る</button>
		<form action="${f:url('./')}" method="post">
			照会年：
			<select name="year">
				<c:forEach var="e" items="${years}">
					<option ${f:select("year", f:h(e))}>${f:h(e)}年</option>
				</c:forEach>
			</select>
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
				<option ${f:select("month", "12")}>全て</option>
			</select>
			<button type="submit">照会</button>
			<button type="submit" name="pdf">PDF作成</button>
			<c:if test="${loginUser.authShukkaEdit}">
				<button type="submit" name="sum">前月残集計</button>
			</c:if>
		</form>
		<table>
			<thead>
				<tr>
					<th rowspan="2">出荷日</th><th rowspan="2">品目</th><th rowspan="2">出荷形態</th>
					<th rowspan="2">数量</th><th rowspan="2">重量</th><th rowspan="2">検査結果</th>
					<th rowspan="2">不合格品<br/>処分方法</th><th rowspan="2">出荷先</th><th colspan="3">JAS格付表示</th>
					<th rowspan="2">備考<br/>（担当者）</th><th rowspan="2">削除</th>
				</tr>
				<tr>
					<th>受入</th><th>使用数</th><th>残数</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="sum" value="${zenZan}"/>
					<tr class="odd">
						<td><fmt:formatDate value="${startDate}" pattern="yyyy/MM/dd"/></td>
						<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
						<td class="num"><fmt:formatNumber value="${zenZan}" pattern="###,###,##0"/></td>
						<td>前月残</td><td></td>
					</tr>
				<c:forEach var="e" items="${list}" varStatus="vs">
					<tr <c:if test="${vs.count%2==0}">class="odd"</c:if>>
						<td><fmt:formatDate value="${e.date}" pattern="yyyy/MM/dd"/></td>
						<td>
							<c:if test="${e.shukkaRef.model!=null}">
								<c:forEach var="e2" items="${e.shukkaRef.model.recListRef.modelList}" varStatus="vs">
									<c:if test="${!vs.first}"><br/>,</c:if>
									${f:h(e2.hinmokuRef.model.name)}
								</c:forEach>
							</c:if>
							<c:if test="${e.shukkaRecRef.model!=null}">
								${f:h(e.shukkaRecRef.model.hinmokuRef.model.name)}
							</c:if>
						</td>
						
						<td>
							<c:if test="${e.shukkaRef.model!=null}">
								<c:forEach var="e2" items="${e.shukkaRef.model.recListRef.modelList}" varStatus="vs">
									<c:if test="${!vs.first}"><br/>,</c:if>
									${f:h(e2.shukkaKeitaiRef.model.hoso)}
								</c:forEach>
							</c:if>
							<c:if test="${e.shukkaRecRef.model!=null}">
								${f:h(e.shukkaRecRef.model.shukkaKeitaiRef.model.hoso)}
							</c:if>
						</td>
						<td class="num">
							<c:if test="${e.shukkaRef.model!=null}">
								<c:forEach var="e2" items="${e.shukkaRef.model.recListRef.modelList}" varStatus="vs">
									<c:if test="${!vs.first}"><br/>,</c:if>
									<fmt:formatNumber value="${e2.suryo}" pattern="###,###,##0"/>
								</c:forEach>
							</c:if>
							<c:if test="${e.shukkaRecRef.model!=null}">
								<fmt:formatNumber value="${e.shukkaRecRef.model.suryo}" pattern="###,###,##0"/>
							</c:if>
						</td>
						<td class="num">
							<c:if test="${e.shukkaRef.model!=null}">
								<c:forEach var="e2" items="${e.shukkaRef.model.recListRef.modelList}" varStatus="vs">
									<c:if test="${!vs.first}"><br/>,</c:if>
									<fmt:formatNumber value="${e2.shukkaKeitaiRef.model.juryo}" pattern="###,###,##0"/>g
								</c:forEach>
							</c:if>
							<c:if test="${e.shukkaRecRef.model!=null}">
								<fmt:formatNumber value="${e.shukkaRecRef.model.shukkaKeitaiRef.model.juryo}" pattern="###,###,##0"/>g
							</c:if>
						</td>
						<td><c:if test="${e.shukkaRef.model!=null || e.shukkaRecRef.model!=null}">合格</c:if></td>
						<td>&nbsp;</td>
						<td>
							<c:set var="shukka"/>
							<c:if test="${e.shukkaRef.model!=null}">
								<c:set var="shukka" value="${e.shukkaRef.model}"/>
							</c:if>
							<c:if test="${e.shukkaRecRef.model!=null}">
								<c:set var="shukka" value="${e.shukkaRecRef.model.shukkaRef.model}"/>
							</c:if>
							<c:if test="${!empty(shukka)}">
								<c:if test="${empty(shukka.okurisaki)}">${f:h(shukka.torihikisakiRef.model.name)}</c:if>
								<c:if test="${!empty(shukka.okurisaki)}">${f:h(shukka.okurisaki)}</c:if>
							</c:if>
						</td>
						<td>
							<c:if test="${e.plus>0}"><fmt:formatNumber value="${e.plus}" pattern="###,###,##0"/></c:if>
						</td>
						<td>
							<c:if test="${e.plus<0}"><fmt:formatNumber value="${-e.plus}" pattern="###,###,##0"/></c:if>
						</td>
						<td>
							<c:set var="sum" value="${sum+e.plus}"/>
							<fmt:formatNumber value="${sum}" pattern="###,###,##0"/>
						</td>
						<td>
							<c:if test="${empty(e.biko)}">${f:h(e.userRef.model.name)}</c:if>
							<c:if test="${!empty(e.biko)}">${f:h(e.biko)}</c:if>
						</td>
						<td>
							<c:if test="${loginUser.authShukkaEdit && e.shukkaRef.model==null && e.shukkaRecRef.model==null}">
								<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
								<a href="${f:url(deleteUrl)}" onclick="return confirm('削除して良いですか?')">削除</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<jsp:include page="/footer2.jsp"/>
	</div>
	<jsp:include page="/side.jsp"/>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
