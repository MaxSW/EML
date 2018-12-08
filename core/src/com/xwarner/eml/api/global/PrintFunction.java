package com.xwarner.eml.api.global;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.tools.ErrorHandler;

public class PrintFunction extends Function {

	public Object run(ArrayList<Object> args, Bundle bundle) {
		if (args == null)
			return null;
		for (Object o : args) {
			if (o != null)
				bundle.output.println(o);
			else
				ErrorHandler.error("printing null object");
		}
		return null;
	}

}
