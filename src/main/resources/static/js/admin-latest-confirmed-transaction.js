/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

$(function() {
	getTransactions("3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd");
})

function getTransactions(address) {
	$
			.getJSON(
					"https://chain.so/api/v2/get_tx_spent/BTC/" + address,
					function(result) {
						var output = [];
						// output.push('<tr><th
						// class="span12"><strong>Transaction</strong></th><th><strong>Time_utc</strong></th><th><strong>Confirmations</strong></th></tr>');
						for (var i = 0; i < result.data.txs.length; i++) {

							var detailButton = "<button type='button' class='btn btn-info view' "
									+ "data-toggle='modal' data-target='.modal-edit' "
									+ "onclick='displayTransactions(\""
									+ result.data.txs[i].txid
									+ "\",\""
									+ result.data.txs[i].script_hex
									+ "\",\""
									+ result.data.txs[i].value
									+ "\",\""
									+ result.data.txs[i].confirmations
									+ "\",\""
									+ result.data.txs[i].time
									+ "\")'>Details</button>";
							output
									.push(
											'<tr><td class="span12"><strong><a target = _blank href="https://blockchain.info/tx/',
											result.data.txs[i].txid,
											'">',
											result.data.txs[i].txid
													+ '<a></strong></td><td>',
											getLocalTime(result.data.txs[i].time),
											'</td><td>',
											result.data.txs[i].confirmations,
											'</td><td>',
											result.data.txs[i].value,
											'</td><td>', detailButton,
											'</td></tr>');
						}
						$(".sbcon").html(output.join(''))
					})
}

function displayTransactions(txid, script_hex, value, confirmations, time) {
	$("#txid").val(txid);
	$("#script_hex").val(script_hex);
	$("#value").val(value);
	$("#confirmations").val(confirmations);
	$("#time").val(getLocalTime(time));
	$("#showCerts").html("Show");
	$(".table-dislay").hide();
}

function getLocalTime(nS) {
	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,
			' ');
}

function generatePageHTML1(number) {
	var currentPage = parseInt($(".current-page1").val());

	if (currentPage == 1) {
		var str = '<ul class="pagination">';
	} else {
		var str = '<ul class="pagination"><li><a onclick="getCertsInfoList('
				+ (currentPage - 1) + ')">&laquo;</a></li>';
	}

	if (currentPage == number) {
		var strAfter = '</ul>';
	} else {
		var strAfter = '<li><a onclick="getCertsInfoList(' + (currentPage + 1)
				+ ')">&raquo;</a></li></ul>';
	}

	for (var i = 1; i <= number; i++) {
		if (i == $(".current-page1").val()) {
			str += '<li class="active"><a onclick="getCertsInfoList(' + i
					+ ')"> ' + i + ' </a></li>';
		} else {
			str += '<li><a onclick="getCertsInfoList(' + i + ')"> ' + i
					+ ' </a></li>';
		}
	}
	str += strAfter;
	$("#pages").html(str);
}


function getCertsInfoList(pagenumber) {
	var queryObject = '{"broadcast_transaction_id":"'
			+ broadcast_transaction_id + '"}'
	var queryConditionJson = {
		'pageNumber' : pagenumber,
		'pageSize' : 10,
		'queryObject' : queryObject,
		'rowCount' : 1,
		'sortColumn' : 'apply_time',
		'sortType' : 'DESC'
	};
	var queryConditionStr = JSON.stringify(queryConditionJson);
	queryConditionStr = $.base64('encode', queryConditionStr);
	$.ajax({
		url : "/issuer/getCertsInfoListFormBroadcastId/" + queryConditionStr,
		dataType : 'json',
		type : 'get',
		contentType : 'application/json',
		// data: queryConditionStr,
		processData : false,
		success : function(result, textStatus, jQxhr) {
			var str = '';
			var numbers = 0;
			if (result.rows) {
				$.each(result.rows, function(i, item) {

					/*
					 * var editstr = "<td><button type='button' class='btn
					 * btn-info view' " + "data-toggle='modal'
					 * data-target='.modal-edit' " +
					 * "onclick='getCertificate(\"" + item.id + "\")'>Edit</button></td>";
					 */

					// data-raw='" + JSON.stringify(item) + "'
					str += ("<tr id='id" + item.id + "'><td>"
							+ item.openbadges.recipient.identity + "</td><td>"
							+ item.openbadges.issuedOn + "</td><td>"
							+ item.openbadges.badge.issuer.name + "</td><td>");
				});
				$(".sbcon1").html(str);
				$(".current-page1").val(result.pageNumber);
				numbers = Math.ceil(parseInt(result.total)
						/ parseInt(result.pageSize));
				generatePageHTML1(numbers);
			} else {
				$(".table-dislay").hide();
			}

			$(".loadingsb1").hide();

		},
		error : function(jqXhr, textStatus, errorThrown) {
			console.log(errorThrown);
		}
	});
}

$("#showCerts").click(function() {
	if ($(this).html() == "Show") {
		broadcast_transaction_id = $("#txid").val();
		getCertsInfoList(1);
		$(".table-dislay").show();
		$(this).html("hidden");
	} else {
		$(this).html("Show");
		$(".table-dislay").hide();
	}
});
