package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.helper.DatabaseHelper;

public class TransactionProxy implements Proxy{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

	private final ThreadLocal<Boolean> FLAG = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};
	
	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result;
		
		boolean flag = FLAG.get();
		Method method = proxyChain.getTargetMethod();
		
			if(!flag && method.isAnnotationPresent(Transaction.class)){
				try {
					FLAG.set(true);
					DatabaseHelper.beginTransaction();
					result = proxyChain.doProxyChain();
					DatabaseHelper.commitTransaction();
				} catch (Exception e) {
					DatabaseHelper.rollBackTransaction();
					LOGGER.debug("rollback transaction",e);
					throw e;
					
				} finally{
					FLAG.remove();
				}
					
				
			}else{
				result = proxyChain.doProxyChain();
			}
			
		return result;
	}
}
