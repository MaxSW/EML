package com.xwarner.eml.api.libraries.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;

public class InputLibrary implements Library {

	public SubContext export() {
		SubContext ctx = new SubContext(null);

		ctx.setFunction("getNumber", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {

				String text = "Input a number";
				String title = "Number Input";

				if (args.size() > 0)
					text = (String) args.get(0);
				if (args.size() > 1)
					title = (String) args.get(1);

				String s = (String) JOptionPane.showInputDialog(null, text, title, JOptionPane.PLAIN_MESSAGE, null,
						null, null);

				if (s == null || s.equals(""))
					return BigDecimal.ZERO;
				else {
					return new BigDecimal(s);
				}
			}
		});

		return ctx;
	}

}
