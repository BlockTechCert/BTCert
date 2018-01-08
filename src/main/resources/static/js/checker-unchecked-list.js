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
				formatter : NameFormatter
			}, {
				field : 'id',
				title : 'Action',
				formatter : ActionFormatter
			} ]
		});
	};

	oTableInit.queryParams = function(params) {
		var queryConditionJson = {
			'pageNumber' : params.pageNumber,
			'pageSize' : params.pageSize,
			'queryObject' : '{"apply_state":"new","id":"matchAll_'
					+ params.searchText + '"}',
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

function NameFormatter(value, row, index) {
	return prefix_state(value);
};

function ActionFormatter(value, row, index) {
	var button = "<button type='button' class='btn btn-info view' "
			+ "data-toggle='modal' data-target='.modal-edit' "
			+ "onclick='forward2checkerForm(\"" + value
			+ "\")'>Check Detail</button>";
	return button;
};

function forward2checkerForm(id) {
	window.location.href = "/checker/checker_form?id=" + id;
}

function displayCertificate(id) {
	id = $.base64('encode', id);
	$.getJSON("/checker/getCertsInfo/" + id, function(result) {
		$("#certsInfo").JSONView({})
		$("#certsInfo").JSONView(result.openbadges, {
			collapsed : true
		});
	});
}