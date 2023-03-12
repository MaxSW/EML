package com.xwarner.eml.interpreter.context.variables;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;

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

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, int level) {
		VariableReferenceNode node = (VariableReferenceNode) ref.getChildren().get(level);
		if (node.name.equals("round")) {
			if (args == null) {
				this.setValue(value.setScale(0, RoundingMode.HALF_UP));
			} else {
				BigDecimal places = (BigDecimal) args.get(0);
				this.setValue(value.setScale(places.intValue(), RoundingMode.HALF_UP));
			}
		} else if (node.name.equals("floor")) {
			if (args == null) {
				this.setValue(value.setScale(0, RoundingMode.FLOOR));
			} else {
				BigDecimal places = (BigDecimal) args.get(0);
				this.setValue(value.setScale(places.intValue(), RoundingMode.FLOOR));
			}
		} else if (node.name.equals("ceil")) {
			if (args == null) {
				this.setValue(value.setScale(0, RoundingMode.CEILING));
			} else {
				BigDecimal places = (BigDecimal) args.get(0);
				this.setValue(value.setScale(places.intValue(), RoundingMode.CEILING));
			}
		} else if (node.name.equals("abs")) {
			this.setValue(value.abs());
		}
		return null;
	}

	public String toString() {
		return value.toString();
	}

}
