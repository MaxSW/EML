package com.xwarner.eml.nodes.variables;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;

public class ArrayMemberNode extends Node {

	public String toString() {
		return "array member";
	}

	public Object invoke(Bundle bundle) {
		return getChildren().get(0).invoke(bundle);
	}
}
