/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

$(function(){
    $(".btn-checker").click(function(){
        $(".checker-des").hide();
        $(".checker-login").fadeIn();
    });
});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


function generatePageHtml(number,func) {
    var currentPage =parseInt($(".current-page").val());

    if(currentPage==1){
        var str = '<ul class="pagination">';
    }else{
        var str = '<ul class="pagination"><li><a onclick="'+func+'(' + (currentPage - 1) + ')">&laquo;</a></li>';
    }

    if(currentPage==number){
        var strAfter  = '</ul>';
    }else{
        var strAfter  = '<li><a onclick="'+func+'(' + (currentPage + 1) + ')">&raquo;</a></li></ul>';
    }

    for(var i=1;i<=number;i++){
        if(i==$(".current-page").val()){
            str += '<li class="active"><a onclick="'+func+'(' + i + ')"> ' + i  +  ' </a></li>';
        }else{
            str += '<li><a onclick="'+func+'(' + i + ')"> ' + i  +  ' </a></li>';
        }
    }
    str += strAfter;
    $("#pages").html(str);
}


function prefix_state(state) {
    if(state==='passed')
        return '<span class="state state-passed"><i class="fa fa-check" aria-hidden="true"></i> ' + state + '</span>';
    if(state==='new')
        return '<span class="state state-new"><i class="fa fa-star" aria-hidden="true" ></i> ' + state + '</span>';
    if(state==='rejected')
        return '<span class="state state-rejected"><i class="fa fa-exclamation" aria-hidden="true"></i> ' + state + '</span>';
    if(state==='merged')
        return '<span class="state state-merged"><i class="fa fa-graduation-cap" aria-hidden="true"></i> ' + state + '</span>';
}
