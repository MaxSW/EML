package com.xwarner.eml.library.global;

import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.library.Invokable;
import com.xwarner.eml.library.invokables.GUI;
import com.xwarner.eml.library.invokables.Maths;
import com.xwarner.eml.library.invokables.Random;

public class InvokeFunction extends Function {

	public HashMap<String, Invokable> map;

	public InvokeFunction() {
		map = new HashMap<String, Invokable>();
		new Maths().load(map);
		new Random().load(map);
		new GUI().load(map);
	}

	public Object run(ArrayList<Object> args, Bundle bundle) {
		if (args == null)
			return null;
		Object o = args.get(0);
		if (!(o instanceof String))
			throw new Error("expecting string");

		return map.get(o).run(args, bundle);
	}
}
