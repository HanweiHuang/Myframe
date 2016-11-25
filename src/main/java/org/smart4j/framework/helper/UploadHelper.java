package org.smart4j.framework.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.spi.LoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.FileUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

public final class UploadHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);
	
	/**
	 * Apache Commons FileUpload 提供的 Servlet 文件上传对象
	 */
	private static ServletFileUpload servletFileUpload;
	
	/**
	 * 1.设置上传文件尺寸
	 * 2.new servletFileUpload 对象
	 * @param servletContext
	 */
	public static void init(ServletContext servletContext){
		File repository = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
		servletFileUpload = new ServletFileUpload(
				new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
		
		int uploadlimit = ConfigHelper.getAppUploadLimit();
		if(uploadlimit != 0){
			servletFileUpload.setFileSizeMax(uploadlimit * 1024 * 1024);
		}
		
	}
	
	/**
	 * 判断请求是否是multipart
	 */
	
	/**
	 * 上传文件
	 * 提供了inputstram 和 outputstream 上传写入
	 *
	 */
	
	public static void uploadFile(String basePath, FileParam fileParam){
		
		try{
			if(fileParam!=null){
				String filePath = basePath + fileParam.getFileName();
				FileUtil.createFile(filePath);
			
				InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
			
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
				
				StreamUtil.copyStream(inputStream, outputStream);
			}
		}catch(Exception e){
			LOGGER.error("upload file failure", e);
			throw new RuntimeException();
		}
	}

	/**
	 * 添加判断是否是上传文件
	 * @param request
	 * @return
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}

	/**
	 * 添加有上传文件的 创建参数的方法
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Param createParam(HttpServletRequest request) throws IOException {
		
		List<FormParam> formParamList = new ArrayList<FormParam>();
		List<FileParam> fileParamList = new ArrayList<FileParam>();
		
		try {
			Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
			if(CollectionUtil.isNotEmpty(fileParamList)){
				for(Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()){
					String fieldName = fileItemListEntry.getKey();
					List<FileItem> itemList = fileItemListEntry.getValue();
					if(CollectionUtil.isNotEmpty(itemList)){
						for(FileItem fileItem: itemList){
							
							if(fileItem.isFormField()){
								String fieldValue = fileItem.getString("UTF-8");
								formParamList.add(new FormParam(fieldName, fieldValue));
							}else{
								String fileName = FileUtil.getRealFileName(
										new String(fileItem.getName().getBytes(),"UTF-8"));
								
								if(StringUtil.isNotEmpty(fileName)){
									long fileSize = fileItem.getSize();
									InputStream inputStream = fileItem.getInputStream();
									String contentType = fileItem.getContentType();
									fileParamList.add(new FileParam(fieldName, fileName, 
											fileSize, contentType, inputStream));
								}
								
								
							}
						}
					}
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			LOGGER.error("create param failure",e);
			throw new RuntimeException();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return new Param(formParamList,fileParamList);
	}
}
