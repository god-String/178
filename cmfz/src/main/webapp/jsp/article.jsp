<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">

    $(function () {
        $("#articleTable").jqGrid(
            {
                url: '${pageContext.request.contextPath}/article/showAllArticles',
                datatype: "json",
                editurl: "${pageContext.request.contextPath}/article/del",
                // 时间格式的处理在后台进行
                colNames: ['ID', '标题', '图片', '内容', /*'创建时间', '发布时间',*/ '状态','所属上师','操作'],
                colModel: [
                    {name: 'id', align: "center", hidden: true},
                    {name: 'title', align: "center", editable: true, editrules: {required: true}},
                    {
                        name: 'img', align: "center", formatter: function (data) {
                            return "<img style='width: 180px;height: 80px' src='" + data + "'>"
                        }, editable: true, edittype: "file", editoptions: {enctype: "multipart/form-data"}
                    },
                    {name: 'content', align: "center", editable: true},
                    /*{
                        name: 'create_date',
                        align: "center",
                        editable: true,
                        editrules: {required: true},
                        edittype: "date"
                    },*/
                    //{name: 'publish_date', align: "center", editable: true, editrules: {required: true}},
                    {
                        name: 'status',
                        align: "center",
                        formatter: function (data) {
                            if (data == "1") {
                                return "展示";
                            } else return "冻结";
                        },
                        editable: true,
                        editrules: {required: true},
                        edittype: "select",
                        editoptions: {value: "1:展示;2:冻结"}
                    },
                    {
                        name: 'guru_id',
                        align: "center",
                    },
                    {
                        name:'option',
                        formatter:function (cellvalue, options, rowObject) {
                            //var button = ;
                            //button+= "<button type=\"button\" class=\"btn btn-danger\" onclick=\"del('\"+rowObject.id+\"')\">删除</button>";

                            //button+= "<button type=\"button\" class=\"btn btn-danger\" onclick=\"del('\"+rowObject.id+\"')\">删除</button>";
                            /*onclick=\"del('"+rowObject.id+"')\"*/
                            // href="/person/delete?id=" class="btn btn-danger"
                            return  "<button type=\"button\" class=\"btn btn-primary\" onclick=\"update('"+rowObject.id+"')\">修改</button>&nbsp;&nbsp; <a class=\"btn btn-default btn-danger\" onclick=\"deleteById('"+rowObject.id+"')\">删除</a>";
                        }



                    }
                ],
                rowNum: 5,
                rowList: [5, 10, 15],
                pager: '#articlePage',
                sortname: 'id',
                mtype: "post",
                viewrecords: true,
                sortorder: "desc",
                autowidth: true,
                multiselect: true,
                styleUI: "Bootstrap",
                height: "500px",
            });

    });

    //删除
    function deleteById(id) {
        if (id != null)
            $("#articleTable").jqGrid('delGridRow', id, {
                reloadAfterSubmit: false
            });
        else {
            alert("Please Select Row to delete!");
        }};




    // 点击添加文章时触发事件
    function showArticle() {
        $("#kindForm")[0].reset();
        KindEditor.html("#editor_id","");
        $.ajax({
            url: "${pageContext.request.contextPath}/article/showAllGuru",
            datatype: "json",
            type: "post",
            success: function (data) {
                // 遍历方法 --> forEach(function(集合中的每一个对象){处理})
                // 一定将局部遍历声明在外部
                var option = "<option value=\"0\">请选择所属上师</option>";
                data.forEach(function (guru) {
                    option += "<option value=" + guru.id + ">" + guru.name + "</option>"
                })
                $("#guru_list").html(option);
                $("#articleTable").trigger("reloadGrid");
            }
        });
        $("#myModal").modal("show");
    }



    // 点击修改时触发事件
    function update(id) {
        // 使用jqGrid("getRowData",id) 目的是屏蔽使用序列化的问题
        // $("#articleTable").jqGrid("getRowData",id); 该方法表示通过Id获取当前行数据
        var data = $("#articleTable").jqGrid("getRowData",id);
        $("#id").val(data.id);
        $("#title").val(data.title);
        //$("#img").val(data.img);
        // 更替KindEditor 中的数据使用KindEditor.html("#editor_id",data.content) 做数据替换
        KindEditor.html("#editor_id",data.content)
        // 处理状态信息
        $("#status").val(data.status);
        var option = "";
        if(data.status=="展示"){
            option += "<option selected value=\"1\">展示</option>";
            option += "<option value=\"2\">冻结</option>";
        }else{
            option += "<option value=\"1\">展示</option>";
            option += "<option selected value=\"2\">冻结</option>";
        }
        $("#status").html(option);

        // 处理上师信息
        $.ajax({
            url: "${pageContext.request.contextPath}/guru/showAllGuru",
            datatype: "json",
            type: "post",
            success: function (gurulist) {
                // 遍历方法 --> forEach(function(集合中的每一个对象){处理})
                // 一定将局部遍历声明在外部
                var option2 = "<option value=\"0\">请选择所属上师</option>";
                gurulist.forEach(function (guru) {
                    if (guru.id==data.guruId){
                        option2 += "<option selected value=" + guru.id + ">" + guru.name + "</option>"
                    }
                    option2 += "<option value=" + guru.id + ">" + guru.name + "</option>"
                })
                $("#guru_list").html(option2);
            }
        });
        $("#myModal").modal("show");

    }


    // 文件添加及修改方法
    function sub() {
        $('#myModal').modal('toggle')
        $.ajaxFileUpload({
            url: "${pageContext.request.contextPath}/article/insertArticle",
            type: "post",
            // ajaxFileUpload 不支持serialize() 格式化形式
            // 只支持{"id":1,XXX:XX}
            // 解决: 1. 手动封装  2. 更改ajaxFileUpload的源码

            // 异步提交时 无法传输修改后的kindeditor内容,需要刷新
            data: {
                "id": $("#id").val(),
                "title": $("#title").val(),
                //"img":$("#img").val(),
                "content": $("#editor_id").val(),
                "status": $("#status").val(),
                "guru_id": $("#guru_list").val()
            },
            datatype: "json",
            fileElementId: "inputfile",
            success: function (data) {
                $("#articleTable").trigger("reloadGrid");
            }
        })
    }
</script>
<div class="page-header">
    <h4>文章管理</h4>
</div>
<ul class="nav nav-tabs">
    <li><a>文章信息</a></li>
    <li><a onclick="showArticle()">添加文章</a></li>
</ul>
<div class="panel">
    <table id="articleTable"></table>
    <div id="articlePage" style="height: 50px"></div>
</div>

