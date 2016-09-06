package app.controller.www.admin;

import java.io.IOException;
import net.rails.tpl.TplCache;
import app.controller.ApplicationController;

public class RootProxy extends ActionProxy {
	
	public RootProxy(ApplicationController contro) throws Exception {
		super(contro);
	}
	
	public void indexAction() throws IOException {
		text(new TplCache(0) {
			@Override
			protected String execution() {
				try {
					return htmlTpl();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					return null;
				}
			}

		}.toString());
	}
	
	

}