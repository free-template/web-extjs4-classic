package app.controller;

import net.rails.active_record.exception.MessagesException;
import net.rails.ext.AbsGlobal;
import net.rails.ext.Json;
import net.rails.support.Support;
import net.rails.web.Controller;
import net.rails.web.Route;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import app.global.Global;

public class ApplicationController extends Controller {
	
	protected AbsGlobal g;

	public ApplicationController(FilterConfig config,HttpServletRequest request, HttpServletResponse response,Route route) throws Exception {
		super(config, request, response,route);
		g = new Global(this);
		if(getUserAgent().isMobile()){
			to(parseString("_to","wap"));
		}else{
			to(parseString("_to","www"));
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append(g.options.get("domainUrl")).append("/public/").append(g.options.get("_path")).append("/images");
		g.options.put("images",sbf.toString());
		sbf = new StringBuffer();
		sbf.append(g.options.get("domainUrl")).append("/public/").append(g.options.get("_path")).append("/js");
		g.options.put("js",sbf.toString());
		sbf = new StringBuffer();
		sbf.append(g.options.get("domainUrl")).append("/public/").append(g.options.get("_path")).append("/css");
		g.options.put("css",sbf.toString());
		log.debug("Global option: " + g.options);
		request.setAttribute("g", g);
	}

	public void to(String to) {
		String args = (String)g.options.get("_ARGS");
		args = Support.string(args).def("").replaceFirst("/$","");
		List<String> tos = new ArrayList<String>(Arrays.asList(to,args));
		tos.remove("");
		g.options.put("_to",to);
		g.options.put("_pack",Support.array(tos).join("."));
		g.options.put("_path",Support.array(tos).join("/"));
	}
	
	public Json<String,Object> error(Exception e){
		final Json<String,Object> json = new Json<String,Object>();
		if(e instanceof MessagesException){
			List<String> msgs = ((MessagesException)e).getMessages();
			return json.append("status",0).append("msg",Support.array(msgs).join("|"));
		}else
			return json.append("status",0).append("msg",e.getMessage());
	}
	
	@Override
	public AbsGlobal getGlobal() {
		return g;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void toProxy() throws Exception {
		StringBuffer forname = new StringBuffer();
		forname.append("app.controller.").append(g.options.get("_pack")).append(".").append(getName()).append("Proxy");
		log.debug("ForName: " + forname);
		Class cls = Class.forName(forname.toString());
		Constructor con = cls.getConstructor(ApplicationController.class);
		Object o = con.newInstance(this);
		if(route.isActive()){
			String req = Support.inflect(request.getMethod().toLowerCase()).camelcase();
			List<String> names = new ArrayList<String>();
			for(Method m : cls.getMethods()){
				names.add(m.getName());
			}
			Method m = null;
			String act = new StringBuffer().append(action).append("Action").toString();
			if(names.contains(act)){
				m = cls.getMethod(act);
				m.invoke(o);
				if(route.isActive()){
					act = MessageFormat.format("{0}{1}",action,req);
					if(names.contains(act)){
						m = cls.getMethod(act);
						m.invoke(o);
					}
					if(route.isActive()){
						m = cls.getMethod("render");
						m.invoke(o);
						return;
					}
				}
				return;
			}else{
				act = MessageFormat.format("{0}{1}",action,req);
				if(names.contains(act)){
					m = cls.getMethod(act);
					m.invoke(o);
					if(route.isActive()){
						m = cls.getMethod("render");
						m.invoke(o);
						return;
					}
				}
				return;
			}
		}
	}
	
	@Override
	protected String encoding(String param){
		if(param == null)
			return null;
		
		return super.encoding(param.trim());
	}

}