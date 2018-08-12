package com.swing_boot.reactive.tree.nodes;

import javax.swing.JTable;

public abstract class JTableNode<J extends JTable> extends JComponentNode<J> {

    public JTableNode(J jComponent, String nodeName) {
        super(jComponent, nodeName);
    }

}
