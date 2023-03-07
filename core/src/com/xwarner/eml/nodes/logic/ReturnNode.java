package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.Node;

public class ReturnNode extends Node {

	public String toString() {
		return "return";
	}

	public Object invoke() {
		Object o = null;
		if (getChildren().size() > 0)
			o = getChildren().get(0).invoke();
		return new ReturnFlag(o);
	}

}
