package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JScrollPaneNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.Log;
import com.swing_boot.reactive.SearchEvents;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import java.awt.Color;

import static com.swing_boot.reactive.tree.components.colored.DebugFrame.FOUND_BACKGROUND;

public class RandomColorScrollPane extends JScrollPaneNode<JScrollPane> {

    private static int counter;
    private final TitledBorder border = new TitledBorder(super.name);
    private Color color;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.class, ChangeColorEvents.ClearColor.class));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.class));
    }

    protected RandomColorScrollPane() {
        super(new JScrollPane(), "scroll_pane_" + ++counter);
        super.it.setBorder(border);
    }

    @Override
    public synchronized void update(Events e, Object v) {
        if (v instanceof Integer && e instanceof ChangeColorEvents) {
            final Integer i = (Integer) v;
            final ChangeColorEvents changeColorEvent = (ChangeColorEvents) e;
            super.it.setBackground(changeColorEvent.color);
            if (((ChangeColorEvents) e).color == Color.WHITE) {
                super.it.setForeground(Color.BLACK);
            } else {
                super.it.setForeground(Color.WHITE);
            }
            this.color = changeColorEvent.color;

            this.border.setTitleColor(Color.WHITE);
            this.border.setTitle(super.name + " <- " + i);
            Log.logger.log(this, "update", e, v);
        }

        if (e == SearchEvents.SEARCH_NODE_BY_NAME && v instanceof String) {
            final String searchingText = String.valueOf(v);
            if (!searchingText.isEmpty()) {
                final String text = this.border.getTitle();
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
