<%--
  Created by IntelliJ IDEA.
  User: LHL
  Date: 2019/5/11
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery_3.4.0.js"></script>
    <script type="text/javascript" src="src/polyfills.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script>


        $(main);
        function main() {

            if(window.top!=window){//解决登录页不在单独页面的bug
                window.top.location=window.location;
            }

            fun3();
            $(":text").bind("input propertychange",fun2);
            $(":password").bind("input propertychange",fun2)

            //为当前登录页面登录给绑定键盘事件
            ${window}.keydown(function (event) {
                if(event.keyCode==13){//回车键（登录）
                    fun5();
                }
            });

            $("#submitBtn").click(fun5);//登录
        }
        function fun2() {
            $("#msg").text("");
        }

        function fun3() {
            $(":text[name='username']").val("");//每次加载完页面就把文本内容删除
            $(":text[name='username']").focus();//自动获得焦点
        }

        function fun5() {//登录校验
            //校验登录密码和用户名不为空，删除左右空格，     .trim()或者    $.trim(text);
            var loginAct = $.trim($(":text[name='username']").val());
            var loginPwd = $.trim($(":password[name='password']").val());

            if(loginAct==""||loginPwd==""||loginAct==null||loginPwd==null||typeof loginPwd=="undefined"||typeof loginAct=="undefined"){
                alert("请输入用户名或密码");
                return;
            }
            //ajax校验后台的数据库
            $.ajax({
                    url : "user/userLogin.do",
                    type : "post",
                    //data : $("form").serialize(),//参数form表单中的参数
                    data : {
                        "username" : loginAct,
                        "password" : loginPwd
                    },
                    dataType : "json",
                    success : function (data) {
                        if(data.success){//登录成功
                            window.location.href = "workbench/index.jsp";
                        }else{//登录失败
                            $("#msg").text(data.msg);
                        }
                    }
                }
            )
        }

    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>



<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="user/userLogin.do" class="form-horizontal" role="form" method="post">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" name="username">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" name="password">
                </div>
                <div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

                    <span id="msg" class="label label-danger"></span>

                </div>
                <!--button   -->
                <button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
