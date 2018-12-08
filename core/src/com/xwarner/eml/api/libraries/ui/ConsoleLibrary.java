package com.xwarner.eml.api.libraries.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import com.xwarner.eml.api.Library;
import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.SubContext;
import com.xwarner.eml.interpreter.context.functions.Function;

public class ConsoleLibrary implements Library {

	public SubContext export() {
		SubContext ctx = new SubContext(null);

		ctx.setFunction("getNumber", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				Scanner scanner = new Scanner(System.in);
				String s = scanner.nextLine();
				scanner.close();
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new BigDecimal(s);
			}
		});

		ctx.setFunction("getString", new Function() {
			public Object run(ArrayList<Object> args, Bundle bundle) {
				Scanner scanner = new Scanner(System.in);
				String s = "";
				while (scanner.hasNextLine())
					s = scanner.nextLine();
				scanner.close();
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return s;
			}
		});

		return ctx;
	}

}
