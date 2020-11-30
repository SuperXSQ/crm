<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">


	$(function(){

		//日期模块
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});


        //页面加载后自动执行一个方法来刷新列表
        pageList(1, 2);


		//点击创建弹出模态窗口
		$("#addBtn").click(function () {

			//查询全部的所有者的姓名
			$.ajax({
				url : "workbench/activity/findAll.do",
				dataType : "json",
				success : function (data) {

					var html = "";

					$.each(data, function (i, n) {
						html += "<option value='" + n.id + "'>" + n.name + "</option>";
					})

					$("#create-owner").html(html);

					//把默认值设置成当前用户姓名
					$("#create-owner").val("${user.id}");

					//显示模态窗口
					$("#createActivityModal").modal("show");

				}
			})
		})

		//点击保存后台insert
		$("#saveBtn").click(function () {

			$.ajax({
				url : "workbench/activity/save.do",
				dataType: "json",
				type : "post",
				data : {
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val()),
				},
				success : function (data) {
					//{"success":true/false}
					if (data.success) {
						//如果返回true，刷新列表
						alert("保存成功");
                        //刷新列表
                        //pageList(1,2);
                        //页码返回到第一页，每页显示的条数维持当前设置
                        pageList(1
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                        //reset模态窗口中已经填写的数据
						//先把jquery对象转成dom对象才有reset方法
						$("#form")[0].reset();

						//然后关闭模态窗口
						$("#createActivityModal").modal("hide");

					}else {
						alert(data.s)
					}
				}
			})
		})

		//为查询按钮绑定查询刷新列表事件
		$("#search-btn").click(function () {

			//查询后将搜索条件保存到隐藏域
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			//pageList(1 ,2);
            pageList(1
                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


        })

        /*
            以下是对复选款的操作
            $("#checkAll").click(function () {
                $("input[name=check]").prop("checked", this.checked);
                                      执行选中或取消选中 如果$("#checkAll")被选中
                                                       为true就执行 让 $("input[name=check]") 被选中
                                                       为false就 让$("input[name=check]")取消选中
            })
        */
		//为复选框绑定事件，触发全选操作
        $("#checkAll").click(function () {
            $("input[name=check]").prop("checked", this.checked);
        })
        /*
            动态生成的元素 要以on方法的形式来触发事件

            语法：
                $(需要绑定元素的有效的外层元素).on(绑定事件的方式, 需要绑定的元素的jqeury对象, 回调函数)
        */
        $("#tbody").on("click", $("input[name=check]"), function () {

            $("#checkAll").prop("checked", $("input[name=check]").length == $("input[name=check]:checked").length);
        })

        //为删除按钮绑定事件，触发批量删除操作（1.不选  2.选一条  3.选多条）
        $("#deleteBtn").click(function () {

			//$("input[name=check]:checked") 这个jquery对象是这个数组，里面是所有符合条件的对象
			var $obj = $("input[name=check]:checked");
			var text = "";

			$.each($obj, function (i, n) {
				text += "id="+n.value;

				//i是每个元素的下标 下标=长度-1  就是最后一个元素 这时不加&符号
				if(i != $obj.length-1){
					text += "&";
				}
			})

			alert(text);

            $.ajax({
                url : "workbench/activity/delete.do",
                dataType : "json",
                type : "post",
				//id:  &id:  &id:
                data : text,
                success : function (data) {
                	//"success":true/false
					if(data.success){
						alert("已成功删除"+$obj.length+"条记录！");

						//pageList(1,2);
                        pageList(1
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                    }else alert("删除失败！");
                }
            })
        })


		//为修改按钮绑定单击事件
		$("#editBtn").click(function () {

			if ($("input[name=check]:checked").length == 1){

				//查询全部的所有者去填充所有者下拉框
				$.ajax({
					url : "workbench/activity/findAll.do",
					dataType : "json",
					success : function (data) {

						var html = "";

						$.each(data, function (i, n) {
							html += "<option value='" + n.id + "'>" + n.name + "</option>";
						})

						$("#edit-owner").html(html);
					}
				})

				var text = "id=" + $("input[name=check]:checked").val();

				$.ajax({
					url : "workbench/activity/findByCheck.do",
					dataType : "json",
					type : "get",
					data : text,
					success : function (data) {
						//{"owner":"张三", "name":"发传单", "startDate":"", "endDate":""...}
						//填充修改市场活动的模态窗口中的其他信息
						$("#edit-owner").val(data.owner);
						$("#edit-name").val(data.name);
						$("#edit-startDate").val(data.startDate);
						$("#edit-endDate").val(data.endDate);
						$("#edit-cost").val(data.cost);
						$("#edit-description").val(data.description);
					}
				})

				//打开修改的模态窗口
				$("#editActivityModal").modal("show");

			}else alert("请选择一项修改！");
		})


		//点击修改模态窗口的中的更新  将信息保存到后台数据库，然后关闭模态窗口
		$("#updateBtn").click(function () {

			//把Activity的id传到后端，通过id来保存记录
			var id = $("input[name=check]:checked").val();

			$.ajax({
				url : "workbench/activity/update.do",
				dataType : "json",
				type : "post",
				data : {
					"id" : id,
					"owner" : $("#edit-owner").val(), //val() 就是取的<option>中value的值 是所有者的id
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val()) //textarea标签对 用val()函数赋值
				},
				success : function (data) {
					if (data.success) {
						alert("修改成功！");

						//刷新列表
						//pageList(1,2);
                        //列表停留在当前页 并且每页的显示条数维持当前的设置
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                        //关闭模态窗口
						$("#editActivityModal").modal("hide");

					} else alert("修改失败！");
				}
			});
		});
	});

	function pageList(pageNo, pageSize) {

		//每次刷新列表的时候都重置复选框里的✓
		$("#checkAll").prop("checked", false);

		$("#search-name").val($("#hidden-name").val());
		$("#search-owner").val($("#hidden-owner").val());
		$("#search-startDate").val($("#hidden-startDate").val());
		$("#search-endDate").val($("#hidden-endDate").val());


		//pageList(pageNo , pageSize)
        //          页码      显示条数

		/*
            (pageNo-1) * pageSize = 从第几条记录开始（略过的条数）
              页码        显示条数

            limit （(pageNo-1) * pageSize） ， pageSize
         */

		$.ajax({
			url : "workbench/activity/pageList.do",
			dataType: "json",
			type : "get",
			data : {
			    "name" : $.trim($("#search-name").val()),
                "owner" : $.trim($("#search-owner").val()),
                "startDate" : $.trim($("#search-startDate").val()),
                "endDate" : $.trim($("#search-endDate").val()),
                "pageNo" : pageNo,
                "pageSize" : pageSize
			},
			success : function (data) {


			        /*
			        收到的数据格式
			        {
			            "total" : "",
                        "dataList" : {
                                   "id":"",
                                   "name":"",
                                   "owner":"",
                                   "startDate":"",
                                   "endDate":""
                                    }
                     }

                     */

                var html = "";

                $.each(data.dataList , function (i,n) {

                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="check" value="'+n.id+'"/></td>';
                    //这里可以把单击事件单独拿出来 单击的时候把id保存到session域中 跳转页面后取出，就可以在别的页面中通过activity的id查询 然后返回的时候删除
                    //！想多了。。。直接在跳转页面的时候在路径后面带上参数就可以了  workbench/activity/detail.do?id=XXX  然后请求转发到detail.jsp
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id=' + n.id + '\';">' + n.name + '</a></td>';
                    html += '<td>'+ n.owner +'</td>';
                    html += '<td>'+ n.startDate +'</td>';
                    html += '<td>'+ n.endDate +'</td>';
                    html += '</tr>';

                })
                $("#tbody").html(html);


                //计算总页数
				var totalPages = (data.total % pageSize == 0) ? (data.total / pageSize) : (parseInt(data.total / pageSize) +1);

                //分页组件
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		});
	}
	
</script>
</head>
<body>

	<%--隐藏域，用来储存用户的搜索条件--%>
	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-startDate" />
	<input type="hidden" id="hidden-endDate" />

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

									<%--此处动态拼接--%>

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="edit-form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="search-btn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tbody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>


			<div style="height: 50px; position: relative;top: 30px;">

                <div id="activityPage"></div>
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>--%>
			</div>
			
		</div>
		
	</div>
</body>
</html>