package com.xwarner.eml.interpreter.context.variables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.ArrayMemberNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;

public class ArrayVariable extends Variable {

	private HashMap<Integer, Variable> values;
	private int length;
	public String type;

	// TODO when setting a variable, it is retrieved and then put back, causing the
	// reference node to be evaluated twice

	public ArrayVariable(String type) {
		this.type = type;
		values = new HashMap<Integer, Variable>();
		length = 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < values.size(); i++) {
			sb.append(values.get(i));
			if (i != values.size() - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	public Variable getVariable(ReferenceNode ref, int level, Bundle bundle) {
		if (ref.getChildren().get(level) instanceof ArrayMemberNode) {
			ArrayMemberNode node = (ArrayMemberNode) ref.getChildren().get(level);
			int i = ((BigDecimal) node.invoke(bundle)).intValue();
			if (i + 1 > length)
				length = i + 1;
			if (!values.containsKey(i)) {
				values.put(i, bundle.vars.blankVariable(type));
			}
			return values.get(i);
		} else {
			VariableReferenceNode node = (VariableReferenceNode) ref.getChildren().get(level);
			if (node.name.equals("length")) {
				return new NumericVariable(BigDecimal.valueOf(length));
			}
		}
		return null;
	}

	public void setVariable(ReferenceNode ref, int level, Bundle bundle, Object object) {
		ArrayMemberNode node = (ArrayMemberNode) ref.getChildren().get(level);
		int i = ((BigDecimal) node.invoke(bundle)).intValue();
		values.put(i, (Variable) object);
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, Bundle bundle, int level) {
		VariableReferenceNode node = (VariableReferenceNode) ref.getChildren().get(level);
		Object object = args.get(0);
		if (node.name.equals("push")) {
			values.put(length, (Variable) object);
			length++;
		}
		return null;
	}

}
