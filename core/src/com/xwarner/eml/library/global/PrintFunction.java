package com.xwarner.eml.library.global;

import java.util.ArrayList;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.functions.Function;

public class PrintFunction extends Function {

	public Object run(ArrayList<Object> args) {
		if (args == null)
			return null;
		for (Object o : args) {
			if (o != null)
				Core.output.println(o);
			else
				Core.error.error("printing null object");
		}
		return null;
	}

}
