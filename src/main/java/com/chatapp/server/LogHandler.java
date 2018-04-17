package com.chatapp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class LogHandler {

	private static Logger logger = null;

	private static LogHandler logHandler = null;
	
	private LogHandler() {
		init();
	}

	public static LogHandler getLogger() {
		if (logHandler == null) {
			logHandler = new LogHandler();
		}
		
		return logHandler;
	}
	
	public void init() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		loggerContext.reset();
		configurator.setContext(loggerContext);
		try {
			configurator.doConfigure(ChatAppConstants.LOG_BACK_CONFIG_FILE);
			logger = LoggerFactory.getLogger(LogHandler.class);
		} catch (JoranException e) {
			e.printStackTrace(); // Do Nothing
		}
	}

	public void log(String chatroom, String msg, String user) {
		if (logger != null) {
			MDC.put("Chatroom", chatroom);
			logger.info(user + "> " + msg);
		}
	}
	
	public void log(String msg) {
		if (logger != null) {
			log("server-log", msg, "server");
		}
	}

}