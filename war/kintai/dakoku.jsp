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
	<style>
	div.content {
		margin-left:10px;
	}
	div.content button.dakoku {
		width:300px;
		height:180px;
		font-size:35px;
		margin-top:20px;
		margin-left:10px;
		background-color:#f2dae8;
		border-radius: 5px; 
	}
	
	div.content button.s1 {
		background-color:#c9d744;
	}
	
	div.content button.s2 {
		background-color:#cccccc;
	}
	
	div#dakokuBox {
		display:none;
	}
	div#dakokuBox button.dakoku {
		background-color:#ebc061;
	}
	
	h1#userName {
		font-size:40px;
	}
	span.dakokuIcon {
		color:#b3ce5b;
	}
	
	button#shiftButton {
		background-color:#b2d6d4;
	}
	</style>
	
	<script type="text/javascript">
	
	$(function () {
		check(true);
  		init();
	});
	
	function init() {
		$("#userBox button.dakoku").each(function(){
    		var userKey = $(this).attr("id");
    		var status = Number($(this).attr("name"));
    		switch(status) {
    			case 0:
    				$(this).css("background-color", "#f2dae8");
    				break;
    			case 1:
    				$(this).css("background-color", "#c9d744");
    				break;
    			case 2:
    				$(this).css("background-color", "#cccccc");
    				break;
    			case 3:
    				$(this).css("background-color", "#c9d744");
    				break;
    			case 4:
    				$(this).css("background-color", "#f2dae8");
    				break;
    			case 5:
    				$(this).css("background-color", "#c9d744");
    				break;
    			case 6:
    				$(this).css("background-color", "#cccccc");
    				break;
    		}
  		});
	}
	
	function check(first) {
		var serverDay = $('#day').val();
		var localDay = (new Date()).getDate();
		if(serverDay != localDay && first == false) {
			window.location.href ='/kintai/dakoku';
		}
		
		setTimeout("check(false)", 500);
	}
		
	function selectUser(key, name) {
		$('#userBox').fadeOut('fast',function() {
			$('#dakokuBox').fadeIn('fast');
		});
		$('#user').val(key);
		$('#userName').text(name);
		var status = Number($('#' + key).attr("name"));
		//alert(status);
		switch(status) {
			case 0:
			$('#dakokuButton').text('出勤');
			$('#dakokuButton').show();
			$('#teiseiButton').hide();
			$("#dakokuButton").unbind();
			$('#dakokuButton').click(function() {
				dakoku(1);
			});
			$('#outButton').hide();
			break;
			case 1:
			$('#dakokuButton').text('退勤');
			$('#dakokuButton').show();
			$('#teiseiButton').text("出勤を取消");
			$('#teiseiButton').show();
			$("#dakokuButton").unbind();
			$('#dakokuButton').click(function() {
				dakoku(2);
			});
			$("#teiseiButton").unbind();
			$('#teiseiButton').click(function() {
				teisei(0);
			});
			$('#outButton').show();
			
			break;
			case 2:
			$('#outButton').hide();
			$('#dakokuButton').hide();
			$('#teiseiButton').text("退勤を取消");
			$('#teiseiButton').show();
			$("#teiseiButton").unbind();
			$('#teiseiButton').click(function() {
				teisei(1);
			});
			$('#outButton').hide();
			break;
			
			case 3:
			$('#dakokuButton').text('退勤');
			$('#dakokuButton').show();
			$('#teiseiButton').text("出勤を取消");
			$('#teiseiButton').show();
			$("#dakokuButton").unbind();
			$('#dakokuButton').click(function() {
				dakoku(2);
			});
			$("#teiseiButton").unbind();
			$('#teiseiButton').click(function() {
				teisei(0);
			});
			$('#outButton').show();
			break;
			
			case 4:
			$('#dakokuButton').show();
			$('#dakokuButton').text('戻り');
			$("#dakokuButton").unbind();
			$('#dakokuButton').click(function() {
				dakoku(5);
			});
			$('#teiseiButton').text("外出を取消");
			$('#teiseiButton').show();
			$("#teiseiButton").unbind();
			$('#teiseiButton').click(function() {
				teisei(3);
			});
			$('#outButton').hide();
			break;
			case 5:
			$('#dakokuButton').text('退勤');
			$('#dakokuButton').show();
			$('#teiseiButton').text("戻りを取消");
			$('#teiseiButton').show();
			$("#dakokuButton").unbind();
			$('#dakokuButton').click(function() {
				dakoku(6);
			});
			$("#teiseiButton").unbind();
			$('#teiseiButton').click(function() {
				teisei(4);
			});
			$('#outButton').hide();
			break;
			
			case 6:
			$('#outButton').hide();
			$('#dakokuButton').hide();
			$('#teiseiButton').text("退勤を取消");
			$('#teiseiButton').show();
			$("#teiseiButton").unbind();
			$('#teiseiButton').click(function() {
				teisei(5);
			});
			$('#outButton').hide();
			break;
		}
	}
		
		function dakoku(status) {
			blockUI();
			$.ajax({
      			url: '/kintai/dakoku/' + $('#user').val() + '/' + status,
      			type: 'POST',
      			cache: false
    		})
    		.done(function( data, textStatus, jqXHR ) {
    			$('#' + $('#user').val()).attr("name", status);
      			init();
    		})
    		.fail(function( jqXHR, textStatus, errorThrown ){
      			alert('エラー!');
    		})      
    		.always(function( data, textStatus, errorThrown ){
    			unblockUI();
      			$('#dakokuBox').fadeOut(function() {
      				$('#userBox').fadeIn();
      			});
    		});
		}
		
		function teisei(status) {
			blockUI();
			var key = $('#user').val();
			
			$.ajax({
      			url: '/kintai/dakokuTeisei/' + key + '/' + status,
      			type: 'POST',
      			cache: false
    		})
    		.done(function( data, textStatus, jqXHR ) {
    			$('#' + key).attr("name", status);
      			init();
    		})
    		.fail(function( jqXHR, textStatus, errorThrown ){
      			alert('エラー!');
    		})      
    		.always(function( data, textStatus, errorThrown ){
    			unblockUI();
				$('#dakokuBox').fadeOut(function() {
					$('#userBox').fadeIn();
				});
				
    		});
		}
		
		function back() {
			$('#dakokuBox').fadeOut(function() {
				$('#userBox').fadeIn();
			});
			
		}
	</script>
</head>

<body>
	<jsp:include page="/header2.jsp"/>
	<div class="contentBox">
		<div class="content">
			<h1>
				クラウド打刻 <span class="dakokuIcon"><i class="fa fa-hand-o-up"></i></span>　
				<i class="fa fa-clock-o"></i> <fmt:formatDate value="${today}" pattern="yyyy-MM-dd（E）"/>
				<span id="time"></span>
			</h1>
			
			<div id="userBox">
				<c:set var="url" value="/kintai/shift/dakokuShift" />
				<button id="shiftButton" class="dakoku" type="button" onclick="window.location.href='${f:url(url)}';">シフト表示</button>
				<c:forEach var="e" items="${userList}">
					<%-- idにUserのKeyを、nameにステータスを格納 --%>
					<button id="${f:h(e.key)}" name="${f:h(map[e.key])}" class="dakoku" type="button" onclick="selectUser('${f:h(e.key)}','${f:h(e.name)}');">${f:h(e.name)}</button>
				</c:forEach>
			</div>
			<div id="dakokuBox">
				<h1 id="userName"></h1>
				<input type="hidden" name="user" id="user"/>
				<button id="dakokuButton" class="dakoku" type="button"></button><br/>
				<button class="dakoku" type="button" onclick="back();">戻る</button>
				<button id="teiseiButton" class="dakoku" type="button"></button>
				<button id="outButton" class="dakoku" type="button" onclick="dakoku(4);">外出</button>
				<input type="hidden" id="day" value="<fmt:formatDate value='${today}' pattern='d'/>"/>
				<input type="hidden" name="size" value="${f:h(size)}"/>
			</div>
		</div>
		
	</div>
</body>
</html>
