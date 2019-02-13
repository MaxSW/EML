package com.xwarner.eml.interpreter.context.variables;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;

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

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, Bundle bundle, int level) {
		VariableReferenceNode node = (VariableReferenceNode) ref.getChildren().get(level);
		Object object = args.get(0);
		if (node.name.equals("subString")) {
			if (object instanceof BigDecimal) {
				if (args.size() == 1)
					return (value.substring(((BigDecimal) object).intValue()));
				else if (args.size() == 2) {
					Object object2 = args.get(1);
					if (object2 instanceof BigDecimal) {
						return (value.substring(((BigDecimal) object).intValue(), ((BigDecimal) object2).intValue()));
					}
				}
			}
		} else if (node.name.equals("charAt")) {
			if (object instanceof BigDecimal) {
				return Character.toString((value.charAt(((BigDecimal) object).intValue())));
			}
		}
		return null;
	}

	public Variable getVariable(ReferenceNode ref, int level, Bundle bundle) {
		VariableReferenceNode node = (VariableReferenceNode) ref.getChildren().get(level);
		if (node.name.equals("length")) {
			return new NumericVariable(value.length());
		}
		return null;
	}

	public String toString() {
		return value;
	}

}
