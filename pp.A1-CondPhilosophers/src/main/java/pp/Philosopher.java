package pp;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread implements IPhilosopher {

    private Philosopher left;
    private Philosopher right;
    private Lock table;
    private int seat;
    private int eaten;
    private volatile boolean eating;
    private volatile boolean stop;
    private final Random random;
    private Condition canEat;

    public Philosopher() {
        this.eaten = 0;
        this.eating = false;
        this.stop = false;
        this.random = new Random();
    }

    @Override
    public void setLeft(IPhilosopher left) {
        this.left = (Philosopher) left;
    }

    @Override
    public void setRight(IPhilosopher right) {
        this.right = (Philosopher) right;
    }

    @Override
    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public void setTable(Lock table) {
        this.table = table;
        this.canEat = this.table.newCondition();
    }

    @Override
    public void stopPhilosopher() {
        log(seat, eaten, "stopping.");
        this.stop = true;
        this.interrupt();
    }

    @Override
    public void run() {
        log(seat, eaten, "starting");
        try {
            while (!this.stop) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        this.table.lock();
        try {
            if (this.eating) {
                log(seat, eaten, "left and right released.");
                this.eating = false;
                this.left.canEat.signalAll();
                this.right.canEat.signalAll();
                log(seat, eaten, "thinking");
            }
        } finally {
            this.table.unlock();
        }
        Thread.sleep(this.random.nextInt(
            PhilosopherExperiment.MAX_THINKING_DURATION_MS
        ));
    }

    private void eat() throws InterruptedException {

        this.table.lock();
        try {
            while (this.right.eating || this.left.eating) {
                log(seat, eaten, "try taking left or right. waiting!!!");
                this.canEat.await();
            }
        } finally {
            this.table.unlock();
        }

        Thread.sleep(this.random.nextInt(
            PhilosopherExperiment.MAX_TAKING_TIME_MS
        ));

        this.table.lock();
        try {
            while (this.right.eating || this.left.eating) {
                this.canEat.await();
            }
            this.eating = true;
            eaten++;
            log(seat, eaten, "left and right acquired.");
            log(seat, eaten, "eating.");
        } finally {
            this.table.unlock();
        }

        Thread.sleep(this.random.nextInt(
            PhilosopherExperiment.MAX_EATING_DURATION_MS
        ));
        log(seat, eaten, "finished eating.");
    }
}
