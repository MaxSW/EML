package com.xwarner.eml.nodes.logic;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.flags.BreakFlag;
import com.xwarner.eml.nodes.Node;

public class BreakNode extends Node {

	public String toString() {
		return "break";
	}

	public Object invoke(Bundle bundle) {
		return new BreakFlag();
	}
}
