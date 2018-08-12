package com.swing_boot.reactive.tree.nodes;

import javax.swing.JLabel;

public abstract class JLabelNode<J extends JLabel> extends JComponentNode<J> {

    public JLabelNode(J label, String nodeName) {
        super(label, nodeName);
    }
}
