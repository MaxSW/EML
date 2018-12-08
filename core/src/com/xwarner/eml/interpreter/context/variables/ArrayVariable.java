package com.xwarner.eml.interpreter.context.variables;

import java.math.BigDecimal;
import java.util.HashMap;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.ArrayMemberNode;

public class ArrayVariable extends Variable {

	private HashMap<Integer, Variable> values;

	// TODO when setting a variable, it is retrieved and then put back, causing the
	// reference node to be evaluated twice

	public ArrayVariable() {
		values = new HashMap<Integer, Variable>();
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
		ArrayMemberNode node = (ArrayMemberNode) ref.getChildren().get(level);
		int i = ((BigDecimal) node.invoke2(bundle)).intValue();
		if (!values.containsKey(i))
			values.put(i, new NumericVariable(BigDecimal.ZERO));
		return values.get(i);

	}

	public void setVariable(ReferenceNode ref, int level, Bundle bundle, Object object) {
		ArrayMemberNode node = (ArrayMemberNode) ref.getChildren().get(level);
		int i = ((BigDecimal) node.invoke2(bundle)).intValue();
		if (object instanceof NumericVariable)
			values.put(i, (NumericVariable) object);
		else if (object instanceof BigDecimal)
			values.put(i, new NumericVariable((BigDecimal) object));
	}

}
