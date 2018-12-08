package com.xwarner.eml.interpreter;

import java.io.PrintStream;
import java.math.BigDecimal;

import com.xwarner.eml.api.global.ExpFunction;
import com.xwarner.eml.api.global.ImportFunction;
import com.xwarner.eml.api.global.LnFunction;
import com.xwarner.eml.api.global.PrintFunction;
import com.xwarner.eml.interpreter.context.Context;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.tools.Evaluator;

public class Bundle {

	public Context context;
	public Evaluator evaluator;
	public PrintStream output;

	public Bundle() {
		this.context = new Context();
		this.evaluator = new Evaluator();
		this.output = System.out;
	}

	public void init() {
		ImportFunction.loadLibraries();
		// initialise global functions
		context.setFunction("print", new PrintFunction());
		context.setFunction("import", new ImportFunction());
		context.setFunction("exp", new ExpFunction());
		context.setFunction("ln", new LnFunction());
		context.createVariable("pi", new NumericVariable(BigDecimal.valueOf(Math.PI)));
		context.createVariable("e", new NumericVariable(BigDecimal.valueOf(Math.E)));
	}

}
