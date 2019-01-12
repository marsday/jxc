
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

//要完善接口
function listcommonoperation(type,table)
{	
	var delaction;
	var showaction;
	if(type === 'target')
	{
		delaction = '/hellojxc/deltarget';
		showaction = 'index.html?function=showtarget&&id=';
	}else if(type === 'xxxxx')
	{
		
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
			if(mymessage==false)
			{
				event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
				return false;//返回false，取消submit
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
                    if(full[5] === '1')
						return '-';  
				   else
					   return data;
                }
            }
            ,{
                'targets': 4,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
                    if(full[5] === '1')
						return '-';  
				   else
					   return data;
                }
            }			
            ,{
                'targets': 5,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
                    if(data === "0")
                        return "销售对象";
                    else if (data === "1")
                        return "日常对象";
					else
						return "两者兼有";
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
		
	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/addtarget" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input name="name" type="text" class="form-control" style="width:30%" maxlength="20" required>' +
		'</div>' +
		'<br>' +                
        '<div class="input-group">' +	
			'<span class="input-group-addon">对象类型</span>' +
			'<select name="type" class="selectpicker" data-style="btn-info" ></select>' +
         '</div>' +  
		'<br>' +
		'<div class="input-group">' + 
			'<span class="input-group-addon">单位</span>' +
			'<input id="units" name="units" type="text" class="form-control" style="width:30%" maxlength="200" placeholder="单位1,单位2,单位3" required>' +
		'</div>' +
		'<br>' +  	
		'<div class="input-group">' + 
			'<span class="input-group-addon">级别</span>' +
			'<input id="grades" name="grades" type="text" class="form-control" style="width:30%" maxlength="200" placeholder="级别1,级别2,级别3" required>' +
		'</div>' +
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');
        
    $('.selectpicker').selectpicker();
	$('.selectpicker').append("<option value=\"0\" selected=\"selected\" >销售对象</option>");
	$('.selectpicker').append("<option value=\"1\">日常对象</option>");
	$('.selectpicker').append("<option value=\"2\">两者兼有</option>");
	
	$('.selectpicker').selectpicker('refresh');
    $('.selectpicker').selectpicker('render');
	
	$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
		var selected = $(e.currentTarget).val();
		if(selected === "1")
		{
			$("#units").attr("disabled","true");
			$("#grades").attr("disabled","true");
		}else
		{
			$("#units").removeAttr("disabled");
			$("#grades").removeAttr("disabled");
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
    	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/updatetarget" method="POST">'+
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
		'<div class="input-group">' +	
			'<span class="input-group-addon">对象类型</span>' +
			'<select id="slk" name="type" class="selectpicker" data-style="btn-info" ></select>' +
                 '</div>' +                   
		'<br>' +
		'<div class="input-group">' + 
			'<span class="input-group-addon">单位</span>' +
			'<input id="units" name="units" type="text" class="form-control" style="width:30%" maxlength="200" placeholder="单位1,单位2,单位3" required>' +
		'</div>' +
		'<br>' +  	
		'<div class="input-group">' + 
			'<span class="input-group-addon">级别</span>' +
			'<input id="grades" name="grades" type="text" class="form-control" style="width:30%" maxlength="200" placeholder="级别1,级别2,级别3" required>' +
		'</div>' +
		'<br>' +		
		'<button type="submit" class="btn btn-default">提交</button>');  
           
    var geturl = "/hellojxc/gettarget?id=" + id;
        $.getJSON(geturl,function(result){
            $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
                //alert(field[0].name);//id,name,mobile,location,type
                $("#id").val(field[0].id);
                $("#name").val(field[0].name);
				$("#units").val(field[0].units);
				$("#grades").val(field[0].grades);
				
                $('.selectpicker').selectpicker();               
                if(field[0].type === '0')
                {
                    $('.selectpicker').append("<option value=\"0\" selected=\"selected\" >销售对象</option>");
                    $('.selectpicker').append("<option value=\"1\">日常对象</option>");
					$('.selectpicker').append("<option value=\"2\">两者兼有</option>");
                }
                else if(field[0].type === '1')
                {
                    $('.selectpicker').append("<option value=\"0\">销售对象</option>");
                    $('.selectpicker').append("<option value=\"1\" selected=\"selected\" >日常对象</option>");  
					$('.selectpicker').append("<option value=\"2\">两者兼有</option>");	
					
					$("#units").attr("disabled","true");
					$("#grades").attr("disabled","true");
                }else
				{
					$('.selectpicker').append("<option value=\"0\">销售对象</option>");
					$('.selectpicker').append("<option value=\"1\">日常对象</option>");
					$('.selectpicker').append("<option value=\"2\" selected=\"selected\">两者兼有</option>");
				}
                $('.selectpicker').selectpicker('refresh');
                $('.selectpicker').selectpicker('render');                
 
				$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, newValue, oldValue) {
					var selected = $(e.currentTarget).val();
					if(selected === "1")
					{
						$("#units").attr("disabled","true");
						$("#grades").attr("disabled","true");
					}else
					{
						$("#units").removeAttr("disabled");
						$("#grades").removeAttr("disabled");
					}
				}); 
               return false;
            });
         });
}
