package org.smart4j.framework;

/**
 * config some static constant
 * @author huanghanwei
 *
 */
public interface ConfigConstant {
	//name of config file
	String CONFIG_FILE = "smart.properties";
	
	//database connection config
	String JDBC_DRIVER = "smart.framework.jdbc.driver";
	String JDBC_URL = "smart.framework.jdbc.url";
	String JDBC_USERNAME = "smart.framework.jdbc.username";
	String JDBC_PASSWORD = "smart.framework.jdbc.password";
	
	String APP_BASE_PACKAGE = "smart.framework.app.base_package";
	String APP_JSP_PATH = "smart.framework.app.jsp_path";
	String APP_ASSET_PATH = "smart.framework.app.asset_path";
}