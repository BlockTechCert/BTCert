/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
$.getJSON("/issuer/getAllSchIdentity/",function(result){
	 $("#schIdentity").JSONView(result, { collapsed: false });
	 /*$('#schIdentity').JSONView('expand', 3);*/
 })
   
 function toggleJson(){
	 $('#schIdentity').JSONView('toggle', 1);	
 }