<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function () {
        $("#listsg11").jqGrid(
            {
                url :'${pageContext.request.contextPath}/album/showAllAlbums',
                styleUI:"Bootstrap",
                datatype : "json",
                height : "400px",
                //auto:null,
                //autoheight:true,
                autowidth: true,
                //width:  "1300px",
                editurl: "${pageContext.request.contextPath}/album/editAlbum",
                colNames : [ '编号', '标题','评分','作者','播音员','集数','简介','状态','创建时间','封面'],
                colModel : [
                    {name : 'id',align : "center"},
                    {name : 'title',align : "center",editable:true},
                    {name : 'score',align : "center",editable:true},
                    {name : 'author',align : "center",editable:true},
                    {name : 'broadcast',align : "center",editable:true},
                    {name : 'count',align : "center",editable:true},
                    {name : 'desc',align : "center",editable:true},
                    {name : 'status',align : "center",editable:true,
                        formatter:function (data) {
                            if (data==1){
                                return "展示";
                            } else return "冻结";
                    },edittype:"select",editoptions: {value:"1:展示;2:冻结"}
                    },
                    {name : 'create_date',align : "center",editable:true,edittype: "date"},
                    {name : 'cover',editable:true,
                        formatter:function (data) {
                            return "<img src='"+data+"' style='width: 80px;height: 80px'>"
                        },edittype:"file",editoptions:{enctype:"multipart/form-data"}

                    }
                ],
                rowNum : 4,
                rowList : [ 4, 8, 10],
                pager : '#pagersg11',
                sortname : 'id',
                viewrecords : true,
                sortorder : "desc",
                multiselect : true,align:"center",
                subGrid : true,
                caption : "专辑信息",
                subGridRowExpanded : function(subgrid_id, row_id) {

                    addSubgrid(subgrid_id,row_id);

                },
                subGridRowColapsed : function(subgrid_id, row_id) {
                    // this function is called before removing the data
                    //var subgrid_table_id;
                    //subgrid_table_id = subgrid_id+"_t";
                    //jQuery("#"+subgrid_table_id).remove();

                }
            });

        $("#listsg11").jqGrid('navGrid', '#pagersg11',{search:false,edit : true,add : true,del : true,edittext:"编辑",addtext:"添加",deltext:"删除"},
            /*add : true,
            edit : true,
            del : true,*/
        {
            closeAfterEdit: true,
            afterSubmit:function (response,postData) {
                var albumId =response.responseJSON.albumId;
                $.ajaxFileUpload({
                    url:"${pageContext.request.contextPath}/album/uploadAlbum",
                    datatype:"json",
                    type:"post",
                    data:{albumId:albumId},
                    fileElementId:"cover",
                    success:function (data) {
                        $("#listsg11").trigger("reloadGrid");
                    }
                });
                return postData;
            }
        },
        {
            closeAfterAdd:true,
            afterSubmit:function (response,postData) {
               var albumId =response.responseJSON.albumId;
                $.ajaxFileUpload({
                   url:"${pageContext.request.contextPath}/album/uploadAlbum",
                   datatype:"json",
                   type:"post",
                   data:{albumId:albumId},
                   fileElementId:"cover",
                    success:function (data) {
                            $("#listsg11").trigger("reloadGrid");
                    }
                });
                return postData;
            }

        },
        {
            closeAfterDel:true,
        });
    });

    // subgrid_id: 行id  row_id: 数据id
    function addSubgrid(subgrid_id,row_id){



        var subgrid_table_id, pager_id;
        subgrid_table_id = subgrid_id + "_t";
        pager_id = "p_" + subgrid_table_id;
        $("#" + subgrid_id).html(
            "<table id='" + subgrid_table_id
            + "' class='scroll'></table><div id='"
            + pager_id + "' class='scroll'></div>");
        $("#" + subgrid_table_id).jqGrid(
            {
                url :"${pageContext.request.contextPath}/chapter/showAllChapters?albumId="+ row_id,
                styleUI:"Bootstrap",
                autowidth: true,
                //width:  "400px",
                viewrecords : true,
                multiselect:true,
                editurl:"${pageContext.request.contextPath}/chapter/editChapter?albumId="+row_id,
                datatype : "json",
                colNames : [ '编号', '名字', '音频路径', '音频大小','时长','创建时间','操作'],
                colModel : [
                    {name : "id",align : "center"},
                    {name : "title",align : "center",editable:true},
                    {name : "url",align : "center",editable:true,edittype:'file',
                        formatter:function (data) {
                            //var button = "<button type=\"button\" class=\"btn btn-primary\" onclick=\"download('"+data+"')\">下载</button>&nbsp;&nbsp;";
                            return "<audio controls loop preload='auto'>"+"<source  src='"+data+"' style='width: 80px;height: 80px'>"+"</audio>";
                            // return button;
                        },edittype:"file",editoptions:{enctype:"multipart/form-data"}
                    },
                    {name : "size",align : "center"},
                    {name : "time",align : "center"},
                    {name : "create_time",align : "center",editable:true,edittype:"date"},
                    {name : "url",
                        formatter:function (data) {
                            //var ss= location.href = "${pageContext.request.contextPath}/chapter/downloadChapter?url="+data
                            var button = "<button type=\"button\" class=\"btn btn-primary\" onclick=\"download('"+data+"')\">下载</button>&nbsp;&nbsp;";
                            return button;

                            /*function download(data) {
                                location.href = "/chapter/downloadChapter?url="+data;
                            }*/
                        },editoptions:{enctype:"multipart/form-data"}
                    },
                ],

                rowNum : 5,
                rowList : [ 5, 10, 15,20,30],
                pager : pager_id,
                sortname : 'num',
                sortorder : "asc",
                height : '400px'
            });
        $("#" + subgrid_table_id).jqGrid('navGrid',
            "#" + pager_id, {search:false,edit : true,add : true,del : true,edittext:"编辑",addtext:"添加",deltext:"删除"},
            {
                closeAfterEdit: true,
                afterSubmit:function (response,postData) {
                    var chapterId =response.responseJSON.chapterId;
                    $.ajaxFileUpload({
                        url:"${pageContext.request.contextPath}/chapter/uploadChapter",
                        datatype:"json",
                        type:"post",
                        data:{chapterId:chapterId},
                        fileElementId:"url",
                        success:function (data) {
                            $("#listsg11").trigger("reloadGrid");
                        }
                    });
                    return postData;
                },
            },
            {
                closeAfterAdd:true,
                afterSubmit:function (response,postData) {
                    var chapterId =response.responseJSON.chapterId;
                    $.ajaxFileUpload({
                        url:"${pageContext.request.contextPath}/chapter/uploadChapter",
                        datatype:"json",
                        type:"post",
                        data:{chapterId:chapterId},
                        fileElementId:"url",
                        success:function (data) {
                            $("#listsg11").trigger("reloadGrid");
                        }
                    });
                    return postData;
                },
            });

    }

    function onPlay(data) {
        $("#music").attr("src",data);
        $("#myModal").modal("show");
    }
    function download(data) {
        location.href = "${pageContext.request.contextPath}/chapter/downloadChapter?url="+data;
    }
</script>

<div class="page-header">
    <h4>专辑管理</h4>
</div>
<%--<ul class="nav nav-tabs">
    <li><a>专辑信息</a></li>
</ul>--%>
<div class="panel">
<table id="listsg11"></table>
<div id="pagersg11" style="height: 50px"></div>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <audio id="music" src="" controls="controls">
        </audio>
    </div><!-- /.modal -->
</div>

