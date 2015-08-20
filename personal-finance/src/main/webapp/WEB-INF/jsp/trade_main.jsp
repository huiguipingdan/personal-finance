<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title></title>
	<script src="${contextPath}/resources/datetimepicker/bootstrap-datetimepicker.js"></script>
	<script src="${contextPath}/resources/validate/jquery-validate.js"></script>
	<link rel="stylesheet" type="text/css" href="${contextPath}/resources/datetimepicker/bootstrap-datetimepicker.css">
</head>
<body>
	<script>
	
		$(document).ready(function() {
			$('#datetimepicker2').datetimepicker({
			 	format: 'YYYY-MM-DD',
                locale: 'zh_cn',
                defaultDate:new Date()
            });
			$("#reason").val('${tradeModel.reason}');
			$("input[name='amtFlag'][value='${tradeModel.amtFlag}']").click();
			if(Number(${tradeModel.amt})>0){
				$("#amt").val(${tradeModel.amt});
			}
			$("select[name='catagory'] option[value='${tradeModel.catagory}']").attr("selected",true);
			$('#datetimepicker2').data("DateTimePicker").date('${tradeModel.updateTime}');
			//
			$('form').validate({
				description : {
			        test : {
			            required : '<div class="error">必填项</div>',
			            pattern : '<div class="error">请输入数字</div>',
			            conditional : '<div class="error">Conditional</div>',
			            valid : '<div class="success">Valid</div>'
			        }
			    },
			    onBlur : true,
				eachValidField : function() {
					$(this).closest('div').parent().removeClass('has-error');
					$("#"+$(this).attr("data-describedby")).empty();
				},
				eachInvalidField : function() {
					$(this).closest('div').parent().addClass('has-error');
				}
			});
		});
	</script>
	<div id="trade_main_div" class="modal fade" role="dialog" style="display: none">
		<div class="modal-dialog">
	    <!-- Modal content-->
		    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">日记账</h4>
			      </div>
			      
			      <div class="modal-body">
			     	<form class="form-horizontal" name="tradeForm" role="form">
						<div class="form-group">
							<label class="control-label col-sm-2" for="reason-label">用途:</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="reason" name="reason" data-required data-describedby="messages_1" data-description="test"
									placeholder="简单一句话描述">
									<span id="messages_1"></span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="amtFlag-label">收支:</label>
							<div class="col-sm-5">
								<label><input type="radio" name="amtFlag" value="1">收</label>
								<label><input type="radio" checked name="amtFlag" value="2">支</label>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="amt-label">金额:</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="amt" name="amt" data-required data-pattern="^[0-9]+$"  data-describedby="messages_2" data-description="test"
									placeholder="">
									<span id="messages_2"></span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="catagory-label">类型:</label>
							<div class="col-sm-5">
								<select name="catagory" class="form-control select select-primary select-block mbl" data-required>
								    <c:forEach items="${catagoryList}" var="catagory" varStatus="s">
								    	<option value="${catagory.enumKey}">${catagory.enumValue}</option>
								    </c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" for="updateTime-label">发生日期:</label>
							<div class="col-sm-5">
				                <div class='input-group date' id='datetimepicker2'>
				                    <input type='text' name="updateTime" class="form-control"/>
				                    <span class="input-group-addon">
				                        <span class="glyphicon glyphicon-calendar"></span>
				                    </span>
				                </div>
							</div>
						</div>
					</form>
			      </div>
			      
			      <div class="modal-footer">
			      	<button type="submit" class="btn btn-primary" onclick="parent.saveTrade(${tradeModel.id});" id="modalConfirmBtn">确认</button>
                	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			      </div>
		    </div>
	  </div>
	</div>
</body>
</html>