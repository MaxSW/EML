package com.xwarner.eml.interpreter.context.variables;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;

public class MatrixVariable extends Variable {

	private Matrix matrix;

	public MatrixVariable(int h, int w) {
		matrix = new Matrix(h, w);
	}

	public MatrixVariable(Matrix matrix) {
		this.matrix = matrix;
	}

	public MatrixVariable(Definition definition) {
		setEquation(true);
		setDefinition(definition);
	}

	public Object getValue(Bundle bundle) {
		if (isEquation())
			matrix = (Matrix) definition.evaluate(bundle);
		return matrix;
	}

	public void setValue(Object value) {
		super.setValue(value);
		this.matrix = (Matrix) value;
	}

}
