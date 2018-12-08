package com.xwarner.eml.interpreter.context.objects;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.Variable;

public class EObject extends Variable {

	private EClass cls;

	public EObject(EClass cls) {
		this.cls = cls;
	}

	public void instantiate(Bundle bundle) {
		bundle.context.enter(this);
		if (cls != null) {
			cls.getBody().invoke1(bundle);
			cls.getBody().invoke2(bundle);
		}
		bundle.context.exit();
	}

}
