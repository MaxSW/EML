package com.xwarner.eml.interpreter.context.variables;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;

public class StringVariable extends Variable {

	private String value;

	public StringVariable(String value) {
		this.value = value;
	}

	public StringVariable(Definition definition) {
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
		value = (String) obj;
	}

}
