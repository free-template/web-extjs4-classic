package app.service;

import org.apache.log4j.Logger;
import net.rails.ext.AbsGlobal;

public class ApplicationService {

    protected Logger log;
	protected AbsGlobal g;
	protected Object statement;
	protected String controller;
	protected String action;
	
	public ApplicationService(AbsGlobal g) {
		super();
		log = Logger.getLogger(getClass());
		this.g = g;
		controller = g.options.get("controller").toString();
		action = g.options.get("action").toString();
	}
	
	public void setStatement(Object statement){
		this.statement = statement;
	}
	
	public <T extends Object> T getStatement(){
		return (T) statement;
	}
	
	public String getController(){
		return controller;
	}
	
	public String getAction(){
		return action;
	}
	
}
