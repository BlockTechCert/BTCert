/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
function getBalance(address) {
	$.getJSON("https://blockchain.info/q/addressbalance/" + address, function(
			result) {
		mbtc = parseInt(result) / (100000000)
		$("#balance").html("Balance: " + mbtc)
	})
}


getBalance("3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd");

function getBalance(address) {
	$.getJSON("https://blockchain.info/q/addressbalance/" + address, function(
			result) {
		mbtc = parseInt(result) / (100000000)
		$("#balance").html("Balance: " + mbtc)
	})
}

$(function() {
	getTransactions("3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd");
})

function getTransactions(address) {
	$
			.getJSON(
					"http://btc.blockr.io/api/v1/address/txs/" + address,
					function(result) {
						var output = [];
						// output.push('<tr><th
						// class="span12"><strong>Transaction</strong></th><th><strong>Time_utc</strong></th><th><strong>Confirmations</strong></th></tr>');

						// for(var i = 0 ; i < result.data.txs.length; i++ ){
						for (var i = 0; i < 10; i++) {
							output
									.push(
											'<tr><td class="span12"><strong><a target = _blank href="https://blockchain.info/tx/',
											result.data.txs[i].tx,
											'">',
											result.data.txs[i].tx.substring(0,
													5)
													+ '...<a></strong></td><td>',
											result.data.txs[i].time_utc,
											'</td><td>',
											result.data.txs[i].confirmations,
											'</td><td>',
											result.data.txs[i].amount,
											'</td></tr>');
						}
						$(".sbcon").html(output.join(''))
					})
}

function openTransactionDetail() {
	getTransactions("3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd");
	$("#detailAddressDiv").hide();
	$("#detailSignDiv").hide();
	$("#detailTransactionDiv").toggle();
}

function openAddressDetail() {
	$
			.getJSON(
					"/issuer/getAllSchIdentity/",
					function(result) {
						result = result[0];
						latest_redeem_script = result.latest_redeem_script;
						btc_address_list = result.btc_address_list[0];

						latest_btc_address = "";

						for ( var item in btc_address_list) {
							latest_btc_address = item;
							break;
						}
						latest_recycle_address = result.latest_recycle_address;
						$("#detailTransactionDiv").hide();
						$("#detailSignDiv").hide();
						$("#detailAddressDiv").toggle();

						$("#address").html(latest_btc_address)
						$("#recycle_address").html(latest_recycle_address)
						$("#multiSig").html(latest_redeem_script)

						var pubInfo = btcert
								.redeemScript2pubkeys(latest_redeem_script);

						$("#signaturesRequired").html(
								"It needs at least "
										+ pubInfo.signaturesRequired
										+ " signers to sign before broadcast");
						$("#pubkeyNums").html(
								"There are " + pubInfo.pubkeyNums
										+ " Public keys");
						// $("#signaturesRequired").html(pubInfo.pubkeys);

						var publicKeyHtml = "";
						for (var i = 0; i < pubInfo.pubkeys.length; i++) {
							publicKeyHtml = publicKeyHtml
									+ "<p style=' word-break:break-all; word-wrap:break-word ;'>"
									+ pubInfo.pubkeys[i] + "</p>";
						}

						$("#publicKey").html(publicKeyHtml);

					})
}

openAddressDetail();

function openSignDetail() {
	// detailSignDiv

	$("#detailTransactionDiv").hide();
	$("#detailAddressDiv").hide();
	$("#detailSignDiv").toggle();
	var oTable = new TableInit();
	oTable.Init();
	var oButtonInit = new ButtonInit();
	oButtonInit.Init();
}

var TableInit = function() {
	var oTableInit = new Object();
	oTableInit.Init = function() {
		$('#table-list').bootstrapTable({
			url : '/issuer/postTansacInfoList', 
			method : 'post', 
			toolbar : '#toolbar', 
			striped : true, 
			cache : false, 
			pagination : true, 
			sortable : false, 
			sortOrder : "asc", 
			queryParams : oTableInit.queryParams,
			sidePagination : "server", 
			queryParamsType : '',
			pageNumber : 1, 
			pageSize : 10, 
			pageList : [ 10, 25, 50, 100 ], 
			search : true, 
			strictSearch : true,
			showColumns : true, 
			showRefresh : true, 
			minimumCountColumns : 2, 
			clickToSelect : true, 
			height : 500, 
			uniqueId : "ID", 
			showToggle : true, 
			cardView : false, 
			detailView : false, 
			columns : [ {
				checkbox : true
			}, {
				field : 'id',
				title : 'id'
			}, {
				field : 'tran_hash',
				title : 'tran_hash'
			}, {
				field : 'sign_number',
				title : 'sign_number'
			}, {
				field : 'sign_state',
				title : 'sign_state'
			} ]
		});
	};
	oTableInit.queryParams = function(params) {
		var queryConditionJson = {
			'pageNumber' : params.pageNumber,
			'pageSize' : params.pageSize,
			'queryObject' : '{"broadcast_state":"0","id":"matchAll_'+params.searchText+'"}',
			'rowCount' : 1,
			'sortColumn' : 'apply_time',
			'sortType' : 'DESC'
		};

		return queryConditionJson;
	};
	return oTableInit;
};

var ButtonInit = function() {
	var oInit = new Object();
	oInit.Init = function() {
	};

	return oInit;
};

function signTransaction() {
	var items = $('#table-list').bootstrapTable('getSelections');
	if (items.length == 1) {
		$(".modal-edit").modal('show');
		id = items[0].id;
		$.getJSON("/issuer/getTansacInfo/" + id, function(result) {
			$.each(result, function(name, item) {
				if (name == 'input') {
					btcert._tranction_inout = item;
				} else if (name == 'output') {
					btcert._tranction_output = item;
				} else if (name == 'signatures') {
					btcert._tranction_signatures = item;
				} else {
					$("#" + name).val(item);
				}
			});
		});
		// $("#alert-message-fail").hide();
		// $("#alert-message-success").hide();
	} else {
		sweetAlert("Oops...", "You should select one row!", "error")
	}
	;
}

function getsignature(key) {
	var sig = ""
	for (var i = 0; i < btcert._tranction_signatures.length; i++) {
		if (btcert._tranction_signatures[i]["publickey"] == key) {
			sig = btcert._tranction_signatures[i]["signature"];
			break;
		}
	}
	return sig;
}

function signTrsanction() {
	var privatekey = $("#privatekey").val();
	var tran_hash = $("#tran_hash").val();
	pubkeyInfo = btcert.wif2pubkey(privatekey);
	sigs = Crypto.util.hexToBytes(btcert
			.getHashSignature(tran_hash, privatekey))
	pubkey = pubkeyInfo.pubkey;

	var signatures = [];
	var signature = Crypto.util.bytesToHex(sigs);
	alert(btcert._tranction_signatures.length)
	btcert._tranction_signatures.push({
		"publickey" : pubkey,
		"signature" : signature
	})
	alert(btcert._tranction_signatures.length);
	// signatures.push({"publickey":publickey,"signature":signature})
	scripts = btcert._tranction_inout[0].scripts
	pubkeys = btcert.redeemScript2pubkeys(scripts).pubkeys;

	var scriptResult = [ 0 ];
	for (var i = 0; i < pubkeys.length; i++) {
		var pubkey = pubkeys[i];
		var sig = getsignature(pubkey)
		console.log(pubkey + "@@@@" + sig)
		if (sig != "") {
			sigb = Crypto.util.hexToBytes(sig);
			scriptResult = scriptResult.concat(btcert.numToVarInt(sigb.length));
			scriptResult = scriptResult.concat(sigb);
		}
	}

	// scriptBytes = btcert._tranction_inout[0].scriptBytes;

	scriptBytes = Crypto.util.hexToBytes(btcert._tranction_inout[0].scripts);

	scriptResult.push(76);
	scriptResult = scriptResult.concat(btcert.numToVarInt(scriptBytes.length))
	scriptResult = scriptResult.concat(scriptBytes);

	var txin = {};
	txin.scriptBytes = scriptResult;
	txin.hash = btcert._tranction_inout[0].hash;
	txin.index = btcert._tranction_inout[0].index;
	txin.scripts = btcert._tranction_inout[0].scripts;
	txin.sequence = btcert._tranction_inout[0].sequence;

	// txin.scripts = Crypto.util.bytesToHex(scriptResult);
	btcert._tranction_inout.pop()
	btcert._tranction_inout.push(txin);

	// btcert.initOutput("3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd");
	// btcert.initOutput("1Jw1qkMvP7tp2RuErFoSbxMwfyrnFVHkRr");
	// btcert.initOutput("64303362353033363435616264313932393564393362333462636664626461363039643538613035636135383839306562646632623161326363626139313534");

	var txous = Object.create(btcert._tranction_output);

	btcert._tranction_output = [];
	for (var i = 0; i < txous.length; i++) {
		var txou = {
			scriptHex : txous[i].scriptHex,
			scriptBytes : Crypto.util.hexToBytes(txous[i].scriptHex),
			value : txous[i].value
		}
		btcert._tranction_output.push(txou);
	}

	raw_tran = btcert.serialize();

	console.log(raw_tran);

	var merkleRoot = $("#merkleRoot").val();
	var certIdList = $("#certIdList").val();
	// var raw_tran = $("#raw_tran").val();
	var sign_number = parseInt($("#sign_number").val()) + 1;
	var sign_state = $("#sign_state").val();
	var tran_hash = $("#tran_hash").val();
	var broadcast_state = $("#broadcast_state").val();
	var id = $("#id").val();

	var queryConditionJson = {
		"id" : id,
		"merkleRoot" : merkleRoot,
		"certIdList" : certIdList,
		"raw_tran" : raw_tran,
		"sign_number" : sign_number,
		"sign_state" : sign_state,
		"tran_hash" : tran_hash,
		"broadcast_state" : broadcast_state,
		"input" : btcert._tranction_inout,
		"output" : btcert._tranction_output,
		"signatures" : btcert._tranction_signatures
	}

	var queryConditionStr = JSON.stringify(queryConditionJson);

	$.ajax({
		url : "/issuer/updateTansacInfo",
		dataType : 'json',
		type : 'post',
		contentType : 'application/json',
		data : queryConditionStr,
		processData : false,
		success : function(result, textStatus, jQxhr) {
			alert("gengxin")
		},
		error : function(jqXhr, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});

	// initialization options for the progress bar
	var progress = $("#progress")
			.shieldProgressBar(
					{
						min : 0,
						max : 100,
						value : 0,
						layout : "circular",
						layoutOptions : {
							circular : {
								borderColor : "#FF7900",
								width : 17,
								borderWidth : 3,
								color : "#1E98E4",
								backgroundColor : "transparent"
							}
						},
						text : {
							enabled : true,
							template : '<span style="font-size:47px; color: #1E98E4">{0:n1}%</span>'
						},
					}).swidget();

	function resetActive(event, percent, step) {
		progress.value(percent);

		$(".progress-bar").css("width", percent + "%").attr("aria-valuenow",
				percent);
		$(".progress-completed").text(percent + "%");

		$("div").each(function() {
			if ($(this).hasClass("activestep")) {
				$(this).removeClass("activestep");
			}
		});

		if (event.target.className == "col-md-2") {
			$(event.target).addClass("activestep");
		} else {
			$(event.target.parentNode).addClass("activestep");
		}

		hideSteps();
		showCurrentStepInfo(step);
	}

	function hideSteps() {
		$("div").each(function() {
			if ($(this).hasClass("activeStepInfo")) {
				$(this).removeClass("activeStepInfo");
				$(this).addClass("hiddenStepInfo");
			}
		});
	}

	function showCurrentStepInfo(step) {
		var id = "#" + step;
		$(id).addClass("activeStepInfo");
	}

}
