<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
<head>
<title>表单</title>
<script type="text/javascript">var contextPath="${request.contextPath}";</script>
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/themes/smoothness/jquery-ui.css" />
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/jquery-ui.min.js"></script>
<script type="text/javascript" src="/example/struts1/ExampleForm.js"></script>
</head>
<body>
<div>表单：<input type="button" value="保存" id="btnSave" /><input
	type="button" id="btnContinueNew" value="继续新建" /></div>

<fieldset style="width:250px;margin:10px;">
<html:form action="example.do" styleId="qcForm">
	<div>&nbsp;</div>
	<div style="padding:4px;">名称： <html:text property="name" /></div>
	<div style="padding:4px;">编码： <html:text property="code" /></div>
	<div>&nbsp;</div>
	<div>id: <html:text property="id" styleId="id" readonly="true" style="width:80px;"/></div>
</html:form>
</fieldset>
</body>
</html>