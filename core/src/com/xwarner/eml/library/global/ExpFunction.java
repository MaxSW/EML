package com.xwarner.eml.library.global;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;

public class ExpFunction extends Function {

	public Object run(ArrayList<Object> args, Bundle bundle) {
		if (args == null)
			return null;
		Object o = args.get(0);
		if (o instanceof BigDecimal) {
			BigDecimal val = (BigDecimal) o;
			return BigDecimal.valueOf(Math.exp(val.doubleValue()));
		}
		return null;
	}

}