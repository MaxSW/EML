package com.xwarner.eml.library;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;

public interface Invokable {

	public Object run(ArrayList<Object> args, Bundle bundle);

}
