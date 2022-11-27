package com.xwarner.eml.interpreter.context.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;

public class NativeFunction extends Function {

	private Method method;

	public NativeFunction(Method method) {
		this.method = method;
	}

	public Object run(ArrayList<Object> args, Bundle bundle) {
		// TODO handle arguments and return in a smart way here so the native methods
		// can use native types where possible
		try {
			return method.invoke(null, new Object[] {});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
