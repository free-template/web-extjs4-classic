package app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import net.rails.active_record.ActiveRecord;
import net.rails.ext.AbsGlobal;
import net.rails.ext.Json;
import net.rails.sql.query.Query;
import net.rails.support.Support;
import net.rails.tpl.Tpl;
import net.rails.tpl.TplText;
import net.rails.web.QueryString;
import net.rails.web.Route;
import app.controller.ApplicationController;

public class ApplicationProxy {
	
	protected Logger log;
	protected Map<String,Object> params;
	protected Map<String,Object> queies;
	protected ApplicationController contro;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected Route route;
	protected AbsGlobal g;
	
	public ApplicationProxy(ApplicationController contro) {
		super();
		log = Logger.getLogger(getClass());
		this.contro = contro;
		g = contro.getGlobal();
		request = contro.getRequest();
		response = contro.getResponse();
		session = request.getSession();
		route = contro.getRoute();
		params = contro.getParams();
		queies = contro.getQueies();
	}	
	
	public Map<String,Object> form(String name, Map<String, Object> params) {
		return contro.form(name, params);
	}

	public Map<String, Object> form(String name) {
		return form(name, params);
	}
	
	public void set(String key,Object value) {
		contro.set(key, value);
	}
	
	public Number parseNumber(String name,Number def) throws ParseException {
		return contro.parseNumber(name,def);
	}
	
	public Number parseNumber(String name) throws ParseException {
		return contro.parseNumber(name);
	}
	
	public Timestamp parseTimestamp(String name,Timestamp def) throws ParseException {
		return contro.parseTimestamp(name,def);
	}
	
	public Timestamp parseTimestamp(String name) throws ParseException {
		return contro.parseTimestamp(name);
	}
	
	public Date parseDate(String name,Date def) throws ParseException {
		return contro.parseDate(name,def);
	}
	
	public Date parseDate(String name) throws ParseException  {
		return contro.parseDate(name);
	}
	
	public Time parseTime(String name,Time def) throws ParseException {
		return contro.parseTime(name,def);
	}
	
	public Time parseTime(String name) throws ParseException  {
		return contro.parseTime(name);
	}
	
	public String parseString(String name,String def){
		return contro.parseString(name,def);
	}
	
	public String parseString(String name){
		return contro.parseString(name);
	}
	
	public Boolean parseBoolean(String name,Boolean def){
		return contro.parseBoolean(name,def);
	}
	
	public Boolean parseBoolean(String name){
		return contro.parseBoolean(name);
	}
	
	public List<Object> parseJsonArray(String name,String def){
		return contro.parseJsonArray(name,def);
	}
	
	public <T extends Object> List<T> parseJsonArray(String name){
		return (List<T>) contro.parseJsonArray(name);
	}
	
	public <T extends Object> List<T> parseArray(String name,String def){
		return (List<T>) contro.parseArray(name,def);
	}
	
	public <T extends Object> List<T> parseArray(String name){
		return (List<T>) contro.parseArray(name);
	}
	
	public <T extends Object> Map<String, T> parseJson(String name,String def){
		return (Map<String, T>) contro.parseJson(name,def);
	}
	
	public <T extends Object> Map<String, T> parseJson(String name){
		return (Map<String, T>) contro.parseJson(name);
	}
	
	public void bind(ActiveRecord model) {
		contro.bind(model);
	}
	
	public void bind(Map<String,Object> params) {
		contro.bind(params);
	}
	
	public void location(int status,String url) throws IOException, ServletException{
		contro.location(status, url);
	}
	
	public void location(String url) throws IOException, ServletException{
		contro.location(url);
	}
	
	public void redirectRoute(String route,QueryString qs) throws IOException, ServletException{
		contro.redirectRoute(route, qs);
	}
	
	public void redirect(String controller,String action,QueryString qs) throws IOException, ServletException{
		contro.redirect(controller, action, qs);
	}
	
	public void redirect(String action,QueryString qs) throws IOException, ServletException{
		contro.redirect(action, qs);
	}
	
	public void redirect(String url) throws IOException, ServletException{
		contro.redirect(url);
	}
	
	public void forward(String action) throws IOException, ServletException{
		contro.forward(action);
	}
	
	public void forward(String controller,String action) throws IOException, ServletException{
		contro.forward(controller, action);
	}
	
	public void forwardRoute(String route) throws IOException, ServletException{
		contro.forwardRoute(route);
	}
	
	public void render() throws IOException, ServletException{
		contro.render(g.options.get("_path") + "/" + contro.getName(),contro.getRoute().getAction());
	}
	
	public void render(String path,String action) throws IOException, ServletException{
		contro.render(g.options.get("_path") + "/" + path,action);
	}
	
	public void text(String text) throws IOException {
		contro.text(text);
	}
	
	public String tpl(String tplFile) throws IOException{
		StringBuffer sbf = new StringBuffer("Route: " + route.getController() + "/" + route.getAction());
		sbf.append("\nTplFile: " + tplFile);
		TplText text = new TplText(sbf.toString(),g,tplFile);
		text.params().put("Query", Query.class);
		text.params().put("Support", Support.class);
		text.params().put("Log", Logger.getLogger(getClass()));
		text.params().put("Json", Json.class);
		text.params().put("g",g);
		Tpl tpl = new Tpl(g,text);
		if(tplFile.endsWith(".js")){
			tpl.setCompressed(true);
			tpl.setDocType(Tpl.DOCTYPE_JS);
		}else if(tplFile.endsWith(".css")){
			tpl.setCompressed(true);
			tpl.setDocType(Tpl.DOCTYPE_CSS);
		}else if(tplFile.endsWith(".html")){
			tpl.setCompressed(true);
			tpl.setDocType(Tpl.DOCTYPE_HTML);
		}else{
			tpl.setCompressed(true);
			tpl.setDocType(Tpl.DOCTYPE_OTHER);
		}		
		return tpl.generate().toString();
	}
	
	public String jsTpl() throws IOException{
		String tplFile = g.options.get("_path") + "/" + contro.getName() + "/" + contro.getRoute().getAction() + ".tpl.js";
		return tpl(tplFile);
	}
	
	public String htmlTpl() throws IOException{
		String tplFile = g.options.get("_path") + "/" + contro.getName() + "/" + contro.getRoute().getAction() + ".tpl.html";
		return tpl(tplFile);
	}
	
	public PrintWriter out() throws IOException{
		return contro.out();
	}
	
	public void sendError(int code) throws IOException{
		contro.sendError(code);
	}
	
	public void sendError(int code,String text) throws IOException{
		contro.sendError(code,text);
	}

}
