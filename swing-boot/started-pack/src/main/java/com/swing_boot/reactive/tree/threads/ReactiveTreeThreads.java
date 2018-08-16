package com.swing_boot.reactive.tree.threads;

import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
import com.swing_boot.reactive.tree.nodes.ReactiveTreeNode;
import com.swing_boot.reactive.tree.utils.LogUtils;
import lombok.NonNull;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactiveTreeThreads {

    public static ConcurrentLinkedDeque<FutureTask<Long>> tasks = new ConcurrentLinkedDeque<>();

    public volatile static Long start = 0L;
    public volatile static Long stop = 0L;

    public static ExecutorService executor = Executors.newFixedThreadPool(1000);

    public static Boolean timerIsOn = false;

    public static Collection<Integer> usedDigit = new ArrayList<>();

    public static long waitForLastThread = (long) (0.5 * 1000);

    /**
     *
     * @param node
     * @param dispatchingNode
     * @param event
     * @param message
     */
    public static void dispatchUp(
            @NonNull final ReactiveTreeNode<?> node,
            @NonNull final ReactiveTreeNode<?> dispatchingNode,
            @NonNull final Events event,
            @NonNull final Object message)
    {
        logoutAboutEmitting(node, event, message);
        final Callable<Long> callable = dispatchUpFunc(node, dispatchingNode, event, message);
        execute(callable);
    }

    /**
     *
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
        final Callable<Long> callable = dispatchDownFunc(node, event, message);
        execute(callable);
    }

    /**
     * @param node
     * @param event
     * @param message
     * @return
     */
    private static Callable<Long> dispatchDownFunc(@NonNull final ReactiveTreeNode<?> node,
                                                   @NonNull final Events event,
                                                   @NonNull final Object message) {
        return () -> {
            if (event instanceof ChangeColorEvents) {
                final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage((ChangeColorEvents) event);
                node.dispatchDown(node, eventAndMessage.getLeft(), eventAndMessage.getRight());
                usedDigit.remove(eventAndMessage.getRight());
            } else {
                node.dispatchDown(node, event, message);
            }
            final long result = System.currentTimeMillis();
            return result;
        };
    }

    /**
     * @param node
     * @param event
     * @param message
     * @return
     */
    private static Callable<Long> dispatchUpFunc(@NonNull final ReactiveTreeNode<?> node,
                                                 @NonNull final ReactiveTreeNode<?> dispatchingChild,
                                                 @NonNull final Events event,
                                                 @NonNull final Object message) {
        return () -> {
            final ReactiveTreeNode<?> parent = node.getParent();
            if (event instanceof ChangeColorEvents) {
                final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage((ChangeColorEvents) event);
                final ChangeColorEvents debugEvent = eventAndMessage.getLeft();
                final Integer debugMessage = eventAndMessage.getRight();
                parent.dispatch(dispatchingChild, debugEvent, debugMessage);
                usedDigit.remove(debugMessage);
            } else {
                parent.dispatch(node, event, message);
            }
            final long result = System.currentTimeMillis();
            return result;
        };
    }

    private static Pair<ChangeColorEvents, Integer> getDebugEventAndMessage(ChangeColorEvents event) {
        if (event.color == Color.WHITE) {
            return Pair.of(event, 0);
        }
        int b = RandomUtils.nextInt(15, 255);
        while (usedDigit.contains(b)) {
            b = RandomUtils.nextInt(15, 255);
        }
        usedDigit.add(b);
        Thread.currentThread().setName(Thread.currentThread().getName() + " <" + b + ">");
        return Pair.of(new ChangeColorEvents(getColor(b)), b);
    }

    private static AtomicInteger colorI = new AtomicInteger();

    private static Color getColor(int i) {
        Color bg;
        if (i > 126) {
            bg = new Color(i, i/8, 0);
        } else {
            bg = new Color(0, i/8, i);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bg;
    }

    private static void logoutAboutEmitting(@NonNull final ReactiveTreeNode<?> node,
                                            @NonNull final Events event,
                                            @NonNull final Object message) {
        if (start == null) {
            start = System.currentTimeMillis();
            LogUtils.LOGGER.line();
            LogUtils.LOGGER.soutln(node, "eventEmitting", event, message);
            LogUtils.LOGGER.line();
        }
    }

    private static void execute(Callable<Long> callable) {
        FutureTask<Long> futureTask = new FutureTask<>(callable);
        tasks.add(futureTask);
        if (!timerIsOn) {
            countTime();
        }
        executor.execute(futureTask);
        timerIsOn = true;
    }

    static AtomicInteger counter = new AtomicInteger();
    private static void countTime() {
        if (timerIsOn) return;

        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (areTasksDone()) {
                        sleep(waitForLastThread);
                    }
                    if (areTasksDone()) {
                        stop = getLastTaskResult();
                        LogUtils.LOGGER.line();
                        LogUtils.LOGGER.date(LogUtils.dateColor).thread().counter().text("время начала     диспатчинга: ").time(start).ln();
                        LogUtils.LOGGER.line();
                        LogUtils.LOGGER.date(LogUtils.dateColor).thread().counter().text("время окончания  диспатчинга: ").time(stop).ln();
                        LogUtils.LOGGER.line();
                        LogUtils.LOGGER.date(LogUtils.dateColor).thread().counter().text("время выполнения диспатчинга: ").time(stop - start).ln();
                        LogUtils.LOGGER.line().ln().ln().flush();
                        timerIsOn = false;
                        start = null;
                        return;
                    }
                }
            }
        });
        timer.setName("Timer-Thread#" + counter.incrementAndGet());
        timer.start();

    }

    private static boolean areTasksDone() {
        boolean isDone = true;
        for (FutureTask<Long> task : tasks) {
            if (!task.isDone()) {
                isDone = false;
                break;
            }
        }
        return isDone;
    }

    private static Long getLastTaskResult() {
        try {

            final LinkedList<FutureTask<Long>> list = new LinkedList<>(tasks);
            Collections.sort(list, new Comparator<FutureTask<Long>>() {
                @Override
                public int compare(FutureTask<Long> o1, FutureTask<Long> o2) {
                    try {
                        return o1.get().compareTo(o2.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            return list.getLast().get();
        } catch (NullPointerException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private static void sleep(long millis) {
        try {
            LogUtils.LOGGER.date().thread().counter().sout("sleep", "\"waiting for last thread\"").ln().flush();
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
