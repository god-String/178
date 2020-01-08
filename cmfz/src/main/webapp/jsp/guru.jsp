<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function () {
        $("#guruTable").jqGrid(
            {
                url : '${pageContext.request.contextPath}/guru/showAllGurus',
                datatype : "json",
                // 时间格式的处理在后台进行
                colNames : [ 'ID', '名字', '图片','状态','nick_name'],
                colModel : [
                    {name : 'id',align:"center",hidden:true},
                    {name : 'name',align:"center",editable: true,editrules:{required:true}},

                    {name : 'photo',align:"center",editrules:{required:true},formatter:function (data) {
                            return "<img style='width: 80px;height: 80px' src='"+data+"'>";
                        },editable: true,edittype:"file",editoptions: {enctype:"multipart/form-data"}},

                    {name : 'status',align:"center",editable:true,formatter:function (data) {
                            if (data=="1"){
                            return "展示";
                            } else return "冻结";
                    },editable:true,editrules:{required:true},edittype:"select",editoptions: {value:"1:展示;2:冻结"}},
                    {name : 'nick_name',align:"center",editable:true},
                ],
                rowNum : 5,
                rowList : [ 5, 10, 15 ],
                pager : '#guruPage',
                sortname : 'id',
                mtype : "post",
                viewrecords : true,
                sortorder : "desc",
                caption : "上师信息",
                autowidth: true,
                multiselect:true,
                styleUI:"Bootstrap",
                height:"500px",
                editurl: "${pageContext.request.contextPath}/guru/editGuru"
            });
        $("#guruTable").jqGrid('navGrid', '#guruPage', {search:false,edit : true,add : true,del : true,edittext:"编辑",addtext:"添加",deltext:"删除"},
            {
                closeAfterEdit: true,
                afterSubmit:function (response,postData) {
                    var guruId = response.responseJSON.guruId;
                    $.ajaxFileUpload({
                        url:"${pageContext.request.contextPath}/guru/guruUpload",
                        type:"post",
                        datatype: "json",
                        data:{guruId:guruId},
                        fileElementId:"photo",
                        success:function (data) {
                            $("#guruTable").trigger("reloadGrid");
                        }
                    })
                    return postData;
                }
            },{
                closeAfterAdd:true,
                afterSubmit:function (response,postData) {
                    var guruId = response.responseJSON.guruId;
                    $.ajaxFileUpload({
                        url:"${pageContext.request.contextPath}/guru/guruUpload",
                        type:"post",
                        datatype: "json",
                        data:{guruId:guruId},
                        fileElementId:"photo",
                        success:function (data) {
                            $("#guruTable").trigger("reloadGrid");
                        }
                    })
                    return postData;
                }
            },{
                closeAfterDel:true
            });
    })
</script>
<div class="page-header">
    <h4>上师管理</h4>
</div>
<ul class="nav nav-tabs">
    <li><a>上师信息</a></li>
</ul>
<div class="panel">
    <table id="guruTable"></table>
    <div id="guruPage" style="height: 50px"></div>
</div>