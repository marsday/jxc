
// list customers
function list_addcustomer()
{
      $("section.content-header").html(
            '<h1>' +
            '客户管理' +
            ' <small>客户一览</small>' +
             '</h1>'
        );      
      $("#target").html(
             '<form id="frm-example">'+
               // '<div class="container">' + 
                    '<a class="btn btn-primary" id="add" href="index.html?function=addcustomer">添加</a> ' + "\n" + 
                    '<button  id="del" type="button" class="btn btn-danger">删除</button>' +  "\n" + 
                    '<button  id="update" type="button" class="btn btn-primary">更新</button>' + 
                //'</div>' + 
                '<br>' + 
                 '<br>' + 
                ' <table id="example" class="display select" width="100%" cellspacing="0">' + 
                    ' <thead>' + 
                        '   <tr>' + 
                        '     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
                        '     <th>姓名</th>' + 
                        '     <th>电话</th>' + 
                        '     <th>地点</th>' + 
                        '     <th>类型</th>' + 
                        '   </tr>' + 
                     '</thead>' + 
                    '<tfoot>' + 
                    '   <tr>' + 
                    '     <th></th>' + 
                        '     <th>姓名</th>' + 
                        '     <th>电话</th>' + 
                        '     <th>地点</th>' + 
                        '     <th>类型</th>' + 
                    '   </tr>' + 
                    '</tfoot>' + 
                 '</table>' + 
             '</form>'
    );
    var table = $('#example').DataTable({
	   searching: false,
      'ajax': {
         'url': '/hellojxc/listcustomers'
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
                'targets': 4,
                'searchable': false,
                //'orderable': false,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
                    //return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
                    if(data === "0")
                        return "买家";
                    else if(data === "1")
                        return "卖家";
                    else
                        return "买家兼卖家";
                }
        }        
        ],
      'order': [[1, 'asc']]
    });
    
    $("#del").on('click', function(){
        $("#frm-example").attr("action","/hellojxc/delcustomer");
        $("#frm-example").attr("method","POST");
        $("#frm-example").submit();
    });

    $("#update").on('click', function(){
      //只获取第一个被选取的，如果没有，则出提示信息
      var isok = 0;
      //var form = this;
      var getparam="index.html?function=showcustomer&&id=";
      // Iterate over all checkboxes in the table
      table.$('input[type="checkbox"]').each(function(){
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
      }
   });
}

function show_updatecustomer(id)
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
			'<input id="name" name="name" type="text" class="form-control" style="width:30%">' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">地点</span>' +
			'<input id="location" name="location" type="text" class="form-control" style="width:30%">' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">电话</span>' +
			'<input id="mobile" name="mobile" type="text" class="form-control" style="width:30%" placeholder="130xxxxxxxx">' +
		'</div>' + 
		'<br>' +
		'<div class="input-group">' +	
			'<span class="input-group-addon">客户类型</span>' +
			'<select id="slk" name="type" class="selectpicker" data-style="btn-info" ></select>' +
                 '</div>' +      
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');  
           
    var geturl = "/hellojxc/getcustomer?id=" + id;
        $.getJSON(geturl,function(result){
            $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
                //alert(field[0].name);//id,name,mobile,location,type
                $("#id").val(field[0].id);
                $("#name").val(field[0].name);
                $("#location").val(field[0].location);
                $("#mobile").val(field[0].mobile);
                
                $('.selectpicker').selectpicker();
                
                if(field[0].type === '0')
                {
                    $('.selectpicker').append("<option value=\"0\" selected=\"selected\" >买家</option>");
                    $('.selectpicker').append("<option value=\"1\">卖家</option>");
                    $('.selectpicker').append("<option value=\"2\">买家兼卖家</option>");
                }
                else if(field[0].type === '1')
                {
                    $('.selectpicker').append("<option value=\"0\">买家</option>");
                    $('.selectpicker').append("<option value=\"1\" selected=\"selected\" >卖家</option>");
                    $('.selectpicker').append("<option value=\"2\">买家兼卖家</option>");                   
                } else if(field[0].type === '2')
                {
                    $('.selectpicker').append("<option value=\"0\">买家</option>");
                    $('.selectpicker').append("<option value=\"1\">卖家</option>");
                    $('.selectpicker').append("<option value=\"2\" selected=\"selected\" >买家兼卖家</option>");
                }
               $('.selectpicker').selectpicker('refresh');
               $('.selectpicker').selectpicker('render');
               
               return false;
            });
         });
    // alert("show_updatecustomer");
}

function show_addcustomer()
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
			'<input name="name" type="text" class="form-control" style="width:30%">' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">地点</span>' +
			'<input name="location" type="text" class="form-control" style="width:30%">' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">电话</span>' +
			'<input name="mobile" type="text" class="form-control" style="width:30%" placeholder="130xxxxxxxx">' +
		'</div>' + 
		'<br>' +
		'<div class="input-group">' +	
			'<span class="input-group-addon">客户类型</span>' +
			'<select name="type" class="selectpicker" data-style="btn-info" ></select>' +
                '</div>' +      
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');
		
	$('.selectpicker').selectpicker();
	$('.selectpicker').append("<option value=\"0\" selected=\"selected\" >买家</option>");
	$('.selectpicker').append("<option value=\"1\">卖家</option>");
	$('.selectpicker').append("<option value=\"2\">买家兼卖家</option>");
	
	$('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
}

function clear_target()
{
    $("#target").html('');
}

function listgoods()
{
  $("section.content-header").html(
            '<h1>' +
            '货物管理' +
            ' <small>货物一览</small>' +
             '</h1>'
        );      
      $("#target").html('<form id="frm-example">'+
           // '<div class="container">' + 
           '<a class="btn btn-primary" id="add" href="index.html?function=addgoods" role="button">添加</a> ' + "\n" + 
           ' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
           ' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
        //'</div>' + 
    '<br>' + 
     '<br>' + 
       ' <table id="example" class="display select" width="100%" cellspacing="0">' + 
       ' <thead>' + 
        '   <tr>' + 
        '     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
        '     <th>名称</th>' + 
        '     <th>单位</th>' + 
        '     <th>类型</th>' + 
        '   </tr>' + 
        '</thead>' + 
        '<tfoot>' + 
        '   <tr>' + 
        '     <th></th>' + 
        '     <th>名称</th>' + 
        '     <th>单位</th>' + 
        '     <th>类型</th>' + 
        '   </tr>' + 
        '</tfoot>' + 
         '</table>' + 
    '</form>'
    );
    
    var table = $('#example').DataTable({
	   searching: false,
      'ajax': {
         'url': '/hellojxc/listgoods'
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
                'searchable': false,
                //'orderable': false,
                'className': 'dt-body-left',
                'render': function (data, type, full, meta){
                    if(data === "0")
                        return "核销货物";
                    else
                        return "非核销货物";
                }
             }
        ],
      'order': [[1, 'asc']]
    });
    
    $("#del").on('click', function(){
        $("#frm-example").attr("action","/hellojxc/delgoods");
        $("#frm-example").attr("method","POST");
        $("#frm-example").submit();
    });

    $("#update").on('click', function(){
      //只获取第一个被选取的，如果没有，则出提示信息
      var isok = 0;
      //var form = this;
      var getparam="index.html?function=showgoods&&id=";
      // Iterate over all checkboxes in the table
      table.$('input[type="checkbox"]').each(function(){
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
      }
   });   
}

 function show_addgoods()
{
          $("section.content-header").html(
            '<h1>' +
            '货物管理' +
            ' <small>货物添加</small>' +
             '</h1>'
        ); 

	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/addgoods" method="POST">'+
		'<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input name="name" type="text" class="form-control" style="width:30%">' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">单位</span>' +
			'<input name="unit" type="text" class="form-control" style="width:30%">' +
		'</div>' + 
		'<br>' +                
                '<div class="input-group">' +	
			'<span class="input-group-addon">货物类型</span>' +
			'<select name="type" class="selectpicker" data-style="btn-info" ></select>' +
                '</div>' +  
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');
        
        $('.selectpicker').selectpicker();
	$('.selectpicker').append("<option value=\"0\" selected=\"selected\" >核销货物</option>");
	$('.selectpicker').append("<option value=\"1\">非核销货物</option>");
	
	$('.selectpicker').selectpicker('refresh');
        $('.selectpicker').selectpicker('render');
        
}
  
 function show_updategoods(id)
{
      $("section.content-header").html(
            '<h1>' +
            '货物管理' +
            ' <small>货物更新</small>' +
             '</h1>'
        ); 
    	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/updategoods" method="POST">'+
		'<input type="hidden" id="id" name="id">' +
                '<div class="input-group">' + 
			'<span class="input-group-addon">名称*</span>' +
			'<input id="name" name="name" type="text" class="form-control" style="width:30%">' +
		'</div>' +
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">单位</span>' +
			'<input id="unit" name="unit" type="text" class="form-control" style="width:30%">' +
		'</div>' +   
		'<br>' +
		'<div class="input-group">' +	
			'<span class="input-group-addon">货物类型</span>' +
			'<select id="slk" name="type" class="selectpicker" data-style="btn-info" ></select>' +
                 '</div>' +                   
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');  
           
    var geturl = "/hellojxc/getgoods?id=" + id;
        $.getJSON(geturl,function(result){
            $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
                //alert(field[0].name);//id,name,mobile,location,type
                $("#id").val(field[0].id);
                $("#name").val(field[0].name);
                $("#unit").val(field[0].unit);   

                $('.selectpicker').selectpicker();               
                if(field[0].type === '0')
                {
                    $('.selectpicker').append("<option value=\"0\" selected=\"selected\" >核销货物</option>");
                    $('.selectpicker').append("<option value=\"1\">非核销货物</option>");
                }
                else
                {
                    $('.selectpicker').append("<option value=\"0\">核销货物</option>");
                    $('.selectpicker').append("<option value=\"1\" selected=\"selected\" >非核销货物</option>");                 
                } 
               $('.selectpicker').selectpicker('refresh');
               $('.selectpicker').selectpicker('render');                
                
               return false;
            });
         });
}
  
function listinput()
{
  $("section.content-header").html(
            '<h1>' +
            '进货管理' +
            ' <small>进货一览</small>' +
             '</h1>'
        );      
      $("#target").html('<form id="frm-example">'+
           // '<div class="container">' + 
           '<a class="btn btn-primary" id="add" href="index.html?function=addinput" role="button">添加</a> ' + "\n" + 
           ' <button  id="del" role="button" class="btn btn-danger">删除</button>' + "\n" + 
           ' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
           ' <button  id="query" role="button" class="btn btn-primary">查询</button>' + 
        '<br>' + 
        '<br>' + 
      '<p>开始日期：<input type="text" id="datepicker_start" name="datepicker_start"></p>'+
      '<p>结束日期：<input type="text" id="datepicker_end" name="datepicker_end"></p>'+
     '<br>' + 
       ' <table id="example" class="display select" width="100%" cellspacing="0">' + 
       ' <thead>' + 
        '   <tr>' + 
        '     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
        '     <th>货物名称</th>' + 
        '     <th>数量</th>' + 
        '     <th>总价</th>' + 
        '     <th>购买日期</th>' + 
        '     <th>记录日期</th>' + 
        '     <th>经办人</th>' + 
        '     <th>备注</th>' + 
        '   </tr>' + 
        '</thead>' + 
        '<tfoot>' + 
        '   <tr>' + 
        '     <th></th>' + 
        '     <th>货物名称</th>' + 
        '     <th>数量</th>' + 
        '     <th>总价</th>' + 
        '     <th>购买日期</th>' + 
        '     <th>记录日期</th>' + 
        '     <th>经办人</th>' + 
        '     <th>备注</th>' +         
        '   </tr>' + 
        '</tfoot>' + 
         '</table>' + 
    '</form>'
    );

    var d = new Date(), ld = new Date(d.getFullYear(), d.getMonth(), 1);
    
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
             'ajax': {
                'url': '/hellojxc/listinput',
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
                   }
               ],
             'order': [[1, 'asc']]
           });       
    }
    //加载页面时初始化datatable
    refresh();
    
    $("#del").on('click', function(){
        $("#frm-example").attr("action","/hellojxc/delinput");
        $("#frm-example").attr("method","POST");
        $("#frm-example").submit();
    });

    $("#update").on('click', function(){
      //只获取第一个被选取的，如果没有，则出提示信息
      var isok = 0;
      //var form = this;
      var getparam="index.html?function=showinput&&id=";
      // Iterate over all checkboxes in the table
      table.$('input[type="checkbox"]').each(function(){
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

    $("#query").on('click', function(){ 
       if(table)
       {
           table.fnDestroy();
           refresh();
       }
       return false;
     });
   // Handle click on "Select all" control
   $('#example-select-all').on('click', function(){
      // Get all rows with search applied
      var t = $('#example').DataTable();
      var rows = t.rows({ 'search': 'applied' }).nodes();
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
      }
   });
}

 function addinput()
{
          $("section.content-header").html(
            '<h1>' +
            '进货管理' +
            ' <small>进货记录添加</small>' +
             '</h1>'
        ); 

	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/addinput" method="POST">'+
                '<div class="input-group">' +	
			'<span class="input-group-addon">货物名称</span>' +
			'<select name="goodsname" class="selectpicker" data-style="btn-info"></select>' +
                '</div>' +  
		'<br>' +                
		'<div class="input-group">' + 
			'<span class="input-group-addon">数量  </span>' +
			'<input name="volume" type="text" class="form-control" style="width:30%">' +
		'</div>' +                
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">总价  </span>' +
			'<input name="price" type="text" class="form-control" style="width:30%">' +
		'</div>' + 
		'<br>' + 
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input name="operation_day" id="operation_day"  type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +   
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注  </span>' +
			'<textarea name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' + 
		'<br>' +                
		'<button type="submit" class="btn btn-default">提交</button>');
        //初始化交易日期：默认为当天
        var d = new Date();
        $("#operation_day").datepicker();
        $("#operation_day").datepicker("option", "dateFormat", "yy-mm-dd");
        $("#operation_day").val(d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()).datepicker({ dateFormat: 'yy-mm-dd' });
    
        //获取货物名称列表
        $.getJSON('/hellojxc/listgoods',function(result){
            $('.selectpicker').selectpicker();
            for(var i=0;i<result.data.length;i++)
            {
                if(i===0)
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\" selected=\"selected\">"+ result.data[i][0] +"</option>");
                else
                    $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][0] +"</option>");
            }
            $('.selectpicker').selectpicker('refresh');
            $('.selectpicker').selectpicker('render');
        });
}

 function showinput(id)
{
      $("section.content-header").html(
            '<h1>' +
            '进货管理' +
            ' <small>进货记录更新</small>' +
             '</h1>'
        ); 
    	$("#target").html('<form class="bs-example bs-example-form" role="form" action="/hellojxc/updateinput" method="POST">'+
		'<input type="hidden" id="id" name="id">' +
                '<div class="input-group">' +	
			'<span class="input-group-addon">货物名称</span>' +
			'<select name="goodsname" class="selectpicker" data-style="btn-info"></select>' +
                '</div>' +  
		'<br>' +                
		'<div class="input-group">' + 
			'<span class="input-group-addon">数量  </span>' +
			'<input id="volume" name="volume" type="text" class="form-control" style="width:30%">' +
		'</div>' +                
		'<br>' +
		'<div class="input-group">' +
			'<span class="input-group-addon">总价  </span>' +
			'<input id="price" name="price" type="text" class="form-control" style="width:30%">' +
		'</div>' + 
		'<br>' + 
 		'<div class="input-group">' +
			'<span class="input-group-addon">交易日期</span>' +
			'<input id="buytime" name="buytime" type="text" class="form-control" style="width:28%">' +
		'</div>' + 
		'<br>' +   
		'<div class="input-group">' +
			'<span class="input-group-addon">经办人</span>' +
			'<input id="operator" name="operator" type="text" readonly class="form-control" style="width:30%">' +
		'</div>' + 
		'<br>' +                 
 		'<div class="input-group">' +
			'<span class="input-group-addon">备注  </span>' +
			'<textarea id="refer" name="refer" class="form-control" style="width:30%"></textarea>' +
		'</div>' +         
		'<br>' +
		'<button type="submit" class="btn btn-default">提交</button>');  
    //购买日期初始化
    $("#buytime").datepicker();
    $("#buytime").datepicker("option", "dateFormat", "yy-mm-dd");
    
    //重要：改为同步执行，否则多个getJSON的情况下，会数据混乱
    $.ajaxSettings.async = false;
    //获取货物名称列表
    $.getJSON('/hellojxc/listgoods',function(result){
            $('.selectpicker').selectpicker();
            for(var i=0;i<result.data.length;i++)
            {
                $('.selectpicker').append("<option value=\""+result.data[i][0]+"\">"+ result.data[i][0] +"</option>");
            }
            $('.selectpicker').selectpicker('refresh');
            $('.selectpicker').selectpicker('render');
    });  	
    var geturl = "/hellojxc/getinput?id=" + id;
    $.getJSON(geturl,function(result){
            $.each(result, function(i, field){//就一条json数据，每条json数据里也只有一条记录
                $("#id").val(field[0].id);
                $("#volume").val(field[0].volume);   
                $("#price").val(field[0].price);
                $("#buytime").val(field[0].buytime).datepicker({ dateFormat: 'yy-mm-dd' });//购买时间
                $("#operator").val(field[0].operator);
                $("#refer").val(field[0].refer);
                
                $(".selectpicker").selectpicker('val',field[0].goods_name);//货物名称选择
                $('.selectpicker').selectpicker('refresh');
               return false;
            });
   });
   //恢复设置为异步执行
   $.ajaxSettings.async = true;
}
  