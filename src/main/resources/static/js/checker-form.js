/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

$(function() {
	$('#myTab a:last').tab('show');
})

$('#myTab a').click(function(e) {
	e.preventDefault();
	$(this).tab('show');
})

$(function() {
	var id = getQueryString("id");
	getCertificate(id);

	var picker = new Pikaday({
		field : document.getElementById('created'),
		toString : function(date, format) {
			return dateFns.format(date, format);
		},
		parse : function(dateString, format) {
			return dateFns.parse(dateString);
		},
		onSelect : function(selectedDate) {
		}
	});

	var picker1 = new Pikaday({
		field : document.getElementById('expires'),
		toString : function(date, format) {
			return dateFns.format(date, format);
		},
		parse : function(dateString, format) {
			return dateFns.parse(dateString);
		},
		onSelect : function(selectedDate) {
		}
	});

})

function getCertificate(id) {
	var queryConditionStr = $.base64('encode', id);
	$.getJSON("/checker/getStuConf/" + queryConditionStr, function(result) {
		$.each(result, function(name, item) {
			$("#" + name).val(item);
		});
	});
	$("#alert-message-fail").hide();
	$("#alert-message-success").hide();
}


function checkStuConf(apply_state) {

	var queryArray1 = [ "given_name", "family_name", "birthday", "apply_type",
			"identity", "identity_type", "file_hash", "apply_note",
			"apply_time", "handle_time", "id", "user_id" ];
	var stuConfStr = "{";
	$.each(queryArray1, function(i, item) {
		stuConfStr += '"' + item + '" : "' + $("#" + item).val() + '", ';
	});
	stuConfStr = stuConfStr.substring(0, stuConfStr.length - 1)
			+ "\"apply_state\":\"" + apply_state + "\"}";
	stuConfStr = $.base64('encode', stuConfStr)

	var standard = "";
	$('input[name="standard"]:checked').each(function() {
		standard = standard + $(this).val() + ","
	});
	standard = standard.substring(0, standard.length - 1);

	var queryArray2 = [ "badgeDescription", "badgeType", "badgeId",
			"badgeName", "badgeClass", "badgeImage", "id", "created", "expires" ];
	var checkInfoStr = "{";
	$.each(queryArray2, function(i, item) {
		checkInfoStr += '"' + item + '" : "' + $("#" + item).val() + '", ';
	});
	checkInfoStr = checkInfoStr.substring(0, checkInfoStr.length - 1)
			+ "\"standard\":\"" + standard + "\"}";
	checkInfoStr = $.base64('encode', checkInfoStr)

	var postData = {
		"checkInfoStr" : checkInfoStr,
		"stuConfStr" : stuConfStr
	}

	$.post("/checker/checkStuConf", postData, function(result) {
		result = eval('(' + result + ')');
		if (result.success == 1) {
			swal({
				title : "Approved!",
				text : "The Applicant has been Approved.",
				type : "success",
				showCancelButton : false,
				confirmButtonText : "OK",
				closeOnConfirm : false
			}, function() {
				window.location.href = "/checker/checker_home";
			});

		} else {
			swal("Oops...", "Something happened", "error");
		}
	});
}