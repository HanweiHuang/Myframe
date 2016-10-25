package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.transform.impl.InterceptFieldCallback;

public abstract class AspectProxy implements Proxy{
	
	private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);
	
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;
		
		Class<?> cls = proxyChain.getTargetClass();
		Method method = proxyChain.getTargetMethod();
		Object[] params = proxyChain.getMethodParams();
		
		
		begin();
		try{
			if(intercept(cls,method,params)){
				before(cls,method,params);
				result = proxyChain.doProxyChain();
				after(cls,method,params,result);
			}else{
				proxyChain.doProxyChain();
			}
		} catch(Exception e){
			logger.error("proxy failure", e);
			error(cls,method,params,e);
			throw e;
		} finally{
			end();
		}
		
		
		return result;
	}

	
	private void before(Class<?> cls, Method method, Object[] params) {
		// TODO Auto-generated method stub
		
	}


	private void after(Class<?> cls, Method method, Object[] params, Object result) {
		// TODO Auto-generated method stub
		
	}


	private void error(Class<?> cls, Method method, Object[] params, Exception e) {
		// TODO Auto-generated method stub
		
	}


	private void end() {
		// TODO Auto-generated method stub
		
	}


	private boolean intercept(Class<?> cls, Method method, Object[] params) {
		// TODO Auto-generated method stub
		return true;
	}


	public void begin(){
	}
	
	
}
