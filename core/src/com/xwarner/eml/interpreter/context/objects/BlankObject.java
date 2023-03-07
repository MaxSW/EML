package com.xwarner.eml.interpreter.context.objects;

import java.util.HashMap;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.variables.Variable;

/**
 * A blank object that can be created programmatically in Java
 * 
 * @author max
 *
 */

public class BlankObject extends EObject {

	public HashMap<String, Variable> vars;
	public HashMap<String, Function> funcs;

	public BlankObject() {
		super(null);
		vars = new HashMap<String, Variable>();
		funcs = new HashMap<String, Function>();
	}

	/**
	 * Sets up the object in the context, including setting the functions and
	 * variables
	 */
	public void instantiate() {
		Core.context.enterObject(this);
		for (String s : vars.keySet())
			Core.context.createVariable(s, vars.get(s));
		for (String s : funcs.keySet())
			Core.context.setFunction(s, funcs.get(s));
		Core.context.exitObject();
	}

}
