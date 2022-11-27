package com.xwarner.eml.interpreter.context.objects;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.variables.Variable;

public class EObject extends Variable {

	private EClass cls;
	public SubContext context;

	public EObject(EClass cls) {
		this.cls = cls;
	}

	public void instantiate(Bundle bundle) {
		bundle.context.enterObject(this);
		if (cls != null) {
			cls.getBody().pre_invoke(bundle);
			cls.getBody().invoke(bundle);
		}
		bundle.context.exitObject();
	}

	public String toString() {
		return "object";
	}

}
