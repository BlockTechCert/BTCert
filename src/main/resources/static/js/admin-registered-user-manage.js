/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
function updateSchConf() {
	// var id = $("id").val();
	var queryArray = [ "id", "name", "email", "url", "image" ];
	var queryConditionStr = "{";
	$.each(queryArray,
			function(i, item) {
				queryConditionStr += '"' + item + '" : "' + $("#" + item).val()
						+ '", ';
			});
	queryConditionStr = queryConditionStr.substring(0,
			queryConditionStr.length - 1)
			+ "}";
	queryConditionStr = $.base64('encode', queryConditionStr);

	$.getJSON("/admin/updateSchConf/" + queryConditionStr, function(result) {
		if (result.success == 1) {
			$("#alert-message-fail").hide();
			$("#alert-message-success").fadeIn();
		} else {
			$("#alert-message-success").hide();
			$("#alert-message-fail").fadeIn();
		}
	});
}

function getCertificate(id) {
	var queryConditionStr = $.base64('encode', id);
	$.getJSON("/admin/getSchConf/" + queryConditionStr, function(result) {
		console.log(result);
		$.each(result, function(name, item) {
			$("#" + name).val(item);
		});
	});
	$("#alert-message-fail").hide();
	$("#alert-message-success").hide();
}

function editSelected() {
	var item = $('#table-list').bootstrapTable('getSelections');
	if (item.length == 1) {
		$(".modal-edit").modal('show');
		getCertificate(item[0].id);
	} else {
		sweetAlert("Oops...", "You can only select one row!", "error")
	}
	;
}

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
			url : '/admin/postUsersList', 
			method : 'post', 
			toolbar : '#toolbar', 
			striped : true, 
			cache : false, 
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
				field : 'u_name',
				title : 'u_name'
			}, {
				field : 'role',
				title : 'role'
			}, {
				field : 'state',
				title : 'state'
			} ]
		});
	};
	oTableInit.queryParams = function(params) {
		var queryConditionJson = {
			'pageNumber' : params.pageNumber,
			'pageSize' : params.pageSize,
			'queryObject' : '{"id":"matchAll_' + params.searchText + '"}',
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
	};
	return oInit;
};