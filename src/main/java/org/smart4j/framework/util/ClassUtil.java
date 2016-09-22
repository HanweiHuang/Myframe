package org.smart4j.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class load util
 * @author huanghanwei
 *
 */
public final class ClassUtil {
	//set logger
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
	
	//get classloader first
	public static ClassLoader getClassLoader(){
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * reutn Class<?> obj associated with className
	 * @param className
	 * @param isInitialized
	 * @return
	 */
	public static Class<?> loadClass(String className,boolean isInitialized){
		Class<?> cls;
		try {
			cls = Class.forName(className,isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			LOGGER.error("load class failure", e);
			e.printStackTrace();
			throw new RuntimeException();
		}
		return cls;
	}
	
	/**
	 * get all classes in a package by package name
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClassSet(String packageName){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try{
			//1.find all url include packageName
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			//2.iterator all the urls
			while(urls.hasMoreElements()){
				URL url = urls.nextElement();
				
				System.out.println(url);
				
				if(url!=null){
					//2.1 get type of url
					String protocol = url.getProtocol();
					
					//2.1.1 if protocol is normal file:
					if(protocol.equals("file")){
						String packagePath = url.getPath().replace("%20", " ");
						addClass(classSet, packagePath, packageName);
						
					//2.1.2	if protocal is jar package
					}else if(protocol.equals("jar")){
						// 1.get connection
						JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
						if(jarURLConnection !=null){
							
							//2.get jarfile by connection
							JarFile jarFile = jarURLConnection.getJarFile();
							if(jarFile!=null){
								Enumeration<JarEntry> jarEntries = jarFile.entries();
								while(jarEntries.hasMoreElements()){
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if(jarEntryName.endsWith(".class")){
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replaceAll("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}//else jar
				}
			}
		}catch(Exception e){
			LOGGER.error("get class set failture", e);
		}
		return classSet;
	}
	
	/**
	 * @param sets
	 * @param packagePath
	 * @param packageName
	 */
	private static void addClass(Set<Class<?>> sets, String packagePath, String packageName){
		//1. list all files include normal files or directory by packagePath
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			
			//filter for returning only file or directory
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
				
			}
		});
		//2.iterator all the files
		for (File file : files) {
			String fileName = file.getName();
			//System.out.println(fileName);
			//2.1 deal with file
			if(file.isFile()){
				String className = fileName.substring(0,fileName.lastIndexOf("."));
				//build complete file path
				if(StringUtil.isNotEmpty(packageName)){
					className = packageName+"."+className;
				}
				doAddClass(sets, className);
				
			}
			//2.2 deal with directory 
			//use recursion in this part
			else{
				String subPackagePath = fileName;
				if(StringUtil.isNotEmpty(packagePath)){
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				String subPackageName = fileName;
				if(StringUtil.isNotEmpty(packageName)){
					subPackageName = packageName + "." + subPackageName;
				}
				
				addClass(sets, subPackagePath, subPackageName);
			}
		}
		
	}
	
	/**
	 * loadclass and put it in the sets
	 * @param sets
	 * @param className
	 */
	private static void doAddClass(Set<Class<?>> sets, String className){
		Class<?> cls = loadClass(className, false);
		sets.add(cls);
	}
}
