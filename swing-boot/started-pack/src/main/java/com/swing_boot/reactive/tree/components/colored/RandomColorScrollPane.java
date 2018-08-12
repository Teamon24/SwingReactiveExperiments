package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JScrollPaneNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.SearchEvents;

import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import java.awt.Color;

import static com.swing_boot.reactive.tree.components.colored.NodeSearchFrame.FOUND_BACKGROUND;

public class RandomColorScrollPane extends JScrollPaneNode<JScrollPane> {

    private static int counter;
    private final TitledBorder border = new TitledBorder(super.name);
    private Color color;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.TYPE));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.values()));
    }

    protected RandomColorScrollPane() {
        super(new JScrollPane(), "scroll_pane_" + ++counter);
        super.it.setBorder(border);
    }

    @Override
    public void update(Events event, Object value) {
        if (value instanceof Integer && event instanceof ChangeColorEvents) {
            final Integer i = (Integer) value;
            final ChangeColorEvents changeColorEvent = (ChangeColorEvents) event;
            super.it.setBackground(changeColorEvent.color);
            super.it.setForeground(Color.WHITE);
            this.color = changeColorEvent.color;

            this.border.setTitleColor(Color.WHITE);
            this.border.setTitle(super.name + " <- " + i);
            LogUtils.LOGGER.soutln(this, "update", event, value);
        }

        if (event == SearchEvents.SEARCH_NODE_BY_NAME && value instanceof String) {
            final String searchingText = String.valueOf(value);
            if (!searchingText.isEmpty()) {
                final String text = this.border.getTitle();
                final boolean contains = text.contains(searchingText);
                final boolean startsWith = text.startsWith(searchingText);
                if (contains && startsWith) {
                    super.it.setBackground(FOUND_BACKGROUND);
                    LogUtils.LOGGER.soutln(this, "update", event, value);
                } else {
                    super.it.setBackground(this.color);
                }
            } else {
                super.it.setBackground(this.color);
            }

        }
    }
}
