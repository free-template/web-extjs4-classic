package app.controller.service;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import app.service.ApplicationService;

import net.rails.ext.AbsGlobal;
import net.rails.ext.Json;
import net.rails.sql.query.Query;
import net.rails.support.Support;
import net.rails.support.worker.AbsConfigWorker;
import net.rails.tpl.Tpl;
import net.rails.tpl.TplText;

public class ResourceService extends ApplicationService {
	
	public ResourceService(AbsGlobal g) {
		super(g);
		String args = (String)g.options.get("_ARGS");
		args = to(args);
		this.g.options.put("images",g.options.get("domainUrl") + "/public/" + args + "images");
		this.g.options.put("js",g.options.get("domainUrl") + "/public/" + args + "js");
		this.g.options.put("css",g.options.get("domainUrl") + "/public/" + args + "css");
	}
	
	private String to(String args) {
		Pattern p = Pattern.compile("\\w++/(admin/)?+");
		Matcher m = p.matcher(args);
		return m.find() ? m.group() : "";
	}
	
	public String includeAction(String filename) throws IOException{
		String tplFileName = MessageFormat.format("{0}/{1}",g.options.get("controller"),filename);
		File tplFile = new File(AbsConfigWorker.CONFIG_PATH + "/../view/" + tplFileName);
		if(tplFile.exists()){
			return FileUtils.readFileToString(tplFile,g.getApplicationCharset());
		}else{
			Pattern p = Pattern.compile(".(js|css|html)$");
			Matcher m = p.matcher(tplFileName);
			if(m.find()){
				tplFileName = tplFileName.replaceFirst(m.group() + "$", ".tpl" + m.group());
				tplFile = new File(tplFile.getParent() + "/" + tplFileName);
				TplText text = new TplText(tplFileName,g,tplFileName);
				text.params().put("Query", Query.class);
				text.params().put("Support", Support.class);
				text.params().put("Log", Logger.getLogger(ResourceService.class));
				text.params().put("Json", Json.class);
				text.params().put("g",g);
				Tpl tpl = new Tpl(g,text);
				
				if(tplFileName.endsWith(".js")){
					tpl.setDocType(Tpl.DOCTYPE_JS);
				}else if(tplFileName.endsWith(".css")){
					tpl.setDocType(Tpl.DOCTYPE_CSS);
				}else if(tplFileName.endsWith(".html")){
					tpl.setDocType(Tpl.DOCTYPE_HTML);
				}else{
					tpl.setDocType(Tpl.DOCTYPE_OTHER);
				}
				tpl.setCompressed(true);
				return tpl.generate().toString();
			}
		}
		return "";
	}

}
