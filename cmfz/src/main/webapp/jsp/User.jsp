<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function () {
        $("#userTable").jqGrid(
            {
                url :'${pageContext.request.contextPath}/user/showAllUsers',
                datatype : "json",
                // 时间格式的处理在后台进行
                colNames : [ '编号', '电话', '密码','盐','状态', '头像','名字','nick_name','性别','sign','location','登录时间' ],
                colModel : [
                    {name : 'id',align:"center",hidden:true},
                    {name : 'phone',align:"center",editable: true},
                    {name : 'password',align:"center",editable: true},
                    {name : 'salt',align:"center",editable:true},
                    {name : 'status',align:"center",editable:true,formatter:function (data) {
                        if (data=="1"){
                            return "激活";
                        } else return "冻结";
                    },editable:true,editrules:{required:true},edittype:"select",editoptions: {value:"1:激活;2:冻结"}},
                    {name : 'photo',align:"center",editable:true},
                    {name:'name',align:"center",editable:true},
                    {name:'nick_name',align:"center",editable:true},
                    {name:'sex',align:"center",editable:true},
                    {name:'sign',align:"center",editable:true},
                    {name:'location',align:"center",editable:true},
                    /*{name:'rigest_date',align:"center",editable:true},*/
                    {name:'last_login',align:"center",editable:true}
                ],
                rowNum : 5,
                rowList : [ 5, 10, 15 ],
                pager : '#userPage',
                sortname : 'id',
                mtype : "post",
                viewrecords : true,
                sortorder : "desc",
                //caption : "用户信息",
                autoheight:true,
                autowidth: true,
                multiselect:true,
                styleUI:"Bootstrap",
                height:"400px",
                editurl: "${pageContext.request.contextPath}/user/edituser"
            });
        $("#userTable").jqGrid('navGrid', '#userPage', {search:false,edit : true,add : true,del : true,edittext:"编辑",addtext:"添加",deltext:"删除"},
            {
                closeAfterEdit: true,
            },{
                closeAfterAdd:true,
                /*afterSubmit:function (response,postData) {
                    var bannerId = response.responseJSON.bannerId;
                    $.ajaxFileUpload({
                        url:"/banner/uploadBanner",
                        type:"post",
                        datatype: "json",
                        data:{bannerId:bannerId},
                        fileElementId:"url",
                        success:function (data) {
                            $("#bannerTable").trigger("reloadGrid");
                        }
                    })
                    return postData;
                }*/
            },{
                closeAfterDel:true
            });
    })
</script>
<div class="page-header">
    <h4>用户管理</h4>
</div>
<ul class="nav nav-tabs">
    <li><a>用户信息</a></li>
</ul>
<div class="panel">
    <table id="userTable"></table>
    <div id="userPage" style="height: 50px"></div>
</div>

