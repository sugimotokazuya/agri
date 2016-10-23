<%@page pageEncoding="UTF-8" isELIgnored="false" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<fmt:setTimeZone value="Asia/Tokyo" />

<meta charset=UTF-8" />
<title>あぐりクラウドβ</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0" />  
<link rel="stylesheet" type="text/css" href="/css/global2.css"/>
<link rel="stylesheet" href="/css/font-awesome/css/font-awesome.min.css"/>
<script type="text/javascript" src="${f:url('/js/jquery-2.1.3.min.js')}"></script>
<script type="text/javascript" src="${f:url('/js/jquery.blockUI.js')}"></script>
<script type="text/javascript">
noBlock=false;
$(function() {
	$("#toggleButton").click(function() {
		$("#menu").slideToggle();
	});
	setTime();

	$("form").submit(function() {
		if(!noBlock) blockUI();	
	});
});

function blockUI() {
	$.blockUI({ 
    	message: '<div class="executing"><i class="fa fa-cog fa-spin"></i> 処理中です．．．</div>', 
    	timeout: 30000 
	}); 
}

function unblockUI() {
	$.unblockUI();
}

function setTime() {
	var now = new Date();
	var hour = now.getHours(); // 時
	var min = now.getMinutes(); // 分
	var sec = now.getSeconds(); // 秒
	// 数値が1桁の場合、頭に0を付けて2桁で表示する指定
	if(hour < 10) { hour = "0" + hour; }
	if(min < 10) { min = "0" + min; }
	if(sec < 10) { sec = "0" + sec; }
	// フォーマットを指定（不要な行を削除する）
	var watch = hour + '時' + min + '分';
	$('#time').text(watch);
	setTimeout("setTime()", 1000);
}


	var clientId = "475270187368-nh0qnq3pjhamb0v598ocpb4r5g61guel.apps.googleusercontent.com"; //事前に生成しておいたClient ID
    var scopes = "https://www.googleapis.com/auth/gmail.readonly"; 
    
    function handleClientLoad() {
        window.setTimeout(checkAuth,1);
    }
 
    function checkAuth() {
        gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: true}, handleAuthResult);
    }
 
    function handleAuthResult(authResult) {
    	
        var authorizeButton = document.getElementById('authorize-button');
        if (authResult && !authResult.error) {
            init();
        } else {
        	$("#authorize-button").show();
        	authorizeButton.onclick = handleAuthClick;
        }
     }
 
    function handleAuthClick(event) {
        gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
        return false;
    }

	function init(){
        makeApiCall({ 
           'userId': 'me', //取得対象の指定（この場合は自分）
           'labelIds':['INBOX'], //ラベルを指定することで取得したいメッセージのレベルを指定できる
           'maxResults':4 //最大取得件数
　　		});
　	}
　	
	function makeApiCall(option) {
        gapi.client.load('gmail', 'v1', function() {
            var request = gapi.client.gmail.users.messages.list(option);
            request.execute(function(resp) {
				var result = resp.messages;
			　　	var requests = {};
			    var httpBatch = gapi.client.newHttpBatch(); //①
			　　	for(var count in result){
			        var messageId = result[count].id;
			　　　	requests[messageId] = makeGetRequest(messageId);
			        httpBatch.add(requests[messageId],{'id':messageId}); //②
			    }
			　  getUserMail(requests, httpBatch);
			});
		});
	}
				
	function makeGetRequest(messageId){
     	return gapi.client.request({
        	'path':'gmail/v1/users/me/messages/'+messageId,
        	'method':'GET',
        	'params':{'format': "full"}
 		});
	}
 
	function getUserMail(requests,httpBatch) {
     	httpBatch.execute(function(resp){ //③
     		var count = 0;
       		for(var messageId in resp){
           		var result = resp[messageId].result;
           		var headers = result.payload.headers;
           		var labelIds = result.labelIds;
           		if(labelIds.indexOf('UNREAD') > 0) count++;
　  			}	
			$("#midoku").text("(" + count + ")");
     	});
	}

</script>
<script src="https://apis.google.com/js/client.js?onload=handleClientLoad"></script>