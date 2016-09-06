package app.controller;

import net.rails.web.Route;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RootController extends ApplicationController {

	public RootController(FilterConfig config,HttpServletRequest request, HttpServletResponse response,Route route) throws Exception {
		super(config, request, response,route);
		toProxy();		
	}

}