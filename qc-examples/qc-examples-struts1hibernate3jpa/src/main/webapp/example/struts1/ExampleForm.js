jQuery(function() {
	$('#btnSave').click(function() {
		$.ajax( {
			type : "POST",
			dataType : "json",
			url : "../../example.do?method=save",
			data : $("form").serialize(),
			success : function(result, _status) {
				if (result.success) { // 保存成功
					if (result.id) $("#id").val(result.id); // 默认更新id的值
					if (result.msg) alert(result.msg);
				}else{
					alert(result.msg || "保存失败！");
				}
			},
			error : function(xhr, errorMsg, errorThrown) {
				alert("保存失败了:" + errorMsg + ";" + errorThrown);
			}
		});
	});
	$('#btnContinueNew').click(function() {
		window.open(contextPath + "/example.do?method=create","_self");
	});
});