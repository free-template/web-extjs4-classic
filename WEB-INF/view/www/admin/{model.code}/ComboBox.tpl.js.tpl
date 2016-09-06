Ext.define('${model.code}.ComboBox',{
	extend: 'Ext.form.field.ComboBox'
	,alias: ['widget.${model.code.toLowerCase()}combobox']
	,valueField: 'as_${model.tableName}_id'
	,displayField: 'as_${model.tableName}_${model.displayField}'
	,fieldLabel: '${Escape.d}$model.code'
	,emptyText: '$view.please_select'
	,pageSize: 10
	,minChars: 1
	,triggerAction: 'all'
	,forceSelection: true
	,selectOnFocus: false
	,autoScroll: true
	,anchor: '100%'
    ,initComponent: function(){
		var me = this;
		me.callParent(arguments);
		me.store = new Ext.data.Store({
    		 fields: ['as_${model.tableName}_id','as_${model.tableName}_${model.displayField}']
	 		,proxy: {
		 			type: 'ajax'
		 			,url: '${Escape.d}g.options.domainUrl/${model.code}/boxList/admin'
		 			,reader:{
		 				type: 'json'
		 				,idProperty: 'as_${model.tableName}_id'
		 				,root: 'data'
		 				,totalProperty: 'total'
		 			}
		 			,extraParams: {
		 				and: {eq_deleted:false}
		 			}
		 	 }
    	 });
	}
});
