/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
$(function() {
	var oTable = new TableInit();
	oTable.Init();
	var oButtonInit = new ButtonInit();
	oButtonInit.Init();
});

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
			}, /*
				 * { field: 'raw_tran', title: 'raw_tran' },
				 */{
				field : 'certs_number',
				title : 'certs_number'
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
			'queryObject' : '{"serialize_state":"0","id":"matchAll_'+params.searchText+'"}',
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
	oInit.Init = function() {};
	return oInit;
};

function serializeTrans() {
	$(".modal-edit").modal('show');
	generateMerkleTree();
}
window.items = "";

function generateMerkleTree() {
	$("#loadingcontext").html(
			"Generating the merkle tree from selected certificates");
	window.items = $('#table-list').bootstrapTable('getSelections'); // 设计成全局变量
	if (items.length == 1) {
		$.getJSON("/issuer/getTansacInfo/" + items[0].id, function(result) {
			$("#generateMerkleTree").html(
					"Step1: The merkle tree generate successfully, the root is :"
							+ result.merkleRoot);
			getSchIdentity(result);
		})
	} else {
		sweetAlert("Oops...", "You should no more one row!", "error")
		return;
	}
}

function getSchIdentity(result) {
	window.merkleRoot = result.merkleRoot
	$("#loadingcontext").html(
			"getting the school identity(address) from local database");

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
						$("#gettingIdentity")
								.html(
										"Step2: The the school identity(address) download successfully, its main address is:"
												+ latest_btc_address);

						latest_recycle_address = result.latest_recycle_address;

						btcert.redeem = {
							'addr' : latest_btc_address,
							'type' : 'multiaddrss',
							'script' : latest_redeem_script
						};

						createTransaction(merkleRoot);
					})
}

function createTransaction() {

	$("#loadingcontext").html(
			"fetching the unspent coin from the blockchain by school address");
	btcert.getUnspentTransaction(updateTransactions);
	$("#fetchingCoin").html("Step3: Fetch the unspent coin successfully");

}

var updateTransactions = function() {

	btcert.initOutput(latest_btc_address);
	btcert.initOutput(latest_recycle_address);
	btcert.initOutput(merkleRoot);

	raw_tran = btcert.serialize();
	// tran_hash = btcert.getTransactionHash();
	tran_hash = "";
	// console.log(raw_tran)
	// redeemScript2pubkeys
	$("#loadingcontext").html(
			"Packaging the transaction with unspent coin and merkle tree");
	var queryConditionJson = {
		"id" : items[0].id,
		"merkleRoot" : items[0].merkleRoot,
		"certs_number" : items[0].certs_number,
		"raw_tran" : raw_tran,
		"tran_hash" : tran_hash,
		"sign_number" : items[0].sign_number,
		"sign_state" : items[0].sign_state,
		"broadcast_state" : items[0].broadcast_state,
		"serialize_state" : items[0].serialize_state,
		"input" : btcert._tranction_inout,
		"output" : btcert._tranction_output,
		"signatures" : items[0].signatures,
	}

	var queryConditionStr = JSON.stringify(queryConditionJson);

	$
			.ajax({
				url : "/issuer/postTansacInfo",
				dataType : 'json',
				type : 'post',
				contentType : 'application/json',
				data : queryConditionStr,
				processData : false,
				success : function(result, textStatus, jQxhr) {
					// alert(result)
					$("#packagingTransaction")
							.html(
									"Step4: Package the transaction with unspent coin and merkle tree successfully");
					window.location.href = "/issuer/unserialized_transaction_list"
				},
				error : function(jqXhr, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
}
