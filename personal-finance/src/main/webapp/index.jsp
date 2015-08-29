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
	<!-- ECharts单文件引入 -->
    <script src="${contextPath}/resources/echarts-2.2.7/build/source/echarts-all.js"></script>
    <script src="${contextPath}/resources/personal-finance.js"></script>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/personal-finance.css">
</head>
<body>
	<script>
		$(document).ready(function() {
			initRecordList();
			loadGraphTrade('trade_bar_graph_main', 'getEchartsBarByMon');
			loadGraphTrade('trade_pie_graph_main', 'getEchartsPieByType');
			$("#trade_pie_graph_main").removeClass("in active");//如果2个div都不初始加in active 不加的图形width不够
		});
	</script>

	<nav class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">个人日记账</a>
		</div>
		<!-- 
		<div>
			<ul class="nav navbar-nav">
				<li class="active"><a id="list_a" href="javascript:void(0);">明细表</a></li>
				<li><a id="bar_a" href="javascript:void(0);">帐目统计图</a></li>
			</ul>
		</div>
		 -->
	</nav>
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#trade_bar_graph_main" data-toggle="tab">
				月支出 </a></li>
		<li><a href="#trade_pie_graph_main" data-toggle="tab">构成图</a></li>
	</ul>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active box table-bordered" id="trade_bar_graph_main"></div>
		<div class="tab-pane fade in active box table-bordered" id="trade_pie_graph_main"></div>
	</div>
	
	
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
	
		<table id="example" class="table table-striped table-bordered" cellspacing="0" >
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