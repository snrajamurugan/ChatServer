package com.chatapp.config.language;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.chatapp.server.ChatAppConstants;
import com.chatapp.server.LogHandler;

public class LanguageProcessorConfigParser {

	public LanguageProcessor loadLanguageProcessor() {
		LanguageProcessor words = null;
		try {
			File file = new File(ChatAppConstants.LANGUAGE_CONFIG_FILE);
			JAXBContext jaxbContext = JAXBContext.newInstance(LanguageProcessor.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			words = (LanguageProcessor) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			LogHandler.getLogger().log("Error in parsing the language processor config xml - " +
					ChatAppConstants.LANGUAGE_CONFIG_FILE);
		}
		return words;
	}

}
