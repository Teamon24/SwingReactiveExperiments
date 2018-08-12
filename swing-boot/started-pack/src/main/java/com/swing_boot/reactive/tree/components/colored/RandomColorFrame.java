package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.tree.nodes.JFrameNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import com.swing_boot.reactive.SearchEvents;
import lombok.NonNull;

import javax.swing.JFrame;
import java.awt.Color;

public class RandomColorFrame extends JFrameNode<JFrame> {

    private static int counter;

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.TYPE));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.values()));
    }

    public RandomColorFrame() {
        super(new JFrame(), "randomColorFrame#" + ++counter);
        super.it.setTitle("randomColorFrame#" + counter);
    }

    public RandomColorFrame(@NonNull final JFrame jFrame,
                            @NonNull final String nodeName) {
        super(jFrame, nodeName);
    }


    @Override
    public void update(Events event, Object value) {
        if (event instanceof ChangeColorEvents) {
            super.it.setTitle(this.name + "<-" + String.valueOf(value));
            super.it.setForeground(Color.WHITE);
            super.it.setBackground(((ChangeColorEvents) event).color);
            LogUtils.LOGGER.soutln(this, "update", event, value);
        }


    }

}
