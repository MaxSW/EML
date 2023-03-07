package com.xwarner.eml.interpreter.context.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class NativeFunction extends Function {

	private Method method;

	public NativeFunction(Method method) {
		this.method = method;
	}

	public Object run(ArrayList<Object> args) {
		Object[] args2 = new Object[] {};

		// TODO handle all these errors better

		if (args != null) {
			// if there are arguments
			if (method.getParameterCount() < args.size()) {
				// too many arguments
				System.err.println("invalid arguments");
				return null;
			} else if (method.getParameterCount() > args.size()) {
				// too few arguments is fine, we just backfill with nulls
				Object[] temp_args = args.toArray();
				args2 = new Object[method.getParameterCount()];
				for (int i = 0; i < args2.length; i++) {
					if (i < temp_args.length)
						args2[i] = temp_args[i];
					else
						args2[i] = null;
				}
			} else {
				// exact number of arguments
				args2 = args.toArray();
			}
		} else {
			// if there are no arguments
			if (method.getParameterCount() != 0) {
				System.err.println("invalid arguments");
				return null;
			}
		}

		// run the method
		try {
			return method.invoke(null, args2);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
