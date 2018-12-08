package com.xwarner.eml.api.libraries.display;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.api.libraries.display.tools.GraphWindow;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;

import javafx.application.Application;

public class GraphLibrary implements Library {

	public SubContext export() {
		SubContext ctx = new SubContext(null);

		ctx.setFunction("graph", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {

				NumericVariable var = (NumericVariable) args.get(0);

				GraphWindow.defs = new ArrayList<NumericVariable>();

				int i = 1;
				while (true) {
					Object o = args.get(i);
					if (o instanceof NumericVariable) {
						GraphWindow.defs.add((NumericVariable) o);
						i++;
					} else {
						break;
					}
				}

				int a = 0;
				int b = 10;
				int n = 100;

				if (args.size() > i) {
					a = ((BigDecimal) args.get(i)).intValue();
				}
				if (args.size() > i + 1)
					b = ((BigDecimal) args.get(i + 1)).intValue();
				if (args.size() > i + 2)
					n = ((BigDecimal) args.get(i + 2)).intValue();

				GraphWindow.var = var;
				GraphWindow.a = a;
				GraphWindow.b = b;
				GraphWindow.n = n;
				GraphWindow.bundle = bundle;

				new Thread(new Runnable() {
					public void run() {
						Application.launch(GraphWindow.class, new String[] {});
					}
				}).start();

				return null;
			}
		});

		return ctx;
	}

	public void createWindow() {

	}

}
