package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.Node;

public class ReturnNode extends Node {

	public String toString() {
		return "return";
	}

	public Object invoke2(Bundle bundle) {
		Object o = null;
		if (getChildren().size() > 0)
			o = getChildren().get(0).invoke2(bundle);
		return new ReturnFlag(o);
	}

	public String toSaveString() {
		return "15";
	}
}
