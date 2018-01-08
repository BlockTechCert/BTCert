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
			url : '/checker/postStuConfList',
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
				title : 'Applicant ID'
			}, {
				field : 'apply_time',
				title : 'Apply Time'
			}, {
				field : 'handle_time',
				title : 'Handle Time'
			}, {
				field : 'apply_state',
				title : 'Apply State',
				formatter: NameFormatter
			}, {
				field : 'apply_note',
				title : 'Comment'
			},{
				field : 'certs_id',
				title : 'Action',
				formatter: ActionFormatter
			} ]
		});
	};

	oTableInit.queryParams = function(params) {
		
		var queryConditionJson = {
			'pageNumber' : params.pageNumber,
			'pageSize' : params.pageSize,
			'queryObject' : '{"handle_time":"!null","id":"matchAll_'+params.searchText+'"}',
			'total' : 1,
			'sortColumn' : 'handle_time',
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


function ActionFormatter(value, row, index) {
	var basic_button = "<button type='button' class='btn btn-info view' "
	+ "data-toggle='modal' data-target='.modal-edit' "
	+ "onclick='forward2checkerForm(\""
	+ value
	+ "\")'>Checked Detail</button>";
	
	
	var certs_button = "<button type='button' class='btn btn-info view' "
		+ "data-toggle='modal' data-target='.modal-edit' "
		+ "onclick='displayCertificate(\""
		+ value
		+ "\")'>Certificate Detail</button>";
	
	return basic_button + "&nbsp;&nbsp;" + certs_button;
};


function NameFormatter(value, row, index) {
	return prefix_state(value);
};

function displayCertificate(id) {
	id = $.base64('encode', id);
	$.getJSON("/checker/getCertsInfo/" + id, function(result) {
		$("#certsInfo").JSONView({})
		$("#certsInfo").JSONView(result.openbadges, {
			collapsed : true
		});
	});
}

function toggleJson(){
	   $('#certsInfo').JSONView('toggle', 1);
}