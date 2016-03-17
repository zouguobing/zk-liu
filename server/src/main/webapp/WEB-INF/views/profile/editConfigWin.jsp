<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div id="editConfigWin" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">参数配置</h3>
    </div>
    <div class="modal-body">
        <form id="configForm" class="form-horizontal" action="/config/save/1" method="post">
            <div class="form-group">
                <label class="col-sm-2 control-label">模块：</label>
                <div class="col-sm-9">
                    <select class="form-control" id="config-moduleId-show" disabled="true">
                        <c:forEach items="${modules}" var="module">
                            <option value='<c:out value="${module}"/>'><c:out value="${module}"/></option>
                        </c:forEach>
                    </select>
                </div>
                </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Key：</label>
                <div class="col-sm-9">
                    <input type="hidden" name="moduleName" value='<c:out value="${moduleName}"/>'/>
                    <input type="hidden" name="projectCode" value='<c:out value="${project.code}"/>'/>
                    <input type="hidden" name="profile" value='<c:out value="${profile}"/>'/>
                    <input type="hidden" name="queryModule" value='<c:out value="${moduleName}"/>'/>
                    <input type="hidden" name="queryKey" value='<c:out value="${queryKey}"/>'/>
                    <input type="text" name="key" class="form-control" id="config-configKey" readonly="true">
                </div>
                </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Value：</label>
                <div class="col-sm-9">
                    <textarea rows="3" name="value" class="form-control" id="config-configValue"></textarea>
                </div>
                </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">描述：</label>
                <div class="col-sm-9">
                    <textarea rows="2" class="form-control" name="memo" id="config-configDesc"></textarea>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        <button class="btn btn-primary" id="saveConfig">保存</button>
    </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function updateConfig(id) {
        var tds = $("#row-" + id + " > td");
        $("#config-moduleId-show").val($(tds.get(0)).attr("value"));
        $("#config-configKey").val($(tds.get(1)).attr("value"));
        $("#config-configValue").val($(tds.eq(2).find("a")).attr("data-content"));
        $("#config-configDesc").val($(tds.eq(3).find("a")).attr("data-content"));
        $('#editConfigWin').modal('toggle');
    }
    $(function(){
        $("#saveConfig").click(function(e) {
            if ($("#config-configKey").val().trim() == '' || $("#config-configValue").val().trim() == '') {
                bootbox.alert("Key 或者 Value 不能为空!");
                return;
            }
            if (!/^\w+$/.test($("#config-configKey").val())) {
                bootbox.alert("配置key只能由字母数字下划线组成!");
                return
            }
            var profile = '<c:out value="${profile}"/>';
            if (profile == "production") {
                bootbox.confirm({
                    message: "请确认是否修改正式环境配置项?",
                    callback: function (result) {
                        if (result) {
                            $("#configForm")[0].submit();
                        }
                    }
                });
            } else {
                $("#configForm")[0].submit();
            }
        });
    })
</script>