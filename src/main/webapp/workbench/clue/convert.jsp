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


    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript">
        $(function () {
            $("#isCreateTransaction").click(function () {
                if (this.checked) {
                    $("#create-transaction2").show(200);
                } else {
                    $("#create-transaction2").hide(200);
                }
            });

            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "top-left"
            });

            //查询市场活动
            $("#activity").click(function () {
                //在模态框中先展示数据(线索已经关联的数据)
                $.ajax({
                    url: "clue/selectActivityListByClueId.do",
                    type: "get",
                    data: {
                        "clueId": "${param.id}"
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            var html = "";
                            $.each(data.activities, function (i, n) {
                                html += '<tr>';
                                html += '<td><input type="radio" name="activity" value="'+n.id+'"/></td>';
                                html += '<td id="'+n.id+'">' + n.name + '</td>';
                                html += '<td>' + n.startDate + '</td>';
                                html += '<td>' + n.endDate + '</td>';
                                html += '<td>' + n.owner + '</td>';
                                html += '</tr>';
                            });
                            $("#activityByClueTbody").html(html);
                        } else {
                            alert("查询失败");
                        }
                    }
                });
                //打开模态框
                $("#searchActivityModal").modal("show");

            });

            //回车search查询市场活动，根据名称模糊查询
            $("#search").keydown(function (event) {
                if(event.keyCode==13){

                    $.get(
                        "clue/selectActivityListByClueId.do",
                        {"activityName" : $.trim($("#search").val()),"clueId":"${param.id}"},
                        function (data) {
                            if (data.success) {
                                var html = "";
                                $.each(data.activities, function (i, n) {
                                    html += '<tr>';
                                    html += '<td><input type="radio" name="activity" value="'+n.id+'"/></td>';
                                    html += '<td id="'+n.id+'">' + n.name + '</td>';
                                    html += '<td>' + n.startDate + '</td>';
                                    html += '<td>' + n.endDate + '</td>';
                                    html += '<td>' + n.owner + '</td>';
                                    html += '</tr>';
                                });
                                $("#activityByClueTbody").html(html);
                            } else {
                                alert("查询失败");
                            }
                        },
                        "json"
                    );
                    return false;//防止bootStrop的模态框的默认行为
                }
            });
            //选中了市场活动，将信息显示在input框中
            $("#submitActivityBtn").click(function () {
                var id = $(":radio[name='activity']:checked").val();
                var name = $("#"+id).text();

                $("#activity").val(name);
                $("#hidden-activityid").val(id);

                $("#searchActivityModal").modal("hide");

            });

            //转换
            $("#converBtn").click(function () {
                if($("#isCreateTransaction").prop("checked")){//勾选了创建交易

                    //提交表单
                    $("#createTransaction")[0].submit();

                }else{
                    window.location.href = "clue/clueConver.do?flag=0&clueid=${param.id}";
                }

            });
        });
    </script>

</head>
<body>

<!-- 搜索市场活动的模态窗口 -->
<div class="modal fade" id="searchActivityModal" role="dialog">
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
                            <input type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询" id="search">
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
                    <tbody id="activityByClueTbody">

                    <%--<tr>
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
                <button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
            </div>
        </div>

    </div>
</div>

<div id="title" class="page-header" style="position: relative; left: 20px;">
    <h4>转换线索
        <small>${param.fullname}${param.appellation}-${param.company}</small>
    </h4>
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
<div id="create-transaction2"
     style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;">

    <form id="createTransaction" action="clue/clueConver.do" method="post">
        <div class="form-group" style="width: 400px; position: relative; left: 20px;">
            <label for="amountOfMoney">金额</label>
            <input type="text" class="form-control" id="amountOfMoney" name="amountOfMoney">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="tradeName">交易名称</label>
            <input type="text" class="form-control" id="tradeName" value="${param.company}-" name="tradeName">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="expectedClosingDate">预计成交日期</label>
            <input type="text" class="form-control time" id="expectedClosingDate" name="expectedClosingDate">
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="stage">阶段</label>
            <select id="stage" class="form-control" name="stage">
                <option></option>
                <c:forEach var="eachstage" items="${stage}">
                    <option id="${eachstage.value}">${eachstage.text}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group" style="width: 400px;position: relative; left: 20px;">
            <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" style="text-decoration: none;"><span
                    class="glyphicon glyphicon-search"></span></a></label>
            <!-- data-toggle="modal" data-target="#searchActivityModal"-->
            <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly name="actinityname"/>
            <input type="hidden" id="hidden-activityid" name="activityId"/>
            <input type="hidden" name="clueid" value="${param.id}"/>
        </div>
    </form>

</div>

<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
    记录的所有者：<br>
    <b>${param.owner}</b>
</div>
<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
    <input class="btn btn-primary" type="button" value="转换" id="converBtn">
    &nbsp;&nbsp;&nbsp;&nbsp;
    <input class="btn btn-default" type="button" value="取消">
</div>
</body>
</html>