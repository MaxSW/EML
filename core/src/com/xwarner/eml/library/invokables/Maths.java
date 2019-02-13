package com.xwarner.eml.library.invokables;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.NullVariable;
import com.xwarner.eml.library.Invokable;

/**
 * Loads maths related functions into the invoke function
 * 
 * @author max
 *
 */

public class Maths {

	public void load(HashMap<String, Invokable> map) {
		map.put("maths.round", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				BigDecimal val = (BigDecimal) args.get(1);
				if (args.get(2) instanceof NullVariable)
					return val.setScale(0, RoundingMode.HALF_EVEN);
				BigDecimal val2 = (BigDecimal) args.get(2);
				return val.setScale(val2.intValue(), RoundingMode.HALF_EVEN);
			}
		});
		map.put("maths.floor", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				return ((BigDecimal) args.get(0)).setScale(0, RoundingMode.FLOOR);
			}
		});
		map.put("maths.ceil", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				return ((BigDecimal) args.get(0)).setScale(0, RoundingMode.CEILING);
			}
		});
	}

}
