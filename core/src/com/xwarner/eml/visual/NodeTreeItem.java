package com.xwarner.eml.visual;

import com.xwarner.eml.nodes.Node;

import javafx.scene.control.TreeItem;

public class NodeTreeItem extends TreeItem<String> {

	public NodeTreeItem() {

	}

	public NodeTreeItem(Node node) {
		// TODO styling and icons
		setValue(node.toString());
		setExpanded(true);
	}

}
