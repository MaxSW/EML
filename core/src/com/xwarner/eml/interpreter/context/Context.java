package com.xwarner.eml.interpreter.context;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.EClass;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.nodes.ReferenceNode;

public class Context {

	private SubContext current;

	public Context() {
		current = new SubContext(null);
	}

	public void enter(Variable var) {
		if (!current.children.containsKey(var))
			current.children.put(var, new SubContext(current));
		current = current.children.get(var);
	}

	public void exit() {
		current = current.parent;
	}

	public void importLibrary(Variable var, SubContext ctx) {
		ctx.parent = current;
		current.children.put(var, ctx);
	}

	/** Interface functions **/

	public void createVariable(String name, Object obj) {
		current.createVariable(name, obj);
	}

	public void createVariable(ReferenceNode ref, Object obj, int level, Bundle bundle) {
		current.createVariable(ref, obj, level, bundle);
	}

	public Variable getVariable(String name, ReferenceNode ref) {
		return current.getVariable(name, ref);
	}

	public Variable getVariable(ReferenceNode ref, int level, Bundle bundle) {
		return current.getVariable(ref, level, bundle);
	}

	public void setFunction(String name, Function function) {
		current.setFunction(name, function);
	}

	public Object runFunction(String name, ArrayList<Object> args, Bundle bundle) {
		return current.runFunction(name, args, bundle);
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, Bundle bundle, int level) {
		return current.runFunction(ref, args, bundle, level);
	}

	public void setClass(String name, EClass cls) {
		current.setClass(name, cls);
	}

	public EClass getClass(String name) {
		return current.getClass(name);
	}

	public EClass getClass(ReferenceNode ref, int level) {
		return current.getClass(ref, level);
	}

}
