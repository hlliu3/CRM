<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <base href="${pageContext.request.contextPath}/">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">
        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $(".remarkDiv").mouseover(function () {
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function () {
                $(this).children("span").css("color", "red");
            });

            $(".myHref").mouseout(function () {
                $(this).children("span").css("color", "#E6E6E6");
            });


            //处理后台(页面显示完成，ajax实现备注的局部刷新)
            //备注展示
            showRemark();

            //设置鼠标移入移除事件，显示更新和删除按钮
            $("#remarkBody").on("mouseover",".remarkDiv",function(){
                $(this).children("div").children("div").show();
            });
            $("#remarkBody").on("mouseout",".remarkDiv",function(){
                $(this).children("div").children("div").hide();
            });

            $("#updateRemark").click(function () {
                //使用ajax进行更新
                var rid = $("#remarkId").val();
                $.ajax({
                    url : "activityRemark/updateActivityRemarkByRemarkId.do",
                    type : "post",
                    data : {
                        "id" : rid,//获取隐藏域中的数据id值
                        "noteContent" :$.trim($("#noteContent").val())
                    },
                    dataType : "json",
                    success : function (data) {
                        if(data.success){
                            //此处不用重新执行局部刷新，直接使用jquery更新
                            //showRemark();

                            $("#h5"+rid).html(data.remark.noteContent);
                            $("#show"+rid).html(data.remark.editTime+" 由"+data.remark.editBy);
                            alert("更新成功");
                            $("#editRemarkModal").modal("hide");
                        }else{
                            alert("更新失败");
                            $("#editRemarkModal").modal("hide");
                        }
                    }
                });
            });

            //添加
            $("#insertBtn").click(function () {
                $.ajax({
                    url : "activityRemark/insertRemark.do",
                    type : "post",
                    data : {
                        "noteContent" : $.trim($("#remark").val()),
                        "activityId" : "${activityDetail.id}"
                    },
                    dataType : "json",
                    success : function (data) {
                        if(data.success){
                            var html = "";
                            html += '<div class="remarkDiv" style="height: 60px;">';
                            html += '<img title="${activityDetail.owner}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                            html += '<div style="position: relative; top: -40px; left: 40px;">';
                            html += '<h5 id="'+"h5"+data.remark.id+'">'+data.remark.noteContent+'</h5>';
                            html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activityDetail.name}</b>';
                            html += '<small style="color: gray;" id="'+"show"+data.remark.id+'">'+(data.remark.createTime)+'&nbsp;&nbsp;&nbsp;由'+(data.remark.createBy)+'</small>';
                            html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                            html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;" id="'+"edit"+data.remark.id+'" onclick="editRemark(\''+data.remark.id+'\')"></span></a>';
                            html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                            html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;" id="'+"remove"+data.remark.id+'" onclick="removeRemark(\''+data.remark.id+'\')"></span></a>';
                            html += '</div>';
                            html += '</div>';
                            html += '</div>';
                            $("#remarkDiv").before($(html));

                            //清空文本域
                            $("#remark").val("");
                            alert("添加成功");
                        }else{
                            alert("添加失败");
                        }
                    }
                });
            });
        });

        function editRemark(id) {
            //打开模态框
            $("#editRemarkModal").modal("show");
            //去除上一次的值
            $("#noteContent").val("");
            //给隐藏域设置id
            $("#remarkId").val(id);
        }
        //点击触发删除操作
        function removeRemark(id) {
            $.ajax({
                url : "activityRemark/deleteActivityRemarkByRemarkId.do",
                type : "get",
                data : {
                    "id" : id
                },
                dataType : "json",
                success : function (data) {
                    if(data.success){
                        //此处不用重新执行局部刷新
                        //showRemark();
                        //直接使用jquery删除
                        $("#remove"+id).parent().parent().parent().parent().remove();
                    }else{
                        alert("删除失败");
                    }
                }
            });
        }
        //页面加载完成，展示备注信息列表
        function showRemark() {
            $.ajax({
                url : "activityRemark/selectRemarkByActivityId.do",
                type : "get",
                data : {
                    "id" : "${activityDetail.id}"//获取打开页面之前后台的request域中的数据
                },
                dataType : "json",
                success : function (data) {
                    //清空一下上次的追加数据
                    $(".remarkDiv").remove();
                    var html = "";
                    $.each(data,function (i,eachRemark) {

                        html += '<div class="remarkDiv" style="height: 60px;">';
                        html += '<img title="${activityDetail.owner}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                        html += '<div style="position: relative; top: -40px; left: 40px;">';
                        html += '<h5 id="'+"h5"+eachRemark.id+'">'+eachRemark.noteContent+'</h5>';
                        html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activityDetail.name}</b>';
                        html += '<small style="color: gray;" id="'+"show"+eachRemark.id+'">'+(eachRemark.editFlag==0?eachRemark.createTime:eachRemark.editTime)+'&nbsp;&nbsp;&nbsp;由'+(eachRemark.editFlag==0?eachRemark.createBy:eachRemark.editBy)+'</small>';
                        html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                        html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;" id="'+"edit"+eachRemark.id+'" onclick="editRemark(\''+eachRemark.id+'\')"></span></a>';
                        html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                        html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;" id="'+"remove"+eachRemark.id+'" onclick="removeRemark(\''+eachRemark.id+'\')"></span></a>';
                        html += '</div>';
                        html += '</div>';
                        html += '</div>';
                    });
                    $("#remarkDiv").before($(html));
                }

            });
        }
    </script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemark">更新</button>
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
                <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
                                <option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>
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
                            <input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
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
                <button type="button" class="btn btn-primary" data-dismiss="modal" >更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${activityDetail.name}
            <small>${activityDetail.startDate} ~ ${activityDetail.endDate}</small>
        </h3>
    </div>
    <div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal" id="editBtn"><span
                class="glyphicon glyphicon-edit" id="editSpan"></span> 编辑
        </button>
        <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus" id="removeBtn"></span> 删除</button>
    </div>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.owner}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activityDetail.name}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.startDate}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activityDetail.endDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.cost}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.createBy}&nbsp;&nbsp;</b>
            <small style="font-size: 10px; color: gray;">${activityDetail.createTime}</small>
        </div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activityDetail.editBy}&nbsp;&nbsp;</b>
            <small style="font-size: 10px; color: gray;">${activityDetail.editTime}</small>
        </div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${activityDetail.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 30px; left: 40px;" id="remarkBody">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;" >
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" id="insertBtn">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>