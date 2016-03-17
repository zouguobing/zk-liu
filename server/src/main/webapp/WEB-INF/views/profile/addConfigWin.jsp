<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div id="addConfigWin" class="modal fade" tabindex="-1" data-dismiss='modal' role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>参数配置</h3>
    </div>
    <div class="modal-body">
        <form id="addConfigForm" class="form-horizontal" action="/config/save/0" method="post">
            <div class="form-group">
                <label class="col-sm-2 control-label">模块：</label>
                <div class="col-sm-9">
                    <select class="form-control" id="aModuleName" name="moduleName">
                        <option value="">请选择....</option>
                        <c:forEach items="${modules}" var="module">
                            <option value='${module}' ${module eq moduleName ? 'selected':''}>
                             ${module}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Key：</label>
                <div class="col-sm-9">
                    <input type="hidden" name="projectCode" value='<c:out value="${project.code}"/>'/>
                    <input type="hidden" name="profile" value='<c:out value="${profile}"/>'/>
                    <input type="hidden" name="queryModule" value='<c:out value="${moduleName}"/>'/>
                    <input type="hidden" name="queryKey" value='<c:out value="${queryKey}"/>'/>
                    <input type="text" id="configKey" name="key" class="form-control">
                </div>
                </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Value：</label>
                <div class="col-sm-9">
                    <textarea rows="3" id="configValue" name="value" class="form-control"></textarea>
                </div>
                </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">描述：</label>
                <div class="col-sm-9">
                    <textarea rows="2" class="form-control" name="memo"></textarea>
                </div>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        <button class="btn btn-primary" id="saveAddConfig">保存</button>
    </div>
            </div>
        </div>
</div>
<script type="text/javascript">
    $(function(){
        $("#saveAddConfig").click(function(e) {
            if($("#aModuleName").val() == '') {
                bootbox.alert("请选择模块!");
                return;
            }
            if($("#configKey").val().trim() == '' || $("#configValue").val().trim() == ''){
                bootbox.alert("Key 或者 Value 不能为空!");
                return;
            }
            if(!/^\w+$/.test($("#configKey").val())) {
                bootbox.alert("配置key只能由字母数字下划线组成!");
                return
            }
            $("#addConfigForm")[0].submit();
        });
    })
</script>