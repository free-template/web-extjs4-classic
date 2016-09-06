package app.controller.www.admin;

import app.controller.ApplicationController;
import app.controller.www.admin.service.${model.code}Service;

public class ${model.code}Proxy extends ActionProxy {
	
	public ${model.code}Proxy(ApplicationController contro) throws Exception {
		super(contro);
	}
	
	@Override
	protected ${model.code}Service getService(){
		return new ${model.code}Service(g);
	}
	
}