package com.xwarner.eml.interpreter.context.variables;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.interpreter.context.variables.values.Vector;

public class VectorVariable extends Variable {

	private Vector vector;

	public VectorVariable(int size) {
		vector = new Vector(size);
	}
	
	public VectorVariable(Vector vector) {
		this.vector = vector;
	}

	public VectorVariable(Definition definition) {
		setEquation(true);
		setDefinition(definition);
	}

	public Object getValue(Bundle bundle) {
		if (isEquation())
			return definition.evaluate(bundle);
		else
			return vector;
	}

	public void setValue(Object value) {
		super.setValue(value);
		this.vector = (Vector) value;
	}
}
