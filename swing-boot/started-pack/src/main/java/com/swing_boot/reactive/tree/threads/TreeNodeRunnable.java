package com.swing_boot.reactive.tree.threads;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import lombok.NonNull;
import lombok.ToString;

@ToString
public abstract class TreeNodeRunnable implements Runnable {

    public ReactiveTreeNode<?> dispatchingNode;
    public ReactiveTreeNode<?> dispatchingChild;
    public Events event;
    public Object message;

    public TreeNodeRunnable(@NonNull final ReactiveTreeNode<?> dispatchingNode,
                            @NonNull final ReactiveTreeNode<?> dispatchingChild,
                            @NonNull final Events event,
                            @NonNull final Object message)
    {
        this.dispatchingNode = dispatchingNode;
        this.dispatchingChild = dispatchingChild;
        this.event = event;
        this.message = message;
    }
}
