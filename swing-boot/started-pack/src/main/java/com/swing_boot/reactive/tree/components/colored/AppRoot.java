package com.swing_boot.reactive.tree.components.colored;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.CommonEvents;
import com.swing_boot.reactive.SearchEvents;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.utils.LogUtils;
import lombok.NonNull;

import javax.swing.JFrame;
import java.awt.Color;

public class AppRoot extends ReactiveTreeNode<JFrame> {

    {
        super.consumableEvents.addAll(Sets.newHashSet(ChangeColorEvents.class));
        super.consumableEvents.addAll(Sets.newHashSet(SearchEvents.class));
    }

    public AppRoot(@NonNull final JFrame component, @NonNull final String nodeName) {
        super(component, nodeName);
    }

    @Override
    public void update(Events e, Object v) {
        if (e instanceof ChangeColorEvents) {
            super.it.setTitle(this.name + "<" + String.valueOf(v) + ">");
            super.it.setForeground(Color.WHITE);
            super.it.setBackground(((ChangeColorEvents) e).color);
            LogUtils.LOGGER.soutln(this, "update", e, v);
            super.dispatchUp(this, CommonEvents.PRINT_APP_ROOT_INFO, v);
        }
    }

}
