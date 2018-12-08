package com.xwarner.eml.api.libraries.ui;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;

public class AlertLibrary implements Library {

	public SubContext export() {
		SubContext ctx = new SubContext(null);

		ctx.setFunction("alert", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {

				String text = "";
				String title = "Alert";

				if (args.size() > 0)
					text = (String) args.get(0);
				if (args.size() > 1)
					title = (String) args.get(1);

				JOptionPane.showMessageDialog(null, text, title, JOptionPane.PLAIN_MESSAGE);

				return null;
			}
		});
		
		ctx.setFunction("info", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {

				String text = "";
				String title = "Information";

				if (args.size() > 0)
					text = (String) args.get(0);
				if (args.size() > 1)
					title = (String) args.get(1);

				JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);

				return null;
			}
		});

		
		ctx.setFunction("warn", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {

				String text = "";
				String title = "Warning";

				if (args.size() > 0)
					text = (String) args.get(0);
				if (args.size() > 1)
					title = (String) args.get(1);

				JOptionPane.showMessageDialog(null, text, title, JOptionPane.WARNING_MESSAGE);

				return null;
			}
		});

		
		ctx.setFunction("error", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {

				String text = "";
				String title = "Error";

				if (args.size() > 0)
					text = (String) args.get(0);
				if (args.size() > 1)
					title = (String) args.get(1);

				JOptionPane.showMessageDialog(null, text, title, JOptionPane.ERROR_MESSAGE);

				return null;
			}
		});


		return ctx;
	}

}
