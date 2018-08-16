package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JPanelNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.SearchEvents;
import lombok.NonNull;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;

import static com.swing_boot.reactive.tree.components.colored.NodeSearchFrame.FOUND_BACKGROUND;

public class RandomColorPanel extends JPanelNode {

    private static AtomicInteger counter = new AtomicInteger(0);
    private final TitledBorder border = new TitledBorder(super.name);
    private final JLabel label = new JLabel();
    private Color color;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.TYPE));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.values()));
    }

    public RandomColorPanel() {
        this("p#" + counter.incrementAndGet());
    }

    public RandomColorPanel(@NonNull final String name) {
        super(new JPanel(), name);
        label.setText("                     ");
        super.it.add(label);
        super.it.setBorder(border);
    }

    @Override
    public void update(@NonNull final Events e, Object v) {

        if (v instanceof Integer && e instanceof ChangeColorEvents) {
            final Integer i = (Integer) v;
            final ChangeColorEvents changeColorEvent = (ChangeColorEvents) e;
            this.color = changeColorEvent.color;
            super.it.setBackground(this.color);
            final Color white = Color.WHITE;
            if (((ChangeColorEvents) e).color == white) {
                final Color black = Color.BLACK;
                super.it.setForeground(black);
                this.border.setTitleColor(black);
                this.label.setForeground(black);
            } else {
                super.it.setForeground(white);
                this.border.setTitleColor(white);
                this.label.setForeground(white);
            }

            this.border.setTitle(super.name + "~" + i);
            LogUtils.LOGGER.soutln(this, "update", e, v);
            final int i1 = name.length() + String.valueOf(i).length();
            final StringBuilder labelSpaces = new StringBuilder();
            for (int j = 0; j < i1; j++) {
                labelSpaces.append("   ");
            }
            label.setText(labelSpaces.toString());
        }

        if (e == SearchEvents.SEARCH_NODE_BY_NAME && v instanceof String) {
            final String searchingText = String.valueOf(v);
            if (!searchingText.isEmpty()) {
                final String text = this.border.getTitle();
                final boolean contains = text.contains(searchingText);
                final boolean startsWith = text.startsWith(searchingText);
                if (contains && startsWith) {
                    super.it.setBackground(FOUND_BACKGROUND);
                    LogUtils.LOGGER.soutln(this, "update", e, v);
                } else {
                    super.it.setBackground(this.color);
                }
            } else {
                super.it.setBackground(this.color);
            }
        }
    }
}
