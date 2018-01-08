/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

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
            url: '/issuer/postTansacInfoList',         
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
                field: 'id',
                title: 'id'
            }, {
                field: 'broadcast_transaction_id',
                title: 'broadcast_transaction_id',
                formatter: NameFormatter
            }, {
                field: 'sign_number',
                title: 'sign_number'
            },{
            	field: 'sign_state',
            	title:'sign_state'
            }]
        });
    };
    oTableInit.queryParams = function (params) {
        var queryConditionJson = {
        		'pageNumber':params.pageNumber ,
        		'pageSize':params.pageSize ,
        		'queryObject':'{"broadcast_state":"1","id":"matchAll_'+params.searchText+'"}',
        		'rowCount':1,
        		'sortColumn':'apply_time',
        		'sortType':'DESC'
        };
	        return queryConditionJson;
	    };
	    return oTableInit;
	};
	
	function NameFormatter(value, row, index) {
　　　　return [
　　　　　　'<a target = "_blank" href = "https://blockchain.info/tx/',value,'">',value,'</a>'
　　　　].join('');
};


var ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
    };
    return oInit;
};