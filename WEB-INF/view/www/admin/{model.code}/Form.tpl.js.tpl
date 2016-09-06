Ext.define('${model.code}.Form',{
	extend: 'Ext.form.Panel'
	,alias: ['widget.${model.code.toLowerCase()}form']
	,border: 0
	,autoScroll: true
	,buttonAlign: 'align'	
	,bodyPadding: '10 5 5 5'
	,buttonAlign: 'center'
	,removeButton: false
	,defaults:{
		labelWidth: 70
		,padding: '5 10 5 5'
	}
	,layout: { 
			type: 'column'
		}
    ,initComponent: function(){
        var me = this;
#foreach($field in $model.knownFields)    
#set($belongsToModel=$field.belongsToModel())
#if($belongsToModel != '')
        Ext.syncRequire(['${belongsToModel}.ComboBox']);
#end
#end
        Ext.apply(me,{
            	items:[
            	       {
            	   		 xtype: 'hiddenfield'
            	   		 ,name: '${model.code}[id]'
            	   	   }
#foreach($field in $model.knownFields)
#set($classType=$field.getClassType())
#set($javaType=$classType.getJavaType())
#set($belongsToModel=$field.belongsToModel())
#if($belongsToModel != '')
                      ,{
                          xtype: '$belongsToModel.toLowerCase()combobox'
            			 ,name: '${model.code}[$field.code]'
            			 ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            		   }
#elseif($field.isNumber())
            		   ,{
            			 xtype: 'numberfield'
            			 ,name: '${model.code}[$field.code]'
            			 ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            		   }
#elseif($javaType.equals('Boolean'))
            		   ,{
            		     xtype: 'checkboxfield'
            		     ,name: '${model.code}[$field.code]'
            		     ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            		   }
#elseif($javaType.equals('java.sql.Timestamp'))
            		   ,{
            			  xtype: 'datefield'
            			  ,name: '${model.code}[$field.code]'
            			  ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            			}
#elseif($javaType.equals('java.sql.Date'))
            		   ,{
            	          xtype: 'datefield'
            	          ,name: '${model.code}[$field.code]'
            	          ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            	        }
#elseif($javaType.equals('java.sql.Time'))
            		   ,{
            				  xtype: 'timefield'
            				  ,name: '${model.code}[$field.code]'
            				  ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            			}
#else
                	   ,{
            		     xtype: 'textfield'
            		     ,name: '${model.code}[$field.code]'
            		     ,fieldLabel: '${Escape.d}${model.code}.${field.code}'
            	       }
#end
#end
            	]
        });
        me.callParent(arguments);
    }
});
