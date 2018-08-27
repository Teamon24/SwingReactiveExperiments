package com.swing_boot.reactive.tree.threads;

import com.google.common.collect.Sets;
import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.components.colored.DebugFrame;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import com.swing_boot.reactive.tree.utils.Log;
import lombok.NonNull;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.swing_boot.reactive.ChangeColorEvents.ClearColor.CLEAR_COLOR;
import static com.swing_boot.reactive.tree.utils.Log.TIME_FORMAT;

public final class ReactiveTreeThreads {

    private ReactiveTreeThreads() {}

    @Override
    public String toString() { return "ReactiveTreeThreads{}"; }

    private static ReactiveTreeThreads INSTANCE = new ReactiveTreeThreads();
    public final static ConcurrentLinkedDeque<Future<Pair<Object, Long>>> TASKS = new ConcurrentLinkedDeque<>();

    private static AtomicLong start = new AtomicLong(0L);
    private static AtomicLong stop = new AtomicLong(0L);
    private static final int threadSleep = 0;
    private static ExecutorService executor = Executors.newFixedThreadPool(1000);

    private static Collection<Integer> usedDigit = new ArrayList<>();
    private static long waitForLastThread = (long) (0.5 * 200);
    private static boolean debugIsOn = true;


    public static void execute(@NonNull final TreeNodeRunnable runnable) {
        execute(debugIsOn ? getDebugger(runnable) : getTimer(runnable));
    }

    private static Callable<Pair<Object, Long>> getTimer(@NonNull final TreeNodeRunnable runnable) {
        return () -> {
                start.set(System.currentTimeMillis());
                runnable.run();
                return Pair.of(runnable.message, System.currentTimeMillis());
            };
    }

    private static Callable<Pair<Object, Long>> getDebugger(@NonNull final TreeNodeRunnable runnable) {

        runnable.dispatchingNode.setConsumableEvents(
                Sets.newHashSet(
                    ChangeColorEvents.class,
                    ChangeColorEvents.ClearColor.class));

        final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage();
        runnable.event = eventAndMessage.getLeft();
        runnable.message = eventAndMessage.getRight();

        if (runnable.event.getClass().equals(ChangeColorEvents.ClearColor.class)) {
            runnable.event = ChangeColorEvents.ClearColor.CLEAR_COLOR;
            runnable.message = 0;
        }
        return getTimer(runnable);
    }

    /**
     * @param node
     * @param dispatchedNode
     * @param event
     * @param message
     */
    public static void dispatchUp(
            @NonNull final ReactiveTreeNode<?> node,
            @NonNull final ReactiveTreeNode<?> dispatchedNode,
            @NonNull final Events event,
            @NonNull final Object message)
    {
        logoutAboutEmitting(node, event, message);
        execute(dispatchingUp(node, dispatchedNode, event, message));
    }

    /**
     * @param node
     * @param event
     * @param message
     */
    public static void dispatchDown(
            @NonNull final ReactiveTreeNode<?> node,
            @NonNull final Events event,
            @NonNull final Object message)
    {
        logoutAboutEmitting(node, event, message);
        execute(dispatchingDown(node, event, message));
    }

    private static void execute(Callable<Pair<Object, Long>> futureTask) {

        Log.logger.log(INSTANCE, "execute pre_If ", "start = %s", start.get());
        if (start.get() == 0L) {
            Log.logger.log(INSTANCE, "execute post_If ", "start = %s", start.get());
            countTime();
        }
        Log.logger.log(executor.getClass(), "execute", "task =  %s", futureTask);
        Log.logger.log(executor.getClass(), "isShutdown", executor.isShutdown());
        Log.logger.log(executor.getClass(), "isTerminated", executor.isTerminated());
        final Future<Pair<Object, Long>> result = executor.submit(futureTask);
        TASKS.add(result);
    }

    /**
     * @param node
     * @param event
     * @param message
     * @return
     */
    private static Callable<Pair<Object, Long>> dispatchingDown(@NonNull final ReactiveTreeNode<?> node,
                                                                @NonNull final Events event,
                                                                @NonNull final Object message) {
        return () -> {
            if (event instanceof ChangeColorEvents) {
                final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage();
                ChangeColorEvents debugEvent;
                Integer debugMessage;
                if (event.getClass().equals(ChangeColorEvents.ClearColor.class)) {
                    debugEvent = CLEAR_COLOR;
                    debugMessage = 0;
                } else {
                    debugEvent = eventAndMessage.getLeft();
                    debugMessage = eventAndMessage.getRight();
                }
                node.dispatchDown(node, debugEvent, debugMessage);
                usedDigit.remove(debugMessage);
            } else {
                node.dispatchDown(node, event, message);
            }
            final long result = System.currentTimeMillis();
            return Pair.of(message, result);
        };
    }

    /**
     * @param node
     * @param event
     * @param message
     * @return
     */
    private static Callable<Pair<Object, Long>> dispatchingUp(@NonNull final ReactiveTreeNode<?> node,
                                                              @NonNull final ReactiveTreeNode<?> dispatchingChild,
                                                              @NonNull final Events event,
                                                              @NonNull final Object message)
    {
        return () -> {
            final ReactiveTreeNode<?> parent = node.getParent();
            Events fevent = event;
            Object fmessage = message;

            if (event instanceof ChangeColorEvents) {
                final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage();
                ChangeColorEvents debugEvent = eventAndMessage.getLeft();
                Integer debugMessage = eventAndMessage.getRight();
                if (event.getClass().equals(ChangeColorEvents.ClearColor.class)) {
                    debugEvent = CLEAR_COLOR;
                    debugMessage = 0;
                }
                fevent = debugEvent;
                fmessage = debugMessage;
                usedDigit.remove(debugMessage);
            }

            parent.dispatchUp(dispatchingChild, fevent, fmessage);
            final long result = System.currentTimeMillis();
            return Pair.of(message, result);
        };
    }

    private static Pair<ChangeColorEvents, Integer> getDebugEventAndMessage() {

        int b = RandomUtils.nextInt(15, 255);
        while (usedDigit.contains(b)) {
            b = RandomUtils.nextInt(15, 255);
        }
        usedDigit.add(b);
        Thread.currentThread().setName(Thread.currentThread().getName() + " <" + b + ">");
        return Pair.of(new ChangeColorEvents(getColor(b)), b);
    }

    private static Color getColor(int i) {
        Color bg;
        if (255 > i && i >= 170) {
            bg = new Color(i, i / 2, i / 4);
        } else
            if (170 > i && i >= 85) {
                bg = new Color(i / 4, i, i / 2);
            } else {
                bg = new Color(i / 2, i / 4, i);
            }

        try {
            Thread.sleep(threadSleep); } catch (InterruptedException e) { e.printStackTrace(); }

        return bg;
    }

    private static void logoutAboutEmitting(@NonNull final ReactiveTreeNode<?> node,
                                            @NonNull final Events event,
                                            @NonNull final Object message) {
        if (start.get() == 0L) {
            Log.logger.line();
            Log.logger.log(node, "eventEmitting", event, message);
            Log.logger.line();
        }
    }

    private static AtomicInteger counter = new AtomicInteger();

    private static void countTime() {
        if (start.get() == 0L) {
            Thread timer = new Thread(() -> {
                while (true) {

                    if (tasksAreDone()) {
                        sleep(waitForLastThread);
                    }

                    if (tasksAreDone()) {
                        Log.logger.log(INSTANCE, "tasksAreDone", true);
                        try {
                            stop.set(TASKS.getLast().get().getRight());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }

                        Log.logger.line();
                        Log.logger.time("время начала     диспатчинга: %s", start.get());
                        Log.logger.time("время окончания  диспатчинга: %s", stop.get());
                        Log.logger.time("время выполнения диспатчинга: %s", stop.get() - start.get());
                        Log.logger.line();

                        StringBuilder a = new StringBuilder();
                        int i = 0;

                        for (final Future<Pair<Object, Long>> result : TASKS) {
                            try {
                                a.append(result.toString())
                                        .append(" №").append(++i)
                                        .append(" is done ? ").append(result.isDone())
                                        .append("time: ").append(TIME_FORMAT.format(new Date(result.get().getValue() - start.get()))).append("\n");
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.logger.log(DebugFrame.instance(), "task info\n", a.toString());
                        TASKS.clear();
                        start.set(0L);
                        return;
                    }
                }
            });

            timer.setName("Timer-Thread#" + counter.incrementAndGet());
            start.set(System.currentTimeMillis());
            timer.start();
        }
    }

    private static boolean tasksAreDone() {
        for (Future task : TASKS) {
            final boolean taskIsNotDoneYet = !task.isDone();
            if (taskIsNotDoneYet) {
                return false;
            }
        }
        return true;
    }

    private static void sleep(long millis) {
        try {
            Log.logger.log(ReactiveTreeThreads.INSTANCE, "sleep", "\"waiting for last thread\"");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
