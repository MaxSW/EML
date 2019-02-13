package com.xwarner.eml.library.global;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.util.ErrorHandler;

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
