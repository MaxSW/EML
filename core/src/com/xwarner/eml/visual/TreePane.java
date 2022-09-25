package com.xwarner.eml.visual;

import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.Tree;

import javafx.scene.control.TreeView;

public class TreePane extends TreeView<String> {

	public void update(Tree tree) {
//		getChildren().clear();
		NodeTreeItem root = new NodeTreeItem();
		for (Node n : tree.getChildren()) {
			addNode(root, n);
		}
		setRoot(root);
		setShowRoot(false);
		root.setExpanded(true);
	}

	public void addNode(NodeTreeItem root, Node node) {
		NodeTreeItem item = new NodeTreeItem(node);
		root.getChildren().add(item);
		for (Node n : node.getChildren()) {
			addNode(item, n);
		}
	}

}
