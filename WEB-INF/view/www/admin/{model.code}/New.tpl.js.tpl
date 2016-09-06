Ext.define('${model.code}.New',{
	extend: '${model.code}.Form'
	,alias: ['widget.${model.code.toLowerCase()}new']
	,constrainHeader: true
	,maximizable: true
	,buttons:[{
		text: '${Escape.d}view.save'
		,fixed: true
		,handler: function(btn,e){			
		    var ar = new ActiveRecord('${model.code}');
			var fp = btn.up('form');
			var task = new Ext.util.DelayedTask(function(){
				var rs = ar.save();
				if(rs.status == 1){
					var win = btn.up('window');	
					win.remove(fp,false);
					win.add({xtype: '${model.code.toLowerCase()}edit',postman:{ar: ar}});
					ar.write(win);
					win.setTitle('${Escape.d}view.edit ' + ar.data.${model.displayField});					    
					Locale.success();
				}else{
					Locale.failure(rs.status,rs.msg);
				}
				btn.enable();
				btn.setText('${Escape.d}view.save');
				var store = Ext.data.StoreManager.lookup('${model.code}-Store');
				if(store){
				    store.reload();
				}
			});
			new Ext.util.DelayedTask(function(){				
				var form = fp.getForm();
				if(form.isValid()){
					ar.read(fp);
					btn.setText('${Escape.d}view.saveing');
					btn.disable();					
					task.delay(200);
				}				
			}).delay(1);					
		}
	}]
	,
	initComponent: function() { 
	    var me = this;
        me.callParent(arguments);
		var ar = new ActiveRecord('${model.code}');
		ar.setValidates(me,${Escape.d}${model.code}.config);		
   }
});