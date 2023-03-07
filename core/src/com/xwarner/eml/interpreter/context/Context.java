package com.xwarner.eml.interpreter.context;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.EClass;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.nodes.ReferenceNode;

public class Context {

	private SubContext current;
	private SubContext global;
	private DataStore store;

	public Context() {
		store = new DataStore();
		global = new SubContext(null, store);
		current = global;
	}

	/*
	 * When we enter a function, we don't need to save the context to use again
	 */
	public void enterFunction() {
		current = new SubContext(current, store);
	}

	public void exitFunction() {
		current = current.parent;
	}

	/*
	 * When we enter an object, we want to save the context
	 */
	public void enterObject(EObject obj) {
		if (obj.context == null)
			obj.context = new SubContext(current, store);
		current = obj.context;
	}

	public void exitObject() {
		current = current.parent;
	}

	public void importLibrary(Variable var, SubContext ctx) {
		ctx.parent = current;
//		current.children.put(var, ctx);
	}

	/** Interface functions **/
	public void createVariable(String name, Object obj) {
		current.createVariable(name, obj);
	}

	public void createVariable(ReferenceNode ref, Object obj, int level) {
		current.createVariable(ref, obj, level);
	}

	public Variable getVariable(String name, ReferenceNode ref) {
		return current.getVariable(name, ref);
	}

	public Variable getVariable(ReferenceNode ref, int level) {
		return current.getVariable(ref, level);
	}

	public void setFunction(String name, Function function) {
		current.setFunction(name, function);
	}

	public Object runFunction(String name, ArrayList<Object> args) {
		return current.runFunction(name, args);
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, int level) {
		return current.runFunction(ref, args, level);
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
