package com.chatapp.config.language;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.chatapp.config.language.AbuseWord;

@XmlRootElement(name = "LanguageProcessor")
@XmlAccessorType(XmlAccessType.FIELD)
public class LanguageProcessor {

	@XmlElement(name = "AbuseWord")
	private ArrayList<AbuseWord> words;

	public ArrayList<AbuseWord> getRows() {
		return words;
	}

	public void setRows(ArrayList<AbuseWord> words) {
		this.words = words;
	}

}
