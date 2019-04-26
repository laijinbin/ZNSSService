<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>智能宿舍系统 | 登录</title>

    <meta
            content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"
            name="viewport">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/plugins/ionicons/css/ionicons.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/plugins/adminLTE/css/AdminLTE.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/plugins/iCheck/square/blue.css">
</head>

<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <a href="all-admin-index.html"><b>智能宿舍</b>后台管理系统</a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <p class="login-box-msg">登录系统</p>
        <%--<p style="color: red;text-align:center;font-size: 15px">${sessionScope.msg}</p>--%>
        <form action="${pageContext.request.contextPath}/web/login" method="post">
            <div class="form-group has-feedback">
                <input type="text" name="username" class="form-control"
                       placeholder="用户名"> <span
                    class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="password" class="form-control"
                       placeholder="密码"> <span
                    class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="text" name="code"
                       placeholder="请输入验证码" size="2" style="height: 34px;width: 230px;background-color: #e8f0fe"/>
                <span><img id="code" src="${pageContext.request.contextPath}/manager/checkCode" onclick="getCode()"
                           align="right" style="width: 80px;height: 34px;border: thin solid #e8f0fe;"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label><a href="" data-toggle="modal" data-target="#myModal">忘记密码？</a></label>
                    </div>
                </div>

                <!-- /.col -->
                <div class="col-xs-4">

                    <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>

                </div>
                <!-- /.col -->
            </div>
        </form>

        <%--<a href="#">忘记密码</a><br>--%>


    </div>
    <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<!-- jQuery 2.2.3 -->
<!-- Bootstrap 3.3.6 -->
<!-- iCheck -->
<script
        src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script
        src="${pageContext.request.contextPath}/plugins/bootstrap/js/bootstrap.min.js"></script>
<script
        src="${pageContext.request.contextPath}/plugins/iCheck/icheck.min.js"></script>

<div class="modal fade" tabindex="-1" role="dialog" id="myModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">寻回密码</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-4">
                        <h4 class="modal-title">请输入用户名</h4>
                    </div>
                    <div class="col-xs-8">
                        <div class="form-group has-feedback">
                            <input type="text" name="changPwdName" id="changPwdName" class="form-control"
                                   placeholder="用户名"> <span
                                class="glyphicon glyphicon-user form-control-feedback"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">
                        <h4 class="modal-title">请输入手机号码</h4>
                    </div>
                    <div class="col-xs-8">
                        <div class="form-group has-feedback">
                            <input type="text" name="changPwdPhone" id="changPwdPhone" class="form-control"
                                   placeholder="手机号码"> <span
                                class="glyphicon glyphicon-earphone form-control-feedback"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">
                        <h4 class="modal-title">请输入新密码</h4>
                    </div>
                    <div class="col-xs-8">
                        <div class="form-group has-feedback">
                            <input type="password" name="changPwdNewPwd" id="changPwdNewPwd" class="form-control"
                                   placeholder="新密码"> <span
                                class="glyphicon glyphicon-lock form-control-feedback"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">
                        <h4 class="modal-title"><button type="button" class="btn btn-info" id="getSmsCode" onclick="sendSmsCode()">获取短信验证码</button></h4>
                    </div>
                    <div class="col-xs-8">
                        <div class="form-group has-feedback">
                            <input type="text" name="changPwdSmsCode" id="changPwdSmsCode" class="form-control"
                                   placeholder="短信验证码"> <span
                                class="glyphicon glyphicon-envelope form-control-feedback"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button"  class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="alertPwd()">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript">
    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
    function alertPwd() {
        var changPwdName=$("#changPwdName").val();
        var changPwdNewPwd=$("#changPwdNewPwd").val();
        var changPwdSmsCode=$("#changPwdSmsCode").val();
        var changPwdPhone=$("#changPwdPhone").val();
        if (!changPwdName){
            alert("用户名不能为空");
        }
        if (!changPwdNewPwd){
            alert("密码不能为空");
        }
        if (!changPwdSmsCode){
            alert("短信验证码不能为空");
        }
        var url="/ZNSS/web/alertPwd";
        var data={
            changPwdName:changPwdName,
            changPwdNewPwd:changPwdNewPwd,
            changPwdSmsCode:changPwdSmsCode,
            changPwdPhone:changPwdPhone
        }
        $.ajax({
            url:url,
            type:"post",
            dataType:"json",
            contentType: "application/json",
            data:JSON.stringify(data),
            success:function (result) {
                if (result.code=="0"){
                    alert(result.msg);
                    window.location.reload();

                }else {
                    alert(result.msg);
                    return;
                }
            },
            error:function () {
                alert("服务器忙。。。");
            }
        })
    }
   function sendSmsCode() {

       var changPwdPhone=$("#changPwdPhone").val();
       if (!changPwdPhone){
           alert("手机号码不能为空");
           return;
       }

       $.ajax({
           url:"/ZNSS/web/sendSmsCode",
           data:{
               changPwdPhone:changPwdPhone
           },
           type:"post",
           dataType:"json",
           success:function (result) {
               if (result.code=="0"){
                   var timeNum=60;
                   $("#getSmsCode").prop("disabled","disabled");

                   var timeId=setInterval(function () {
                       timeNum--;
                       $("#getSmsCode").text(timeNum+"已发送");
                       $("#getSmsCode").css("background-color","#848484");
                       if (timeNum==0){
                           $("#getSmsCode").prop("disabled","");
                           $("#getSmsCode").text("获取短信验证码");
                           $("#getSmsCode").css("background-color","");
                           timeNum=60;
                           clearInterval(timeId);
                       }
                   },1000)
               }else {
                   alert("服务器忙。。。");
               }
           },
           error:function () {
               alert("服务器忙。。。");
           }
       })
   }
    function getCode() {
        document.getElementById("code").src = "${pageContext.request.contextPath}/manager/checkCode?id=" + new Date().getTime();
    }

</script>
<%
    String msg = (String) session.getAttribute("msg");
    if (msg != null && !"".equals(msg)) {
%>
<script type="text/javascript">
    alert("<%=msg%>");
</script>
<%
        session.setAttribute("msg", "");
    }
%>
</body>

</html>