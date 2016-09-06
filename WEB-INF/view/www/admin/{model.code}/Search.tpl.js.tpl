Ext.define('${model.code}.Search',{
	extend: 'Ext.form.Panel'
	,alias: ['widget.${model.code.toLowerCase()}search']
	,border: 0
	,autoScroll: true
	,buttonAlign: 'align'
	,bodyPadding: '10 5 5 5'
	,buttonAlign: 'center'
	,defaults:{
		labelWidth: 70,
		padding: '5 10 5 5'
	}
	,layout: { 
			type: 'column'
	}
	,items:[
#set($index=0)
#foreach($field in $model.knownFields)
#set($classType=$field.getClassType())
#set($javaType=$classType.getJavaType())
#if($index != 0)
       ,
#end
#if($field.isNumber())
	   {
		 xtype: 'numberfield'
	     ,name: 'and[eq_${field.code}]'
		 ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
	   	 }
#elseif($javaType.equals('Boolean'))
	   {
	     xtype: 'checkboxfield'
	     ,name: 'and[eq_${field.code}]'
	     ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
	    }
#elseif($javaType.equals('java.sql.Timestamp'))
	   {
		  xtype: 'datefield'
		  ,name: 'and[eq_${field.code}]'
		  ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
		 }
#elseif($javaType.equals('java.sql.Date'))
	   {
       xtype: 'datefield'
  	   ,name: 'and[eq_${field.code}]'
       ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
	    }
#elseif($javaType.equals('java.sql.Time'))
	   {
			  xtype: 'timefield'
			  ,name: 'and[eq_${field.code}]'
			  ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
		 }
#else
	   {
	     xtype: 'textfield'
		 ,name: 'and[any_${field.code}]'
	     ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
	   }
#end
#set($index=$index+1)
#end	  
	]
	,buttons:[{
		text: '${Escape.d}view.search'
		,icon: '${Escape.d}g.options.images/search.png'
		,fixed: true
		,handler: function(btn,e){
			var task = new Ext.util.DelayedTask(function(){
			    var params = {};
	        	var form = btn.up('form').getForm();
	        	var store = Ext.data.StoreManager.lookup('${model.code}-Store');
	        	Ext.apply(params,store.proxy.extraParams);
	        	Ext.apply(params,form.getValues() || {});
	        	store.currentPage = 1;
	        	store.proxy.extraParams = params;			
	        	store.reload();
	        	Locale.searchSuccess();	        	
				btn.enable();
				btn.setText('${Escape.d}view.search');
			});	
			new Ext.util.DelayedTask(function(){
				btn.disable();
				btn.setText('${Escape.d}view.searching');				
	        	task.delay(200);
			}).delay(1);
		}
	}]

});

