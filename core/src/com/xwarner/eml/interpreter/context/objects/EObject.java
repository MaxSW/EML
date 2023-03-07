package com.xwarner.eml.interpreter.context.objects;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.variables.Variable;

public class EObject extends Variable {

	private EClass cls;
	public SubContext context;

	public EObject(EClass cls) {
		this.cls = cls;
	}

	public void instantiate() {
		Core.context.enterObject(this);
		if (cls != null) {
			cls.getBody().pre_invoke();
			cls.getBody().invoke();
		}
		Core.context.exitObject();
	}

	public String toString() {
		return "object";
	}

}
