Ext.define('${model.code}.Tab',{
	extend: 'Ext.panel.Panel'
	,alias: ['widget.${model.code.toLowerCase()}tab']
	,requires: [
	            '${model.code}.Search'
	            ,'${model.code}.Grid'
	          ]
	,title: '${Escape.d}${model.code}'
	,autoScroll: false
	,closable: true
	,layout: 'border'
    ,icon: '${Escape.d}g.options.images/${model.code}.png'
	,items:[
	       {
	    	   xtype: '${model.code.toLowerCase()}search'
	    	   ,region: 'north'
	    	   ,split: true
	       }
	       ,{
	    	   xtype: '${model.code.toLowerCase()}grid'
	    	   ,region: 'center'
	       }
      ]

});