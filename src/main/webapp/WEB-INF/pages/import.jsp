<%--
  Created by IntelliJ IDEA.
  User: tangyilong
  Date: 2018/4/10
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload File</title>
</head>
<body>
<form id="file_form" action="${path}"
      method="post" enctype="multipart/form-data" target="frameFile"
      onsubmit="return check();">
    <input name="file" id="file_input" onchange="change()"
           style="width:260px;   height:30px;" type="file" value="选择文件" multiple/>
    <input id="input" style="width:260pt;height:30pt;"/>
    <input type="submit" value="上传"/>
</form>
<%--无刷新提交表单--%>
<iframe id="frameFile" name="frameFile" style="display:none;"></iframe>
<script type="text/javascript">
    function check() {
        var obj = document.getElementById('file_input');
        var length = obj.files.length;
        if (length === 0) {
            alert('请选择文件');
            return false;
        } else {
            for (var i = 0; i < length; i++) {
                var fileName = obj.files[i].name;
                var fileType = (fileName.substring(fileName
                    .lastIndexOf(".") + 1, fileName.length))
                    .toLowerCase();
                if (fileType !== 'xls' && fileType !== 'xlsx') {
                    alert('文件格式不正确，请上传excel文件！');
                    return false;
                }
            }
        }
    }

    function change() {
        var obj = document.getElementById('file_input');
        var length = obj.files.length;
        var fileNames = '';
        for (var i = 0; i < length; i++) {
            var temp = obj.files[i].name;
            fileNames += temp + ';';
        }
        fileNames = fileNames.substr(0, fileNames.lastIndexOf(';'));
        document.getElementById('input').value = fileNames;
    }
</script>
</body>
</html>
