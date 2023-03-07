package com.xwarner.eml.interpreter;

import java.math.BigDecimal;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.objects.BlankObject;
import com.xwarner.eml.interpreter.context.variables.BooleanVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;

public class VariableTools {

	public Variable blankVariable(String type) {
		switch (type) {
		case "var":
			return new NumericVariable(BigDecimal.ZERO);
		case "str":
			return new StringVariable("");
		case "bool":
			return new BooleanVariable(false);
		case "obj":
			return new BlankObject();
		default:
			return new Variable();
		}
	}

	public Variable generateVariable(Object object) {
		if (object instanceof BigDecimal)
			return new NumericVariable((BigDecimal) object);
		else if (object instanceof String)
			return new StringVariable((String) object);
		else if (object instanceof Boolean)
			return new BooleanVariable((Boolean) object);
		Core.error.error("unknown variable object type " + object);
		return new Variable();
	}

	// TODO some functions for converting between Java vars and EML var

}
