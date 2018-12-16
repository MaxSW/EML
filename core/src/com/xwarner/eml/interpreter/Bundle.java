package com.xwarner.eml.interpreter;

import java.io.PrintStream;
import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.Context;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.tools.Evaluator;
import com.xwarner.eml.library.global.ExpFunction;
import com.xwarner.eml.library.global.ImportFunction;
import com.xwarner.eml.library.global.InvokeFunction;
import com.xwarner.eml.library.global.LnFunction;
import com.xwarner.eml.library.global.PrintFunction;

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
		// initialise global functions
		context.setFunction("___invoke", new InvokeFunction());
		context.setFunction("print", new PrintFunction());
		context.setFunction("import", new ImportFunction());
		context.setFunction("exp", new ExpFunction());
		context.setFunction("ln", new LnFunction());
		context.createVariable("pi", new NumericVariable(BigDecimal.valueOf(Math.PI)));
		context.createVariable("e", new NumericVariable(BigDecimal.valueOf(Math.E)));
	}

}
