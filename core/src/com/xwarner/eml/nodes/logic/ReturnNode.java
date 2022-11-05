package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.Node;

public class ReturnNode extends Node {

	public String toString() {
		return "return";
	}

	public Object invoke(Bundle bundle) {
		Object o = null;
		if (getChildren().size() > 0)
			o = getChildren().get(0).invoke(bundle);
		return new ReturnFlag(o);
	}

}
