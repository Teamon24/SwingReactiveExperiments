package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JButtonNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.Log;
import com.swing_boot.reactive.SearchEvents;
import lombok.NonNull;

import javax.swing.JButton;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

import static com.swing_boot.reactive.tree.components.colored.DebugFrame.FOUND_BACKGROUND;

public class RandomColorButton extends JButtonNode {

    private static AtomicInteger counter = new AtomicInteger(0);
    private int number;
    private Color color;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.class, ChangeColorEvents.ClearColor.class));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.class));
    }

    public RandomColorButton() {
        this("randomColorButton_" + RandomColorButton.counter.incrementAndGet());
    }

    private RandomColorButton(@NonNull final String nodeName) {
        super(new JButton(), nodeName);
        this.number = counter.get();
        super.it.setText("#" + this.number);
        super.addActionDispatcher(actionEvent -> super.dispatchUp(this, new ChangeColorEvents(new Color(34, 34, 34)), 1));
    }

    public synchronized void update(Events e, Object v) {
        if (v instanceof Integer && e instanceof ChangeColorEvents) {
            final Integer i = (Integer) v;
            super.it.setText("b#" + this.number + "~" + String.valueOf(i));
            if (((ChangeColorEvents) e).color == Color.WHITE) {
                super.it.setForeground(Color.BLACK);
            } else {
                super.it.setForeground(Color.WHITE);
            }
            this.color = ((ChangeColorEvents) e).color;
            super.it.setBackground(this.color);
            Log.logger.log(this, "update", e, v);
        }

        if (e == SearchEvents.SEARCH_NODE_BY_NAME && v instanceof String) {
            final String searchingText = String.valueOf(v);
            if (!searchingText.isEmpty()) {
                final String text = super.it.getText();
                final boolean contains = text.contains(searchingText);
                final boolean startsWith = text.startsWith(searchingText);
                if (contains && startsWith) {
                    super.it.setBackground(FOUND_BACKGROUND);
                    Log.logger.log(this, "update", e, v);
                } else {
                    super.it.setBackground(this.color);
                }
            } else {
                super.it.setBackground(this.color);
            }
        }
    }
}
