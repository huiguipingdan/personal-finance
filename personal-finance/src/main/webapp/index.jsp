<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="application" />

<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title></title>
	<!-- jQuery -->
	<script type="text/javascript" charset="utf8"
		src="${contextPath}/resources/DataTables-1.10.7/media/js/jquery.js"></script>
	<!-- css -->
	<link rel="stylesheet" type="text/css"
		href="${contextPath}/resources/bootstrap-3.3.5-dist/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css"
		href="${contextPath}/resources/DataTables-1.10.7/media/css/dataTables.bootstrap.css">

	<script src="${contextPath}/resources/bootstrap-3.3.5-dist/js/bootstrap.js"></script>
	<!-- DataTables js -->
	<script type="text/javascript" charset="utf8"
		src="${contextPath}/resources/DataTables-1.10.7/media/js/jquery.dataTables.js"></script>
	<script type="text/javascript" charset="utf8"
	src="${contextPath}/resources/DataTables-1.10.7/media/js/dataTables.bootstrap.js"></script>
	
	<script src="${contextPath}/resources/datetimepicker/moment-with-locales.js"></script>
	
	<style>
        div.DTTT_container {
            float: left;
            display:block;
            position:relative;
            margin-bottom:1em;
        }
        .dataTables_wrapper {
		  /* position: relative; */
		  clear: both;
		  *zoom: 1;
		  zoom: 1;
		}
    </style>
</head>
<body>
	<script>
		$(document).ready(function() {
			$('#example').dataTable({
				language: {
				  url: '${contextPath}/resources/DataTables-1.10.7/media/i18n/chinese.json'
				},
				"order": [[ 4, "desc" ]],
				"ajax" : "${contextPath}/getList.do",
				"columns": [
		             { "data": "reason" },
		             { "data": "amtFlag",
		            	 "render": function ( data, type, row ) {
		            		 	if(type=="display") {
		            		 		return transAmtFlag(data);
		            		 	} else {
		            		 		return "";
		            		 	}
		                 }
		             },
		             { "data": "amt",
		            	 "render": function ( data, type, row ) {
		            		 if(type=="display") {
		            			 return formatCurrency(data);
	            		 	 } else {
	            		 		return "";
	            		 	 }
		                 }
		             },
		             { "data": "catagory",
		            	"render": function ( data, type, row ) {
		            		 if(type=="display") {//减少重复进入[4-display 2-filter 2-type]s
		            			 return transCatagory(data);
	            		 	 } else {
	            		 		return "";
	            		 	 }
		                 }
		             },
		             { "data": "updateTime" }
		         ],
		         "deferRender":true,
			});
			var table = $('#example').DataTable();
			//
			$('#example tbody').on( 'click', 'tr', function () {
		        if ( $(this).hasClass('active') ) {
		            $(this).removeClass('active');
		        }
		        else {
		            table.$('tr.active').removeClass('active');
		            $(this).addClass('active');
		        }
		    } );
		 	//
		    $('#del_trade_button').click( function () {
		    	if(table.row('.active').length>0 && window.confirm('确认删除')) {
		    		delTrade(table.row('.active'));
			  		table.row('.active').remove().draw( false );
		    	}
		    });
		    //
			
		    //
		    $('#new_trade_button').click( function () {
		    	loadMainTrade(-1);
		    });
		    $('#edit_trade_button').click( function () {
		    	if(table.row('.active').length>0) {
		    		loadMainTrade(table.row('.active').data().id);
		    	}
		    });
		    
		    $('[data-toggle="tooltip"]').tooltip();
		});
		function loadMainTrade(currentId) {
			$("#trade_main_wrapper_div").empty();
			$("#trade_main_wrapper_div").load(
					"${contextPath}/getMainTradeRecord.do", {
						id : currentId
					}, function() {
						$("#trade_main_div").modal();
					});
		}
		
		function formatCurrency(num) {
			var sign = "";
			if (isNaN(num)) {
				num = 0;
			}
			if (num < 0) {
				sign = "-";
			}
			var strNum = num + "";
			var arr1 = strNum.split(".");
			var hasPoint = false;//是否有小数部分 
			var piontPart = "";//小数部分 
			var intPart = strNum;//整数部分 
			if (arr1.length >= 2) {
				hasPoint = true;
				piontPart = arr1[1];
				intPart = arr1[0];
			}

			var res = '';//保存添加逗号的部分 
			var intPartlength = intPart.length;//整数部分长度 
			var maxcount = Math.ceil(intPartlength / 3);//整数部分需要添加几个逗号 
			for (var i = 1; i <= maxcount; i++)//每三位添加一个逗号 
			{
				var startIndex = intPartlength - i * 3;//开始位置 
				if (startIndex < 0)//开始位置小于0时修正为0 
				{
					startIndex = 0;
				}
				var endIndex = intPartlength - i * 3 + 3;//结束位置 
				var part = intPart.substring(startIndex, endIndex) + ",";
				res = part + res;
			}
			res = res.substr(0, res.length - 1);//去掉最后一个逗号 
			if (hasPoint) {
				return "&yen;" + sign + res + "." + piontPart;
			} else {
				return "&yen;" + sign + res;
			}

		}
		function transAmtFlag(data) {
			var amtFlagArray = [ '收', '支' ];
			return amtFlagArray[Number(data) - 1];
		}
		function transCatagory(enumKey) {
			var result = "";
			$.ajax({
				url:"${contextPath}/getTransWord.do", 
				dataType:"json",
				async:false,
				data:{
					"type" : "catagory",
					"enumKey" : enumKey
				}, 
				success:function(data) {
					result = data.enumValue;
				}
			});
			return result;
		}
		function delTrade(selectedObj) {
			$.post("${contextPath}/delTrade.do", {
				"id" : selectedObj.data().id
			}, function(data) {
				console.log('delTrade-->' + data);
			});
		}
		function saveTrade(id) {
			if(checkTradeForm()) {
				return false;
			}
			var urlPath = "updateTrade";
			if (typeof (id) == "undefined") {
				urlPath = "saveTrade";
			}
			$.post("${contextPath}/" + urlPath + ".do", {
				"id" : id,
				"reason" : $("#reason").val(),
				"amtFlag" : $("input[name='amtFlag']:checked").val(),
				"amt" : $("#amt").val(),
				"catagory" : $("select[name='catagory'] option:checked").val(),
				"updateTime" : $('#datetimepicker2').data("DateTimePicker")
						.date().format("YYYY-MM-DD")
			}, function(data) {
				if (data == "SUCCESS") {
					location.reload();
				} else {
					alert(data);
				}
			});
		}
		function checkTradeForm() {
			$("input[data-required]").each(function(i, n){
				$(this).trigger("blur");
			})
			return ($(".form-horizontal .has-error").length > 0);
		}
	</script>
	<div id="trade_main_wrapper_div"></div>
	
	<div class="dataTables_wrapper">
		<div class="DTTT_container">
			<button id="new_trade_button" class="btn btn-primary" data-toggle="tooltip" data-placement="bottom" title="单击新增">
				<span class="glyphicon glyphicon-plus"></span>
			</button>
			<button id="edit_trade_button" class="btn btn-primary" data-toggle="tooltip" data-placement="bottom" title="选中列表一条记录后点击修改">
				<span class="glyphicon glyphicon-pencil"></span>
			</button>
			<button id="del_trade_button" class="btn btn-primary" data-toggle="tooltip" data-placement="bottom" title="选中列表一条记录后点击删除">
				<span class="glyphicon glyphicon-trash"></span>
			</button>
		</div>
	
		<table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
			 <thead>
	            <tr>
	                <th>用途</th>
	                <th>收支</th>
	                <th>金额</th>
	                <th>类型</th>
	                <th>发生日期</th>
	            </tr>
	        </thead>
		</table>
	</div>
</body>
</html>