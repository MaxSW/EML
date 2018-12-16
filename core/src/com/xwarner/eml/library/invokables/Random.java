package com.xwarner.eml.library.invokables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.library.Invokable;

public class Random {

	private java.util.Random random;

	public void load(HashMap<String, Invokable> map) {
		map.put("random.between", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				if(random == null)
					random = new java.util.Random();
				int a = ((BigDecimal) args.get(1)).intValue();
				int b = ((BigDecimal) args.get(2)).intValue();
				return BigDecimal.valueOf(random.nextInt(b + 1 - a) + a);
			}
		});
	}

}
