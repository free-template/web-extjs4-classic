Ext.define('${model.code}.Window',{
	extend: 'Ext.window.Window'
	,alias: ['widget.${model.code.toLowerCase()}window']
	,constrainHeader: true
	,maximizable: true
	,title: '${Escape.d}view.new ${Escape.d}${model.code}'
    ,icon: '${Escape.d}g.options.images/${model.code}.png'
	,width: 685
	,height: 473
	,layout: 'fit'
});