package com.projectSta.services;

import org.apache.log4j.Logger;

import com.projectSta.utils.AppUtils;

public class LoggingService {
	public static Logger getLog(@SuppressWarnings("rawtypes") Class classes) {

		/*
		 * String output = Executions.getCurrent().getDesktop().getWebApp()
		 * .getRealPath(AppUtils.PATHFILES_ROOT +
		 * "/logs/appLog.log").replace("\\", "/");
		 * 
		 * String directory = Executions.getCurrent().getDesktop().getWebApp()
		 * .getRealPath("/WEB-INF/classes/log4j.properties");
		 * 
		 * InputStream in = classes.getResourceAsStream(directory);
		 * 
		 * Properties prop = new Properties();
		 * 
		 * try { prop.load(new FileInputStream(directory)); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 * 
		 * prop.setProperty("log4j.appender.file.File", output); try { prop.store(new
		 * FileOutputStream(directory), null); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 */

		Logger logger = Logger.getLogger(classes);
		return logger;
	}

	public String loggerWritter(String process, String time, String msg) {
		return AppUtils.FORMAT_OUTPUT_LOG.replace("{process}", process).replace("{time}", time).replace("{msg}", msg);
	}
}
