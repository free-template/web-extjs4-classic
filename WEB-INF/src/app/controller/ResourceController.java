package app.controller;

import java.net.URI;
import net.rails.ext.AbsGlobal;
import net.rails.tpl.TplCache;
import net.rails.web.Controller;
import net.rails.web.Route;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import app.global.Global;
import app.controller.service.ResourceService;

public class ResourceController extends Controller {

	protected AbsGlobal g;
	private ResourceService service;
	
	public ResourceController(FilterConfig config,HttpServletRequest request, HttpServletResponse response,Route route) throws Exception {
		super(config, request, response,route);
		g = new Global(this);
		service = new ResourceService(g);
		request.setAttribute("g", g);
	}
	
	public void includeGet() throws Exception{
		final String file = params.get("_ARGS").toString();
		String filePath = new URI(file).getPath();
		String contentType = null;
		if(filePath.endsWith(".js")){
			contentType = "text/javascript";
		}else if (filePath.endsWith(".css")) {
			contentType = "text/css";
		}else if (filePath.endsWith(".html")) {
			contentType = "text/html";
		}else if (filePath.endsWith(".xml")) {
			contentType = "text/xml";
		}else{
			contentType = "text/html";
		}
		text(contentType,new TplCache(0,route) {
			@Override
			protected String execution() {
				try {
					return service.includeAction(file);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					return "";
				}
			}
		}.toString());
	}

	@Override
	protected AbsGlobal getGlobal() {
		return g;
	}

}