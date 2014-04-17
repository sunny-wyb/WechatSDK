package com.wechat.controller;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class BasicServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1903046354903543992L;

	@Override 
	public void init(ServletConfig config) {
		String app_root = config.getServletContext().getRealPath(".");
		
		System.setProperty("app_root", app_root);
	}
}
