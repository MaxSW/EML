package com.xwarner.eml.interpreter.context.variables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.ArrayMemberNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;

public class ArrayVariable extends Variable {

	private HashMap<Integer, Variable> values;
	private int length;

	// TODO when setting a variable, it is retrieved and then put back, causing the
	// reference node to be evaluated twice

	public ArrayVariable() {
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
			int i = ((BigDecimal) node.invoke2(bundle)).intValue();
			if (i + 1 > length)
				length = i + 1;
			if (!values.containsKey(i))
				values.put(i, new NumericVariable(BigDecimal.ZERO));
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
		int i = ((BigDecimal) node.invoke2(bundle)).intValue();
		System.out.println("!");
		if (object instanceof NumericVariable)
			values.put(i, (NumericVariable) object);
		else if (object instanceof BigDecimal)
			values.put(i, new NumericVariable((BigDecimal) object));
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, Bundle bundle, int level) {
		VariableReferenceNode node = (VariableReferenceNode) ref.getChildren().get(level);
		Object object = args.get(0);
		if (node.name.equals("push")) {
			if (object instanceof NumericVariable)
				values.put(length, (NumericVariable) object);
			else if (object instanceof BigDecimal)
				values.put(length, new NumericVariable((BigDecimal) object));
			length++;
		}
		return null;
	}

}
