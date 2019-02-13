package com.xwarner.eml.nodes.values;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.nodes.Node;

public class StringNode extends Node {

	public String value;

	public StringNode(String value) {
		super();
		this.value = value;
	}

	public StringNode() {
		// TODO Auto-generated constructor stub
	}

	public Object invoke2(Bundle bundle) {
		return value;
	}

	public String toString() {
		return "string - value: " + value;
	}

	public String toSaveString() {
		return "16 \"" + value + "\"";
	}

	public void fromSaveString(String[] split, String str) {
		// handles the possibilities of spaces in strings
		List<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(str);
		while (m.find())
			list.add(m.group(1));
		value = list.get(2).substring(1, list.get(2).length() - 1);
	}

}
