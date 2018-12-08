package com.xwarner.eml.nodes.values;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.VectorVariable;
import com.xwarner.eml.interpreter.context.variables.values.Vector;
import com.xwarner.eml.nodes.Node;

public class VectorNode extends Node {

	public String toString() {
		return "vector";
	}

	public String toSaveString() {
		return "24";
	}

	public Object invoke2(Bundle bundle) {
		VectorVariable var = new VectorVariable(getChildren().size());
		Vector v = new Vector(getChildren().size());
		for (int i = 0; i < getChildren().size(); i++) {
			v.vals[i] = (BigDecimal) getChildren().get(i).invoke2(bundle);
		}
		var.setValue(v);
		return var;
	}

}
