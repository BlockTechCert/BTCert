/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

function startMergedCerts() {

	var items = $('#table-list').bootstrapTable('getSelections');
	if (items.length == 0) {
		sweetAlert("Oops...", "You should select at least one row!", "error")
		return;
	} else {
		$(".modal-edit").modal('show');
	}
	;
}

var curTranType = "";
var curTranId = "";
function loadNewTransaction() {
	curTranType = "NewTransaction";
	curTranId = "";
	$("#tranlist").hide();
}

function loadExistedTransaction() {
	$("#tranlist").show();
	curTranType = "ExistedTransaction";
	postTansacInfoList(1);
}

function confirmMerge() {
	if (curTranType == "NewTransaction") {

	} else if (curTranType == "ExistedTransaction") {
		var obj = $("input[name='subBox']:checked");
		if (obj.length != 1) {
			sweetAlert("Oops...", "You should select one transaction!", "error")
		} else {
			curTranId = obj[0].value;
		}
	} else {
		sweetAlert("Oops...", "You should select transaction type!", "error")
		return;
	}

	var items = $('#table-list').bootstrapTable('getSelections');
	window.certsId = ""; // 全局
	$.each(items, function(i, item) {
		certsId = certsId + "'" + item.id + "',"
	})
	certsId = certsId.substring(0, certsId.length - 1)
	generateMerkleTree(certsId, curTranId);
}

function generateMerkleTree(certsId, curTranId) {

	var postData = {
		"certsId" : certsId,
		"curTranId" : curTranId
	}
	$.post("/checker/mergeCertificate", postData, function(result) {

		result = eval('(' + result + ')');

		if (result.success == 1) {
			swal({
				title : "Good job!",
				text : "Your certificate has been megered successfully.",
				type : "success",
				showCancelButton : false,
				confirmButtonText : "OK",
				closeOnConfirm : false
			}, function() {
				window.location.href = "unmerged_certificate_list";
			});

		} else {
			swal("Oops...", "Something happened", "error");
		}
	})
}

/*
 * function getSchIdentity(){ $("#loadingcontext").html("getting the school
 * identity(address) from local database");
 * 
 * 
 * $.getJSON("/issuer/getAllSchIdentity/",function(result){
 * 
 * result = result[0]; latest_redeem_script = result.latest_redeem_script;
 * btc_address_list = result.btc_address_list[0];
 * 
 * latest_btc_address = "";
 * 
 * for(var item in btc_address_list){ latest_btc_address = item; break; }
 * $("#gettingIdentity").html("Step2: The the school identity(address) download
 * successfully, its main address is:" + latest_btc_address);
 * 
 * latest_recycle_address = result.latest_recycle_address;
 * 
 * btcert.redeem = {'addr':latest_btc_address, 'type':'multiaddrss',
 * 'script':latest_redeem_script}; createTransaction(merkleRoot); }) }
 * 
 * function createTransaction(){
 * 
 * $("#loadingcontext").html("fetching the unspent coin from the blockchain by
 * school address"); btcert.getUnspentTransaction(insertTransactions);
 * $("#fetchingCoin").html("Step3: Fetch the unspent coin successfully");
 * 
 * btcert.initOutput(latest_btc_address);
 * btcert.initOutput(latest_recycle_address); btcert.initOutput(merkleRoot);
 * 
 * raw_tran = btcert.serialize(); hash = btcert.getTransactionHash();
 *  }
 */

/*
 * var insertTransactions = function(certsId,merkleRoot,curTranId){
 * 
 * raw_tran = ""; hash = "";
 * 
 * var queryConditionJson = { "id":curTranId, "merkleRoot":merkleRoot,
 * "certIdList":certsId, "raw_tran":raw_tran, "sign_number":"0",
 * "sign_state":"siger1_0,siger2_0,siger3_0", "tran_hash":hash, "input":[],
 * "output":[] }
 * 
 * var queryConditionStr = JSON.stringify(queryConditionJson);
 * 
 * $.ajax({ url: "/issuer/postTansacInfo", dataType: 'json', type: 'post',
 * contentType: 'application/json', data: queryConditionStr, processData: false,
 * success: function( result, textStatus, jQxhr ){ sweetAlert("Good job", "Your
 * certificate has been megered successfully", "OK") }, error: function( jqXhr,
 * textStatus, errorThrown ){ sweetAlert("Oops...", "Something went wrong!",
 * "error"); } }); }
 */

$(function() {
	var oTable = new TableInit();
	oTable.Init();
	var oButtonInit = new ButtonInit();
	oButtonInit.Init();

});

var TableInit = function() {
	var oTableInit = new Object();
	// 初始化Table
	oTableInit.Init = function() {
		$('#table-list').bootstrapTable({
			url : '/checker/postCertsInfoList', // 请求后台的URL（*）
			method : 'post', // 请求方式（*）
			toolbar : '#toolbar', // 工具按钮用哪个容器
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			sortOrder : "asc", // 排序方式
			queryParams : oTableInit.queryParams,// 传递参数（*）
			sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
			queryParamsType : '',
			pageNumber : 1, // 初始化加载第一页，默认第一页
			pageSize : 10, // 每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], // 可供选择的每页的行数（*）
			search : true, // 是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : true,
			showColumns : true, // 是否显示所有的列
			showRefresh : true, // 是否显示刷新按钮
			minimumCountColumns : 2, // 最少允许的列数
			clickToSelect : true, // 是否启用点击选中行
			height : 500, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "ID", // 每一行的唯一标识，一般为主键列
			showToggle : true, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'id',
				title : 'Applicant ID'
			}, {
				field : 'transactionId',
				title : 'Transaction Id'
			}, {
				field : 'createTime',
				title : 'Create Time'
			}, {
				field : 'openbadges.recipient.identity',
				title : 'Recipient Name'
			}, {
				field : 'cstate',
				title : 'State'
			}, {
				field : 'openbadges.badge.issuer.name',
				title : 'Issuer Name'
			} ]
		});
	};

	// 得到查询的参数
	oTableInit.queryParams = function(params) {
		var queryConditionJson = {
			'pageNumber' : params.pageNumber,
			'pageSize' : params.pageSize,
			'queryObject' : '{"cstate":"new","id":"matchAll_'+params.searchText+'"}',
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
	var postdata = {};

	oInit.Init = function() {
		// 初始化页面上面的按钮事件
	};

	return oInit;
};

function postTansacInfoList(pagenumber) {
	var queryConditionJson = {
		'pageNumber' : pagenumber,
		'pageSize' : 10,
		'queryObject' : '{"serialize_state":"0"}',
		'rowCount' : 1,
		'sortColumn' : 'apply_time',
		'sortType' : 'DESC'
	};
	var queryConditionStr = JSON.stringify(queryConditionJson);
	// queryConditionStr = $.base64('encode', queryConditionStr);
	$
			.ajax({
				url : "/checker/postTansacInfoList",
				dataType : 'json',
				type : 'post',
				contentType : 'application/json',
				data : queryConditionStr,
				processData : false,
				success : function(result, textStatus, jQxhr) {
					var str = '';
					var numbers = 0;
					if (result.rows) {
						$
								.each(
										result.rows,
										function(i, item) {
											/*
											 * var deletestr = "<td><button
											 * type='button' class='btn btn-info
											 * view' " +
											 * "onclick='delStuConf(\"" +
											 * item.id + "\")'>Delete</button></td> ";
											 */

											str += ("<tr id='id"
													+ item.id
													+ "'><td><input type='checkbox' name='subBox' value='"
													+ item.id + "'></td><td>"
													+ item.id + "</td><td>"
													+ item.certs_number
													+ "</td><td>"
													+ item.sign_number + "</td>");
										});
						$(".sbcon").html(str);
						$(".current-page").val(result.pageNumber);
						numbers = Math.ceil(parseInt(result.total)
								/ parseInt(result.pageSize));
						console.log(numbers);
						generatePageHTML(numbers);
					} else {
						$(".table-recent-apply").hide();
					}

					$(".loadingsb").hide();

				},
				error : function(jqXhr, textStatus, errorThrown) {
					console.log(errorThrown);
				}
			});
}

// 生成页面
function generatePageHTML(number) {
	var currentPage = parseInt($(".current-page").val());

	if (currentPage == 1) {
		var str = '<ul class="pagination">';
	} else {
		var str = '<ul class="pagination"><li><a onclick="postTansacInfoList('
				+ (currentPage - 1) + ')">&laquo;</a></li>';
	}

	if (currentPage == number) {
		var strAfter = '</ul>';
	} else {
		var strAfter = '<li><a onclick="postTansacInfoList('
				+ (currentPage + 1) + ')">&raquo;</a></li></ul>';
	}

	for (var i = 1; i <= number; i++) {
		if (i == $(".current-page").val()) {
			str += '<li class="active"><a onclick="postTansacInfoList(' + i
					+ ')"> ' + i + ' </a></li>';
		} else {
			str += '<li><a onclick="postTansacInfoList(' + i + ')"> ' + i
					+ ' </a></li>';
		}
	}
	str += strAfter;
	$("#pages").html(str);

}
