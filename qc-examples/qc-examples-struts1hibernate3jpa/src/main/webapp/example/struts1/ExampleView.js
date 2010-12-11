jQuery(function() {
	//刷新按钮
	$("#btnRefresh").click(refresh);
	
	//新建按钮
	$("#btnNew").click(function(){
		window.open(contextPath + "/example.do?method=create");
	});
	
	//修改按钮
	$("input[name='btnEdit']").click(function() {
		var id = $(this).parent().siblings(":first").text();
		window.open(contextPath + "/example.do?method=edit&id=" + id);
	});
	
	//删除按钮
	$("input[name='btnDelete']").click(function() {
		var id = $(this).parent().siblings(":first").text();
		$.ajax( {
			type : "POST",
			dataType : "json",
			url : contextPath + "/example.do?method=delete",
			data : {id:id},
			success : function(result, _status) {
				if (result.success) {
					if (result.msg){
						alert(result.msg);
					}
					refresh();
				}else{
					alert(result.msg || "删除失败！");
				}
			},
			error : function(xhr, errorMsg, errorThrown) {
				alert("删除失败了:" + errorMsg + ";" + errorThrown);
			}
		});
	});
	
	var $pageNo = $("input[name='pageNo']");
	var $pageSize = $("input[name='pageSize']");
	var $totalCount = $("#totalCount");
	var $pageCount = $("#pageCount");
	
	//上一页按钮
	$("#btnPrevPage").click(function(){
		$pageNo.val(parseInt($pageNo.val()) - 1);
		$("form").attr("action",contextPath + "/example.do?method=view").submit();
	});
	
	//下一页按钮
	$("#btnNextPage").click(function(){
		$pageNo.val(parseInt($pageNo.val()) + 1);
		$("form").attr("action",contextPath + "/example.do?method=view").submit();
	});
	
	var pageNo = parseInt($pageNo.val());
	var pageCount = parseInt($pageCount.val());
	if(pageNo >= pageCount){
		$("#btnNextPage").attr("disabled",true);
	}
	if(pageNo == 1){
		$("#btnPrevPage").attr("disabled",true);
	}
	
	//自定义的函数
	function refresh(){
		window.location.reload();
	}
});