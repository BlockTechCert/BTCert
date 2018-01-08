/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */


function toggleJson(){
	   $('#certsInfo').JSONView('toggle', 1);
}

function showDetails(){
		var items= $('#table-list').bootstrapTable('getSelections');
	$(".modal-details").modal('show'); 		    	
    $("#certsInfo").JSONView({})
    $("#certsInfo").JSONView(items[0].openbadges, { collapsed: true });
}

function downloadQr(){
	
	$('#downcavas').qrcode("http://");

    var canvas = $('#downcavas');
    console.log(canvas);
    var img = canvas.get(0).toDataURL("image/png");
    //or
    //var img = $(canvas)[0].toDataURL("image/png");
    document.write('<img src="'+img+'"/>');
}


function generateQr() {
	$("#qrcode").html("");
	var items= $('#table-list').bootstrapTable('getSelections');
    if(items.length == 1){
    	var downJson = JSON.stringify(items[0].openbadges.signature);
    	var qrstr = JSON.stringify(downJson)
    	var w = (screen.availWidth > screen.availHeight ? screen.availWidth : screen.availHeight)/3; 
		var qrcode = new QRCode("qrcode", {width:w, height:w});
		if(qrstr.length > 1024){
			$("#qrcode").html("<p>Sorry the data is too long for the QR generator.</p>");
		}  
		qrcode.makeCode(qrstr);
    	$(".modal-edit").modal('show');
    }else{
        sweetAlert("Oops...", "You should select no more one row!", "error")
    };
}

var downloadjson = function (content, filename) {
    var eleLink = document.createElement('a');
    eleLink.download = filename;
    eleLink.style.display = 'none';
    var blob = new Blob([content]);
    eleLink.href = URL.createObjectURL(blob);
  //  eleLink.href = "data:text/json;charset=utf-8," + encodeURIComponent(content);
    document.body.appendChild(eleLink);
    eleLink.click();
    document.body.removeChild(eleLink);
};

function downloadCerts(){
	var items= $('#table-list').bootstrapTable('getSelections');
    if(items.length == 1){
    	var downJson = JSON.stringify(items[0].openbadges);
    	downloadjson(downJson,'certs.json')
    }else{
        sweetAlert("Oops...", "You should select no more one row!", "error")
    };
}


$(function () {

    var oTable = new TableInit();
    oTable.Init();
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
});


var TableInit = function () {
    var oTableInit = new Object();
    oTableInit.Init = function () {
        $('#table-list').bootstrapTable({
            url: '/student/postCertsInfoList',         
            method: 'post',                      
            toolbar: '#toolbar',                
            striped: true,                      
            cache: false,                       
            pagination: true,                   
            sortable: false,                     
            sortOrder: "asc",                   
            queryParams: oTableInit.queryParams,
            sidePagination: "server",           
            queryParamsType:'',
            pageNumber:1,                       
            pageSize: 10,                       
            pageList: [10, 25, 50, 100],        
            search: true,                       
            strictSearch: true,
            showColumns: true,                 
            showRefresh: true,                  
            minimumCountColumns: 2,             
            clickToSelect: true,                
            height: 500,                        
            uniqueId: "ID",                     
            showToggle:true,                    
            cardView: false,                    
            detailView: false,                  
            columns: [{
                checkbox: true
            }, {
                field: 'createTime',
                title: 'Create Time'
            }, {
                field: 'id',
                title: 'Applicant ID'
            },  {
                field: 'recipient',
                title: 'Recipient Name'
            }, {
                field: 'cstate',
                title: 'State'
            },{
            	field: 'openbadges.badge.issuer.name',
            	title:'Issuer Name'
            }]
        });
    };

    oTableInit.queryParams = function (params) {
    	var studentName = $("#student_name").html();
    	var queryObject = '{"recipient":"'+studentName+'"}'
    	var queryObject = '{"cstate":"notExists_revoked","recipient":"'+studentName+'","id":"matchAll_'+params.searchText+'"}'
        var queryConditionJson = {'pageNumber':params.pageNumber ,'pageSize':params.pageSize ,'queryObject':queryObject,'rowCount':1,'sortColumn':'_id','sortType':'DESC'};
        // var queryConditionStr = JSON.stringify(queryConditionJson);
        // queryConditionStr = $.base64('encode', queryConditionStr);
        return queryConditionJson;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
    };
    return oInit;
};