package com.xwarner.eml.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOManager {

	public static String root = "";

	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(root + path));
		return new String(encoded, Charset.defaultCharset());
	}

	public static void writeFile(String path, String str) throws IOException {
		PrintWriter out = new PrintWriter(root + path);
		out.write(str);
		out.close();
	}

}
