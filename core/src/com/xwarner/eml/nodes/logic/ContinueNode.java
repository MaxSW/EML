package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.flags.ContinueFlag;
import com.xwarner.eml.nodes.Node;

public class ContinueNode extends Node {

	public String toString() {
		return "continue";
	}

	public Object invoke(Bundle bundle) {
		return new ContinueFlag();
	}
}
