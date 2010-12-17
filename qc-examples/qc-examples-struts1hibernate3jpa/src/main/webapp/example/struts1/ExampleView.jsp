<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html>
<head>
<title><bean:message key="example.title.view"/></title>
<script type="text/javascript">var contextPath="${request.contextPath}";</script>
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/themes/smoothness/jquery-ui.css" />
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/jquery-ui.min.js"></script>
<script type="text/javascript" src="/example/struts1/ExampleView.js"></script>
<style type="text/css">
.rborder {
	border-right: 1px solid #888;
}

.bborder {
	border-bottom: 1px solid #888;
}

.w {
	width: 40px;
}
</style>
</head>
<body>
<div><bean:message key='example.title.view'/>：<input type="button" id="btnRefresh" value="<bean:message key='button.refresh'/>" /><input
	type="button" id="btnNew" value="<bean:message key='button.new'/>" /></div>
<table style="width: 600px; border: 1px solid black; margin: 4px;"
	cellpadding="4" cellspacing="0">
	<tr style="background-color: #ccc;">
		<td style="width: 40px" class="rborder bborder">id</td>
		<td style="width: 200px" class="rborder bborder">name</td>
		<td class="rborder bborder">code</td>
		<td style="width: 120px; text-align: center;" class="bborder">操作</td>
	</tr>
	<logic:iterate id="example" name="page" property="list"
		type="qc.test.Example">
		<tr>
			<td class="rborder bborder"><bean:write name="example"
				property="id" format="#" /></td>
			<td class="rborder bborder"><bean:write name="example"
				property="name" /></td>
			<td class="rborder bborder"><bean:write name="example"
				property="code" /></td>
			<td style="text-align: center;" class="bborder"><input
				type="button" name="btnDelete" value="<bean:message key='button.delete'/>" /><input type="button"
				name="btnEdit" value="<bean:message key='button.modify'/>" /></td>
		</tr>
	</logic:iterate>
</table>
<form method="post">
<div>页码： <input class="w" type="text" name="pageNo"
	value="${page.pageNo}" readonly="readonly" /> 页数： <input class="w" type="text" id="pageCount"
	value="${page.pageCount}" readonly="readonly" /> 总数： <input
	class="w" type="text" id="totalCount" value="${page.totalCount}"
	readonly="readonly" /> 每页限制： <input class="w" type="text"
	name="pageSize" value="${page.pageSize}" /> <input type="button"
	id="btnPrevPage" value="上一页" /> <input type="button" id="btnNextPage"
	value="下一页" /></div>
</form>
</body>
</html>