
function getUrlVars()  
{  
        var vars = [], hash;  
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');  
        for(var i = 0; i < hashes.length; i++)  
        {  
            hash = hashes[i].split('=');  
            vars.push(hash[0]);  
            vars[hash[0]] = hash[1];  
        }  
        return vars;  
} 
	
function getloginfo()
{
	//获取登录者信息
	$.getJSON('/hellojxc/loginfo',function(result){
            //alert("log info " + result.data[0].name_en);
			$("#account_simple").text(result.data[0].name_en);
	        $("#account_full").text(result.data[0].name_en + "--" + result.data[0].name_ch);
			$("#last_login").text("上次登录时间:" + result.data[0].last_login);			
			$("#account_en").text(result.data[0].name_en);
			$("#account_ch").text(result.data[0].name_ch);
			userType = result.data[0].type;
	}); 
}

function getMatchedIndex(targetarray,item)
{
	for( i = 0 ; i < targetarray.length; i++)
	{
		if(item === targetarray[i])
			return i;
	}
	return -1;
}

//要完善接口
function listcommonoperation(type,table)
{	
	var delaction;
	var showaction;
	if(type === 'target')
	{
		delaction = '/hellojxc/deltarget';
		showaction = 'index.html?function=showtarget&&id=';
	}else if(type === 'customer')
	{
		delaction = '/hellojxc/delcustomer';
		showaction = 'index.html?function=showcustomer&&id=';		
	}else if(type === 'pay')
	{
		delaction = '/hellojxc/delpay';
		showaction = 'index.html?function=showpay&&id=';		
	}
    else if(type === 'dailyinput')
	{
		delaction = '/hellojxc/deldailyinput';
		showaction = 'index.html?function=showdailyinput&&id=';		
	}else if(type === 'dailyoutput')
	{
		delaction = '/hellojxc/deldailyoutput';
		showaction = 'index.html?function=showdailyoutput&&id=';		
	}else if(type === 'salesinput')
	{
		delaction = '/hellojxc/delsalesinput';
		showaction = 'index.html?function=showsalesinput&&id=';		
	}
	
	$("#del").on('click', function(){
			$("#frm-example").attr("action",delaction);
			$("#frm-example").attr("method","POST");
			$("#frm-example").submit();
	});

	$("#update").on('click', function(){
		//只获取第一个被选取的，如果没有，则出提示信息
		var isok = 0;
		//var form = this;
		var getparam=showaction;
		// Iterate over all checkboxes in the table
		table.$('input[type="checkbox"]').each(function()
		{
		// If checkbox doesn't exist in DOM
		//if(!$.contains(document, this)){ //该判断总是无效，所以去除；原因不明
			// If checkbox is checked
			if(this.checked){
				// Create a hidden element
				getparam+=this.value;
				//alert('URL is' + getparam);
				isok=1;
				return false;// return false 相当于 break;只要获取第一个即可； return true; = continue;
			}
		//}
		});
		if(isok == 0)
{
			alert("请选取需要更新的记录");
			return false;
		}
		window.location.href=getparam;
		//window.location.replace(getparam);
		event.preventDefault();
		//return false;//这里必须返回false，否则会触发submit事件；原因不明
		//$(location).attr('href', getparam);
	});

	// Handle click on "Select all" control
	$('#example-select-all').on('click', function(){
	  // Get all rows with search applied
	  var rows = table.rows({ 'search': 'applied' }).nodes();
	  // Check/uncheck checkboxes for all rows in the table
	  $('input[type="checkbox"]', rows).prop('checked', this.checked);
	});

	// Handle click on checkbox to set state of "Select all" control
	$('#example tbody').on('change', 'input[type="checkbox"]', function(){
	  // If checkbox is not checked
	  if(!this.checked){
		 var el = $('#example-select-all').get(0);
		 // If "Select all" control is checked and has 'indeterminate' property
		 if(el && el.checked && ('indeterminate' in el)){
			// Set visual state of "Select all" control
			// as 'indeterminate'
			el.indeterminate = true;
		 }
	  }
	});

	// Handle form submission event
	$('#frm-example').on('submit', function(e){
		//alert("test");
		var form = this;
		var anyselected = 0;
		// Iterate over all checkboxes in the table
		table.$('input[type="checkbox"]').each(function(){
		   // If checkbox doesn't exist in DOM
		  // if(!$.contains(document, this)){
			  // If checkbox is checked
			  if(this.checked){
				 // Create a hidden element
				 anyselected = 1;
				 $(form).append(
					$('<input>')
					   .attr('type', 'hidden')
					   .attr('name', this.name)
					   .val(this.value)
				 );
			  }
		  // }
		});
		
		if(anyselected == 0)
		{
			alert("请选取需要删除的记录");
			event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
			return false;//返回false，取消submit
		}else
		{
			var mymessage=confirm("你确定要删除所选记录吗？");
			event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
			if(mymessage==false)
			{				
				return false;//返回false，取消submit
			}else
			{
				return true;
			}
		}
	});		
}

function listtarget()
{
	var prepareaction= 'preparetarget';
	var listaction='/hellojxc/listtarget';
	
	$("section.content-header").html(
            '<h1>' +
            '对象管理' +
            ' <small>对象一览</small>' +
             '</h1>'
    ); 
	
	var targetval='<form id="frm-example">';
	
	if(userType === "0")
	{
            //只有admin用户有增删减权限
            targetval += 	'<a class="btn btn-primary" id="add" href="index.html?function=' + prepareaction + '" role="button">添加</a> ' + "\n" + 
						' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
						' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
						'<br>' + 
						'<br>';
	}
	targetval +='<table id="example" class="display select" width="100%" cellspacing="0">' + 
					' <thead>' + 
					'   <tr>' + 
					'     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
					'     <th>ID</th>' + 
					'     <th>名称</th>' + 
					'     <th>单位</th>' + 
					'     <th>等级</th>' + 					
					'     <th>类型</th>' + 
					'   </tr>' + 
					'</thead>' + 
					'<tfoot>' + 
					'   <tr>' + 
					'     <th></th>' + 
					'     <th>ID</th>' + 		
					'     <th>名称</th>' + 
					'     <th>单位</th>' + 
					'     <th>等级</th>' + 							
					'     <th>类型</th>' + 
					'   </tr>' + 
					'</tfoot>' + 
				'</table>';
	targetval += '</form>';

	$("#target").html(targetval);

    var table = $('#example').DataTable({
	   searching: false,
      'ajax': {
         'url': listaction
      },
      'columnDefs': [
            {
                'targets': 0,
                'searchable': false,
                'orderable': false,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
                    return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                }
            }
            ,{
                'targets': 3,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
					var type = Number.parseInt(full[5]);
					var a = type&1;
                    if(a != 1)//非销售收入对象
						return '-';  
				   else
					   return data;
                }
            }
            ,{
                'targets': 4,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
					var type = Number.parseInt(full[5]);
					var a = type&1;
                    if(a != 1)//非销售收入对象
						return '-';  
				   else
					   return data;
                }
            }			
            ,{
                'targets': 5,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
					var newdata = Number.parseInt(data);
					var rtn="";
					var a = newdata&1;
					var b = newdata&2;
					var c = newdata&4;
					if(a === 1)
						rtn = "|销入";
					if(b === 2)
						rtn += "|日入";
					if(c === 4)
						rtn += "|日出";	
					return rtn;
                }
             }
        ],
      'order': [[1, 'asc']]
    });
    
    listcommonoperation('target',table);   
}


function preparetarget()
{
    $("section.content-header").html(
            '<h1>' +
            '对象管理' +
            ' <small>对象添加</small>' +
             '</h1>'
    ); 
		
	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/addtarget" method="POST">'+
		'<input type="hidden" id="allunits" name="allunits">' +
		'<input type="hidden" id="allgrades" name="allgrades">' +
		'<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input name="name" type="text" class="form-control" style="width:30%" maxlength="20" required>' +
		'</div>' +
		'<br>' +                
        '<div class="input-group">' +	
			'<span class="input-group-addon">对象类型</span>' +
			'<select name="types" class="selectpicker" multiple data-style="btn-info" ></select>' +
         '</div>' +  
		'<br>' +
		'<div id="InputUnitsWrapper"> '+
			'<button  id="AddMoreUnitBox" role="button" class="btn btn-info">+</button>' + 
			'<div class="input-group">' + 
				'<span class="input-group-addon">单位</span>' +
				'<input id="unit_1" name="unit_1" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
			'</div>' +
		'</div>' +
		'<br>' + 
		'<div id="InputGradesWrapper"> '+
			'<button  id="AddMoreGradeBox" role="button" class="btn btn-info">+</button>' + 
			'<div class="input-group">' + 
				'<span class="input-group-addon">级别</span>' +
				'<input id="grade_1" name="grade_1" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
			'</div>' +
		'</div>' +
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');
	var MaxInputs = 8;
	//动态增加[单位]输入
	var InputUnitsWrapper  = $("#InputUnitsWrapper"); //Input boxes wrapper ID
	var AddMoreUnitBox    = $("#AddMoreUnitBox"); //Add button ID
	var x = InputUnitsWrapper.length; //initlal text box count			
	$(AddMoreUnitBox).click(function (e) //on add input button click
	{
		if(x <= MaxInputs) //max input box allowed
		{
		  x++; //text box increment
		  //add input box
		  $(InputUnitsWrapper).append(			
				'<div class="input-group">' + 
				'<span class="input-group-addon">单位</span>' +
				'<input id="unit_' + x + '" name="unit_' + x + '" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
				'<a href="#" id="removeclass" rel="external nofollow" class="removeclass"><button role="button" class="btn btn-primary">删除</button></a>'+
				'</div>');
		}
		return false;
	});

	//响应[删除]按钮
	$("body").on("click","#removeclass", function(e){ //user click on remove text
		if( x > 1 ) {
			$(this).parent('div').remove(); //remove text box
			x--; //decrement textbox
		}
		return false;
	});

	//动态增加[级别]输入
	var InputGradesWrapper  = $("#InputGradesWrapper"); //Input boxes wrapper ID
	var AddMoreGradeBox    = $("#AddMoreGradeBox"); //Add button ID
	var y = InputGradesWrapper.length; //initlal text box count			
	$(AddMoreGradeBox).click(function (e) //on add input button click
	{
		if(y <= MaxInputs) //max input box allowed
		{
		  y++; //text box increment
		  //add input box
		  $(InputGradesWrapper).append(			
				'<div class="input-group">' + 
				'<span class="input-group-addon">级别</span>' +
				'<input id="grade_' + y + '" name="grade_' + y + '" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
				'<a href="#" id="removegradeclass" rel="external nofollow" class="removegradeclass"><button role="button" class="btn btn-primary">删除</button></a>'+
				'</div>');
		}
		return false;
	});


	//响应[删除]按钮
	$("body").on("click","#removegradeclass", function(e){ //user click on remove text
		if( y > 1 ) {
			$(this).parent('div').remove(); //remove text box
			y--; //decrement textbox
		}
		return false;
	});

	//提交时合并所有单位值到hidden控件
	$('#frm-target').on('submit', function(e){
		var selectedtypes = $('.selectpicker').val();
		if(selectedtypes.length === 0)
		{
			alert("请至少选项一项对象类型");
			event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
			return false;//返回false，取消submit
		}
		if($.inArray("1",selectedtypes) != -1)//判断是否选择了销售对象
		{
			//统计所有"单位"
			var sum_unit='';
			var index=0;		
			$("input[id^='unit']").each(function(){
			
				if($(this).val()!=""){
					if(index !=0)
						sum_unit+='-';
					sum_unit += $(this).val();
					index++;
				}
			});
			$('#allunits').val(sum_unit);
			
			//统计所有"规格"
			var sum_grade='';
			index=0;		
			$("input[id^='grade']").each(function(){
			
				if($(this).val()!=""){
					if(index !=0)
						sum_grade+='-';
					sum_grade += $(this).val();
					index++;
				}
			});
		}		
		
		$('#allgrades').val(sum_grade);
   });
        
    $('.selectpicker').selectpicker();
	$('.selectpicker').append("<option value=\"1\" selected=\"selected\" >销售收入对象</option>");
	$('.selectpicker').append("<option value=\"2\">日常收入对象</option>");
	$('.selectpicker').append("<option value=\"4\">日常支出对象</option>");
	
	$('.selectpicker').selectpicker('refresh');
    $('.selectpicker').selectpicker('render');
	
	$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
		var selected = $(e.currentTarget).val();
		if($.inArray("1",selected) === -1)//判断是否选择了销售对象
		{
			$("input[id^='unit']").attr("disabled","true");
			$("input[id^='grade']").attr("disabled","true");
		}else
		{
			$("input[id^='unit']").removeAttr("disabled");
			$("input[id^='grade']").removeAttr("disabled");
		}
	});

}

function showtarget(id)
{
    $("section.content-header").html(
            '<h1>' +
            '对象管理' +
            ' <small>对象更新</small>' +
             '</h1>'
    ); 


    	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/updatetarget" method="POST">'+
		'<input type="hidden" id="allunits" name="allunits">' +
		'<input type="hidden" id="allgrades" name="allgrades">'+		
        '<div class="input-group">' + 
			'<span class="input-group-addon">ID</span>' +
			'<input id="id" name="id" readonly type="text" class="form-control" style="width:10%">' +
		'</div>' +	
		'<br>' +	
        '<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input id="name" name="name" type="text" class="form-control" style="width:30%" maxlength="20" required>' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +	
			'<span class="input-group-addon">对象类型</span>' +
			'<select id="slk" name="types" class="selectpicker" multiple data-style="btn-info" ></select>' +
                 '</div>' +                   
		'<br>' +
		'<div id="InputUnitsWrapper"> '+
			'<button  id="AddMoreUnitBox" role="button" class="btn btn-info">+</button>' + 
			'<div class="input-group">' + 
				'<span class="input-group-addon">单位</span>' +
				'<input id="unit_1" name="unit_1" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
			'</div>' +
		'</div>' +
		'<br>' + 
		'<div id="InputGradesWrapper"> '+
			'<button  id="AddMoreGradeBox" role="button" class="btn btn-info">+</button>' + 
			'<div class="input-group">' + 
				'<span class="input-group-addon">级别</span>' +
				'<input id="grade_1" name="grade_1" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
			'</div>' +
		'</div>' +
		'<br>' +		
		'<button type="submit" class="btn btn-default">提交</button>');  
	var MaxInputs = 8;
	//动态增加[单位]输入
	var InputUnitsWrapper  = $("#InputUnitsWrapper"); //Input boxes wrapper ID
	var AddMoreUnitBox    = $("#AddMoreUnitBox"); //Add button ID
	var x = InputUnitsWrapper.length; //initlal text box count			
	$(AddMoreUnitBox).click(function (e) //on add input button click
	{
		if(x <= MaxInputs) //max input box allowed
		{
		  x++; //text box increment
		  //add input box
		  $(InputUnitsWrapper).append(			
				'<div class="input-group">' + 
				'<span class="input-group-addon">单位</span>' +
				'<input id="unit_' + x + '" name="unit_' + x + '" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
				'<a href="#" id="removeclass" rel="external nofollow" class="removeclass"><button role="button" class="btn btn-primary">删除</button></a>'+
				'</div>');
		}
		return false;
	});

	//响应[删除]按钮
	$("body").on("click","#removeclass", function(e){ //user click on remove text
		if( x > 1 ) {
			$(this).parent('div').remove(); //remove text box
			x--; //decrement textbox
		}
		return false;
	});
	//动态增加[级别]输入
	var InputGradesWrapper  = $("#InputGradesWrapper"); //Input boxes wrapper ID
	var AddMoreGradeBox    = $("#AddMoreGradeBox"); //Add button ID
	var y = InputGradesWrapper.length; //initlal text box count			
	$(AddMoreGradeBox).click(function (e) //on add input button click
	{
		if(y <= MaxInputs) //max input box allowed
		{
		  y++; //text box increment
		  //add input box
		  $(InputGradesWrapper).append(			
				'<div class="input-group">' + 
				'<span class="input-group-addon">级别</span>' +
				'<input id="grade_' + y + '" name="grade_' + y + '" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
				'<a href="#" id="removegradeclass" rel="external nofollow" class="removegradeclass"><button role="button" class="btn btn-primary">删除</button></a>'+
				'</div>');
		}
		return false;
	});


	//响应[删除]按钮
	$("body").on("click","#removegradeclass", function(e){ //user click on remove text
		if( y > 1 ) {
			$(this).parent('div').remove(); //remove text box
			y--; //decrement textbox
		}
		return false;
	});

	//提交时合并所有单位值到hidden控件
	$('#frm-target').on('submit', function(e){
		var selectedtypes = $('.selectpicker').val();
		if(selectedtypes.length === 0)
		{
			alert("请至少选项一项对象类型");
			event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
			return false;//返回false，取消submit
		}
		
		if($.inArray("1",selectedtypes) != -1)//判断是否选择了销售对象
		{
			//统计所有"单位"
			var sum_unit='';
			var index=0;		
			$("input[id^='unit']").each(function(){
			
				if($(this).val()!=""){
					if(index !=0)
						sum_unit+='-';
					sum_unit += $(this).val();
					index++;
				}
			});
			$('#allunits').val(sum_unit);
			
			//统计所有"规格"
			var sum_grade='';
			index=0;		
			$("input[id^='grade']").each(function(){
			
				if($(this).val()!=""){
					if(index !=0)
						sum_grade+='-';
					sum_grade += $(this).val();
					index++;
				}
			});		
			
			$('#allgrades').val(sum_grade);
		}
   });
 	
    var geturl = "/hellojxc/gettarget?id=" + id;
        $.getJSON(geturl,function(result){
            $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
                //alert(field[0].name);//id,name,mobile,location,type
                $("#id").val(field[0].id);
                $("#name").val(field[0].name);
				//$("#units").val(field[0].units);
				//$("#grades").val(field[0].grades);
				var units = field[0].units.split('-');
				for(var i = 0 ; i < units.length; i++)
				{
					if(i === 0)
					{
						$("#unit_1").val(units[i]);
					}else if(i != 0 && x <= MaxInputs)
					{
						x++; //text box increment
						//add input box
						var newunit = 'unit_' + x;
						$(InputUnitsWrapper).append(			
								'<div class="input-group">' + 
								'<span class="input-group-addon">单位</span>' +
								'<input id="'+ newunit + '" name="' + newunit + '" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
								'<a href="#" id="removeclass" rel="external nofollow" class="removeclass"><button role="button" class="btn btn-primary">删除</button></a>'+
								'</div>');
						$('#' + newunit).val(units[i]);
					}
				}
				
				var grades = field[0].grades.split('-');
				for(var i = 0 ; i < grades.length; i++)
				{
					if(i === 0)
					{
						$("#grade_1").val(grades[i]);
					}else if(i != 0 && y <= MaxInputs)
					{
						y++; //text box increment
						//add input box
						var newunit = 'grade_' + y;
						$(InputGradesWrapper).append(			
								'<div class="input-group">' + 
								'<span class="input-group-addon">级别</span>' +
								'<input id="'+ newunit + '" name="' + newunit + '" type="text" class="form-control" style="width:10%" maxlength="200" required>' +
								'<a href="#" id="removegradeclass" rel="external nofollow" class="removegradeclass"><button role="button" class="btn btn-primary">删除</button></a>'+
								'</div>');
						$('#' + newunit).val(grades[i]);
					}
				}
				
                $('.selectpicker').selectpicker();
				var type = Number.parseInt(field[0].type);
				var a = type&1;
				var b = type&2;
				var c = type&4;	
				if(a === 1)
					$('.selectpicker').append("<option value=\"1\" selected=\"selected\" >销售收入对象</option>");
				else
				{
					$('.selectpicker').append("<option value=\"1\">销售收入对象</option>");
					$("input[id^='unit']").attr("disabled","true");
					$("input[id^='grade']").attr("disabled","true");						
				}
				
				if(b === 2)
					$('.selectpicker').append("<option value=\"2\" selected=\"selected\" >日常收入对象</option>");
				else
					$('.selectpicker').append("<option value=\"2\">日常收入对象</option>");					
				
				if(c === 4)
					$('.selectpicker').append("<option value=\"4\" selected=\"selected\" >日常支出对象</option>");
				else
					$('.selectpicker').append("<option value=\"4\">日常支出对象</option>");			

                $('.selectpicker').selectpicker('refresh');
                $('.selectpicker').selectpicker('render');                
 
				$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
					var selected = $(e.currentTarget).val();
					if($.inArray("1",selected))//判断是否选择了销售对象
					{
						$("input[id^='unit']").attr("disabled","true");
						$("input[id^='grade']").attr("disabled","true");
					}else
					{
						$("input[id^='unit']").removeAttr("disabled");
						$("input[id^='grade']").removeAttr("disabled");
					}
				}); 
               return false;
            });
         });
}
function listcustomer()
{
	var prepareaction= 'preparecustomer';
	var listaction='/hellojxc/listcustomer';
	
	$("section.content-header").html(
            '<h1>' +
            '客户管理' +
            ' <small>客户一览</small>' +
             '</h1>'
     );  
	
	var targetval='<form id="frm-example">';
	if(userType === "0")
	{
		//只有admin用户有增删减权限			
		targetval +=  	'<a class="btn btn-primary" id="add" href="index.html?function=' + prepareaction+ '">添加</a> ' + "\n" + 
						'<button  id="del" type="button" class="btn btn-danger">删除</button>' +  "\n" + 
						'<button  id="update" type="button" class="btn btn-primary">更新</button>' + 
						'<br>' + 
						'<br>'
	}
	targetval += ' <table id="example" class="display select" width="100%" cellspacing="0">' + 
                    ' <thead>' + 
                        '   <tr>' + 
                        '     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
                        '     <th>姓名</th>' + 
                        '     <th>电话</th>' + 
                        '     <th>省份</th>' + 
                        '     <th>城市</th>' + 
                        '   </tr>' + 
                     '</thead>' + 
                    '<tfoot>' + 
                    '   <tr>' + 
                    '     <th></th>' + 
                        '     <th>姓名</th>' + 
                        '     <th>电话</th>' + 
                        '     <th>省份</th>' + 
                        '     <th>城市</th>' + 
                    '   </tr>' + 
                    '</tfoot>' + 
                 '</table>' + 
				'</form>';
	$("#target").html(targetval);
	
    var table = $('#example').DataTable({
	   searching: false,
      'ajax': {
         'url': listaction
      },
      'columnDefs': [
        {
            'targets': 0,
            'searchable': false,
            'orderable': false,
            'className': 'dt-body-left',
            'render': function (data, type, full, meta){
                return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
            }
        }       
        ],
      'order': [[1, 'asc']]
    });	
	
	listcommonoperation('customer',table);
}

function preparecustomer()
{
    $("section.content-header").html(
        '<h1>' +
        '客户管理' +
        ' <small>客户添加</small>' +
         '</h1>'
    ); 

	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/addcustomer" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">姓名*</span>' +
			'<input name="name" type="text" class="form-control" style="width:30%" required>' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">电话</span>' +
			'<input name="mobile" type="text" class="form-control" style="width:30%" placeholder="130xxxxxxxx" digits>' +
		'</div>' + 
		'<br>' +
		'<div class="input-group">' +	
			'<span class="input-group-addon">省市</span>' +
			'<select id="province" name="province" class="selectpicker" data-style="btn-info" ></select>' +
            '</div>' +      
		'<br>' +		
		'<div class="input-group">' +	
			'<span class="input-group-addon">城市</span>' +
			'<select id="city" name="city" class="selectpicker" data-style="btn-info" ></select>' +
            '</div>' +      
		'<br>' +		
		'<button type="submit" class="btn btn-default">提交</button>');
		
	var parray=Array(
		"北京","上海","天津","重庆","河北","山西","内蒙古",
		"辽宁","吉林","黑龙江","江苏","浙江","安徽","福建",
		"江西","山东","河南","湖北","湖南","广东","广西",
		"海南","四川","贵州","云南","西藏","陕西","甘肃",
		"宁夏","青海","新疆","香港","澳门","台湾")
	var carray=Array(
		"北京,东城,西城,崇文,宣武,朝阳,丰台,石景山,海淀,门头沟,房山,通州,顺义,昌平,大兴,平谷,怀柔,密云,延庆" ,
		"上海,黄浦,卢湾,徐汇,长宁,静安,普陀,闸北,虹口,杨浦,闵行,宝山,嘉定,浦东,金山,松江,青浦,南汇,奉贤,崇明" ,
		"天津,和平,东丽,河东,西青,河西,津南,南开,北辰,河北,武清,红挢,塘沽,汉沽,大港,宁河,静海,宝坻,蓟县,大邱庄",
		"重庆,万州,涪陵,渝中,大渡口,江北,沙坪坝,九龙坡,南岸,北碚,万盛,双挢,渝北,巴南,黔江,长寿,綦江,潼南,铜梁,大足,荣昌,壁山,梁平,城口,丰都,垫江,武隆,忠县,开县,云阳,奉节,巫山,巫溪,石柱,秀山,酉阳,彭水,江津,合川,永川,南川",
		"石家庄,邯郸,邢台,保定,张家口,承德,廊坊,唐山,秦皇岛,沧州,衡水",
		"太原,大同,阳泉,长治,晋城,朔州,吕梁,忻州,晋中,临汾,运城",
		"呼和浩特,包头,乌海,赤峰,呼伦贝尔盟,阿拉善盟,哲里木盟,兴安盟,乌兰察布盟,锡林郭勒盟,巴彦淖尔盟,伊克昭盟" ,
		"沈阳,大连,鞍山,抚顺,本溪,丹东,锦州,营口,阜新,辽阳,盘锦,铁岭,朝阳,葫芦岛" ,
		"长春,吉林,四平,辽源,通化,白山,松原,白城,延边",
		"哈尔滨,齐齐哈尔,牡丹江,佳木斯,大庆,绥化,鹤岗,鸡西,黑河,双鸭山,伊春,七台河,大兴安岭",
		"南京,镇江,苏州,南通,扬州,盐城,徐州,连云港,常州,无锡,宿迁,泰州,淮安",
		"杭州,宁波,温州,嘉兴,湖州,绍兴,金华,衢州,舟山,台州,丽水",
		"合肥,芜湖,蚌埠,马鞍山,淮北,铜陵,安庆,黄山,滁州,宿州,池州,淮南,巢湖,阜阳,六安,宣城,亳州",
		"福州,厦门,莆田,三明,泉州,漳州,南平,龙岩,宁德",
		"南昌市,景德镇,九江,鹰潭,萍乡,新馀,赣州,吉安,宜春,抚州,上饶",
		"济南,青岛,淄博,枣庄,东营,烟台,潍坊,济宁,泰安,威海,日照,莱芜,临沂,德州,聊城,滨州,菏泽,博兴",
		"郑州,开封,洛阳,平顶山,安阳,鹤壁,新乡,焦作,濮阳,许昌,漯河,三门峡,南阳,商丘,信阳,周口,驻马店,济源",
		"武汉,宜昌,荆州,襄樊,黄石,荆门,黄冈,十堰,恩施,潜江,天门,仙桃,随州,咸宁,孝感,鄂州",
		"长沙,常德,株洲,湘潭,衡阳,岳阳,邵阳,益阳,娄底,怀化,郴州,永州,湘西,张家界",
		"广州,深圳,珠海,汕头,东莞,中山,佛山,韶关,江门,湛江,茂名,肇庆,惠州,梅州,汕尾,河源,阳江,清远,潮州,揭阳,云浮",
		"南宁,柳州,桂林,梧州,北海,防城港,钦州,贵港,玉林,南宁地区,柳州地区,贺州,百色,河池",
		"海口,三亚",
		"成都,绵阳,德阳,自贡,攀枝花,广元,内江,乐山,南充,宜宾,广安,达川,雅安,眉山,甘孜,凉山,泸州",
		"贵阳,六盘水,遵义,安顺,铜仁,黔西南,毕节,黔东南,黔南",
		"昆明,大理,曲靖,玉溪,昭通,楚雄,红河,文山,思茅,西双版纳,保山,德宏,丽江,怒江,迪庆,临沧",
		"拉萨,日喀则,山南,林芝,昌都,阿里,那曲",
		"西安,宝鸡,咸阳,铜川,渭南,延安,榆林,汉中,安康,商洛",
		"兰州,嘉峪关,金昌,白银,天水,酒泉,张掖,武威,定西,陇南,平凉,庆阳,临夏,甘南",
		"银川,石嘴山,吴忠,固原",
		"西宁,海东,海南,海北,黄南,玉树,果洛,海西",
		"乌鲁木齐,石河子,克拉玛依,伊犁,巴音郭勒,昌吉,克孜勒苏柯尔克孜,博 尔塔拉,吐鲁番,哈密,喀什,和田,阿克苏",
		"香港",
		"澳门",
		"台北,高雄,台中,台南,屏东,南投,云林,新竹,彰化,苗栗,嘉义,花莲,桃园,宜兰,基隆,台东,金门,马祖,澎湖");	
	
	$('.selectpicker').selectpicker({
		liveSearch:true
	});
	
	$('#city').append("<option value=\"请选择城市\">请选择城市</option>");
	$('#city').selectpicker('refresh');
    $('#city').selectpicker('render');	
	

	$('#province').append("<option value=\"请选择省市\">请选择省市</option>");
	for(i=0;i<parray.length;i++)
	{
		$('#province').append("<option value=\""+parray[i] +"\">" + parray[i] +"</option>");	
	}
	$('#province').selectpicker('refresh');
    $('#province').selectpicker('render');	

	$('#province').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
		console.log(this.value, clickedIndex, newValue, oldValue)
		var x = clickedIndex - 1;
		$('#city').empty();
		if(x>=0)
		{
			var citylist=carray[x].split(',');
			 
			for(i=0;i<citylist.length;i++)
			{
				$('#city').append("<option value=\""+citylist[i] +"\">" + citylist[i] +"</option>");
			}
		}
		else
		{
			$('#city').append("<option value=\"请选择城市\">请选择城市</option>");
		}
		$('#city').selectpicker('refresh');
		$('#city').selectpicker('render');			
	});

}
function showcustomer(id)
{
      $("section.content-header").html(
            '<h1>' +
            '客户管理' +
            ' <small>客户更新</small>' +
             '</h1>'
        ); 
		
    	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/updatecustomer" method="POST">'+
		'<input type="hidden" id="id" name="id">' +
        '<div class="input-group">' + 
			'<span class="input-group-addon">姓名*</span>' +
			'<input id="name" name="name" type="text" class="form-control" style="width:30%" required>' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">电话</span>' +
			'<input id="mobile" name="mobile" type="text" class="form-control" style="width:30%" placeholder="130xxxxxxxx">' +
		'</div>' + 
		'<br>' +		
		'<div class="input-group">' +	
			'<span class="input-group-addon">省市</span>' +
			'<select id="province" name="province" class="selectpicker province" data-style="btn-info" ></select>' +
            '</div>' +      
		'<br>' +		
		'<div class="input-group">' +	
			'<span class="input-group-addon">城市</span>' +
			'<select id="city" name="city" class="selectpicker city" data-style="btn-info" ></select>' +
        '</div>' +      
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');  
		
	var parray=Array(
		"北京","上海","天津","重庆","河北","山西","内蒙古",
		"辽宁","吉林","黑龙江","江苏","浙江","安徽","福建",
		"江西","山东","河南","湖北","湖南","广东","广西",
		"海南","四川","贵州","云南","西藏","陕西","甘肃",
		"宁夏","青海","新疆","香港","澳门","台湾")
	var carray=Array(
		"北京,东城,西城,崇文,宣武,朝阳,丰台,石景山,海淀,门头沟,房山,通州,顺义,昌平,大兴,平谷,怀柔,密云,延庆" ,
		"上海,黄浦,卢湾,徐汇,长宁,静安,普陀,闸北,虹口,杨浦,闵行,宝山,嘉定,浦东,金山,松江,青浦,南汇,奉贤,崇明" ,
		"天津,和平,东丽,河东,西青,河西,津南,南开,北辰,河北,武清,红挢,塘沽,汉沽,大港,宁河,静海,宝坻,蓟县,大邱庄",
		"重庆,万州,涪陵,渝中,大渡口,江北,沙坪坝,九龙坡,南岸,北碚,万盛,双挢,渝北,巴南,黔江,长寿,綦江,潼南,铜梁,大足,荣昌,壁山,梁平,城口,丰都,垫江,武隆,忠县,开县,云阳,奉节,巫山,巫溪,石柱,秀山,酉阳,彭水,江津,合川,永川,南川",
		"石家庄,邯郸,邢台,保定,张家口,承德,廊坊,唐山,秦皇岛,沧州,衡水",
		"太原,大同,阳泉,长治,晋城,朔州,吕梁,忻州,晋中,临汾,运城",
		"呼和浩特,包头,乌海,赤峰,呼伦贝尔盟,阿拉善盟,哲里木盟,兴安盟,乌兰察布盟,锡林郭勒盟,巴彦淖尔盟,伊克昭盟" ,
		"沈阳,大连,鞍山,抚顺,本溪,丹东,锦州,营口,阜新,辽阳,盘锦,铁岭,朝阳,葫芦岛" ,
		"长春,吉林,四平,辽源,通化,白山,松原,白城,延边",
		"哈尔滨,齐齐哈尔,牡丹江,佳木斯,大庆,绥化,鹤岗,鸡西,黑河,双鸭山,伊春,七台河,大兴安岭",
		"南京,镇江,苏州,南通,扬州,盐城,徐州,连云港,常州,无锡,宿迁,泰州,淮安",
		"杭州,宁波,温州,嘉兴,湖州,绍兴,金华,衢州,舟山,台州,丽水",
		"合肥,芜湖,蚌埠,马鞍山,淮北,铜陵,安庆,黄山,滁州,宿州,池州,淮南,巢湖,阜阳,六安,宣城,亳州",
		"福州,厦门,莆田,三明,泉州,漳州,南平,龙岩,宁德",
		"南昌市,景德镇,九江,鹰潭,萍乡,新馀,赣州,吉安,宜春,抚州,上饶",
		"济南,青岛,淄博,枣庄,东营,烟台,潍坊,济宁,泰安,威海,日照,莱芜,临沂,德州,聊城,滨州,菏泽,博兴",
		"郑州,开封,洛阳,平顶山,安阳,鹤壁,新乡,焦作,濮阳,许昌,漯河,三门峡,南阳,商丘,信阳,周口,驻马店,济源",
		"武汉,宜昌,荆州,襄樊,黄石,荆门,黄冈,十堰,恩施,潜江,天门,仙桃,随州,咸宁,孝感,鄂州",
		"长沙,常德,株洲,湘潭,衡阳,岳阳,邵阳,益阳,娄底,怀化,郴州,永州,湘西,张家界",
		"广州,深圳,珠海,汕头,东莞,中山,佛山,韶关,江门,湛江,茂名,肇庆,惠州,梅州,汕尾,河源,阳江,清远,潮州,揭阳,云浮",
		"南宁,柳州,桂林,梧州,北海,防城港,钦州,贵港,玉林,南宁地区,柳州地区,贺州,百色,河池",
		"海口,三亚",
		"成都,绵阳,德阳,自贡,攀枝花,广元,内江,乐山,南充,宜宾,广安,达川,雅安,眉山,甘孜,凉山,泸州",
		"贵阳,六盘水,遵义,安顺,铜仁,黔西南,毕节,黔东南,黔南",
		"昆明,大理,曲靖,玉溪,昭通,楚雄,红河,文山,思茅,西双版纳,保山,德宏,丽江,怒江,迪庆,临沧",
		"拉萨,日喀则,山南,林芝,昌都,阿里,那曲",
		"西安,宝鸡,咸阳,铜川,渭南,延安,榆林,汉中,安康,商洛",
		"兰州,嘉峪关,金昌,白银,天水,酒泉,张掖,武威,定西,陇南,平凉,庆阳,临夏,甘南",
		"银川,石嘴山,吴忠,固原",
		"西宁,海东,海南,海北,黄南,玉树,果洛,海西",
		"乌鲁木齐,石河子,克拉玛依,伊犁,巴音郭勒,昌吉,克孜勒苏柯尔克孜,博 尔塔拉,吐鲁番,哈密,喀什,和田,阿克苏",
		"香港",
		"澳门",
		"台北,高雄,台中,台南,屏东,南投,云林,新竹,彰化,苗栗,嘉义,花莲,桃园,宜兰,基隆,台东,金门,马祖,澎湖");	

	$('.selectpicker').selectpicker({
		liveSearch:true
	});
	
	$('#city').append("<option value=\"请选择城市\">请选择城市</option>");
	$('#city').selectpicker('refresh');
    $('#city').selectpicker('render');	
	

	$('#province').append("<option value=\"请选择省市\">请选择省市</option>");
	for(i=0;i<parray.length;i++)
	{
		$('#province').append("<option value=\""+parray[i] +"\">" + parray[i] +"</option>");	
	}
	$('#province').selectpicker('refresh');
    $('#province').selectpicker('render');	

	$('#province').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
		//console.log(this.value, clickedIndex, newValue, oldValue)
		var x = clickedIndex - 1;
		$('#city').empty();
		if(x>=0)
		{
			var citylist=carray[x].split(',');
			 
			for(i=0;i<citylist.length;i++)
			{
				$('#city').append("<option value=\""+citylist[i] +"\">" + citylist[i] +"</option>");
			}
		}
		else
		{
			$('#city').append("<option value=\"请选择城市\">请选择城市</option>");
		}
		$('#city').selectpicker('refresh');
		$('#city').selectpicker('render');			
	});
	
    var geturl = "/hellojxc/getcustomer?id=" + id;
        $.getJSON(geturl,function(result){
            $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
                //alert(field[0].name);//id,name,mobile,location,type
                $("#id").val(field[0].id);
                $("#name").val(field[0].name);
				$("#mobile").val(field[0].mobile);
				
                $("#province").selectpicker('val',field[0].province);
				
				var x = getMatchedIndex(parray,field[0].province);
				$('#city').empty();
				if(x>=0)
				{
					var citylist=carray[x].split(',');
					 
					for(i=0;i<citylist.length;i++)
					{
						$('#city').append("<option value=\""+citylist[i] +"\">" + citylist[i] +"</option>");
					}
					$("#city").selectpicker('val',field[0].city);
					$('#city').selectpicker('refresh');
					$('#city').selectpicker('render');	
				}
				else
				{
					$('#city').append("<option value=\"请选择城市\">请选择城市</option>");
					$('#city').selectpicker('refresh');
					$('#city').selectpicker('render');	
				}				               
               return false;
            });
         });
    // alert("show_updatecustomer");	
}
function listpay()
{
	var prepareaction= 'preparepay';
	var listaction='/hellojxc/listpay';
	
	$("section.content-header").html(
            '<h1>' +
            '支付方式管理' +
            ' <small>支付方式一览</small>' +
             '</h1>'
    ); 
	
	var targetval='<form id="frm-example">';
	
	if(userType === "0")
	{
		//只有admin用户有增删减权限
		targetval += 	'<a class="btn btn-primary" id="add" href="index.html?function=' + prepareaction + '" role="button">添加</a> ' + "\n" + 
						' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
						' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
						'<br>' + 
						'<br>';
	}
	targetval +='<table id="example" class="display select" width="100%" cellspacing="0">' + 
					' <thead>' + 
					'   <tr>' + 
					'     <th width="10%><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
					'     <th width="10%">ID</th>' + 
					'     <th>名称</th>' + 
					'   </tr>' + 
					'</thead>' + 
					'<tfoot>' + 
					'   <tr>' + 
					'     <th width="10%></th>' + 
					'     <th width="10%>ID</th>' + 		
					'     <th>名称</th>' + 
					'   </tr>' + 
					'</tfoot>' + 
				'</table>';
	targetval += '</form>';

	$("#target").html(targetval);

    var table = $('#example').DataTable({
	   searching: false,
      'ajax': {
         'url': listaction
      },
      'columnDefs': [
            {
                'targets': 0,
                'searchable': false,
                'orderable': false,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
                    return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                }
            }
        ],
      'order': [[1, 'asc']]
    });	
	
	listcommonoperation('pay',table);	
}

function preparepay()
{
	$("section.content-header").html(
            '<h1>' +
            '支付方式管理' +
            ' <small>支付方式添加</small>' +
             '</h1>'
    ); 
		
	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/addpay" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input name="name" type="text" class="form-control" style="width:30%" maxlength="20" required>' +
		'</div>' +
		'<br>' + 
		'<button type="submit" class="btn btn-default">提交</button>');	
}

function showpay(id)
{
    $("section.content-header").html(
            '<h1>' +
            '支付方式管理' +
            ' <small>支付方式更新</small>' +
             '</h1>'
    ); 
    	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/updatepay" method="POST">'+
		//'<input type="hidden" id="id" name="id">' +
        '<div class="input-group">' + 
			'<span class="input-group-addon">ID</span>' +
			'<input id="id" name="id" readonly type="text" class="form-control" style="width:10%">' +
		'</div>' +	
		'<br>' +	
        '<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input id="name" name="name" type="text" class="form-control" style="width:30%" maxlength="20" required>' +
		'</div>' +
		'<br>' +		
		'<button type="submit" class="btn btn-default">提交</button>');  
           
    var geturl = "/hellojxc/getpay?id=" + id;
    $.getJSON(geturl,function(result){
        $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
            //alert(field[0].name);//id,name,mobile,location,type
            $("#id").val(field[0].id);
            $("#name").val(field[0].name);
           return false;
        });
    });	
}

function listdailyinput()
{
	var prepareaction= 'preparedailyinput';
	var listaction='/hellojxc/listdailyinput';
	
	$("section.content-header").html(
            '<h1>' +
            '日常收入一览' +
            ' <small style="color:red">红色表示未支付</small>' +
             '</h1>'
    ); 
	
	var targetval='<form id="frm-example">';
	
	targetval += ' <button  id="query" role="button" class="btn btn-primary">查询</button>' + "\n";
	if(userType === "0")
	{
            //只有admin用户有增删减权限
            targetval += 	'<a class="btn btn-primary" id="add" href="index.html?function=' + prepareaction + '" role="button">添加</a> ' + "\n" + 
							' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
							' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
							'<br>' + 
							'<br>';
	}
	
	targetval +=	'<p>开始日期：<input type="text" id="datepicker_start" name="datepicker_start"></p>'+
					'<p>结束日期：<input type="text" id="datepicker_end" name="datepicker_end"></p>'+
					'<br>';
				 
	targetval +='<table id="example" class="display select" width="100%" cellspacing="0">' + 
					' <thead>' + 
					'   <tr>' + 
					'     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
					//'     <th width="5%">ID</th>' + 
					'     <th>摘要</th>' + 
					'     <th>对象名称</th>' + 
					'     <th>支付方式</th>' + 					
					'     <th>总价</th>' + 
					'     <th>交易日期</th>' + 
					'     <th>记录日期</th>' + 			
					'     <th>备考</th>' + 					
					'   </tr>' + 
					'</thead>' + 
					'<tfoot>' + 
					'   <tr>' + 
					'     <th width="5%"></th>' + 
					//'     <th>ID</th>' + 
					'     <th>摘要</th>' + 
					'     <th>对象名称</th>' + 
					'     <th>支付方式</th>' + 					
					'     <th>总价</th>' + 
					'     <th>交易日期</th>' + 
					'     <th>记录日期</th>' + 			
					'     <th>备考</th>' + 	
					'   </tr>' + 
					'</tfoot>' + 
				'</table>';
	targetval += '</form>';

	$("#target").html(targetval);

    var d = new Date(), ld = new Date(d.getFullYear(), d.getMonth()-6, 1);
    
    $("#datepicker_start").datepicker();
    $("#datepicker_start").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#datepicker_start").val(ld.getFullYear() + '-' + (ld.getMonth() + 1) + '-' + ld.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
    
    $("#datepicker_end").datepicker();
    $("#datepicker_end").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#datepicker_end").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
	
    var table;
    var refresh = function() {
        var startday=$("#datepicker_start").val();
        var endday=$("#datepicker_end").val();
        //DataTable seems to be for API calls back into the object and dataTable seems to be the intialisation method.
        table = $('#example').dataTable({
			  searching: false,
			  dom: 'Bfrtip',
			  buttons:[
				  {
					  extend: 'excelHtml5',
					  text: '导出excel',
					  title: '日常收入一览_' +  startday + '_TO_' + endday
				  }
			  ],			  
             'ajax': {
                'url': '/hellojxc/listdailyinput',
                'type': 'POST'
             },
             'fnServerParams':function(aoData){
               aoData.push(
                       {
                         "name": "startday",
                         "value": startday?startday:null
                       },
                       {
                           "name":"endday",
                           "value": endday?endday:null
                        }
               );  
             },
             'columnDefs': [
                   {
                       'targets': 0,
                       'searchable': false,
                       'orderable': false,
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                       }
                   },
				   /*
                   {
                       'targets': 1,
					   'visible':false,//隐藏货物ID列
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           return data;
                       }
                   },
				   */
                   {
                       'targets': 2,//对象名称
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           if(full[8] != '0')//判断对象是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   },
                   {
                       'targets': 3,//支付方式
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
						   if(data === '未支付')//判断是否未支付
							   return '<p style="color:red">'+data+'</p>'; 
                           if(full[9] != '0')//判断支付方式是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   }				   
               ],
             'order': [[1, 'asc']]
           });       
    }
    //加载页面时初始化datatable
    refresh();
    
	$("#query").on('click', function(){ 
		if(table)
		{
			table.fnDestroy();
			refresh();
		}
		return false;
    });
	 
    listcommonoperation('dailyinput',table);   	
}
function preparedailyinput()
{
    $("section.content-header").html(
        '<h1>' +
        '日常收入管理' +
        ' <small>日常收入添加</small>' +
         '</h1>'
    ); 

	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/adddailyinput" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">摘要*</span>' +
			'<input name="title" type="text" class="form-control" style="width:30%" required>' +
		'</div>' +
		'<br>' +
        '<div class="input-group">' +	
			'<span class="input-group-addon">关联对象</span>' +
			'<select name="target_id" class="selectpicker" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +   	
        '<div class="input-group">' +	
			'<span class="input-group-addon">支付方式</span>' +
			'<select name="pay_id" class="selectpicker_pay" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +  		
		'<div class="input-group">' +
			'<span class="input-group-addon">价格</span>' +
			'<input name="price" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input name="operationtime" id="operationtime"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +  		
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注信息</span>' +
			'<textarea name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' + 		
		'<button type="submit" class="btn btn-default">提交</button>');	
		
        //初始化交易日期：默认为当天
        var d = new Date();
        $("#operationtime").datepicker();
        $("#operationtime").datepicker("option", "dateFormat", "yy-mm-dd");
        $("#operationtime").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });	
		
		$('#frm-target').on('submit', function(e){
			var selected = $('.selectpicker').val();
			if(selected === null || selected.length === 0)
			{
				alert("请选择一项关联对象");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
		});
		
        //获取关联日常管理对象
		$('.selectpicker').selectpicker();
        $.getJSON('/hellojxc/listdailyinputtarget',function(result){

            for(var i=0;i<result.data.length;i++)
            {
                if(i===0)
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\" selected=\"selected\">"+ result.data[i][2] +"</option>");
                else
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });
        $('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
			
        //获取支付方式
		$('.selectpicker_pay').selectpicker();
		$('.selectpicker_pay').append("<option value=\"0\" selected=\"selected\">未支付</option>");
        $.getJSON('/hellojxc/listpay',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				$('.selectpicker_pay').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });	
		$('.selectpicker_pay').selectpicker('refresh');
		$('.selectpicker_pay').selectpicker('render');		
}
function showdailyinput(id)
{
	    $("section.content-header").html(
        '<h1>' +
        '日常收入管理' +
        ' <small>日常收入更新</small>' +
         '</h1>'
    );
	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/updatedailyinput" method="POST">'+
		'<input type="hidden" id="id" name="id">' +
		'<div class="input-group">' + 
			'<span class="input-group-addon">摘要*</span>' +
			'<input id="title" name="title" type="text" class="form-control" style="width:30%" required>' +
		'</div>' +
		'<br>' +
        '<div class="input-group">' +	
			'<span class="input-group-addon">关联对象</span>' +
			'<select id="target_id" name="target_id" class="selectpicker" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +   	
        '<div class="input-group">' +	
			'<span class="input-group-addon">支付方式</span>' +
			'<select id="pay_id" name="pay_id" class="selectpicker_pay" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +  		
		'<div class="input-group">' +
			'<span class="input-group-addon">价格</span>' +
			'<input id="price" name="price" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input id="operationtime" name="operationtime" id="operationtime"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +  		
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注信息</span>' +
			'<textarea id="refer" name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' + 		
		'<button type="submit" class="btn btn-default">提交</button>');	
		
		$('#frm-target').on('submit', function(e){
			var selected = $('.selectpicker').val();
			if(selected === null || selected.length === 0)
			{
				alert("请选择一项关联对象");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
		});
		
        //获取关联日常管理对象
		$('.selectpicker').selectpicker();
        $.getJSON('/hellojxc/listdailyinputtarget',function(result){
            for(var i=0;i<result.data.length;i++)
            {
                $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });
        $('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
			
        //获取支付方式
		$('.selectpicker_pay').selectpicker();
		$('.selectpicker_pay').append("<option value=\"0\">未支付</option>");
        $.getJSON('/hellojxc/listpay',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				$('.selectpicker_pay').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });	
		$('.selectpicker_pay').selectpicker('refresh');
		$('.selectpicker_pay').selectpicker('render');

		var geturl = "/hellojxc/getdailyinput?id=" + id;
		$.getJSON(geturl,function(result){
				$.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
					$("#id").val(field[0].id);
					$("#title").val(field[0].title);  
					//对象选择
					$(".selectpicker").selectpicker('val',field[0].target_id);
					$('.selectpicker').selectpicker('refresh');
					//支付方式选择
					$(".selectpicker_pay").selectpicker('val',field[0].pay_id);
					$('.selectpicker_pay').selectpicker('refresh');		
					
					$("#price").val(field[0].price);
					$("#operationtime").val(field[0].operationtime).datepicker({ dateFormat: 'yy-mm-dd' });//购买时间
					$("#refer").val(field[0].refer);
					
				   return false;
				});
	   });		
}

function listdailyoutput()
{
	var prepareaction= 'preparedailyoutput';
	var listaction='/hellojxc/listdailyoutput';
	
	$("section.content-header").html(
            '<h1>' +
            '日常支出一览' +
            ' <small style="color:red">红色表示未支付</small>' +
             '</h1>'
    ); 
	
	var targetval='<form id="frm-example">';
	
	targetval += ' <button  id="query" role="button" class="btn btn-primary">查询</button>' + "\n";
	if(userType === "0")
	{
            //只有admin用户有增删减权限
            targetval += 	'<a class="btn btn-primary" id="add" href="index.html?function=' + prepareaction + '" role="button">添加</a> ' + "\n" + 
							' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
							' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
							'<br>' + 
							'<br>';
	}
	
	targetval +=	'<p>开始日期：<input type="text" id="datepicker_start" name="datepicker_start"></p>'+
					'<p>结束日期：<input type="text" id="datepicker_end" name="datepicker_end"></p>'+
					'<br>';
				 
	targetval +='<table id="example" class="display select" width="100%" cellspacing="0">' + 
					' <thead>' + 
					'   <tr>' + 
					'     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
					//'     <th width="5%">ID</th>' + 
					'     <th>摘要</th>' + 
					'     <th>对象名称</th>' + 
					'     <th>支付方式</th>' + 					
					'     <th>总价</th>' + 
					'     <th>交易日期</th>' + 
					'     <th>记录日期</th>' + 			
					'     <th>备考</th>' + 					
					'   </tr>' + 
					'</thead>' + 
					'<tfoot>' + 
					'   <tr>' + 
					'     <th width="5%"></th>' + 
					//'     <th>ID</th>' + 
					'     <th>摘要</th>' + 
					'     <th>对象名称</th>' + 
					'     <th>支付方式</th>' + 					
					'     <th>总价</th>' + 
					'     <th>交易日期</th>' + 
					'     <th>记录日期</th>' + 			
					'     <th>备考</th>' + 	
					'   </tr>' + 
					'</tfoot>' + 
				'</table>';
	targetval += '</form>';

	$("#target").html(targetval);

    var d = new Date(), ld = new Date(d.getFullYear(), d.getMonth()-6, 1);
    
    $("#datepicker_start").datepicker();
    $("#datepicker_start").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#datepicker_start").val(ld.getFullYear() + '-' + (ld.getMonth() + 1) + '-' + ld.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
    
    $("#datepicker_end").datepicker();
    $("#datepicker_end").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#datepicker_end").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
	
    var table;
    var refresh = function() {
        var startday=$("#datepicker_start").val();
        var endday=$("#datepicker_end").val();
        //DataTable seems to be for API calls back into the object and dataTable seems to be the intialisation method.
        table = $('#example').dataTable({
			  searching: false,
			  dom: 'Bfrtip',
			  buttons:[
				  {
					  extend: 'excelHtml5',
					  text: '导出excel',
					  title: '日常支出一览_' +  startday + '_TO_' + endday
				  }
			  ],			  
             'ajax': {
                'url': '/hellojxc/listdailyoutput',
                'type': 'POST'
             },
             'fnServerParams':function(aoData){
               aoData.push(
                       {
                         "name": "startday",
                         "value": startday?startday:null
                       },
                       {
                           "name":"endday",
                           "value": endday?endday:null
                        }
               );  
             },
             'columnDefs': [
                   {
                       'targets': 0,
                       'searchable': false,
                       'orderable': false,
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                       }
                   },
				   /*
                   {
                       'targets': 1,
					   'visible':false,//隐藏货物ID列
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           return data;
                       }
                   },
				   */
                   {
                       'targets': 2,//对象名称
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           if(full[8] != '0')//判断对象是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   },
                   {
                       'targets': 3,//支付方式
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
						   if(data === '未支付')//判断是否未支付
							   return '<p style="color:red">'+data+'</p>'; 
                           if(full[9] != '0')//判断支付方式是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   }				   
               ],
             'order': [[1, 'asc']]
           });       
    }
    //加载页面时初始化datatable
    refresh();
    
	$("#query").on('click', function(){ 
		if(table)
		{
			table.fnDestroy();
			refresh();
		}
		return false;
    });
	 
    listcommonoperation('dailyoutput',table);   	
}
function preparedailyoutput()
{
    $("section.content-header").html(
        '<h1>' +
        '日常支出管理' +
        ' <small>日常支出添加</small>' +
         '</h1>'
    ); 

	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/adddailyoutput" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">摘要*</span>' +
			'<input name="title" type="text" class="form-control" style="width:30%" required>' +
		'</div>' +
		'<br>' +
        '<div class="input-group">' +	
			'<span class="input-group-addon">关联对象</span>' +
			'<select name="target_id" class="selectpicker" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +   	
        '<div class="input-group">' +	
			'<span class="input-group-addon">支付方式</span>' +
			'<select name="pay_id" class="selectpicker_pay" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +  		
		'<div class="input-group">' +
			'<span class="input-group-addon">价格</span>' +
			'<input name="price" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input name="operationtime" id="operationtime"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +  		
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注信息</span>' +
			'<textarea name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' + 		
		'<button type="submit" class="btn btn-default">提交</button>');	
		
        //初始化交易日期：默认为当天
        var d = new Date();
        $("#operationtime").datepicker();
        $("#operationtime").datepicker("option", "dateFormat", "yy-mm-dd");
        $("#operationtime").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });	
		
		$('#frm-target').on('submit', function(e){
			var selected = $('.selectpicker').val();
			if(selected === null || selected.length === 0)
			{
				alert("请选择一项关联对象");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
		});
		
        //获取关联日常管理对象
		$('.selectpicker').selectpicker();
        $.getJSON('/hellojxc/listdailyoutputtarget',function(result){

            for(var i=0;i<result.data.length;i++)
            {
                if(i===0)
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\" selected=\"selected\">"+ result.data[i][2] +"</option>");
                else
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });
        $('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
			
        //获取支付方式
		$('.selectpicker_pay').selectpicker();
		$('.selectpicker_pay').append("<option value=\"0\" selected=\"selected\">未支付</option>");
        $.getJSON('/hellojxc/listpay',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				$('.selectpicker_pay').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });	
		$('.selectpicker_pay').selectpicker('refresh');
		$('.selectpicker_pay').selectpicker('render');		
}
function showdailyoutput(id)
{
	    $("section.content-header").html(
        '<h1>' +
        '日常支出管理' +
        ' <small>日常支出更新</small>' +
         '</h1>'
    );
	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/updatedailyoutput" method="POST">'+
		'<input type="hidden" id="id" name="id">' +
		'<div class="input-group">' + 
			'<span class="input-group-addon">摘要*</span>' +
			'<input id="title" name="title" type="text" class="form-control" style="width:30%" required>' +
		'</div>' +
		'<br>' +
        '<div class="input-group">' +	
			'<span class="input-group-addon">关联对象</span>' +
			'<select id="target_id" name="target_id" class="selectpicker" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +   	
        '<div class="input-group">' +	
			'<span class="input-group-addon">支付方式</span>' +
			'<select id="pay_id" name="pay_id" class="selectpicker_pay" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +  		
		'<div class="input-group">' +
			'<span class="input-group-addon">价格</span>' +
			'<input id="price" name="price" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input id="operationtime" name="operationtime" id="operationtime"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +  		
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注信息</span>' +
			'<textarea id="refer" name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' + 		
		'<button type="submit" class="btn btn-default">提交</button>');	
		
		$('#frm-target').on('submit', function(e){
			var selected = $('.selectpicker').val();
			if(selected === null || selected.length === 0)
			{
				alert("请选择一项关联对象");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
		});
		
        //获取关联日常管理对象
		$('.selectpicker').selectpicker();
        $.getJSON('/hellojxc/listdailyoutputtarget',function(result){
            for(var i=0;i<result.data.length;i++)
            {
                $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });
        $('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
			
        //获取支付方式
		$('.selectpicker_pay').selectpicker();
		$('.selectpicker_pay').append("<option value=\"0\">未支付</option>");
        $.getJSON('/hellojxc/listpay',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				$('.selectpicker_pay').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });	
		$('.selectpicker_pay').selectpicker('refresh');
		$('.selectpicker_pay').selectpicker('render');

		var geturl = "/hellojxc/getdailyoutput?id=" + id;
		$.getJSON(geturl,function(result){
				$.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
					$("#id").val(field[0].id);
					$("#title").val(field[0].title);  
					//对象选择
					$(".selectpicker").selectpicker('val',field[0].target_id);
					$('.selectpicker').selectpicker('refresh');
					//支付方式选择
					$(".selectpicker_pay").selectpicker('val',field[0].pay_id);
					$('.selectpicker_pay').selectpicker('refresh');		
					
					$("#price").val(field[0].price);
					$("#operationtime").val(field[0].operationtime).datepicker({ dateFormat: 'yy-mm-dd' });//购买时间
					$("#refer").val(field[0].refer);
					
				   return false;
				});
	   });		
}

function listsalesinput()
{
	var prepareaction= 'preparesalesinput';
	var listaction='/hellojxc/listsalesinput';
	
	$("section.content-header").html(
            '<h1>' +
            '销售收入一览' +
            ' <small style="color:red">红色表示未支付</small>' +
             '</h1>'
    ); 
	
	var targetval='<form id="frm-example">';
	
	targetval += ' <button  id="query" role="button" class="btn btn-primary">查询</button>' + "\n";
	if(userType === "0")
	{
            //只有admin用户有增删减权限
            targetval += 	'<a class="btn btn-primary" id="add" href="index.html?function=' + prepareaction + '" role="button">添加</a> ' + "\n" + 
							' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
							' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
							'<br>' + 
							'<br>';
	}
	
	targetval +=	'<p>开始日期：<input type="text" id="datepicker_start" name="datepicker_start"></p>'+
					'<p>结束日期：<input type="text" id="datepicker_end" name="datepicker_end"></p>'+
					'<br>';
				 
	targetval +='<table id="example" class="display select" width="100%" cellspacing="0">' + 
					' <thead>' + 
					'   <tr>' + 
					'     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
					'     <th>对象名称</th>' + 
					'     <th>支付方式</th>' + 	
					'     <th>客户名称</th>' + 							
					'     <th>总价</th>' + 
					'     <th>数量</th>' + 					
					'     <th>单位</th>' + 		
					'     <th>等级</th>' + 						
					'     <th>交易日期</th>' + 
					'     <th>记录日期</th>' + 			
					'     <th>备考</th>' + 					
					'   </tr>' + 
					'</thead>' + 
					'<tfoot>' + 
					'   <tr>' + 
					'     <th width="5%"></th>' + 
					'     <th>对象名称</th>' + 
					'     <th>支付方式</th>' + 	
					'     <th>客户名称</th>' + 							
					'     <th>总价</th>' + 
					'     <th>数量</th>' + 					
					'     <th>单位</th>' + 		
					'     <th>等级</th>' + 						
					'     <th>交易日期</th>' + 
					'     <th>记录日期</th>' + 			
					'     <th>备考</th>' + 		
					'   </tr>' + 
					'</tfoot>' + 
				'</table>';
	targetval += '</form>';

	$("#target").html(targetval);

    var d = new Date(), ld = new Date(d.getFullYear(), d.getMonth()-6, 1);
    
    $("#datepicker_start").datepicker();
    $("#datepicker_start").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#datepicker_start").val(ld.getFullYear() + '-' + (ld.getMonth() + 1) + '-' + ld.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
    
    $("#datepicker_end").datepicker();
    $("#datepicker_end").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#datepicker_end").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
	
    var table;
    var refresh = function() {
        var startday=$("#datepicker_start").val();
        var endday=$("#datepicker_end").val();
        //DataTable seems to be for API calls back into the object and dataTable seems to be the intialisation method.
        table = $('#example').dataTable({
			  searching: false,
			  dom: 'Bfrtip',
			  buttons:[
				  {
					  extend: 'excelHtml5',
					  text: '导出excel',
					  title: '销售收入一览_' +  startday + '_TO_' + endday
				  }
			  ],			  
             'ajax': {
                'url': '/hellojxc/listsalesinput',
                'type': 'POST'
             },
             'fnServerParams':function(aoData){
               aoData.push(
                       {
                         "name": "startday",
                         "value": startday?startday:null
                       },
                       {
                           "name":"endday",
                           "value": endday?endday:null
                        }
               );  
             },
             'columnDefs': [
                   {
                       'targets': 0,
                       'searchable': false,
                       'orderable': false,
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                       }
                   },
                   {
                       'targets': 1,//对象名称
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           if(full[11] != '0')//判断对象是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   },
                   {
                       'targets': 2,//支付方式
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
						   if(data === '未支付')//判断是否未支付
							   return '<p style="color:red">'+data+'</p>'; 
                           if(full[12] != '0')//判断支付方式是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   },
                   {
                       'targets': 3,//客户名称
                       'className': 'dt-body-left',
                       'render': function (data, type, full, meta){
                           if(full[13] != '0')//判断客户是否删除
								return '<p style="color:blue">'+data+'</p>';  
						   else
							   return data;
                       }
                   }				   
               ],
             'order': [[1, 'asc']]
           });       
    }
    //加载页面时初始化datatable
    refresh();
    
	$("#query").on('click', function(){ 
		if(table)
		{
			table.fnDestroy();
			refresh();
		}
		return false;
    });
	 
    listcommonoperation('salesinput',table);   	
}
function preparesalesinput()
{
    $("section.content-header").html(
        '<h1>' +
        '销售收入管理' +
        ' <small>销售收入添加</small>' +
         '</h1>'
    ); 

	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/addsalesinput" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">客户名称</span>' +
			'<select name="customer_id" class="selectpicker_customer" data-live-search="true" data-style="btn-info"></select>' +
		'</div>' +
		'<br>' +
        '<div class="input-group">' +	
			'<span class="input-group-addon">关联对象</span>' +
			'<select name="target_id" class="selectpicker" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +   	
        '<div class="input-group">' +	
			'<span class="input-group-addon">支付方式</span>' +
			'<select name="pay_id" class="selectpicker_pay" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +  		
		'<div class="input-group">' +
			'<span class="input-group-addon">价格</span>' +
			'<input name="price" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
		'<div class="input-group">' +
			'<span class="input-group-addon">数量</span>' +
			'<input name="volume" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
		'<div class="input-group">' +
			'<span class="input-group-addon">单位</span>' +
			'<select name="unit" class="selectpicker_unit" data-style="btn-info"></select>' +
		'</div>' + 
		'<br>' + 	
		'<div class="input-group">' +
			'<span class="input-group-addon">等级</span>' +
			'<select name="grade" class="selectpicker_grade" data-style="btn-info"></select>' +
		'</div>' + 
		'<br>' + 		
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input name="operationtime" id="operationtime"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +  		
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注信息</span>' +
			'<textarea name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' + 		
		'<button type="submit" class="btn btn-default">提交</button>');	
		
        //初始化交易日期：默认为当天
        var d = new Date();
        $("#operationtime").datepicker();
        $("#operationtime").datepicker("option", "dateFormat", "yy-mm-dd");
        $("#operationtime").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });	
		
		$('#frm-target').on('submit', function(e){
			var selectedTarget = $(".selectpicker").val();
			if(selectedTarget === null || selectedTarget.length === 0)
			{
				alert("请选择一项关联对象");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
			
			var selectedCustomer = $(".selectpicker_customer").val();
			if(selectedCustomer === null || selectedCustomer.length === 0)
			{
				alert("请选择一项客户");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}				
		});
		
		//获取关联客户对象
		$('.selectpicker_customer').selectpicker();
        $.getJSON('/hellojxc/listcustomer',function(result){

            for(var i=0;i<result.data.length;i++)
            {
                if(i===0)
                    $('.selectpicker_customer').append("<option value=\""+result.data[i][0]+"\" selected=\"selected\">"+ result.data[i][1] +"</option>");
                else
                    $('.selectpicker_customer').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][1] +"</option>");
            }
        });
        $('.selectpicker_customer').selectpicker('refresh');
        $('.selectpicker_customer').selectpicker('render');
		
		var m_units = new Map();
		var m_grades = new Map();
        //获取关联日常管理对象
		$('.selectpicker').selectpicker();
        $.getJSON('/hellojxc/listsalesinputtarget',function(result){

            for(var i=0;i<result.data.length;i++)
            {
				//units 3,grades 4
				m_units.set(result.data[i][0],result.data[i][3]);
				m_grades.set(result.data[i][0],result.data[i][4]);
                if(i===0)
				{
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\" selected=\"selected\">"+ result.data[i][2] +"</option>");
				}
                else
				{
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
				}
            }
        });
        $('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
		
		//初始化单位
		$('.selectpicker_unit').selectpicker();
		
		var refreshunits = function(currenttarget) {
			//var currenttarget = $('.selectpicker').val();
			var currentunits = m_units.get(currenttarget);
			var units = currentunits.split('-');
			$('.selectpicker_unit').empty();
			for(var i = 0 ; i < units.length; i++)
			{
				if(i ===0)
					$('.selectpicker_unit').append("<option value=\""+units[i]+"\" selected=\"selected\">"+ units[i] +"</option>");
				else
					$('.selectpicker_unit').append("<option value=\""+units[i]+"\">"+ units[i] +"</option>");
				
			}
			
			$('.selectpicker_unit').selectpicker('refresh');
			$('.selectpicker_unit').selectpicker('render');		
		};
		refreshunits($('.selectpicker').val());
		
		//初始化等级
		$('.selectpicker_grade').selectpicker();
		var refreshgrades = function(currenttarget) {
			var currenttarget = $('.selectpicker').val();
			var currentgrades = m_grades.get(currenttarget);
			var grades = currentgrades.split('-');
			$('.selectpicker_grade').empty();
			for(var i = 0 ; i < grades.length; i++)
			{
				if(i ===0)
					$('.selectpicker_grade').append("<option value=\""+grades[i]+"\" selected=\"selected\">"+ grades[i] +"</option>");
				else
					$('.selectpicker_grade').append("<option value=\""+grades[i]+"\">"+ grades[i] +"</option>");
				
			}
			
			$('.selectpicker_grade').selectpicker('refresh');
			$('.selectpicker_grade').selectpicker('render');		
		};	
		refreshgrades($('.selectpicker').val());	
	
		//对象和等级，单位的联动
		$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
			var selected = $(e.currentTarget).val();
			refreshunits(selected);
			refreshgrades(selected);
		});		

			
        //获取支付方式
		$('.selectpicker_pay').selectpicker();
		$('.selectpicker_pay').append("<option value=\"0\" selected=\"selected\">未支付</option>");
        $.getJSON('/hellojxc/listpay',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				$('.selectpicker_pay').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });	
		$('.selectpicker_pay').selectpicker('refresh');
		$('.selectpicker_pay').selectpicker('render');		
}

function showsalesinput(id)
{
	$("section.content-header").html(
        '<h1>' +
        '销售收入管理' +
        ' <small>销售收入更新</small>' +
         '</h1>'
    );
	$("#target").html('<form id="frm-target" class="bs-example bs-example-form" role="form" action="/hellojxc/updatesalesinput" method="POST">'+
		'<input type="hidden" id="id" name="id">' +
		'<div class="input-group">' + 
			'<span class="input-group-addon">客户名称</span>' +
			'<select name="customer_id" class="selectpicker_customer" data-live-search="true" data-style="btn-info"></select>' +
		'</div>' +
		'<br>' +
        '<div class="input-group">' +	
			'<span class="input-group-addon">关联对象</span>' +
			'<select id="target_id" name="target_id" class="selectpicker" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +   	
        '<div class="input-group">' +	
			'<span class="input-group-addon">支付方式</span>' +
			'<select id="pay_id" name="pay_id" class="selectpicker_pay" data-style="btn-info"></select>' +
        '</div>' +  
		'<br>' +  		
		'<div class="input-group">' +
			'<span class="input-group-addon">价格</span>' +
			'<input id="price" name="price" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
		'<div class="input-group">' +
			'<span class="input-group-addon">数量</span>' +
			'<input id="volume" name="volume" type="number" class="form-control" style="width:30%" digits required>' +
		'</div>' + 
		'<br>' + 
		'<div class="input-group">' +
			'<span class="input-group-addon">单位</span>' +
			'<select name="unit" class="selectpicker_unit" data-style="btn-info"></select>' +
		'</div>' + 
		'<br>' + 	
		'<div class="input-group">' +
			'<span class="input-group-addon">等级</span>' +
			'<select name="grade" class="selectpicker_grade" data-style="btn-info"></select>' +
		'</div>' + 
		'<br>' + 		
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input id="operationtime" name="operationtime" id="operationtime"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +  		
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注信息</span>' +
			'<textarea id="refer" name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' + 		
		'<button type="submit" class="btn btn-default">提交</button>');	
		
		$('#frm-target').on('submit', function(e){
			var selectedTarget = $(".selectpicker").val();
			if(selectedTarget === null || selectedTarget.length === 0)
			{
				alert("请选择一项关联对象");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
			
			var selectedCustomer = $(".selectpicker_customer").val();
			if(selectedCustomer === null || selectedCustomer.length === 0)
			{
				alert("请选择一项客户");
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
			}
		});
		
		//获取关联客户对象
		$('.selectpicker_customer').selectpicker();
        $.getJSON('/hellojxc/listcustomer',function(result){

            for(var i=0;i<result.data.length;i++)
            {
                if(i===0)
                    $('.selectpicker_customer').append("<option value=\""+result.data[i][0]+"\" selected=\"selected\">"+ result.data[i][1] +"</option>");
                else
                    $('.selectpicker_customer').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][1] +"</option>");
            }
        });
        $('.selectpicker_customer').selectpicker('refresh');
        $('.selectpicker_customer').selectpicker('render');
		
		var m_units = new Map();
		var m_grades = new Map();
        //获取关联日常管理对象
		$('.selectpicker').selectpicker();
        $.getJSON('/hellojxc/listsalesinputtarget',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				//units 3,grades 4
				m_units.set(result.data[i][0],result.data[i][3]);
				m_grades.set(result.data[i][0],result.data[i][4]);
                $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });
        $('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
			
        //获取支付方式
		$('.selectpicker_pay').selectpicker();
		$('.selectpicker_pay').append("<option value=\"0\">未支付</option>");
        $.getJSON('/hellojxc/listpay',function(result){
            for(var i=0;i<result.data.length;i++)
            {
				$('.selectpicker_pay').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
            }
        });	
		$('.selectpicker_pay').selectpicker('refresh');
		$('.selectpicker_pay').selectpicker('render');
		
		//初始化单位
		$('.selectpicker_unit').selectpicker();
		var refreshunits = function(currenttarget) {
			//var currenttarget = $('.selectpicker').val();
			var currentunits = m_units.get(currenttarget);
			var units = currentunits.split('-');
			$('.selectpicker_unit').empty();
			for(var i = 0 ; i < units.length; i++)
			{
				if(i ===0)
					$('.selectpicker_unit').append("<option value=\""+units[i]+"\" selected=\"selected\">"+ units[i] +"</option>");
				else
					$('.selectpicker_unit').append("<option value=\""+units[i]+"\">"+ units[i] +"</option>");
				
			}
			
			$('.selectpicker_unit').selectpicker('refresh');
			$('.selectpicker_unit').selectpicker('render');		
		};
		
		//初始化等级
		$('.selectpicker_grade').selectpicker();
		var refreshgrades = function(currenttarget) {
			var currenttarget = $('.selectpicker').val();
			var currentgrades = m_grades.get(currenttarget);
			var grades = currentgrades.split('-');
			$('.selectpicker_grade').empty();
			for(var i = 0 ; i < grades.length; i++)
			{
				if(i ===0)
					$('.selectpicker_grade').append("<option value=\""+grades[i]+"\" selected=\"selected\">"+ grades[i] +"</option>");
				else
					$('.selectpicker_grade').append("<option value=\""+grades[i]+"\">"+ grades[i] +"</option>");
				
			}
			
			$('.selectpicker_grade').selectpicker('refresh');
			$('.selectpicker_grade').selectpicker('render');		
		};	
		
		var geturl = "/hellojxc/getsalesinput?id=" + id;
		$.getJSON(geturl,function(result){
				$.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
					$("#id").val(field[0].id);
					//对象选择
					$(".selectpicker").selectpicker('val',field[0].target_id);
					$('.selectpicker').selectpicker('refresh');
					//单位和等级与对象的管理的联动
					var currenttarget = $('.selectpicker').val();
					refreshunits(currenttarget);
					refreshgrades(currenttarget);
					$(".selectpicker_unit").selectpicker('val',field[0].unit);
					$(".selectpicker_grade").selectpicker('val',field[0].grade);
					
					//支付方式选择
					$(".selectpicker_pay").selectpicker('val',field[0].pay_id);
					$('.selectpicker_pay').selectpicker('refresh');		
					
					//客户选择
					$(".selectpicker_customer").val(field[0].customer_id);  
					$('.selectpicker_customer').selectpicker('refresh');	
					
					$("#price").val(field[0].price);
					$("#volume").val(field[0].volume);
					$("#operationtime").val(field[0].operationtime).datepicker({ dateFormat: 'yy-mm-dd' });//购买时间
					$("#refer").val(field[0].refer);
					
				   return false;
				});
	   });
		//对象和等级，单位的联动
		$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
			var selected = $(e.currentTarget).val();
			refreshunits(selected);
			refreshgrades(selected);
		});
}

function analysistarget()
{
	$("section.content-header").html(
        '<h1>' +
		'管理对象资金出入统计' +
		' <small style="color:blue">利润 = 销售收入 + 日常收入-日常支出</small>' +
         '</h1>'
	);
		
	$("#target").html('<form id="frm-example">'+
	' <button  id="query" role="button" class="btn btn-primary">查询</button>' +
	//' <button  id="download" role="button" class="btn btn-info">下载</button>' +  
	'<br>' + 
	'<br>' + 
	'<p>开始日期：<input type="text" id="datepicker_start" name="datepicker_start"></p>'+
	'<p>结束日期：<input type="text" id="datepicker_end" name="datepicker_end"></p>'+
	'<br>' + 
	'<div class="input-group">' +	
		'<span class="input-group-addon">对象名称</span>' +
		'<select id="targetid" name="targetid" class="selectpicker" data-style="btn-info"></select>' +
	'</div>' + 			
	'<br>' + 		
	' <table id="example" class="display select" width="100%" cellspacing="0">' + 
	' <thead>' + 
	'   <tr>' + 
	'     <th>序号</th>' + 
	'     <th>对象名称</th>' + 
	'     <th>日常支出总计</th>' + 
	'     <th>日常收入总计</th>' + 
	'     <th>销售收入总计</th>' + 
	'     <th>利润总计</th>' + 	
	'   </tr>' + 
	'</thead>' + 
	/*
	'<tfoot>' + 
	'   <tr>' + 
	'     <th>序号</th>' + 
	'     <th>对象名称</th>' + 
	'     <th>日常支出总计</th>' + 
	'     <th>日常收入总计</th>' + 
	'     <th>销售收入总计</th>' + 
	'     <th>利润总计</th>' +     
	'   </tr>' + 
	'</tfoot>' + 
	*/
	 '</table>' + 
	 /*
	 '<br>' + 
	 ' <table id="detail" class="display select" width="100%" cellspacing="0">' + 
	 ' <thead>' + 
	 '   <tr>' + 
	 '     <th>序号</th>' + 
	 '     <th>对象名称</th>' + 
	 '     <th>日常支出</th>' + 
	 '     <th>日常收入</th>' + 
	 '     <th>销售收入</th>' + 
	 '     <th>交易日期</th>' + 		
	 '   </tr>' + 
	 '</thead>' + 
	 '<tfoot>' + 
	 '   <tr>' + 
	 '     <th>序号</th>' + 
	 '     <th>对象名称</th>' + 
	 '     <th>日常支出</th>' + 
	 '     <th>日常收入</th>' + 
	 '     <th>销售收入</th>' +   
	 '     <th>交易日期</th>' + 		
	 '   </tr>' + 
	 '</tfoot>' + 
	  '</table>' + 	
	  */ 
	 '<br>' + 
	 '<br>' + 
	 '<br>' + 	 
	'</form>'
	);

	//获取货物名称列表
	$.getJSON('/hellojxc/listtarget',function(result){
		$('.selectpicker').selectpicker();
		//默认选择所有对象
		$('.selectpicker').append("<option value=\"all\" selected=\"selected\">all</option>");
		for(var i=0;i<result.data.length;i++)
		{
			$('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
		}
		$('.selectpicker').selectpicker('refresh');
		$('.selectpicker').selectpicker('render');
	});
		
	var d = new Date(), ld = new Date(d.getFullYear(), d.getMonth()-6, 1);

	$("#datepicker_start").datepicker();
	$("#datepicker_start").datepicker("option", "dateFormat", "yy-mm-dd");
	$("#datepicker_start").val(ld.getFullYear() + '-' + (ld.getMonth() + 1) + '-' + ld.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });

	$("#datepicker_end").datepicker();
	$("#datepicker_end").datepicker("option", "dateFormat", "yy-mm-dd");
	$("#datepicker_end").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });

	/*
	var tabledetail;
	var refreshdetail = function() {
		var startday=$("#datepicker_start").val();
		var endday=$("#datepicker_end").val();
		var target_id=$("#targetid").val();
		//DataTable seems to be for API calls back into the object and dataTable seems to be the intialisation method.
		tabledetail = $('#detail').dataTable({
			searching: false,
			'ajax': {
				'url': '/hellojxc/finadetailbytarget',
				'type': 'POST'
			},
			'fnServerParams':function(aoData){
			aoData.push(
					{
						"name": "startday",
						"value": startday?startday:null
					},
					{
						"name":"endday",
						"value": endday?endday:null
					},
					{
						"name":"target_id",
						"value":target_id
					}
			);  
			},
			'order': [[1, 'asc']]
		});  	
	}
	refreshdetail();
	*/
	var table;
	var refresh = function() {
		var startday=$("#datepicker_start").val();
		var endday=$("#datepicker_end").val();
		var target_id=$("#targetid").val();
		//DataTable seems to be for API calls back into the object and dataTable seems to be the intialisation method.
		table = $('#example').DataTable({
			dom: 'Bfrtip',
			buttons:[
				{
					extend: 'excelHtml5',
					text: '导出excel',
					title: '对象资金统计入一览_' +  startday + '_TO_' + endday
				}
			],
			searching: false,
			'ajax': {
				'url': '/hellojxc/finabytarget',
				'type': 'POST'
			},
			'fnServerParams':function(aoData){
			aoData.push(
					{
						"name": "startday",
						"value": startday?startday:null
					},
					{
						"name":"endday",
						"value": endday?endday:null
					},
					{
						"name":"target_id",
						"value":target_id
					}
			);  
			},
			'order': [[0, 'asc']]
		});  

		//获取DataTable的查询结果json数据
		var api_table = $('#example').DataTable();
		var json = api_table.ajax.json();
		var name_array=new Array(json.data.length); 
		var daily_out_price=new Array(json.data.length);
		var daily_int_price=new Array(json.data.length); 
		var sales_int_price=new Array(json.data.length);
		var net_price=new Array(json.data.length); 		
		for(var i=0;i<json.data.length;i++)
		{
			name_array[i] = json.data[i][1];
			daily_out_price[i] = -json.data[i][2];
			daily_int_price[i] = json.data[i][3];
			sales_int_price[i] = json.data[i][4];
			net_price[i] = json.data[i][5];
		}

		$("#charttarget").attr("style","width: 600px;height:400px;");

		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('charttarget'));

		// 指定图表的配置项和数据
		var option = {
			title: {
				text: '资金出入一览'
			},
			tooltip: {},
			legend: {
				data:['日常支出','日常收入','销售收入','利润']
			},
			xAxis: {
				data: name_array//["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
			},
			yAxis: {},
			series: [
				{
					name: '日常支出',
					type: 'bar',
					data: daily_out_price//[5, 20, 36, 10, 10, 20]
				},
				{
					name: '日常收入',
					type: 'bar',
					data: daily_int_price//[5, 20, 36, 10, 10, 20]
				},
				{
					name: '销售收入',
					type: 'bar',
					data: sales_int_price//[5, 20, 36, 10, 10, 20]
				},
				{
					name: '利润',
					type: 'bar',
					data: net_price//[5, 20, 36, 10, 10, 20]
				},								
			]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option); 			
	}
	//加载页面时初始化datatable
	refresh();	
	
	$("#query").on('click', function(){ 
		if(table)
		{
			table.fnDestroy();
			refresh();
		}
		/*
		if(tabledetail)
		{
			tabledetail.fnDestroy();
			refreshdetail();
		}
		*/
		return false;
	});	

}

function analysispay()
{
	$("section.content-header").html(
        '<h1>' +
		'支付方式资金出入统计' +
		' <small style="color:blue">利润 = 销售收入 + 日常收入 - 日常支出</small>' +
         '</h1>'
	);	

	$("#target").html('<form id="frm-example">'+
		' <button  id="query" role="button" class="btn btn-primary">查询</button>' + 
		'<br>' + 
		'<br>' + 
		'<p>开始日期：<input type="text" id="datepicker_start" name="datepicker_start"></p>'+
		'<p>结束日期：<input type="text" id="datepicker_end" name="datepicker_end"></p>'+
		'<br>' + 
		'<div class="input-group">' +	
			'<span class="input-group-addon">支付账号</span>' +
			'<select id="pay_id" name="pay_id" class="selectpicker" data-style="btn-info"></select>' +
		'</div>' + 	
		'<br>' + 		
		' <table id="example" class="display select" width="100%" cellspacing="0">' + 
		' <thead>' + 
		'   <tr>' + 
		'     <th>序号</th>' + 
		'     <th>支付账号</th>' + 
		'     <th>日常支出</th>' + 
		'     <th>日常收入</th>' + 
		'     <th>销售收入</th>' + 
		'     <th>利润</th>' + 		
		'   </tr>' + 
		'</thead>' + 
		/*
		'<tfoot>' + 
		'   <tr>' + 
		'     <th>序号</th>' + 
		'     <th>支付账号</th>' + 
		'     <th>日常支出</th>' + 
		'     <th>日常收入</th>' + 
		'     <th>销售收入</th>' + 
		'     <th>利润</th>' + 	       
		'   </tr>' + 
		'</tfoot>' + 
		*/
		'</table>' + 
		'</form>'
	);
	$("#chartsrow").html(
		'	<div class="col-md-6">' +
		'		  <div class="box box-primary">' +
		'			<div class="box-header with-border">' +
		'			  <h3 class="box-title">收支一览(柱状图)</h3>' +
		'			  <div class="box-tools pull-right">' +
		'				<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>' +
		'				</button>' +
		'				<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>' +
		'			  </div>' +
		'			</div>' +
		'			<div class="box-body" style="display: block;">' +
		'			  <div id="charttarget1">' +
		'			  </div>' +
		'			</div>' +
		'		  </div>' +
		'	</div>' +
		'	<div class="col-md-6">' +
		'		  <div class="box box-primary">' +
		'			<div class="box-header with-border">' +
		'			  <h3 class="box-title">利润统计(饼状图)</h3>' +
		'			  <div class="box-tools pull-right">' +
		'				<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>' +
		'				</button>' +
		'				<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>' +
		'			  </div>' +
		'			</div>' +
		'			<div class="box-body">' +
		'			  <div id="charttarget2">' +
		'			  </div>' +
		'			</div>' +
		'		  </div>' +
		'	</div>' 	
	);

	//获取支付方式名称列表
	$.getJSON('/hellojxc/listpay',function(result){
		$('.selectpicker').selectpicker();
		$('.selectpicker').append("<option value=\"all\" selected=\"selected\">all</option>");
		$('.selectpicker').append("<option value=\"0\">未支付</option>");
		for(var i=0;i<result.data.length;i++)
		{
			$('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][2] +"</option>");
		}
		$('.selectpicker').selectpicker('refresh');
		$('.selectpicker').selectpicker('render');
	});
		
	var d = new Date(), ld = new Date(d.getFullYear(), d.getMonth()-6, 1);

	$("#datepicker_start").datepicker();
	$("#datepicker_start").datepicker("option", "dateFormat", "yy-mm-dd");
	$("#datepicker_start").val(ld.getFullYear() + '-' + (ld.getMonth() + 1) + '-' + ld.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });

	$("#datepicker_end").datepicker();
	$("#datepicker_end").datepicker("option", "dateFormat", "yy-mm-dd");
	$("#datepicker_end").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });

	var table;
	var refresh = function() {
		var startday=$("#datepicker_start").val();
		var endday=$("#datepicker_end").val();
		var pay_id=$("#pay_id").val();
		//DataTable seems to be for API calls back into the object and dataTable seems to be the intialisation method.
		table = $('#example').dataTable({
			searching: false,
			dom: 'Bfrtip',
			buttons:[
				{
					extend: 'excelHtml5',
					text: '导出excel',
					title: '支付资金统计一览_' +  startday + '_TO_' + endday
				}
			],			
			'ajax': {
				'url': '/hellojxc/finabypay',
				'type': 'POST'
			},
			'fnServerParams':function(aoData){
			aoData.push(
					{
						"name": "startday",
						"value": startday?startday:null
					},
					{
						"name":"endday",
						"value": endday?endday:null
					},
					{
						"name":"pay_id",
						"value":pay_id
					}
			);  
			},
			'order': [[0, 'asc']]
		});  

		//计算饼状图所需数据
		var api_table = $('#example').DataTable();
		var json = api_table.ajax.json();
		var name_array=new Array(json.data.length); 
		var daily_out_array=new Array(json.data.length); 
		var daily_in_array=new Array(json.data.length); 
		var sales_in_array=new Array(json.data.length); 
		var net_array=new Array(json.data.length);
		for(var i=0;i<json.data.length;i++)
		{
			name_array[i] = json.data[i][1];
			daily_out_array[i] = -json.data[i][2];
			daily_in_array[i] = json.data[i][3];
			sales_in_array[i] = json.data[i][4];
			net_array[i] = json.data[i][5];
		}
		
		$("#charttarget1").attr("style","width: 600px;height:400px;");
		
		// 基于准备好的dom，初始化echarts实例
		var myChart1 = echarts.init(document.getElementById('charttarget1'));

		// 指定图表的配置项和数据
		var option1 = {			
			title: {
				text: '收支一览'
			},
			
			tooltip: {},
			legend: {
				data:['日常支出','日常收入','销售收入','净收入']
			},
			xAxis: {
				data: name_array
			},
			yAxis: {},
			series: [
			{
				name: '日常支出',
				type: 'bar',
				data: daily_out_array
			},
			{
				name: '日常收入',
				type: 'bar',
				data: daily_in_array
			},
			{
				name: '销售收入',
				type: 'bar',
				data: sales_in_array
			},			
						{
				name: '净收入',
				type: 'bar',
				data: net_array
			}
			]
		};
		myChart1.setOption(option1); 
		
		
		//计算饼状图所需数据
		var daily_out_seriesData=[];
		var daily_in_seriesData=[];
		var sales_in_seriesData=[];
		var net_in_seriesData=[];
		for(var i=0;i<name_array.length;i++)
		{ 
			var obj1=new Object();
			obj1.name=name_array[i];
			obj1.value=daily_out_array[i];
			daily_out_seriesData[i]=obj1;

			var obj2=new Object();
			obj2.name=name_array[i];
			obj2.value=daily_in_array[i];
			daily_in_seriesData[i]=obj2;
			
			var obj3=new Object();
			obj3.name=name_array[i];
			obj3.value=sales_in_array[i];
			sales_in_seriesData[i]=obj3;

			var obj4=new Object();
			obj4.name=name_array[i];
			obj4.value=net_array[i];
			net_in_seriesData[i]=obj4;			
		}

		$("#charttarget2").attr("style","width: 600px;height:400px;");
		var myCharts2 = echarts.init(document.getElementById('charttarget2'));
		var option2 = {
			title : {
					text: '利润统计',
					//subtext: '纯属虚构',
					x:'center'
				},
				tooltip : {
					trigger: 'item',
					formatter: "{a} <br/>{b} : {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					left: 'left',
					data: name_array
				},
				series : [
					{
						name: '利润统计',
						type: 'pie',
						radius : '55%',
						center: ['50%', '60%'],
						data: net_in_seriesData,
						itemStyle: {
							emphasis: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}
				]			
		};
		myCharts2.setOption(option2);	
	}
	//加载页面时初始化datatable
	refresh();	

	$("#query").on('click', function(){ 
	if(table)
	{
		table.fnDestroy();
		refresh();
	}
	return false;
	});	
}