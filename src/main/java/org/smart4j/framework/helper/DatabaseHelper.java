package org.smart4j.framework.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.PropsUtil;

//import com.cp3.utils.PropsUtil;

public final class DatabaseHelper {
	/**
	 * store connection into a thread
	 */
	private static final ThreadLocal<Connection> CONNECTIION_HOLDER = new ThreadLocal<Connection>();
	
	
	/**
	 * dbunit for jdbc
	 */
	private static final QueryRunner QUERY_RUNNER = new QueryRunner();
	
	private static final BasicDataSource DATA_SOURCE;
	

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
	
	private static final String DRIVER;
	
	private static final String URL;
	
	private static final String USERNAME;
	
	private static final String PASSWORD;
	
	static{
		Properties conf = PropsUtil.loadProps("smart.properties");
		DRIVER = conf.getProperty("smart.framework.jdbc.driver");
		URL = conf.getProperty("smart.framework.jdbc.url");
		USERNAME = conf.getProperty("smart.framework.jdbc.username");
		PASSWORD = conf.getProperty("smart.framework.jdbc.password");
		
//		try{
//			Class.forName(DRIVER);
//		}catch(ClassNotFoundException e){
//			LOGGER.error("can not load jdbc driver", e);
//		}
		
		DATA_SOURCE = new BasicDataSource();
		
		DATA_SOURCE.setDriverClassName(DRIVER);
		
		DATA_SOURCE.setUrl(URL);
		
		DATA_SOURCE.setUsername(USERNAME);
		
		DATA_SOURCE.setPassword(PASSWORD);
	}
	
	/********Transaction operations***********/
	
	/**
	 * begin a transaction
	 */
	public static void beginTransaction(){
		Connection conn = getConnection();
		if(conn!=null){
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				LOGGER.error("begin transaction failure", e);
				throw new RuntimeException();
			} finally{
				CONNECTIION_HOLDER.set(conn);
			}
		}
	}
	
	/**
	 * commit an transaction
	 */
	public static void commitTransaction(){
		Connection conn = getConnection();
		if(conn!=null){
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("commit transaction failure", e);
				throw new RuntimeException();
			} finally{
				CONNECTIION_HOLDER.remove();
			}
		}
	}
	
	/**
	 * rollback transaction
	 */
	public static void rollBackTransaction(){
		Connection conn = getConnection();
		
		if(conn!=null){
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("roll back failure",e);
				throw new RuntimeException();
			} finally{
				CONNECTIION_HOLDER.remove();
			}
		}
	}
	
	/*******************/
	
	/**
	 * get connection
	 * @return
	 */
	public static Connection getConnection(){
		Connection conn = CONNECTIION_HOLDER.get();
		if(conn == null){
			try {
				//conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
				conn = DATA_SOURCE.getConnection();
			} catch (SQLException e) {
				LOGGER.error("get connection failure",e);
				throw new RuntimeException();
			}finally{
				CONNECTIION_HOLDER.set(conn);
			}
		}
		return conn;
	}
	
	/**
	 * close connection
	 * @param conn
	 */
//	public static void closeConnection(){
//		Connection conn = CONNECTIION_HOLDER.get();
//		if(conn!=null){
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				LOGGER.error("close connection failure",e);
//				throw new RuntimeException();
//			}finally{
//				CONNECTIION_HOLDER.remove();
//			}
//		}
//	}
	
	/**
	 * DbUtils provide list function
	 * @param entityClass
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public static<T> List<T> queryEntityList(Class<T> entityClass, String sql, Object...params){
		List<T> entityList;
		try {
			Connection conn = getConnection();
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass),params);
		} catch (SQLException e) {
			LOGGER.error("query entity list failure",e);
			throw new RuntimeException();
		} 
//		finally {
//			closeConnection();
//		}
		return entityList;
	}
	
	/**
	 * Dbutil provides find entity function
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static<T> T queryEntity(Class<T> entityClass, String sql, Object...params){
		T entity;
		try {
			Connection conn = getConnection();
			entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass),params);
		} catch (SQLException e) {
			LOGGER.error("query entity failure",e);
			throw new RuntimeException();
		} 
//		finally{
//			closeConnection();
//		}
		return entity;
	}
	
	/**
	 * Dbutil show list
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object...params){
		List<Map<String, Object>> result = null;
		
		try {
			Connection conn = getConnection();
			result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			LOGGER.error("execute query failure",e);
			throw new RuntimeException();
		} 
//		finally{
//			closeConnection();
//		}
		
		return result;
	}
	
	/**
	 * return how many lines were effected
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int executeUpdate(String sql, Object...params){
		int rows = 0;
		
		try {
			Connection conn = getConnection();
			rows = QUERY_RUNNER.update(conn,sql,params);
		} catch (SQLException e) {
			LOGGER.error("execute update failure",e);
			throw new RuntimeException();
		} 
//		finally{
//			closeConnection();
//		}
		return rows;
	}
	
	
	/**
	 * insert an entity
	 * @param entityClass
	 * @param fieldMap
	 * @return
	 */
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
		if(CollectionUtil.isEmpty(fieldMap)){
			LOGGER.error("can not insert entity: fieldMap is empty");
			return false;
		}
		
		String sql = "INSERT INTO " + getTableName(entityClass);
		
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		
		for(String fieldName:fieldMap.keySet()){
			columns.append(fieldName).append(", ");
			values.append("?, ");
		}
		columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
		values.replace(values.lastIndexOf(", "), values.length(), ")");
		sql += columns + " VALUES " + values;
		System.out.println(sql);
		
		Object[] params = fieldMap.values().toArray();
		
		int result = executeUpdate(sql, params);	
		return result == 1;
	}

	/**
	 * update an entity
	 * @param entityClass
	 * @param id
	 * @param fieldMap
	 * @return
	 */
	public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap){
		if(CollectionUtil.isEmpty(fieldMap)){
			LOGGER.error("can not update entity: fieldMap is empty");
			return false;
		}
		
		String sql = "UPDATE " + getTableName(entityClass) + " SET ";
		StringBuilder columns = new StringBuilder();
		for(String fieldName:fieldMap.keySet()){
			columns.append(fieldName).append("=?, ");
		}
		
		sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		//System.out.println(sql);
		Object[] params = paramList.toArray();
		
		return executeUpdate(sql, params) == 1;
		//return true;
	}
	
	/**
	 * delete an entity
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static <T> boolean deleteEntity(Class<T> entityClass, long id){
		
		String sql = "DELETE FROM "+getTableName(entityClass) + " WHERE id = ?";
		return executeUpdate(sql, id)==1;
	}
	
	private static String getTableName(Class<?> entityClass) {
			return entityClass.getSimpleName();
		
	}
	
	/**
	 * initiate database by reading sql file 
	 * @param filePath
	 */
	public static void executeSqlFile(String filePath){
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		try {
			String sql;
			while((sql = reader.readLine())!=null){
				executeUpdate(sql);
			}
		} catch (IOException e) {
			LOGGER.error("read sql file failure", e);
			throw new RuntimeException();
		}
	}
}
