package com.swing_boot.reactive.tree.nodes;

import javax.swing.JCheckBoxMenuItem;

public abstract class JCheckBoxMenuItemNode<J extends JCheckBoxMenuItem> extends JComponentNode<J> {

    public JCheckBoxMenuItemNode(J jComponent, String nodeName) {
        super(jComponent, nodeName);
    }
}
