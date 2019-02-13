package com.xwarner.eml.nodes.variables;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;

public class ArrayMemberNode extends Node {

	public String toString() {
		return "array member";
	}

	public String toSaveString() {
		return "17";
	}

	public Object invoke2(Bundle bundle) {
		return getChildren().get(0).invoke2(bundle);
	}
}
