function show_updatecustomer(id)
{

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

// list customers
function list_addcustomer()
{
      $("#target").html('<form id="frm-example">'+
            '<div class="container">' + 
           '<a class="btn btn-primary" id="addcustomer" href="index.html?function=addcustomer" role="button">添加</a> ' + 
           ' <button  id="del" role="button" class="btn btn-primary">删除</button>' + 
           ' <button  id="update" role="button" class="btn btn-primary">更新</button>' + 
        '</div>' + 
    '<br>' + 
       ' <table id="example" class="display select" width="100%" cellspacing="0">' + 
       ' <thead>' + 
        '   <tr>' + 
        '     <th><input type="checkbox" name="select_all" value="1" id="example-select-all"></th>' + 
        '     <th>name</th>' + 
        '     <th>mobile</th>' + 
        '     <th>location</th>' + 
        '     <th>type</th>' + 
        '   </tr>' + 
        '</thead>' + 
        '<tfoot>' + 
        '   <tr>' + 
        '     <th></th>' + 
        '     <th>name</th>' + 
        '     <th>mobile</th>' + 
        '     <th>location</th>' + 
        '     <th>type</th>' + 
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
      'columnDefs': [{
         'targets': 0,
         'searchable': false,
         'orderable': false,
         'className': 'dt-body-left',
         'render': function (data, type, full, meta){
             return '<input type="checkbox" name="id[]" value="' + $('<div/>').text(data).html() + '">';
         }
      }],
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
         if(!$.contains(document, this)){
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
         }
      });
      
      if(anyselected == 0)
      {
          alert("请选取需要删除的记录");
          event.preventDefault();//必须调用，否则即使返回false，submit事件还会被触发一次，导致alert出现2次
          return false;//返回false，取消submit
      }
   });
}

   
   
   