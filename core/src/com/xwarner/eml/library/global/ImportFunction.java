package com.xwarner.eml.library.global;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.functions.NativeFunction;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.library.MathsLibrary;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.Parser;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.util.IOManager;

public class ImportFunction extends Function {

	@SuppressWarnings("rawtypes")
	public HashMap<String, Class> nativeLibraries;

	@SuppressWarnings("rawtypes")
	public ImportFunction() {
		nativeLibraries = new HashMap<String, Class>();
		nativeLibraries.put("maths", MathsLibrary.class);
	}

	public Object run(ArrayList<Object> args, Bundle bundle) {
		if (args == null)
			throw new Error("missing which library to import");
		Object o = args.get(0);
		if (!(o instanceof String))
			throw new Error("incorrect agument in import function");

		String str = (String) o;
		System.out.println("importing: " + str);

		if (nativeLibraries.keySet().contains(str)) {
			// Load a native library
			EObject obj = new EObject(null);
			// this sets up the context in a consistent way
			bundle.context.enterObject(obj);
			bundle.context.exitObject();

			@SuppressWarnings("rawtypes")
			Class nativeClass = nativeLibraries.get(str);
			Method[] methods = nativeClass.getDeclaredMethods();

			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				// could exclude methods here if needed
				NativeFunction function = new NativeFunction(method);
				obj.context.setFunction(method.getName(), function);
			}

			return obj;
		} else {
			// Load library from a file
			String src = "";
			if (str.startsWith("./")) {
				String file = str.substring(2);
				try {
					src = IOManager.readFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				String file = str + ".eml";
				try {
					src = IOManager.readLibrary(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			EObject obj = new EObject(null);
			Tree tree = new Parser(src).parse();

			bundle.context.enterObject(obj);
			for (Node node : tree.getChildren())
				node.pre_invoke(bundle);
			for (Node node : tree.getChildren())
				node.invoke(bundle);
			bundle.context.exitObject();

			return obj;
		}
	}

}
