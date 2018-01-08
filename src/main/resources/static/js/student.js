/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
$(function(){

    var picker = new Pikaday({
        field: document.getElementById('birthday'),
        toString: function(date, format) {
            return dateFns.format(date, format);
        },
        parse: function(dateString, format) {
            return dateFns.parse(dateString);
        },
        onSelect: function(selectedDate) {
        }
    });


    getCertificateStatus(); 

    $(document).delegate('#file', 'change', function() {
        var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,
            file = this.files[0],
            chunkSize = 2097152,                             // Read in chunks of 2MB
            chunks = Math.ceil(file.size / chunkSize),
            currentChunk = 0,
            spark = new SparkMD5.ArrayBuffer(),
            fileReader = new FileReader();

        fileReader.onload = function (e) {
            console.log('read chunk nr', currentChunk + 1, 'of', chunks);
            spark.append(e.target.result);                   // Append array buffer
            currentChunk++;

            if (currentChunk < chunks) {
                loadNext();
            } else {
                console.log('finished loading');
                console.info('computed hash', spark.end());  // Compute hash
                $("#file_hash").val(spark.end());
                $("#file_hash_show").html(spark.end());
            }
        };

        fileReader.onerror = function () {
            console.warn('oops, something went wrong.');
        };

        function loadNext() {
            var start = currentChunk * chunkSize,
                end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        }

        loadNext();
    });
});



function getCertificateStatus(){
    var str = $("#student-name").html();
    str = $.base64('encode', str);
    $.getJSON("/student/getCertState/" + str,function(result){
        if(result.certState=="new"){
            $(".step-1").removeClass("disabled").addClass("active");
        }else if(result.certState=="checked"){
            $(".step-1").removeClass("disabled").addClass("complete");
            $(".step-2").removeClass("disabled").addClass("active");
        }else if(result.certState=="merged"){
            $(".step-1").removeClass("disabled").addClass("complete");
            $(".step-2").removeClass("disabled").addClass("complete");
            $(".step-3").removeClass("disabled").addClass("active");
        }else if(result.certState=="valid"){
            $(".step-1").removeClass("disabled").addClass("complete");
            $(".step-2").removeClass("disabled").addClass("complete");
            $(".step-3").removeClass("disabled").addClass("complete");
            $(".step-4").removeClass("disabled").addClass("active");
        }
    });
}


function applyCertificate(){
    
    var id = $("id").val();
    var queryArray = ["given_name","family_name","birthday","apply_type","identity",
        "identity_type","file_hash"];
    var queryConditionStr = "{";
    $.each(queryArray,function(i, item) { queryConditionStr += '"' + item + '" : "' + $("#"+item).val() + '", ';});
    queryConditionStr = queryConditionStr.substring(0,queryConditionStr.length-1) + "}";
    queryConditionStr = $.base64('encode', queryConditionStr);

    $.getJSON("/student/saveStuConf/" + queryConditionStr,function(result){
        if(result.success == 1){
            swal({
                    title: "Good Job",
                    text: "Your applicant has been added to our system.",
                    type: "success",
                    showCancelButton: false,
                    confirmButtonText: "OK",
                    closeOnConfirm: false
                },
                function(){
                    window.location.href = "/student/student_home";
                });
        }else{
            swal("Opps...", "Please check the form", "error");
        }
    });
}

function getCertificate(id){
    var queryConditionStr = $.base64('encode', id);
    $.getJSON("/student/getStuConf/" + queryConditionStr,function(result){
        console.log(result);
        $.each(result,
            function(name, item) {
                $("#"+name).val(item);
            });
    });
    $("#alert-message-fail").hide();
    $("#alert-message-success").hide();
}


function updateStuConf(){
    
    var id = $("id").val();
    var queryArray = ["given_name","family_name","birthday","apply_type","identity",
        "identity_type","file_hash","apply_note","apply_state","apply_time","handle_time","id","user_id"];
    var queryConditionStr = "{";
    $.each(queryArray,function(i, item) { queryConditionStr += '"' + item + '" : "' + $("#"+item).val() + '", ';});
    queryConditionStr = queryConditionStr.substring(0,queryConditionStr.length-1) + "}";
    queryConditionStr = $.base64('encode', queryConditionStr);

    $.getJSON("/student/updateStuConf/" + queryConditionStr,function(result){
        if(result.success == 1){
            $("#alert-message-fail").hide();
            $("#alert-message-success").fadeIn();
        }else{
            $("#alert-message-success").hide();
            $("#alert-message-fail").fadeIn();
        }
    });
}


function delStuConf(id){
    swal({
            title: "Are you sure?",
            text: "You will not be able to recover this applicant",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            closeOnConfirm: false
        },
        function(){
            //console.log(id)
            id = $.base64('encode', id);
            $.getJSON("/student/delStuConf/" + id,function(result){
                if(result.success == "1"){
                    /* swal("Deleted!", "Your imaginary file has been deleted.", "success");
                     window.location.href = "/student";*/
                    swal({
                            title: "Deleted!",
                            text: "Your Applicant has been deleted.",
                            type: "success",
                            showCancelButton: false,
                            confirmButtonText: "OK",
                            closeOnConfirm: false
                        },
                        function(){
                            window.location.href = "/student";
                        });
                }else{
                    swal("Oops...", "Something happened", "error");
                }
            });

        });
}


$(function(){
    getLatestCertificate();
});



function getLatestCertificate(){
    var queryConditionJson = {'pageNumber':1,'pageSize':3,'queryObject':'{}','rowCount':1,'sortColumn':'apply_time','sortType':'DESC'};
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

            $(".sbcon").html("");
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

                        $(".sbcon").append("<tr id='id" + item.id + "'><td>"
                            + item.id + "</td><td>" + item.apply_time + "</td><td>"
                            + item.handle_time +"</td><td>" + prefix_state(item.apply_state) + "</td><td>"
                            + item.apply_note + "</td>" + editstr + deletestr);
                    });
                //$(".current-page").val(result.pageNumber);
                //numbers = Math.ceil(parseInt(result.total) / parseInt(result.pageSize));
                //generatePageHTML(numbers);
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