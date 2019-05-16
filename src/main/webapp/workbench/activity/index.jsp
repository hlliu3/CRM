<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


    <script type="text/javascript">

        $(function () {

            $("#insertBtn").click(function () {//添加用户
                //打开之前先将之前的数据重置

                //获取所有的用户
                $.ajax({
                    url: "activity/selectUserList.do",
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        $.each(data, function (i, eachData) {
                            $("<option></option>").html(eachData.name).val(eachData.id).appendTo($("#create-marketActivityOwner"));
                        })
                        $("#create-marketActivityOwner").val("${userInfo.id}");//设置默认选中的数据
                    }
                });

                //打开模态框
                $("#createActivityModal").modal("show");
            });

            //保存
            $("#insertActivityBtn").click(function () {
                //保存信息
                $.ajax({
                    url: "activity/insertActive.do",
                    data: {
                        "owner": $.trim($("#create-marketActivityOwner").val()),
                        "name": $.trim($("#create-marketActivityName").val()),
                        "startDate": $.trim($("#create-startTime").val()),
                        "endDate": $.trim($("#create-endTime").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-describe").val())
                    },
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        if (data.success) {
                            $("#insertform")[0].reset();
                            //添加成功，刷新列表
                            pageActivityShow(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            alert("添加成功！");
                            $("#createActivityModal").modal("hide");

                        } else {
                            alert("添加失败！");
                        }
                    }
                })
            });
            //设置日期插件
            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            pageActivityShow(1, 5);

            //点击查询将数据保存在隐藏标签域中
            $("#search-activity").click(function () {
                $("#hidden-name").val($.trim($("#select-name").val()));
                $("#hidden-owner").val($.trim($("#select-owner").val()));
                $("#hidden-startDate").val($.trim($("#select-startDate").val()));
                $("#hidden-endDate").val($.trim($("#select-endDate").val()));

                pageActivityShow(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
            });

            //全选
            $("#selectAll").click(function () {
                $(":checkbox[name='ck']").prop("checked",$("#selectAll").prop("checked"));
            });


            //为动态生成的标签添加绑定事件
            $("#activitytbody").on("click",$(":checkbox[name='ck']"),function () {
                if($(":checkbox[name='ck']:checked").length == $(":checkbox[name='ck']").length&&$(":checkbox[name='ck']:checked").length>0){
                    $("#selectAll").prop("checked",true);
                }else{
                    $("#selectAll").prop("checked",false);
                }
            });
                    //这种对动态生成的标签绑定事件的方式不对
                    /*$(":checkbox[name='ck']").click(function () {
                           if($(":checkbox[name='ck']:checked").length == $(":checkbox[name='ck']").length&&$(":checkbox[name='ck']:checked").length>0){
                               $("#selectAll").prop("checked",true);
                           }else{
                               $("#selectAll").prop("checked",false);
                           }
                       });*/

            //批量删除
            $("#deleteBtn").click(function () {
                if($(":checkbox[name='ck']:checked").length==0){
                    alert("请先进行选择");
                    return;
                }
                if(confirm("你确定要删除吗？")){
                    var param = "";
                    var activityCheckeds = $(":checkbox[name='ck']:checked");
                    //获取参数
                    $.each(activityCheckeds,function (i,eachBom) {
                        if(i<activityCheckeds.length-1){
                            param+="id="+$(eachBom).val()+"&";
                        }else{
                            param+="id="+$(eachBom).val();
                        }
                    });

                    //alert(param);
                    //执行删除操作
                    $.ajax({
                        url : "activity/deleteActivityBath.do",
                        type : "post",
                        dataType : "json",
                        data : param,
                        success : function (data) {
                            $(":checkbox[name='ck']:checked").parent().parent().remove();//删除成功后将选中的移除
                            $("#selectAll").prop("checked",false);//将全选改为都不选
                            pageActivityShow(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            if(data.success){
                                alert("删除成功");
                            }else{
                                alert("删除失败");
                            }
                        }
                    });
                }

            });
            


            //更新打开模态框,展示数据和用户列表
            $("#updateBtn").click(function () {
                if($(":checkbox[name='ck']:checked").length==0){
                    alert("请先进行选择");
                    return;
                }
                //查询user的下来列表和activity的信息
                var aid = $($(":checkbox[name='ck']:checked")[0]).val();
                $.ajax({
                    url : "activity/selectUserListAndActivity.do",
                    type : "post",
                    data : {
                        "id" : aid
                    },
                    dataType : "json",
                    success : function (data) {
                        $.each(data.userList,function (i,user) {
                            $("<option></option>").val(user.id).html(user.name).appendTo($("#edit-marketActivityOwner"));
                        });
                        //设置默认选中数据
                        $("#edit-marketActivityOwner").val(data.activity.owner);
                        //设置activity的值
                        //edit-marketActivityName,edit-startTime,edit-endTime,edit-cost,edit-describe
                        $("#edit-marketActivityName").val(data.activity.name);
                        $("#edit-startTime").val(data.activity.startDate);
                        $("#edit-endTime").val(data.activity.endDate);
                        $("#edit-cost").val(data.activity.cost);
                        $("#edit-describe").val(data.activity.description);
                        $("#hidden-activityid").val(aid);
                        $("#edit-marketActivitycreateBy").val(data.activity.createBy);
                        $("#edit-marketActivitycreatetime").val(data.activity.createTime);
                        //打开模态框
                        $("#editActivityModal").modal("show");
                    }
                });
            });

            //更新操作
            $("#updateActivity").click(function () {
                $.ajax({
                    url : "activity/updateActivity.do",
                    type : "post",
                    data : {
                        "id" : $.trim($("#hidden-activityid").val()),
                        "name" :$.trim($("#edit-marketActivityName").val()),
                        "startDate" : $.trim($("#edit-startTime").val()),
                        "endDate" : $.trim($("#edit-endTime").val()),
                        "cost" : $.trim($("#edit-cost").val()),
                        "description" :$.trim($("#edit-describe").val()),
                        "owner" : $.trim($("#edit-marketActivityOwner").val()),
                        "createBy" : $.trim($("#edit-marketActivitycreateBy").val()),
                        "createTime" :$.trim($("#edit-marketActivitycreatetime").val())
                    },
                    dataType : "json",
                    success : function (data) {
                        if(data.success){
                            //pageActivityShow(1,5);
                            pageActivityShow($("#activityPage").bs_pagination('getOption', 'currentPage')
                                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            $("#updateform")[0].reset();
                            alert("更新成功");
                            $("#editActivityModal").modal("hide");
                        }else{
                            alert("更新失败");
                            $("#editActivityModal").modal("hide");
                        }
                    }
                });
            });

        });

        function pageActivityShow(pageCurrent, pageSize) {//显示分页数据
            $.ajax({
                url: "activity/pageActivityShow.do",
                data: {
                    "name": $.trim($("#hidden-name").val()),
                    "owner": $.trim($("#hidden-owner").val()),
                    "startDate": $.trim($("#hidden-startDate").val()),
                    "endDate": $.trim($("#hidden-endDate").val()),
                    "pageCurrent": pageCurrent,
                    "pageSize": pageSize
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    var html = "";
                    $.each(data.datalist, function (i, eachData) {
                        html += "<tr class=\"active\">";
                        html += "<td><input type=\"checkbox\" name='ck' value='"+eachData.id+"'/></td>";
                        html += "<td><a style=\"text-decoration: none; cursor: pointer;\"" +
                            " onclick=\"window.location.href='activity/activityDetail.do?id="+eachData.id+"';\">" + eachData.name + "</a></td>";
                        html += "<td>" + eachData.owner + "</td>";
                        html += "<td>" + eachData.startDate + "</td>";
                        html += "<td>" + eachData.startDate + "</td></tr>";

                    });
                    //$("#activitytbody").append($(html));
                    $("#activitytbody").html(html);
                    //计算总页数
                    var totalpage = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

                    //alert(pageCurrent+","+pageSize+","+totalpage+","+ data.total);

                    //数据处理完毕后，结合分页查询，对前端展现分页信息//分页插件
                    $("#activityPage").bs_pagination({
                        currentPage: pageCurrent, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalpage, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,

                        //该回调函数是在点击分页组件的时候触发的
                        onChangePage: function (event, data) {
                            pageActivityShow(data.currentPage, data.rowsPerPage);
                        }
                    });

                }
            });
        }

    </script>
</head>
<body>
<input type="hidden" id="hidden-name"/>
<input type="hidden" id="hidden-owner"/>
<input type="hidden" id="hidden-startDate"/>
<input type="hidden" id="hidden-endDate"/>
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

                <form class="form-horizontal" role="form" id="insertform">
                    <input type="reset" style="display: none" id="insertreset"/>;
                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">

                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startTime" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endTime" readonly>
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
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="insertActivityBtn">保存</button>
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

                <form class="form-horizontal" role="form" id="updateform">
                    <input type="hidden" id="hidden-activityid"/>
                    <input type="hidden" id="edit-marketActivitycreateBy">
                    <input type="hidden" id="edit-marketActivitycreatetime">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">

                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startTime">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endTime">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="updateActivity">更新</button>
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
                        <input class="form-control" type="text" id="select-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="select-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="select-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="select-endDate">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="search-activity">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="insertBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <!--data-toggle="modal" data-target="#editActivityModal" -->
                <button type="button" class="btn btn-default" id="updateBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="selectAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activitytbody">


                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage">

            </div>
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