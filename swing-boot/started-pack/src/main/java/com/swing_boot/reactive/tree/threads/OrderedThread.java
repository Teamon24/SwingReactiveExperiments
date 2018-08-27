package com.swing_boot.reactive.tree.threads;


/**
 * 23.12.2016.
 * _____________________________________________________________________________________________________________________________________________________________________________________________________________________________
 */
public class OrderedThread extends Thread {

    public int ID;
    private static int current = 1;
    private static final Object lock = new Object();

    OrderedThread(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }

    @Override
    public void run() {
        order(this, lock);
    }

    public static void order(final OrderedThread thread, final Object lock) {

        try {
            synchronized (lock) {
                int ID = thread.getID();
                while (ID > current) {
                    System.out.println("current: Thread#" + ID + "; can pass: Thread#" + current);
                    Thread.sleep(500);
                    lock.wait();
                }
                current++;
                System.out.println("Thread#" + ID + " passed through");
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            new OrderedThread(i).start();
        }
    }
}
