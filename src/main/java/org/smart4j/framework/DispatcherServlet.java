package org.smart4j.framework;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

@WebServlet(urlPatterns="/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext s1=config.getServletContext(); 
		String temp=s1.getRealPath("/");
		System.out.println("temp-37:"+temp);
		//initial helpers 
		HelperLoader.init();
		
		//ServletContext register servlet
		ServletContext servletContext = config.getServletContext();
		//deal with jsp servlets 
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
		//deal with default resource servlets
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
		
		
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		
		if(handler!=null){
			Class<?> controllerClass = handler.getControllerClass();
			Object controllerBean = BeanHelper.getBean(controllerClass);
			
			//create request params object
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Enumeration<String> paramNames = request.getParameterNames();
			
			while(paramNames.hasMoreElements()){
				String paramName = paramNames.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			
			//deal with inputstream of params utf-8
			String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
			
			if(StringUtil.isNotEmpty(body)){
				String[] params = StringUtil.splitStream(body, "&");
				if(ArrayUtil.isNotEmpty(params)){
					for(String param :params){
						String[] array = StringUtil.splitStream(param, "=");
						if(ArrayUtil.isNotEmpty(array) && array.length==2){
							String paramName = array[0];
							String paraValue = array[1];
							paramMap.put(paramName, paraValue);
						}
					}
				}
			}
			
			Param param = new Param(paramMap);
			
			//invoke Action's methods
			Method actionMethod = handler.getActionMethod();
			/**
			 * 1.object have method
			 * 2.method will be invoke
			 * 3.parameters for method
			 * return method's value
			 */
			Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
			
			if(result instanceof View){
				View view = (View)result;
				String path = view.getPath();
				if(StringUtil.isNotEmpty(path)){
					System.out.println("dispatcher-102"+request.getContextPath());
					System.out.println("dispatcher-103"+path);
					if(path.startsWith("/")){
						System.out.println("start with /");
						response.sendRedirect(request.getContextPath() + path);
					}else{
						//set params
						Map<String, Object> model = view.getMap();
						for(Map.Entry<String, Object> entry : model.entrySet()){
							request.setAttribute(entry.getKey(),entry.getValue());
						}
						System.out.println(ConfigHelper.getAppJspPath()+path);
						
						request.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(request, response);
					}
				}
			}
			else if(result instanceof Data){
				//return JSON data
			}
		}
	}
	
	
}
