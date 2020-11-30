<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){

        //日期模块
        $(".time").datetimepicker({
            minView: "month",
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            pickerPosition: "top-left"
        });


		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//打开搜索市场活动的模态窗口
		$("#searchBtn").click(function () {

		    //清空搜索框  只要是从新打开的窗口 就是列出全部的
            $("#aname").val("");

		    //后台查询 展示市场活动列表
            showActivityListByName();

		    //打开搜索市场活动的模态窗口
            $("#searchActivityModal").modal("show");

        })

        //模态窗口中按回车键触发搜索事件
        $("#aname").keydown(function (event) {

            if (event.keyCode == 13){

                //过后台查询并展现列表
                showActivityListByName();

                //结束函数，避免触发模态窗口默认的回车键事件
                return false;
            }
        })

        //点击模态窗口中的选择
        $("#chooseBtn").click(function () {

            //被选中的radio
            var $obj = $("input[name=activity]:checked");
            //取得radio的id
            var id = $obj.val();
            //取得radio的name（市场活动名字）
            var name = $("#"+id).html();

            //如果没有选中任何radio
            if ($obj.length == 0){

                alert("请选择一条市场活动！！")

            } else {
                //选了一个radio 添加到市场活动源中
                $("#activityName").val(name);
                //把id添加到隐藏域中 提交表单的时候将市场活动的id提交
                $("#activityId").val(id);

                //关闭模态窗口
                $("#searchActivityModal").modal("hide");
            }
        })


        //点击转换按钮 跳转到线索页面 刷新线索列表(删除已转换的) 客户和联系人分别新增一条 交易根据是否在转换时创建交易来决定是否新增一条
        $("#convertBtn").click(function () {

            //判断是否勾选创建交易
            if ($("#isCreateTransaction").prop("checked")){
                //为true    勾选了为客户创建交易   需要把交易表单中的参数传递 直接提交表单
                $("#tranForm").submit();

            } else {
                //没有勾选为客户创建交易   把线索的id作为参数传递即可
                //后台要在线索tbl_clue表 和 tbl_clue_activity_relation中删除这条，同时在客户和联系人中添加一条
                window.location.href = "workbench/clue/convert.do?clueId=${param.id}";

            }
        })
	})

	function showActivityListByName(){

	    //取得搜索框中的内容
        var name = $.trim($("#aname").val());

        $.ajax({
            url: "workbench/clue/getActivityListByName.do",
            dataType: "json",
            type: "get",
            data: {
                "name": name
            },
            success: function (data) {

                var html = "";

                $.each(data, function (i,n) {

                    html += '<tr>';
                    html += '<td><input type="radio" name="activity" value="'+n.id+'"/></td>';
                    html += '<td id="'+n.id+'">'+n.name+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '</tr>';
                })

                $("#tbody").html(html);
            }
        })

    }
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input id="aname" type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="tbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="chooseBtn">选择</button>
                </div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form id="tranForm" action="workbench/clue/convert.do" method="post">


          <!--传入一个标记 代表此为勾选了创建交易的请求-->
          <input type="hidden" name="flag" value="y">

            <%--将线索的id传入--%>
          <input type="hidden" name="clueId" value="${param.id}">


		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name" value="${param.company}-">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control"  name="stage">
		    	<option></option>
		    	<%--<option>资质审查</option>
		    	<option>需求分析</option>
		    	<option>价值建议</option>
		    	<option>确定决策者</option>
		    	<option>提案/报价</option>
		    	<option>谈判/复审</option>
		    	<option>成交</option>
		    	<option>丢失的线索</option>
		    	<option>因竞争丢失关闭</option>--%>
                <c:forEach items="${stageList}" var="s">
                    <option value="${s.value}">${s.text}</option>
                </c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="searchBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
              <input type="hidden" id="activityId" name="activityId">
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${user.name}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" value="转换" id="convertBtn">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>