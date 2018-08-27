package com.swing_boot.reactive.tree.nodes;


import com.swing_boot.reactive.Events;

import java.util.concurrent.locks.ReentrantLock;

public abstract class Observer {

    private static ReentrantLock lock = new ReentrantLock();

    protected abstract void update(Events e, Object v);

    public void syncUpdate(Events e, Object v) {
        lock.lock();
        update(e, v);
        lock.unlock();
    }
}
