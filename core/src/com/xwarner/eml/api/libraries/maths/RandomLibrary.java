package com.xwarner.eml.api.libraries.maths;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;

public class RandomLibrary implements Library {

	private Random random;

	public RandomLibrary() {
		random = new Random();
	}

	public SubContext export() {
		SubContext ctx = new SubContext(null);

		ctx.setFunction("between", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				int a = ((BigDecimal) args.get(0)).intValue();
				int b = ((BigDecimal) args.get(1)).intValue();
				return BigDecimal.valueOf(random.nextInt(b + 1 - a) + a);
			}
		});

		return ctx;
	}

}
