/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
$(function(){
    getHistoryCertificateList(1);
    $("#checkAll").click(function() {
        $('input[name="subBox"]').attr("checked",this.checked);
    });
    $("input[name='subBox']").click(function(){
        $("#checkAll").attr("checked", $("input[name='subBox']").length == $("input[name='subBox']:checked").length ? true : false);
    });
});

function generatePageHTML(number) {
    var currentPage =parseInt($(".current-page").val());

    if(currentPage==1){
        var str = '<ul class="pagination">';
    }else{
        var str = '<ul class="pagination"><li><a onclick="getHistoryCertificateList(' + (currentPage - 1) + ')">&laquo;</a></li>';
    }

    if(currentPage==number){
        var strAfter  = '</ul>';
    }else{
        var strAfter  = '<li><a onclick="getHistoryCertificateList(' + (currentPage + 1) + ')">&raquo;</a></li></ul>';
    }

    for(var i=1;i<=number;i++){
        if(i==$(".current-page").val()){
            str += '<li class="active"><a onclick="getHistoryCertificateList(' + i + ')"> ' + i  +  ' </a></li>';
        }else{
            str += '<li><a onclick="getHistoryCertificateList(' + i + ')"> ' + i  +  ' </a></li>';
        }
    }
    str += strAfter;
    $("#pages").html(str);

}

function getHistoryCertificateList(pagenumber){
    var queryConditionJson = {'pageNumber':pagenumber,'pageSize':10,'queryObject':'{}','rowCount':1,'sortColumn':'apply_time','sortType':'DESC'};
    var queryConditionStr = JSON.stringify(queryConditionJson);
    // queryConditionStr = $.base64('encode', queryConditionStr);
    $.ajax({
        url: "/student/getStuConfList",
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: queryConditionStr,
        processData: false,
        success: function( result, textStatus, jQxhr ){
            var str='';
            var numbers = 0;
            if(result.rows){
                $.each(result.rows,
                    function(i, item) {

                        var isdisabled = '';
                        if(item.apply_state==="passed")
                            isdisabled = 'disabled';

                        var deletestr = "<td><button type='button' class='btn btn-info view' " +
                            "onclick='delStuConf(\"" + item.id + "\")'" + isdisabled + ">Delete</button></td> ";

                        var editstr = "<td><button type='button' class='btn btn-info view' " +
                            "data-toggle='modal' data-target='.modal-edit' " +
                            "onclick='getCertificate(\"" + item.id + "\")'>Edit</button></td>";

                        // data-raw='" + JSON.stringify(item) + "'

                        str+=("<tr id='id" + item.id + "'><td>" + item.id + "</td><td>" +
                        item.apply_time + "</td><td>"+ item.handle_time +"</td><td>" +
                        prefix_state(item.apply_state) + "</td><td>" + item.apply_note + "</td>" + editstr + deletestr);
                    });
                $(".sbcon").html(str);
                $(".current-page").val(result.pageNumber);
                numbers = Math.ceil(parseInt(result.total) / parseInt(result.pageSize));
                console.log(numbers);
                generatePageHTML(numbers);
            }else{
                $(".table-recent-apply").hide();
            }

            $(".loadingsb").hide();

        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });

}

function delSelected() {
    var item= $('#table-list').bootstrapTable('getSelections');
    // console.log(item);
    for(var current in item){
        console.log(item[current].id);
    }
}

function editSelected() {
    var item= $('#table-list').bootstrapTable('getSelections');
    if(item.length==1){
        $(".modal-edit").modal('show');
        getCertificate(item[0].id);
    }else{sweetAlert("Oops...", "You can only select one row!", "error")};
}
