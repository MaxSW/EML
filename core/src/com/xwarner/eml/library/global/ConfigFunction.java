package com.xwarner.eml.library.global;

import java.util.ArrayList;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.functions.Function;

public class ConfigFunction extends Function {

	public Object run(ArrayList<Object> args) {
		if (args == null)
			return null;
		if (args.size() == 1) {
			Core.error.error("Too few arguments for config function");
			return null;
		}
		
		Core.config.set((String) args.get(0), args.get(1));
		
		return null;
	}

}
