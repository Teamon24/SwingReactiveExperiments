package com.swing_boot.reactive.tree.nodes;

import com.swing_boot.reactive.ChangeColorEvents;
import com.swing_boot.reactive.Events;
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

public class ReactiveTreeThreads {

    public static ConcurrentLinkedDeque<FutureTask<Long>> tasks = new ConcurrentLinkedDeque<>();

    public volatile static Long start = 0L;
    public volatile static Long stop = 0L;

    public static ExecutorService executor = Executors.newFixedThreadPool(10000);

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
            @NonNull final Object message) {
        logoutAboutEmitting(node, event, message);
        node.dispatchUp(dispatchingNode, event, message);
    }

    /**
     *
     * @param node
     * @param dispatchingNode
     * @param event
     * @param message
     */
    public static void dispatchDown(
            @NonNull final ReactiveTreeNode<?> node,
            @NonNull final ReactiveTreeNode<?> dispatchingNode,
            @NonNull final Events event,
            @NonNull final Object message)
    {
        logoutAboutEmitting(node, event, message);
        final Callable<Long> callable = dispatchDownFunc(node, dispatchingNode, event, message);
        execute(callable);
    }

    /**
     * @param node
     * @param dispatchingNode
     * @param event
     * @param message
     * @return
     */
    private static Callable<Long> dispatchDownFunc(@NonNull final ReactiveTreeNode<?> node,
                                                   @NonNull final ReactiveTreeNode<?> dispatchingNode,
                                                   @NonNull final Events event,
                                                   @NonNull final Object message) {
        return new Callable<Long>() {
            @Override public Long call() {
                if (event instanceof ChangeColorEvents) {
                    final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage();
                    node.dispatchDown(dispatchingNode, eventAndMessage.getLeft(), eventAndMessage.getRight());
                    usedDigit.remove(eventAndMessage.getRight());
                } else {
                    node.dispatchDown(dispatchingNode, event, message);
                }
                final long result = System.currentTimeMillis();
                return result;
            }};
    }

    /**
     * @param node
     * @param dispatchingNode
     * @param event
     * @param message
     * @return
     */
    private static Callable<Long> dispatchUpFunc(@NonNull final ReactiveTreeNode<?> node,
                                                 @NonNull final ReactiveTreeNode<?> dispatchingNode,
                                                 @NonNull final Events event,
                                                 @NonNull final Object message) {
        return new Callable<Long>() {
            @Override public Long call() {
                if (event instanceof ChangeColorEvents) {
                    final Pair<ChangeColorEvents, Integer> eventAndMessage = getDebugEventAndMessage();
                    node.dispatchUp(dispatchingNode, eventAndMessage.getLeft(), eventAndMessage.getRight());
                    usedDigit.remove(eventAndMessage.getRight());
                } else {
                    node.dispatchUp(dispatchingNode, event, message);
                }
                final long result = System.currentTimeMillis();
                return result;
            }
        };
    }

    private static Pair<ChangeColorEvents, Integer> getDebugEventAndMessage() {
        int b = RandomUtils.nextInt(15, 255);
        while (usedDigit.contains(b)) {
            b = RandomUtils.nextInt(15, 255);
        }
        usedDigit.add(b);
        Thread.currentThread().setName(Thread.currentThread().getName() + " == " + b);
        return Pair.of(new ChangeColorEvents(getColor(b)), b);
    }

    private static Color getColor(int i) {
        Color bg;
        if (i > 126) {
            bg = new Color(i, i/4, 0);
        } else {
            bg = new Color(0, i/4, i);
        }
        return bg;
    }

    private static void logoutAboutEmitting(@NonNull final ReactiveTreeNode<?> node,
                                            @NonNull final Events event,
                                            @NonNull final Object message) {
        if (start == null) {
            start = System.currentTimeMillis();
            LogUtils.LOGGER.soutLine();
            LogUtils.LOGGER.soutln(node, "eventEmitting", event, message);
            LogUtils.LOGGER.soutLine();
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
                        LogUtils.LOGGER.soutLine();
                        LogUtils.LOGGER.date(LogUtils.dateColor).thread().counter().text("время начала     диспатчинга: ").time(start).ln();
                        LogUtils.LOGGER.soutLine();
                        LogUtils.LOGGER.date(LogUtils.dateColor).thread().counter().text("время окончания  диспатчинга: ").time(stop).ln();
                        LogUtils.LOGGER.soutLine();
                        LogUtils.LOGGER.date(LogUtils.dateColor).thread().counter().text("время выполнения диспатчинга: ").time(stop - start).ln();
                        LogUtils.LOGGER.soutLine().ln().ln().flush();
                        timerIsOn = false;
                        start = null;
                        return;
                    }
                }
            }
        });
        timer.setName("Timer-Thread");
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
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
