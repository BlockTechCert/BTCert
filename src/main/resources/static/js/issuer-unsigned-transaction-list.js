/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */


function generatePageHTML(number) {
    var currentPage =parseInt($(".current-page").val());
    if(currentPage==1){
        var str = '<ul class="pagination">';
    }else{
        var str = '<ul class="pagination"><li><a onclick="getCertsInfoList(' + (currentPage - 1) + ')">&laquo;</a></li>';
    }

    if(currentPage==number){
        var strAfter  = '</ul>';
    }else{
        var strAfter  = '<li><a onclick="getCertsInfoList(' + (currentPage + 1) + ')">&raquo;</a></li></ul>';
    }

    for(var i=1;i<=number;i++){
        if(i==$(".current-page").val()){
            str += '<li class="active"><a onclick="getCertsInfoList(' + i + ')"> ' + i  +  ' </a></li>';
        }else{
            str += '<li><a onclick="getCertsInfoList(' + i + ')"> ' + i  +  ' </a></li>';
        }
    }
    str += strAfter;
    $("#pages").html(str);
}


function getCertsInfoList(pagenumber){
	var queryObject = '{"transactionId":"'+transactionId+'"}'
    var queryConditionJson = {'pageNumber':pagenumber,'pageSize':10,'queryObject':queryObject,'rowCount':1,'sortColumn':'apply_time','sortType':'DESC'};
    var queryConditionStr = JSON.stringify(queryConditionJson);
    queryConditionStr = $.base64('encode', queryConditionStr);
    $.ajax({
        url: "/issuer/getCertsInfoList/"+queryConditionStr,
        dataType: 'json',
        type: 'get',
        contentType: 'application/json',
        // data: queryConditionStr,
        processData: false,
        success: function( result, textStatus, jQxhr ){
            var str='';
            var numbers = 0;
            if(result.rows){
                $.each(result.rows,
                    function(i, item) {

                        /*
						 * var editstr = "<td><button type='button'
						 * class='btn btn-info view' " +
						 * "data-toggle='modal'
						 * data-target='.modal-edit' " +
						 * "onclick='getCertificate(\"" + item.id +
						 * "\")'>Edit</button></td>";
						 */

                        // data-raw='" + JSON.stringify(item) + "'
                        str+=("<tr id='id" + item.id + "'><td>" + item.openbadges.recipient.identity + "</td><td>" +
                        		item.openbadges.issuedOn + "</td><td>"+ item.openbadges.badge.issuer.name +"</td><td>" );
                    });
                $(".sbcon").html(str);
                $(".current-page").val(result.pageNumber);
                numbers = Math.ceil(parseInt(result.total) / parseInt(result.pageSize));
                console.log(numbers);
                generatePageHTML(numbers);
            }else{
                $(".table-dislay").hide();
            }

            $(".loadingsb").hide();

        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });
}

$("#showCerts").click(function(){
	if($(this).html()=="Show"){
		transactionId = $("#id").val();
    	getCertsInfoList(1);
    	$(".table-dislay").show();
    	$(this).html("hidden");
	}else{
		$(this).html("Show");
		$(".table-dislay").hide();
	}
	
});

$(".showKey").click(function(){
	if($(this).html()=="Show"){
		$("input[type='password']",$(".showKey").parent().parent()).attr('type','text');
    	$("#privatekeydes").show();
    	$(this).html("hidden");
	}else{
		$("#privatekeydes").hide();
		$(this).html("Show");
	}
});

function decodeTransactionScript(transactionData){
	var tx = coinjs.transaction();
	try {
		var decode = tx.deserialize(transactionData);
	// console.log(decode);
		$("#verifyTransactionData .transactionVersion").html(decode['version']);
		$("#verifyTransactionData .transactionSize").html(decode.size()+' <i>bytes</i>');
		$("#verifyTransactionData .transactionLockTime").html(decode['lock_time']);
		$("#verifyTransactionData .transactionRBF").hide();
		$("#verifyTransactionData").removeClass("hidden");
		$("#verifyTransactionData tbody").html("");

		var h = '';
		$.each(decode.ins, function(i,o){
			var s = decode.extractScriptKey(i);
			h += '<tr>';
			h += '<td><input class="form-control" type="text" value="'+o.outpoint.hash+'" readonly></td>';
			h += '<td class="col-xs-1">'+o.outpoint.index+'</td>';
			// h += '<td class="col-xs-2"><input class="form-control" type="text" value="'+Crypto.util.bytesToHex(o.script.buffer)+'" readonly></td>';
			if(s['type']=='multisig' && s['signatures']>=1){
				h += ' '+s['signatures'];
			}
			h += '</td>';
			h += '<td class="col-xs-1">';
			if(s['type']=='multisig'){
				var script = coinjs.script();
				var rs = script.decodeRedeemScript(s.script);
				h += rs['signaturesRequired']+' of '+rs['pubkeys'].length;
			} else {
				h += '<span class="glyphicon glyphicon-remove-circle"></span>';
			}
            h += '<td class="col-xs-1"> <span class="glyphicon glyphicon-'+((s.signed=='true')?'ok':'remove')+'-circle"></span>';

            h += '</td>';
			h += '</tr>';

			// debug
			if(parseInt(o.sequence)<(0xFFFFFFFF-1)){
				$("#verifyTransactionData .transactionRBF").show();
			}
		});

		$(h).appendTo("#verifyTransactionData .ins tbody");
		
		h = '';
		
		$.each(decode.outs, function(i,o){

			if(o.script.chunks.length==2 && o.script.chunks[0]==106){ // OP_RETURN

				var data = Crypto.util.bytesToHex(o.script.chunks[1]);
				var dataascii = hex2ascii(data);

				if(dataascii.match(/^[\s\d\w]+$/ig)){
					data = dataascii;
				}

				h += '<tr>';
				h += '<td><input type="text" class="form-control" value="(OP_RETURN) '+data+'" readonly></td>';
				h += '<td class="col-xs-1">'+(o.value/100000000).toFixed(8)+'</td>';
				// h += '<td class="col-xs-2"><input class="form-control" type="text" value="'+Crypto.util.bytesToHex(o.script.buffer)+'" readonly></td>';
				h += '</tr>';
			} else {

				var addr = '';
				if(o.script.chunks.length==5){
					addr = coinjs.scripthash2address(Crypto.util.bytesToHex(o.script.chunks[2]));
				} else {
					var pub = coinjs.pub;
					coinjs.pub = coinjs.multisig;
					addr = coinjs.scripthash2address(Crypto.util.bytesToHex(o.script.chunks[1]));
					coinjs.pub = pub;
				}

				h += '<tr>';
				h += '<td><input class="form-control" type="text" value="'+addr+'" readonly></td>';
				h += '<td class="col-xs-1">'+(o.value/100000000).toFixed(8)+'</td>';
				// h += '<td class="col-xs-2"><input class="form-control" type="text" value="'+Crypto.util.bytesToHex(o.script.buffer)+'" readonly></td>';
				h += '</tr>';
			}
		});
		$(h).appendTo("#verifyTransactionData .outs tbody");
		$(".verifyLink").attr('href','?verify='+$("#verifyScript").val());
			return true;
		} catch(e) {
			return false;
		}
}
	
function hex2ascii(hex) {
	var str = '';
	for (var i = 0; i < hex.length; i += 2)
		str += String.fromCharCode(parseInt(hex.substr(i, 2), 16));
	return str;
}

function checkTransaction(){
	var items= $('#table-list').bootstrapTable('getSelections');

if(items.length == 1){
	 $(".modal-add").modal('show');
	 decodeTransactionScript(items[0].raw_tran);
    }else{
        sweetAlert("Oops...", "You should select one row!", "error")
	};
	
}
	
    function signTransaction(){
    	var items= $('#table-list').bootstrapTable('getSelections');
    if(items.length == 1){
    	$(".modal-edit").modal('show');
    	id  = items[0].id;
    	 $.getJSON("/issuer/getTansacInfo/" + id,function(result){
    	        $.each(result,
    	            function(name, item) {
    	        	if(name =='input'){
    	        		btcert._tranction_inout = item;
    	        	}else if(name == 'output'){
    	        		btcert._tranction_output = item;
    	        	}else if(name == 'signatures'){
    	        		btcert._tranction_signatures = item;
    	        	}else if(name == 'sign_state'){
    	        		if(item){
    	        			var sigInfo = item.split(",");
    	        			var sign_state_html = "";
    	        			for(var i = 0 ; i < sigInfo.length - 1; i++){
    	        				var shtml = sigInfo[i].split("_");
    	        				sign_state_html = sign_state_html + "<tr><td>"+shtml[0]+"</td><td>"+shtml[1]+"</td></tr>"
    	        			}
    	        			$("#sutb").html(sign_state_html)
    	        		}
    	        		$("#"+name).val(item);
    	        	}else{
    	        		 $("#"+name).val(item);
    	        	}
    	        });
    	 });
    	 // $("#alert-message-fail").hide();
    	 // $("#alert-message-success").hide();
    }else{
        sweetAlert("Oops...", "You should select one row!", "error")
    };
}

function getsignature(key){
	var sig = ""
	for(var i = 0 ; i < btcert._tranction_signatures.length; i++ ){
		if(btcert._tranction_signatures[i]["publickey"] == key){
			sig =  btcert._tranction_signatures[i]["signature"];
			break;
		}
	}
	return sig;
}

function signTrsanction(){
	var privatekey = $("#privatekey").val();
	// var tran_hash = $("#tran_hash").val();
	
	// 解决scriptBytes的问题，需要重新赋值一次
	var txins = Object.create(btcert._tranction_inout);
	btcert._tranction_inout = [];
	for (var i = 0 ; i < txins.length; i++){
		var txin = {hash:txins[i].hash,index:txins[i].index,scripts:txins[i].scripts,scriptBytes:Crypto.util.hexToBytes(txins[i].scripts),sequence:txins[i].sequence}
		btcert._tranction_inout.push(txin);
	}
	
	var txous = Object.create(btcert._tranction_output);
	btcert._tranction_output = [];
	for (var i = 0 ; i < txous.length; i++){
		var txou = {scriptHex: txous[i].scriptHex,scriptBytes:Crypto.util.hexToBytes(txous[i].scriptHex),value:txous[i].value}
		btcert._tranction_output.push(txou);
	}
	
	// 当前用户的公钥
	pubkeyInfo = btcert.wif2pubkey(privatekey);  
	pubkey = pubkeyInfo.pubkey;
	
	// 获取所有的public keys
    scripts = btcert.redeem.script;
	pubkeys = btcert.redeemScript2pubkeys(scripts).pubkeys;
    
    // 初始化signature
    for(var i = 0 ; i < txins.length; i++){
    	var tran_hash = btcert.getTransactionHash(i); 
		sigs = Crypto.util.hexToBytes(btcert.getHashSignature(tran_hash,privatekey))
		btcert._tranction_signatures.push({"publickey":pubkey+tran_hash,"signature":btcert.getHashSignature(tran_hash,privatekey)}) 
    }
    
    // 使用 signature
    btcert._tranction_inout_temp = [];
	for(var i = 0 ; i < txins.length; i++){
		
		var scriptResult = [0];
		var tran_hash = btcert.getTransactionHash(i); 
		for(var j = 0 ; j < pubkeys.length ; j++){
			var pubkey = pubkeys[j];
			var sig = getsignature(pubkey+tran_hash);
			// alert(pubkey.substring(0,5)+"##"+tran_hash.substring(0,5)
			// + "&&" + sig.substring(0,5))
			if(sig != ""){
				sigb = Crypto.util.hexToBytes(sig);
				scriptResult = scriptResult.concat(btcert.numToVarInt(sigb.length));
				scriptResult = scriptResult.concat(sigb);
			}
		}
		
		scriptResult.push(76);
		
		var scriptBytes = Crypto.util.hexToBytes(btcert.redeem.script); // redeem
																		// script
		scriptResult = scriptResult.concat(btcert.numToVarInt(scriptBytes.length))
		scriptResult = scriptResult.concat(scriptBytes);
		
		var txin = {hash:txins[i].hash,index:txins[i].index,scripts:Crypto.util.bytesToHex(scriptResult),scriptBytes:scriptResult,sequence:txins[i].sequence}
		btcert._tranction_inout_temp.push(txin);
	}
	
	btcert._tranction_inout = btcert._tranction_inout_temp;
   
	/*
	 * var txous = Object.create(btcert._tranction_output);
	 * 
	 * btcert._tranction_output = []; for (var i = 0 ; i < txous.length;
	 * i++){ var txou = {scriptHex:
	 * txous[i].scriptHex,scriptBytes:Crypto.util.hexToBytes(txous[i].scriptHex),value:txous[i].value}
	 * btcert._tranction_output.push(txou); }
	 */
	
	raw_tran = btcert.serialize();

	console.log(raw_tran);
	
	var merkleRoot = $("#merkleRoot").val();
	// var raw_tran = $("#raw_tran").val();
	var sign_number = parseInt($("#sign_number").val()) + 1;
	var sign_state = $("#sign_state").val();
	var tran_hash = $("#tran_hash").val();
	var broadcast_state = $("#broadcast_state").val();
	var serialize_state = $("#serialize_state").val();
	var certs_number = $("#certs_number").val();
	var id = $("#id").val();
	
	var queryConditionJson = {
	        "id":id,
	        "merkleRoot":merkleRoot,
    		"raw_tran":raw_tran,
    		"tran_hash":tran_hash,
    		"certs_number":certs_number,
    		"sign_number":sign_number,
    		"sign_state":sign_state,
    		"broadcast_state":broadcast_state,
    		"serialize_state":serialize_state,
    		"input":btcert._tranction_inout,
    		"output":btcert._tranction_output,
    		"signatures":btcert._tranction_signatures
    }
	
    var queryConditionStr = JSON.stringify(queryConditionJson);

     $.ajax({
        url: "/issuer/signTansacInfo",
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: queryConditionStr,
        processData: false,
        success: function( result, textStatus, jQxhr ){
        	  if(result.success == "1"){
                    swal({
                        title: "Good Job!",
                        text: "Your raw transaction has been signed successfully!.",
                        type: "success",
                        showCancelButton: false,
                        confirmButtonText: "OK",
                        closeOnConfirm: false
                    },
                    function(){
                        window.location.href = "/issuer/signed_transaction_list";
                    });
                }else{
                    swal("Oops...", "Something happened", "error");
                }
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    }); 
	
}

$(function () {
    var oTable = new TableInit();
    oTable.Init();
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
});


var TableInit = function () {
    var oTableInit = new Object();
    var student_name = $("#student-name").html();
    // 初始化Table
    oTableInit.Init = function () {
        $('#table-list').bootstrapTable({
            url: '/issuer/postTansacInfoList',         
            method: 'post',                      
            toolbar: '#toolbar',                
            striped: true,                      
            cache: false,                       
            pagination: true,                   
            sortable: false,                     
            sortOrder: "asc",                   
            queryParams: oTableInit.queryParams,
            sidePagination: "server",           
            queryParamsType:'',
            pageNumber:1,                       
            pageSize: 10,                       
            pageList: [10, 25, 50, 100],        
            search: true,                       
            strictSearch: true,
            showColumns: true,                  
            showRefresh: true,                  
            minimumCountColumns: 2,             
            clickToSelect: true,                
            height: 500,                        
            uniqueId: "ID",                     
            showToggle:true,                    
            cardView: false,                    
            detailView: false,                   
            columns: [{
                checkbox: true
            }, {
                field: 'id',
                title: 'id'
            }, /*
				 * { field: 'raw_tran', title: 'raw_tran' }, { field:
				 * 'tran_hash', title: 'tran_hash' },
				 */{
                field: 'sign_number',
                title: 'sign_number'
            },{
            	field: 'sign_state',
            	title:'sign_state'
            }]
        });
    };

    oTableInit.queryParams = function (params) {
        var queryConditionJson = {
        		'pageNumber':params.pageNumber ,
        		'pageSize':params.pageSize ,
        		'queryObject':'{"sign_state":"notExists_'+student_name+'","serialize_state":"1","broadcast_state":"0","id":"matchAll_'+params.searchText+'"}',
        		'rowCount':1,
        		'sortColumn':'apply_time',
        		'sortType':'DESC'
        };
        
        
        return queryConditionJson;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
    };
    return oInit;
};