package org.smart4j.framework.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smart4j.framework.util.CastUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;


//表单的参数分为 表单参数 和 上传的文件参数
public class Param {
	
	List<FormParam> formParamList;
	
	List<FileParam> fileParamList;

	public Param(List<FormParam> formParamList) {
		this.formParamList = formParamList;
	}

	public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
		this.formParamList = formParamList;
		this.fileParamList = fileParamList;
	}

	public List<FormParam> getFormParamList() {
		return formParamList;
	}

	public List<FileParam> getFileParamList() {
		return fileParamList;
	}
	
	//把获得的域对象以map形式返回
	public Map<String, Object> getFieldMap(){
		Map<String, Object> fieldMap = new HashMap<String,Object>();
		if(CollectionUtil.isNotEmpty(formParamList)){
			for(FormParam formParam:formParamList){
				String fieldName = formParam.getFieldName();
				Object object = formParam.getFieldValue();
				if(fieldMap.containsKey(fieldName)){
					object = fieldMap.get(fieldName) + StringUtil.SEPARATOR + object;
				}
				fieldMap.put(fieldName, object);
			}
		}
		
		return fieldMap;
	}
	
	//获取上传文件映射Map 把FileParam对象list 以map 域名加文件列表list 的形式返回。
	public Map<String, List<FileParam>> getFileMap(){
		Map<String, List<FileParam>> filesMap = new HashMap<String,List<FileParam>>();
		
		if(CollectionUtil.isNotEmpty(fileParamList)){
			for(FileParam fileParam : fileParamList){
				String fieldName = fileParam.getFieldName();
				List<FileParam> files;
				if(filesMap.containsKey(fieldName)){
					files = filesMap.get(fieldName);
				}else{
					files = new ArrayList<FileParam>();
				}
				
				files.add(fileParam);
				filesMap.put(fieldName, files);
			}
		}
		
		return filesMap;
	}
	
	//获得上传文件Map中的 指定的某个域的文件list
	public List<FileParam> getFileList(String fieldName){
		return getFileMap().get(fieldName);
	}
	
	//获取唯一上传文件。
	public FileParam getFile(String fieldName){
		List<FileParam> flist = getFileMap().get(fieldName);
		if((flist.size() == 1) && CollectionUtil.isNotEmpty(flist)){
			return flist.get(0);
		}
		return null;
	}
	
	//验证param是否为空。
	public boolean isEmpty(){
		return CollectionUtil.isEmpty(fileParamList) && CollectionUtil.isEmpty(formParamList);
 	}
	

	/**
	 * 根据参数名获取不同类型的数值
	 */
	
	public String getString(String name){
		return CastUtil.castString(getFieldMap().get(name));
	}
	
	public double getDouble(String name){
		return CastUtil.castDouble(getFieldMap().get(name));
	}
	
	public long getLong(String name){
		return CastUtil.castLong(getFieldMap().get(name));
	}
	
	public int getInt(String name){
		return CastUtil.castInt(getFieldMap().get(name));
	}
	
	public boolean getBoolean(String name){
		return CastUtil.castBoolean(getFieldMap().get(name));
	}
	
}