package com.xwarner.eml.ide.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class EMLFile {

	public File file;
	public String name;

	public EMLFile(File file) {
		this.file = file;
		this.name = file.getName();
	}

	public String getText() {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(file.toPath());
			return new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
