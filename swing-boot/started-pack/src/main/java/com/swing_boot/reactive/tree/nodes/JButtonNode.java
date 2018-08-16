package com.swing_boot.reactive.tree.nodes;

import com.swing_boot.reactive.Events;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Set;

public abstract class JButtonNode extends JComponentNode<JButton> {

    public JButtonNode(@NonNull final JButton jButton, @NonNull final String nodeName) {
        super(jButton, nodeName);
    }

    public JButtonNode(@NonNull final JButton jButton) {
        super(jButton);
    }

    public void addActionDispatcher(@NonNull final Set<Pair<Events, Object>> dispatchables) {
        super.it.addActionListener(getDispatcher(dispatchables));
    }

    public void addActionDispatcher(@NonNull final ActionListener actionListener) {
        super.it.addActionListener(actionListener);
    }

    public ActionListener getDispatcher(@NonNull final Set<Pair<Events, Object>> dispatchibles) {
        return actionListener -> {
            for (Pair<Events, Object> dispatchible : dispatchibles) {
                JButtonNode.super.dispatchUp(JButtonNode.this, dispatchible.getLeft(), dispatchible.getRight());
            }
        };
    }
}
