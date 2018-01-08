/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
function batchRevokeCerts(){
	alert("under building")
}

function revokeCerts(){
	var items= $('#table-list').bootstrapTable('getSelections');
	if(items.length == 1){
		$(".modal-edit").modal('show');
		$("#u_name").val(items[0].u_name);
		$("#r_address").val(items[0].r_address);
		$("#revoke_id").val(items[0].id);
	}else{
		sweetAlert("Oops...", "You can only revoke one certificate once", "error")
	}
}

function revokeCertsConfirm(){
	 getSchIdentity();
}

function getSchIdentity(){
	 $.getJSON("/issuer/getAllSchIdentity/",function(result){
		 btc_address_list = result[0].btc_address_list[0];
	 	 latest_btc_address = "";
		 for(var item in btc_address_list){
			 latest_btc_address = item;
			 break;
		 }
		
		 btcert.getUnspentTransaction(insertRevokeTransInfo);
     })
}

function insertRevokeTransInfo(){
	var student_name = $("#u_name").val();
	var revoke_address = $("#r_address").val();
	var revoke_id = $("#revoke_id").val();
	
	btcert.initOutput(latest_btc_address);
	btcert.initOutput(revoke_address);
	raw_tran = btcert.serialize();
	
	var queryConditionJson = {
	        "revoke_id":revoke_id,
	        "student_name":student_name,
	        "revoke_address":revoke_address,
    		"raw_tran":raw_tran,
    		"sign_number":0,
    		"sign_state":"",
    		"broadcast_state":"0",
    		"input":btcert._tranction_inout,
    		"output":btcert._tranction_output,
    		"signatures":""
    }
	
	
    var queryConditionStr = JSON.stringify(queryConditionJson);

     $.ajax({
        url: "/issuer/postRevokeTansacInfo",
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: queryConditionStr,
        processData: false,
        success: function( result, textStatus, jQxhr ){
        	  if(result.success == "1"){
                    swal({
                        title: "Good Job!",
                        text: "The certificate entered the waiting list !.",
                        type: "success",
                        showCancelButton: false,
                        confirmButtonText: "OK",
                        closeOnConfirm: false
                    },
                    function(){
                        window.location.href = "/admin/stu_revoInfo_manage";
                    });
                }else{
                    swal("Oops...", "Something happened", "error");
                }
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log(errorThrown);
        }
    });
}




function lastConfirm(revoke_address,revoke_id){
	
	revoke_address = revoke_address;
	revoke_id = revoke_id;
	
	swal({
	    title: "Are you sure to revoked this certificate?",
	    text: "You will not be able to recovery this certificate !",
	    type: "warning",
	    showCancelButton: true,
	    confirmButtonColor: '#DD6B55',
	    confirmButtonText: 'Yes, I am sure!',
	    cancelButtonText: "No, cancel it!",
	    closeOnConfirm: false,
	    closeOnCancel: false
	 },
	 function(isConfirm){
	   if (isConfirm){
		    $.getJSON("/issuer/getRevokeTansacInfobyAddress/" + revoke_address,function(result){
		        $.each(result,
		            function(name, item) {
			        	if(name =='raw_tran'){
				        		raw_tran = item;
				        		//start broadcast
				        		btcert.broadcastRawtransaction(raw_tran,successnoticeResult,errnoticeResult);
				        		// successnoticeResult(1);
				        	}
			        });
			 });
	    } else {
	      swal("Cancelled", "Your certificate is safe :)", "error");
	    }
	 });
	
	
	var successnoticeResult = function(data){
		
		 // txid = data.data.txid;
		 txid = "test";
		 var queryConditionStr = {"revoke_id":revoke_id,"revoke_address":revoke_address,"txid":txid,"broadcast_success":"1"}
		 queryConditionStr = JSON.stringify(queryConditionStr);
		 queryConditionStr = $.base64('encode', queryConditionStr);
		 
	     $.getJSON("/issuer/updateAllRevokeTansacInfobyAddress/" + queryConditionStr,function(result){
	        if(result.success == "1"){
                 swal({
                     title: "Good Job!",
                     text: "revoked this certificate successfully.",
                     type: "success",
                     showCancelButton: false,
                     confirmButtonText: "OK",
                     closeOnConfirm: false
                 },
                 function(){
                     window.location.href = "/admin/stu_revoInfo_manage";
                 });
             }else{
                 swal("Oops...", "Something happened", "error");
             }
	     });
	}

	var errnoticeResult = function(){
		 sweetAlert("Oops...", "It seems that there are some probelms", "error")
	}
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
        url: '/admin/getStuRevoInfoList',        
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
            field: 'u_id',
            title: 'u_id'
        }, {
            field: 'u_name',
            title: 'user_registered_name'
        }, {
            field: 'cstate',
            title: 'cstate'
        }, {
            field: 'r_address',
            title: 'r_address'
        },{
        	field: 'rb_address',
        	title:'rb_address'
        },{
            field: 'cstate',
            title: 'comment',
            formatter: NameFormatter
        }]
    
    });
};


function NameFormatter(value, row, index) {
　　　　var  btn = "";
	    if(value == 'revoked confirmed'){
	    	btn = [
	    	　　　　　　'<button type="button" class="btn btn-primary" onclick="lastConfirm(\'',row["r_address"],'\',\'',row["id"],'\')">Revoke</button>'
	    	　　　].join('');
	    	btn = "The certificate revoked request is approved, click the button to tell the world." + btn;
	    }else if(value == 'waiting revoked'){
	    	btn = '<span style = "color: blue">The certificate revoked request had sent to the awarding body to assess.<span>'
	    }else if(value == 'revoked reject'){
	    	btn = '<span style = "color: red">This certificate is not allowed to revoke, the majority of committee member disagreed to revoke certificate,'
	    		+'for more information, please contact Admissions Office <br> +44 (0)121 414 3344.<span>'
	    }else if(value == 'revoked successfully'){
	    	btn = '<span style = "color: green" >The certifacte has been revoked successfuly</span>'
	    }
	   
	    return btn;
};

oTableInit.queryParams = function (params) {
    var queryConditionJson = {
    		'pageNumber':params.pageNumber ,
    		'pageSize':params.pageSize ,
    		'queryObject':'{"id":"matchAll_'+params.searchText+'"}',
    		'rowCount':1,
    		'sortColumn':'apply_time',
    		'sortType':'DESC'
    };
    
        return queryConditionJson;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
    };
    return oInit;
};