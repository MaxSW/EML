package com.xwarner.eml.interpreter.context.variables;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;

public class BooleanVariable extends Variable {

	private boolean value;

	public BooleanVariable(boolean value) {
		this.value = value;
	}

	public BooleanVariable(Definition definition) {
		this.setEquation(true);
		this.setDefinition(definition);
	}

	public Object getValue(Bundle bundle) {
		if (isEquation())
			return definition.evaluate(bundle);
		else
			return value;
	}

	public void setValue(Object obj) {
		super.setValue(obj);
		value = (Boolean) obj;
	}

}
