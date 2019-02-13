package com.xwarner.eml.library.invokables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.NullVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.library.Invokable;
import com.xwarner.eml.library.gui.GraphWindow;

import javafx.application.Application;

public class GUI {

	public void load(HashMap<String, Invokable> map) {
		map.put("gui.alert", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				String type = args.get(1).toString();
				int type2 = 0;
				if (type.equals("alert"))
					type2 = JOptionPane.PLAIN_MESSAGE;
				else if (type.equals("info"))
					type2 = JOptionPane.INFORMATION_MESSAGE;
				else if (type.equals("warn"))
					type2 = JOptionPane.WARNING_MESSAGE;
				else if (type.equals("error"))
					type2 = JOptionPane.ERROR_MESSAGE;

				String text = "";
				String title = "Alert";

				if (!(args.get(2) instanceof NullVariable))
					text = (String) args.get(2);
				if (!(args.get(3) instanceof NullVariable))
					title = (String) args.get(3);

				JOptionPane.showMessageDialog(null, text, title, type2);

				return null;
			}
		});

		map.put("gui.get", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				String type = args.get(1).toString();

				String text = "Input a number";
				String title = "Number Input";
				if (type.equals("string")) {
					text = "Text Input";
					title = "Text Input";
				}

				if (!(args.get(2) instanceof NullVariable))
					text = (String) args.get(2);
				if (!(args.get(3) instanceof NullVariable))
					title = (String) args.get(3);

				String s = (String) JOptionPane.showInputDialog(null, text, title, JOptionPane.PLAIN_MESSAGE, null,
						null, null);
				
				if (type.equals("number")) {
					if (s == null || s.equals(""))
						return BigDecimal.ZERO;
					else
						return new BigDecimal(s);
				} else if (type.equals("string")) {
					return s;
				} else {
					return null;
				}
			}
		});
		
		map.put("gui.graph", new Invokable() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				NumericVariable var = (NumericVariable) args.get(1);

				GraphWindow.defs = new ArrayList<NumericVariable>();

				int i = 2;
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
	}
}
