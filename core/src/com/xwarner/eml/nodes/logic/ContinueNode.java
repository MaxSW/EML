package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.flags.ContinueFlag;
import com.xwarner.eml.nodes.Node;

public class ContinueNode extends Node {

	public String toString() {
		return "continue";
	}

	public String toSaveString() {
		return "22";
	}

	public Object invoke2(Bundle bundle) {
		return new ContinueFlag();
	}
}
