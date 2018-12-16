package com.xwarner.eml.library;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;

public interface Invokable {

	public Object run(ArrayList<Object> args, Bundle bundle);

}
