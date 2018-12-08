package com.xwarner.eml.api.libraries.maths;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;

public class MathsLibrary implements Library {

	public SubContext export() {
		SubContext ctx = new SubContext(null);

		ctx.setFunction("round", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				if (args == null)
					throw new Error("invalid arguments");
				if (args.size() == 1) {
					BigDecimal val = (BigDecimal) args.get(0);
					return val.setScale(0, RoundingMode.HALF_UP);
				} else {
					BigDecimal val = (BigDecimal) args.get(0);
					BigDecimal val2 = (BigDecimal) args.get(1);
					return val.setScale(val2.intValue(), RoundingMode.HALF_EVEN);
				}
			}
		});
		ctx.setFunction("floor", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				if (args == null)
					throw new Error("invalid arguments");
				BigDecimal val = (BigDecimal) args.get(0);
				return val.setScale(0, RoundingMode.FLOOR);
			}

		});
		ctx.setFunction("ceil", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				if (args == null)
					throw new Error("invalid arguments");
				BigDecimal val = (BigDecimal) args.get(0);
				return val.setScale(0, RoundingMode.CEILING);
			}

		});
		ctx.setFunction("abs", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				if (args == null)
					throw new Error("invalid arguments");
				BigDecimal val = (BigDecimal) args.get(0);
				return val.abs();
			}

		});

		return ctx;
	}

}
