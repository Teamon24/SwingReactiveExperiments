package com.swing_boot.reactive.tree.nodes;

import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.threads.ReactiveTreeThreads;
import com.swing_boot.reactive.tree.threads.TreeNodeRunnable;
import com.swing_boot.reactive.tree.utils.Log;
import com.swing_boot.reactive.tree.utils.TreeUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class ReactiveTreeNode<It extends Component>
        extends Observer {

    public String name;

    public final It it;

    @Getter
    private ReactiveTreeNode<?> parent;

    public void setParent(ReactiveTreeNode<? extends Component> parent) {
        this.parent = parent;
    }

    @Getter
    @Setter
    protected Set<Class<? extends Events>> consumableEvents = new LinkedHashSet<>();

    @Getter
    protected Set<Events> emittedEvents = new LinkedHashSet<>();

    @Getter
    private List<ReactiveTreeNode<?>> children = new ArrayList<>();

    public ReactiveTreeNode(It it, @NonNull final String nodeName) {
        this.it = it;
        this.name = nodeName;
    }

    public ReactiveTreeNode(It it) {
        this.it = it;
        this.name = "default_node_name";
    }

    public void dispatchUp(@NonNull final ReactiveTreeNode<?> dispatchingChild,
                           @NonNull final Events event,
                           @NonNull final Object message) {



        if (this.hasParent()) {
            if (this.parent.hasOnlyOneChild()) {
                this.parent.dispatchUp(this, event, message);
            } else {
//                ReactiveTreeThreads.execute(dispatchingUp(this, dispatchingChild, event, message));
                ReactiveTreeThreads.dispatchUp(this, dispatchingChild, event, message);
            }
        }

        if (this.hasNoParent() && this.hasChildren()) {
            this.dispatchDown(dispatchingChild, event, message);
        }
    }

    public void dispatchDown(@NonNull final ReactiveTreeNode<?> dispatchingChild,
                             @NonNull final Events event,
                             @NonNull final Object message) {

        Log.logger.log(this, "consumableEvents", this.consumableEvents);
        if (TreeUtils.isEventConsumable(this.consumableEvents, event)) {
            this.syncUpdate(event, message);
        }

        final Collection<Class<? extends Events>> childrenConsumableEvents = TreeUtils.getAllConsumableEventsBelow(this);
        Log.logger.log(this, "childrenConsumableEvents", childrenConsumableEvents);
        ReactiveTreeNode<?> nextChildForDispatching;

        if (!TreeUtils.isEventConsumable(childrenConsumableEvents, event)) {
            return;
        }

        if (this.hasOnlyOneChild()) {
            nextChildForDispatching = this.getChildren().get(0);
            Log.logger.log(nextChildForDispatching, "dispatchDown", event, message);
            nextChildForDispatching.dispatchDown(nextChildForDispatching, event, message);
        }

        if (this.hasMoreOneChild()) {
            nextChildForDispatching = this.getChildForThread(dispatchingChild);
            for (ReactiveTreeNode<?> currentChild : this.children) {
                if (dispatchingChild != currentChild) {
                    if (this.hasMoreOneChild() && nextChildForDispatching == currentChild) {
                        Log.logger.log(nextChildForDispatching, "dispatchDown", event, message);
                        nextChildForDispatching.dispatchDown(nextChildForDispatching, event, message);

                    } else {
//                        ReactiveTreeThreads.execute(dispatchingDown(currentChild, event, message));
                        ReactiveTreeThreads.dispatchDown(currentChild, event, message);
                    }
                }
            }
        }

    }

    private boolean hasNoParent() {
        return this.parent == null;
    }

    private boolean hasNoChildren() {
        return this.children.isEmpty();
    }

    private boolean hasOnlyOneChild() {
        return this.children.size() == 1;
    }

    private boolean hasMoreOneChild() {
        return this.children.size() > 1;
    }

    private ReactiveTreeNode<?> getChildForThread(@NonNull final ReactiveTreeNode<?> dispatchingChild) {
        int i = RandomUtils.nextInt(0, this.children.size());
        ReactiveTreeNode<?> childForCurrentThread = this.children.get(i);
        while (dispatchingChild == childForCurrentThread) {
            i = RandomUtils.nextInt(0, this.children.size());
            childForCurrentThread = this.children.get(i);
        }
        return childForCurrentThread;
    }

    public void addChild(@NonNull final ReactiveTreeNode<?> child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void deleteChild(@NonNull final ReactiveTreeNode<?> child) {
        final String className = this.getClass().getSimpleName();
        throw new UnsupportedOperationException(className + "#deleteChild() has no implementation");
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public String getGenealogyName() {
        final String grandParentGeneology;
        if (this.hasParent()) {
            grandParentGeneology = this.parent.getGenealogyName();
        } else {
            grandParentGeneology = "";
        }
        return StringUtils.join(grandParentGeneology, this.name, ".");
    }

    /**
     * @param dispatchingNode
     * @param event
     * @param message
     * @return
     */
    public static TreeNodeRunnable dispatchingUp(@NonNull final ReactiveTreeNode<?> dispatchingNode,
                                                 @NonNull final ReactiveTreeNode<?> dispatchingChild,
                                                 @NonNull final Events event,
                                                 @NonNull final Object message)
    {
        return
                new TreeNodeRunnable(dispatchingNode, dispatchingChild, event, message) {
                    @Override
                    public void run() {
                        this.dispatchingNode.dispatchUp(this.dispatchingChild, this.event, this.message);
                    }
                };
    }

    /**
     * @param dispatchingNode
     * @param event
     * @param message
     * @return
     */
    public static TreeNodeRunnable dispatchingDown(@NonNull final ReactiveTreeNode<?> dispatchingNode,
                                                   @NonNull final Events event,
                                                   @NonNull final Object message)
    {
        return new TreeNodeRunnable(dispatchingNode, dispatchingNode, event, message) {
            @Override
            public void run() {
                this.dispatchingNode.dispatchDown(this.dispatchingNode, this.event, this.message);
            }
        };
    }

    @Override
    public String toString() {
        return this.name;
    }

}
