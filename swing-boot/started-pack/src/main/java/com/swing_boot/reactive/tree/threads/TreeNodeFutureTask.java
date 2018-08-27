package com.swing_boot.reactive.tree.threads;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import lombok.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public class TreeNodeFutureTask<Result> extends FutureTask<Result> {

    public static AtomicInteger counter = new AtomicInteger(0);

    public ReactiveTreeNode<?> dispatchingNode;
    public Events event;
    public Object message;
    public Integer number;

    public TreeNodeFutureTask(@NonNull final Callable<Result> callable,
                              @NonNull final ReactiveTreeNode<?> dispatchingNode,
                              @NonNull final Events event,
                              @NonNull final Object message)
    {
        super(callable);
        this.dispatchingNode = dispatchingNode;
        this.event = event;
        this.message = message;
        this.number = counter.getAndIncrement();
    }

    @Override
    public String toString() {

        final boolean isTaskNotDone = !super.isDone();
        return isTaskNotDone ?

                "TreeNodeFutureTask {" +
                "dispatchingNode = " + dispatchingNode +
                ", event = " + event +
                ", message = " + message +
                ", is done = " + super.isDone() +
                '}'

                :

                "TreeNodeFutureTask {is done = " + super.isDone() + "}";
    }
}
