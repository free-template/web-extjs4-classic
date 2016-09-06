Ext.define('Formats', {
	statics : {
		datetime : 'Y-m-d H:i:s',
		date : 'Y-m-d',
		time : 'H:i:s',
		date2text : function(date) {
			return Ext.Date.format(date, Formats.date);
		},
		text2date : function(text) {
			return Ext.Date.parse(text, Formats.date);
		},
		datetime2text : function(date) {
			return Ext.Date.format(date, Formats.datetime);
		},
		text2datetime : function(text) {
			return Ext.Date.parse(text, Formats.datetime);
		},
		time2text : function(date) {
			return Ext.Date.format(date, Formats.time);
		},
		text2time : function(text) {
			return Ext.Date.parse(text, Formats.time);
		}
	}
});



Ext.define(null, {
	override : 'Ext.AbstractComponent',
	viewConfig : {
		loadMask : false
	}
});


Ext.define(null, {
	override : 'Ext.form.field.ComboBox'
	,getAnd: function(){
		 var me = this;
		 var type = typeof(me.store.proxy.extraParams.and);
		 var and;
		 if(type === 'string'){
			and = Ext.decode(me.store.proxy.extraParams.and) || {};   
		 }else if(type === 'object'){
			and = me.store.proxy.extraParams.and || {};   
		 }else{
			and = {};
		 }
		 return and;
	}
	,getOr: function(){
		 var me = this;
		 var type = typeof(me.store.proxy.extraParams.or);
		 var or;
		 if(type === 'string'){
			or = Ext.decode(me.store.proxy.extraParams.or) || {};   
		 }else if(type === 'object'){
			or = me.store.proxy.extraParams.or || {};   
		 }else{
			or = {};
		 }
		 return or;
	}
    ,listeners: {
   		 beforequery: function(queryplan,opts){   			
   			 var cbx = queryplan.combo;
   			 if(cbx.queryMode === 'local'){
   				 return true;
   			 }
   			 var and = cbx.getAnd();
   			 var or = cbx.getOr();
			 Ext.apply(and,{});
			 Ext.apply(or,{});
			 Ext.apply(cbx.store.proxy.extraParams,{and:Ext.encode(and),or:Ext.encode(or)});
			 cbx.store.pageSize = cbx.pageSize;
			 return true;
   		 }
   	}
	,initComponent: function(){
		var me = this;		
		me.callParent(arguments);
	}
});

Ext.Ajax.on('beforerequest', function(c, o, e){
	o.headers = {'X-Requested-With': 'XMLRequest'};
});

Ext.define(null, {
	override : 'Ext.form.field.Date',
	format: Formats.date
});

Ext.define(null, {
	override : 'Ext.form.field.Time',
	format: Formats.time
});

Ext.define(null,{
	override: 'Ext.form.field.ComboBox'
	,getValue: function(){
		if(this.queryMode == 'local'){
			return this.callParent();
		}else{
			return this.value;
		}
	}
});

Ext.define('App.init',{
	statics:{
		load: function(){
			  var _models = ${Escape.d}models;
			  var _paths = {};
			  for(var model in _models){
				  _paths[model] = '${Escape.d}g.options.domainUrl/' + model + '/include/admin?file=';
			  }
			  _paths['Rails.form'] = '${Escape.d}g.options.domainUrl/resource/www/admin/js/form';
			  Ext.Loader.setPath(_paths);
		}
		,top: function(){					
			return Ext.create('Ext.panel.Panel',{
						id : '_top',
						region : 'north',
						border : 0,
						header : false,
						collapseMode : 'mini',
						collapsible : true,
						split: true,
						html: "<div style='height:48px;font-size:30px;'>$Escape.html($solution.name)</div>"
					});
		},
		center: function(){
			return {
				  xtype: 'tabpanel',
					id : '_center',
					region : 'center',
					border: 0,
					activeTab : 0,
					items: [{
						xtype: 'panel'
						,title: '${Escape.d}view.index'
						,icon: '${Escape.d}g.options.images/index.png'
					}]
				};
		}
		,navigation: function(){
			var navs = Ext.create('Ext.panel.Panel',{
						id : '_navigation',
						region : 'west',
						title : 'Navigation',
						width : 200,
						minWidth: 200,
						border : 2,
						collapseMode : 'mini',
						collapsible : true,
						split: true,
						layout : {
							type : 'accordion',
							titleCollapse : true,
							//activeOnTop: true,
							animate : true
							
						}
					});
            navs.add({hidden: true});
#foreach($model in $solution.project.classDiagram.knownModels)
			navs.add({
			    xtype:'treepanel',
			    title: '$model.name',
			    icon: '${Escape.d}g.options.images/${model.code}.png',
			    rootVisible: false,
			    layout: 'fit',
			    store: {
			        root: {
			            expanded: true
			            ,children: {
			                model: '$model.code',
			                leaf: true,
			                text: '${Escape.d}view.list',
			                icon: '${Escape.d}g.options.images/list.png'
			            }
			        }
			    },
			    listeners : {
							itemclick : function(view ,record, item, index, e, eOpts ) {
								indexEvent(record.raw.model);
							}
				}
			});
#end
            return navs;
		}
	}
});

Ext.define('Rails.Tip',function(title, format){
	 var msgCt;
     function createBox(t, s){
       return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
     }
     return {
    	 msg : function(title, format){
             if(!msgCt){
                 msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
             }
             var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
             var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
             m.hide();
             m.slideIn('t').ghost("t", { delay: 1800, remove: true});
         }
     }
}());


function indexEvent(model,params){
	  var _center = Ext.getCmp('_center');	 
	  var tab = Ext.getCmp(model + '-Tab');
	  if(tab){
		  	_center.setActiveTab(tab);	
	  }else{
		  tab = Ext.create(model + '.Tab',{id: model + '-Tab'});
		  _center.add(tab);
		  _center.setActiveTab(tab);
	  }
}


Ext.QuickTips.init();


ActiveRecord.args = 'admin';
Query.args = 'admin';
ActiveRecord.url = '${Escape.d}g.options.domainUrl';
Query.url = '${Escape.d}g.options.domainUrl';
Ajax.url = '${Escape.d}g.options.domainUrl';

Ext.onReady(function() {
	App.init.load();
	Ext.create('Ext.container.Viewport', {
		    layout: 'fit',
			border: 0,
			items: [
			        {
			        	xtype: 'panel'
			        	,border: 0			        	
			        	,layout: 'border'
			        	,dockedItems: [
			        	        {
			        	        	 xtype: 'toolbar'
			    	            	 ,dock: 'bottom'
			    	            	 ,style: 'background-color:#DFEAF2;'
		    	            		 ,items:[
				    			             {
				    			                 text: '${Escape.d}view.logout'
				    			            	 ,icon: '${Escape.d}g.options.images/logout.png'
				    			            	 ,xtype: 'button'
				    			             }
				    			          ]
			        	        }       
			        	]
			        	,items : [ App.init.top(), App.init.navigation(), App.init.center() ]
			        }
			]			
	});
});
