function initRecordList() {
	$('#example').dataTable({
		language: {
			url: 'resources/DataTables-1.10.7/media/i18n/chinese.json'
		},
		"processing": true,
	    "serverSide": true,
		"order": [[ 4, "desc" ]],
		"ajax" : "getList.do",
		"columns": [
		            { "data": "reason" },
		            { "data": "amtFlag",
		            	"render": function ( data, type, row ) {
		            		return transAmtFlag(data);
		            	}
		            },
		            { "data": "amt",
		            	"render": function ( data, type, row ) {
		            		return formatCurrency(data);
		            	}
		            },
		            { "data": "catagory",
		            	"render": function ( data, type, row ) {
		            		return transCatagory(data);
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
}
function loadMainTrade(currentId) {
	$("#trade_main_wrapper_div").empty();
	$("#trade_main_wrapper_div").load(
			"getMainTradeRecord.do", {
				id : currentId
			}, function() {
				$("#trade_main_div").modal();
			});
}
function loadGraphTrade(divId, AjaxUrl) {
	// 基于准备好的dom，初始化echarts图表
	var ecConfig = echarts.config.EVENT;
	var myChart = echarts.init(document.getElementById(divId));
	var option = "{}";
	$.ajax({
		url:""+AjaxUrl+".do", 
		dataType:"json",
		async:false,
		success:function(data) {
			option = data.options;
		}
	});
	myChart.on(ecConfig.CLICK, showFilterTable);
	// 为echarts对象加载数据 
	myChart.setOption(option);
}
function showFilterTable(param) {
	var typeName = param.name;
	//如果是月支出统计
	if(Number(typeName)>0) {
		typeName = typeName.substring(0,4)+"-"+typeName.substring(4,6);
	}
	$("input[type='search']").val(typeName);
	$("input[type='search']").trigger("focus").trigger("keyup");

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
		url:"getTransWord.do", 
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
	$.post("delTrade.do", {
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
	$.post(urlPath + ".do", {
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