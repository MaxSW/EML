package com.xwarner.eml.library.global;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.BlankObject;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.library.Invokable;
import com.xwarner.eml.library.invokables.GUI;
import com.xwarner.eml.library.invokables.Maths;
import com.xwarner.eml.library.invokables.Random;

/**
 * A global function that is used in EML to call pre-specified Java functions.
 * Acts as the link between the two languages.
 * 
 * @author max
 *
 */

public class InvokeFunction extends Function {

	public HashMap<String, Invokable> map;

	public InvokeFunction() {
		map = new HashMap<String, Invokable>();
		new Maths().load(map);
		new Random().load(map);
		new GUI().load(map);
		map.put("test", new Invokable() {

			@Override
			public Object run(ArrayList<Object> args, Bundle bundle) {
				BlankObject obj = new BlankObject();
				obj.vars.put("a", new NumericVariable(BigDecimal.valueOf(5.12)));
				obj.funcs.put("b", new Function() {
					public Object run(ArrayList<Object> args2, Bundle bundle) {
						System.out.println("hello world!");
						return BigDecimal.ONE;
					}
				});
				obj.instantiate(bundle);
				return obj;
			}

		});
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
