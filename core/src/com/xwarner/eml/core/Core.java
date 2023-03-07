package com.xwarner.eml.core;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.xwarner.eml.interpreter.VariableTools;
import com.xwarner.eml.interpreter.context.Context;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.evaluator.Evaluator;
import com.xwarner.eml.library.global.ExpFunction;
import com.xwarner.eml.library.global.ImportFunction;
import com.xwarner.eml.library.global.LnFunction;
import com.xwarner.eml.library.global.PrintFunction;

public class Core {

	public static String root = "", libRoot = "";

	public static Context context;
	public static Evaluator evaluator;
	public static PrintStream output;
	public static VariableTools vars;
	public static ErrorHandler error;

	public static void init() {
		context = new Context();
		evaluator = new Evaluator();
		output = System.out;
		vars = new VariableTools();
		error = new ErrorHandler();

		// initialise global functions
		context.setFunction("print", new PrintFunction());
		context.setFunction("import", new ImportFunction());
		context.setFunction("exp", new ExpFunction());
		context.setFunction("ln", new LnFunction());
		context.createVariable("pi", new NumericVariable(BigDecimal.valueOf(Math.PI)));
		context.createVariable("e", new NumericVariable(BigDecimal.valueOf(Math.E)));
	}

	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(root + path));
		return new String(encoded, Charset.defaultCharset());
	}

	public static String readLibrary(String path) throws IOException {
		if (libRoot.equals("")) {
			// TODO better error handling here
			System.err.println("Need to specify library path");
		}
		byte[] encoded = Files.readAllBytes(Paths.get(libRoot + path));
		return new String(encoded, Charset.defaultCharset());
	}

	public static void writeFile(String path, String str) throws IOException {
		PrintWriter out = new PrintWriter(root + path);
		out.write(str);
		out.close();
	}

}
