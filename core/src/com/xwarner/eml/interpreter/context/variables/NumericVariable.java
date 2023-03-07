package com.xwarner.eml.interpreter.context.variables;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.definitions.Definition;

public class NumericVariable extends Variable {

	private BigDecimal value;

	public NumericVariable(int i) {
		this.value = BigDecimal.valueOf(i);
	}

	public NumericVariable(BigDecimal value) {
		this.value = value;
	}

	public NumericVariable(Definition definition) {
		this.setEquation(true);
		this.setDefinition(definition);
	}

	public Object getValue() {
		if (isEquation())
			return definition.evaluate();
		else
			return value;
	}

	public void setValue(Object value) {
		super.setValue(value);
		this.value = (BigDecimal) value;
	}

	public String toString() {
		return value.toString();
	}

}
