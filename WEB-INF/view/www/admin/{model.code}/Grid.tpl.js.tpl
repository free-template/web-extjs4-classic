Ext.define('${model.code}.Grid',{
	extend: 'Ext.grid.Panel'
	,alias: ['widget.${model.code.toLowerCase()}grid']
	,requires: [
	            '${model.code}.New'
	            ,'${model.code}.Edit'
 				,'${model.code}.Window'
	          ]
	,forceFit: true
	,store: {
		storeId:'${model.code}-Store'
		,fields: [
		          'as_${model.tableName}_id'
#foreach($field in $model.knownFields)
		          ,'as_${model.tableName}_$field.code'
#end		          
		 ]
		,proxy: {
			type: 'ajax'
			,url: '${Escape.d}g.options.domainUrl/${model.code}/list/admin'
			,reader: {
				type: 'json'
				,root: 'data'
				,totalProperty: 'total' 
			}
			,extraParams: {
				extraAnd: "{eq_deleted:false}" 
				,extraOr: "{}"
			}
		}
		,autoLoad: false
		,remoteSort: true
		,sorters: [{property: 'as_${model.tableName}_created_at', direction: 'DESC'}]		
	}
	,columns:[
        {xtype: 'rownumberer',text:'NO.',width:40,align:'center'}
		,{
			dataIndex: 'as_${model.tableName}_id'
			,hidden: true
			,hideable:false
		}
#foreach($field in $model.knownFields)
#set($classType=$field.getClassType())
#if($field.isNumber())
	   ,{
		    text: '${Escape.d}${model.code}.${field.code}'
	        ,dataIndex: 'as_${model.tableName}_${field.code}'
	        ,editor: {
		            xtype: 'numberfield'
	        }
		}
#elseif($classType.javaType == 'java.sql.Timestamp')
		,{
			xtype: 'datecolumn'
			,format: Formats.datetime
			,text: '${Escape.d}${model.code}.${field.code}'
			,dataIndex: 'as_${model.tableName}_${field.code}'
			,renderer: function(value){
				if(typeof(value) == 'string')
				  return value;
				else
				  return Ext.Date.format(value,Formats.datetime);
			}
		    ,editor: {
				    xtype: 'datetimefield'
			}
		}
#elseif($classType.javaType == 'java.sql.Date')
		,{
			xtype: 'datecolumn'
			,format: Formats.date
			,text: '${Escape.d}${model.code}.${field.code}'
			,dataIndex: 'as_${model.tableName}_${field.code}'
			,renderer: function(value){
				if(typeof(value) == 'string')
				  return value;
				else
				  return Ext.Date.format(value,Formats.datetime);
			}
		    ,editor: {
				    xtype: 'datefield'
			}
		}
#elseif($classType.javaType == 'java.sql.Time')
		,{
			xtype: 'datecolumn'
		    ,format: Formats.time
			,text: '${Escape.d}${model.code}.${field.code}'
			,dataIndex: 'as_${model.tableName}_${field.code}'
			,renderer: function(value){
				if(typeof(value) == 'string')
				  return value;
				else
				  return Ext.Date.format(value,Formats.datetime);
			}
		    ,editor: {
				    xtype: 'timefield'
			}
		}
#elseif($classType.javaType == 'Boolean')
	   ,{
		    text: '${Escape.d}${model.code}.${field.code}'
	        ,dataIndex: 'as_${model.tableName}_${field.code}'
	        ,editor: {
		            xtype: 'checkboxfield'
	        }
		}
#else
	   ,{
		    text: '${Escape.d}${model.code}.${field.code}'
	        ,dataIndex: 'as_${model.tableName}_${field.code}'
	        ,editor: {
		            xtype: 'textfield'
	        }
		}
#end
#end

		
		,{
      	  text: '${Escape.d}view.edit'
          ,xtype:'actioncolumn'
          ,width:46
          ,lockable:true
          ,menuDisabled: true
          ,hideable:false
          ,align:'center'
          ,items: [{
              icon: '${Escape.d}g.options.images/edit.png'
              ,tooltip: '${Escape.d}view.edit'
              ,handler: function(grid, rowIndex, colIndex) {
                  	var row = grid.getStore().getAt(rowIndex);
                  	var id = row.get('as_${model.tableName}_id');
                  	grid.select(rowIndex);
                  	var ar = new ActiveRecord('${model.code}',id);
                  	var winId = '${model.code.toLowerCase()}window-' + ar.data.id;
                  	if(ar.data !== null){
                  		var edit = Ext.getCmp(winId);
                  		if(!edit){
	                  		edit = new ${model.code}.Window({
	                  			id: winId
	                  			,items: [{
		                  					xtype: '${model.code.toLowerCase()}edit'
		                  					,postman: {ar: ar}
	                  					}]
	                  		});
                  		}
                  		edit.setTitle('${Escape.d}view.edit ' + ar.data.${model.displayField});
                  		ar.write(edit);
                  		edit.show();
                    }else{
                    	Locale.failure(rs.status,rs.msg);
                    }
              }
          }
        ]
      }
	  ,{
      	  text: '${Escape.d}view.remove'
          ,xtype:'actioncolumn'
          ,align:'center'
          ,width:46
          ,lockable:true
          ,menuDisabled: true
          ,hideable:false
          ,items: [{
        	  icon: '${Escape.d}g.options.images/remove.png'
              ,tooltip: '${Escape.d}view.remove'
              ,handler: function(view, rowIndex,colIndex,item,e,record) {
	            	  Locale.rmConfirm(function(){
	            	 		var ar = new ActiveRecord('${model.getCode()}');
	    					var task = new Ext.util.DelayedTask(function(){
	    						var rs = ar.remove();
	        					if(rs.status == 1){
	        						Locale.success();
	        					}else{
	        						Locale.failure(rs.status,rs.msg);
	        					}	
	    						var store = Ext.data.StoreManager.lookup('${model.getCode()}-Store');
	    						store.reload();
	    					});	
	    					new Ext.util.DelayedTask(function(){
	    	                  	var row = view.getStore().getAt(rowIndex);
	    	                  	var id = row.get("as_${model.tableName}_id");
	    	                  	view.select(rowIndex);	    	                  	
	    	                  	ar.data.id = id;	    	                  	
	        					task.delay(200);
	    					}).delay(1);                  					               					
	    				});

              }
          }
        ]
      }
	]
    ,plugins: [
          Ext.create('Ext.grid.plugin.CellEditing', {
              clicksToEdit: 2
              ,listeners: {
          		edit: function(editor,e){
        	  		var id = e.record.get('as_${model.tableName}_id');
          			var attr = e.field.replace(/^as_${model.tableName}_/,'');
          			var nv,ov;
          			if(Ext.isDate(e.value)){
          				nv = Ext.Date.format(e.value,e.column.format);          				
          			}else{
          				nv = e.value || '';
          			}
          			if(Ext.isDate(e.originalValue)){
          				ov = Ext.Date.format(e.originalValue,e.column.format)
          			}else{
          				ov = e.originalValue || '';
          			}
          			if(nv != ov){
          				var ar = new ActiveRecord('${model.code}');
          				ar.data.id = id;
          				ar.data[attr] = nv;
          				var rs = ar.save();
          				if(rs.status == 1){
          					Locale.success();
          				}else{
          					Locale.failure(rs.status,rs.msg);
          				}
          			}
          		}
          	}
          })
	]	
	,initComponent: function() { 
		this.selModel = new Ext.selection.CellModel({});
		this.callParent(arguments);
		var paging = Ext.create('Ext.toolbar.Paging',{
    		pageSize: 25
    		,dock: 'bottom'
    		,displayInfo: true
    		,store: this.getStore()
    	});
		paging.remove(11);
		paging.add(11,'-');
		paging.add(0,[{
			text: '${Escape.d}view.new'
			,icon: '${Escape.d}g.options.images/new.png'
			,handler: function(me,e){
				new ${model.code}.Window({
					items: [{xtype: '${model.code.toLowerCase()}new'}]
				}).show();
			}
		},'-',{xtype: 'tbfill'},'-']);
		this.addDocked(paging);
		this.store.reload();
   }
});