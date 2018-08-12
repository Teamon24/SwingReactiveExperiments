package com.swing_boot.reactive.tree.nodes;

import lombok.NonNull;

import java.awt.Container;
import java.awt.LayoutManager;

public abstract class ContainerNode<C extends Container> extends ReactiveTreeNode<C> {

    public ContainerNode(@NonNull final C container, @NonNull final String nodeName) {
        super(container, nodeName);
    }

    public ContainerNode(@NonNull final C container) {
        super(container);
    }

    public void setLayout(@NonNull final LayoutManager layout) {
        super.it.setLayout(layout);
    }

    public void add(@NonNull final ReactiveTreeNode<?> reactiveTreeNode, @NonNull final Object constraints) {
        super.addChild(reactiveTreeNode);
        super.it.add(reactiveTreeNode.it, constraints);
    }

    public void add(@NonNull final ReactiveTreeNode<?> reactiveTreeNode) {
        super.addChild(reactiveTreeNode);
        super.it.add(reactiveTreeNode.it);
    }
}
