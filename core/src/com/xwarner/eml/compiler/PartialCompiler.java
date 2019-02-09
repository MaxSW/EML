package com.xwarner.eml.compiler;

import java.io.IOException;

import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.functions.FunctionArgumentNode;
import com.xwarner.eml.nodes.functions.FunctionNode;
import com.xwarner.eml.nodes.functions.InvocationNode;
import com.xwarner.eml.nodes.logic.BreakNode;
import com.xwarner.eml.nodes.logic.ContinueNode;
import com.xwarner.eml.nodes.logic.ForNode;
import com.xwarner.eml.nodes.logic.IfNode;
import com.xwarner.eml.nodes.logic.ReturnNode;
import com.xwarner.eml.nodes.logic.WhileNode;
import com.xwarner.eml.nodes.objects.ClassNode;
import com.xwarner.eml.nodes.objects.ObjectCreationNode;
import com.xwarner.eml.nodes.values.BooleanNode;
import com.xwarner.eml.nodes.values.MatrixNode;
import com.xwarner.eml.nodes.values.MatrixRowNode;
import com.xwarner.eml.nodes.values.NumberNode;
import com.xwarner.eml.nodes.values.OperatorNode;
import com.xwarner.eml.nodes.values.StringNode;
import com.xwarner.eml.nodes.values.VectorNode;
import com.xwarner.eml.nodes.variables.ArrayMemberNode;
import com.xwarner.eml.nodes.variables.AssignmentNode;
import com.xwarner.eml.nodes.variables.DeclarationNode;
import com.xwarner.eml.nodes.variables.VariableChangeNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;
import com.xwarner.eml.parser.Tree;
import com.xwarner.eml.util.IOManager;

public class PartialCompiler {

	public void save(String file, Tree tree) {
		StringBuilder sb = new StringBuilder();
		for (Node node : tree.getChildren()) {
			outputNode(node, 0, sb);
		}
		sb.setLength(sb.length() - 1);
		try {
			IOManager.writeFile(file, sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void outputNode(Node node, int offset, StringBuilder sb) {
		sb.append(offset);
		sb.append(" ");
		sb.append(node.toSaveString());
		sb.append("\n");
		for (Node n : node.getChildren()) {
			outputNode(n, offset + 1, sb);
		}

	}

	private int location = 0;

	public Tree load(String file) {
		location = 0;
		String str = "";
		try {
			str = IOManager.readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] split = str.split("\n");

		Tree tree = new Tree();

		readNode(split, 0, tree);

		return tree;
	}

	private void readNode(String[] split, int depth, Node parent) {
		String current = split[location];
		location++;
		String[] split2 = current.split(" ");
		Node n = i2n(Integer.parseInt(split2[1]));
		n.fromSaveString(split2, current);

		while (location < split.length) {
			int level = Integer.parseInt(split[location].split(" ")[0]);
			if (level == depth + 1) {
				readNode(split, depth + 1, n);
			} else if (level == depth) {
				parent.addChild(n);
				readNode(split, depth, parent);
				return;
			} else {
				parent.addChild(n);
				return;
			}
		}
		if (location == split.length) {
			parent.addChild(n);
		}
	}

	private Node i2n(int id) {
		switch (id) {
		case 1:
			return new ReferenceNode();
		case 2:
			return new ExpressionNode();
		case 3:
			return new BodyNode();
		case 4:
			return new FunctionArgumentNode();
		case 5:
			return new FunctionNode();
		case 6:
			return new InvocationNode();
		case 7:
			return new ForNode();
		case 8:
			return new IfNode();
		case 9:
			return new WhileNode();
		case 10:
			return new ClassNode();
		case 11:
			return new ObjectCreationNode();
		case 12:
			return new BooleanNode();
		case 13:
			return new NumberNode();
		case 14:
			return new OperatorNode();
		case 15:
			return new ReturnNode();
		case 16:
			return new StringNode();
		case 17:
			return new ArrayMemberNode();
		case 18:
			return new AssignmentNode();
		case 19:
			return new DeclarationNode();
		case 20:
			return new VariableChangeNode();
		case 21:
			return new VariableReferenceNode();
		case 22:
			return new ContinueNode();
		case 23:
			return new BreakNode();
		case 24:
			return new VectorNode();
		case 25:
			return new MatrixNode();
		case 26:
			return new MatrixRowNode();
		default:
			throw new Error("unkown node");
		}
	}

}
