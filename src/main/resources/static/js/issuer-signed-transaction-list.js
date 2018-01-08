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
	var student_name = $("#student-name").html();
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
				field : 'sign_number',
				title : 'sign_number'
			}, {
				field : 'sign_state',
				title : 'sign_state',
				formatter: stateFormatter
			} ]
		});
	};
	
	function stateFormatter(value, row, index) {
		
		var vs = value.split(",");
		var htm = '';
		for(var i = 0 ; i < vs.length -1; i++){
			var content = vs[i].split("_")
			htm = htm + '<p><span class="state state-passed" ><i class="fa fa-check" aria-hidden="true" ></i> ' + content[0] + '</span></p>';
		}
		
		return htm;
	};

	oTableInit.queryParams = function(params) {
		var queryConditionJson = {
			'pageNumber' : params.pageNumber,
			'pageSize' : params.pageSize,
			'queryObject' : '{"sign_state":"regex_' + student_name
					+ '","serialize_state":"1","id":"matchAll_'+params.searchText+'"}',
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