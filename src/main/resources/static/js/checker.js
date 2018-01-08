/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

$(function() {
	getRecentStuConfList(); 
	getStuConfList();
});

function forward2checkerForm(id) {
	window.location.href = "/checker/checker_form?id=" + id;
}

function getRecentStuConfList(pagenumber) {
	var queryConditionJson = {
		"pageNumber" : pagenumber,
		"pageSize" : 5,
		"queryObject" : "{\"apply_state\":\"new\"}",
		"rowCount" : 1,
		"sortColumn" : "apply_time",
		"sortType" : "DESC"
	};
	var queryConditionStr = JSON.stringify(queryConditionJson);
	$.ajax({
				url : "/checker/postStuConfList",
				dataType : 'json',
				type : 'post',
				contentType : 'application/json',
				data : queryConditionStr,
				processData : false,
				success : function(result, textStatus, jQxhr) {

					$(".sbcon").html("");
					var numbers = 0;
					if (result.rows) {
						$
								.each(
										result.rows,
										function(i, item) {
											var approvedstr = "<td><button type='button' class='btn btn-info view' "
													+ "data-toggle='modal' data-target='.modal-edit' "
													+ "onclick='forward2checkerForm(\""
													+ item.id
													+ "\")'>Check Detail</button></td>";

											/* var rejectstr = "<td><button type='button' class='btn btn-danger' " +
											 "onclick='rejectForm(\"" + item.id + "\")'>Reject</button></td> ";*/

											// data-raw='" + JSON.stringify(item) + "'
											$(".sbcon").append(
													"<tr id='id" + item.id
															+ "'><td>"
															+ item.id
															+ "</td><td>"
															+ item.identity
															+ "</td><td>"
															+ item.given_name
															+ "&nbsp;"
															+ item.family_name
															+ "</td><td>"
															+ item.apply_time
															+ "</td><td>"
															+ item.apply_state
															+ "</td>"
															+ approvedstr);
										});
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

function generatePageHTML(number) {
	var currentPage = parseInt($(".current-page").val());

	if (currentPage == 1) {
		var str = '<ul class="pagination">';
	} else {
		var str = '<ul class="pagination"><li><a onclick="getRecentStuConfList('
				+ (currentPage - 1) + ')">&laquo;</a></li>';
	}

	if (currentPage == number) {
		var strAfter = '</ul>';
	} else {
		var strAfter = '<li><a onclick="getRecentStuConfList('
				+ (currentPage + 1) + ')">&raquo;</a></li></ul>';
	}

	for (var i = 1; i <= number; i++) {
		if (i == $(".current-page").val()) {
			str += '<li class="active"><a onclick="getRecentStuConfList(' + i
					+ ')"> ' + i + ' </a></li>';
		} else {
			str += '<li><a onclick="getRecentStuConfList(' + i + ')"> ' + i
					+ ' </a></li>';
		}
	}
	str += strAfter;
	$("#pages").html(str);
}

function getCheckHistoryList() {
	//var queryConditionJson = {"pageNumber":1,"pageSize":100,"queryObject":"{\"apply_state\":{$ne:''}}","rowCount":1,"sortColumn":"apply_time","sortType":"DESC"};
	var queryConditionJson = {
		'pageNumber' : 1,
		'pageSize' : 20,
		'queryObject' : '{"handle_time":"!null"}',
		'rowCount' : 1,
		'sortColumn' : 'apply_time',
		'sortType' : 'DESC'
	};

	var queryConditionStr = JSON.stringify(queryConditionJson);
	queryConditionStr = $.base64('encode', queryConditionStr);

	$.getJSON("/checker/getStuConfList/" + queryConditionStr, function(result) {
		console.log(result);
	});
}
