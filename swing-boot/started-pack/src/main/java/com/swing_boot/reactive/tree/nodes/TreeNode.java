package com.swing_boot.reactive.tree.nodes;

import com.swing_boot.reactive.Events;

import java.util.Collection;

public interface TreeNode<It extends java.awt.Component> {

    It getIt();

    void setParent(TreeNode<It> parent);
    TreeNode<It> getParent();

    void addChild(TreeNode<It> child);
    Collection<? extends TreeNode<It>> getChildren();
    Collection<? extends Events> getConsumableEvents();
    Collection<? extends Events> getEmittedEvents();
    Collection<? extends Events> getChildrenConsumableEvents();
    Collection<? extends Events> getChildrenEmmitedEvents();

}
