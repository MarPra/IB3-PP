package pp.dining.cond;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread implements IPhilosopher {

    private Philosopher leftNeighbour, rightNeighbour;
    private Lock table;
    protected static Random random = new Random();
    Condition noNeighbourEating;
    Condition neighbourEating;
    private int timesEaten = 0;
    private volatile boolean isEating = false;
    private boolean isStopped = false;

    public void eat(int time) {
        try {
            table.lock();
            if (leftNeighbour.isEating() || rightNeighbour.isEating()) {
                noNeighbourEating.await();
            }
            else {
            this.isEating = true;
//                this.leftNeighbour.neighbourEating.signal();
//                this.rightNeighbour.neighbourEating.signal();
            System.out.println(getWhitespace() + Thread.currentThread().getName() + " starts eating");
            try {
                Thread.sleep(random.nextInt(time));
            } catch (InterruptedException ex) {
                Logger.getLogger(Philosopher.class.getName()).log(Level.FINE, null, ex);
            }
            this.timesEaten++;
            System.out.println(getWhitespace() + Thread.currentThread().getName() + " done eating");
            this.isEating = false;
            this.leftNeighbour.noNeighbourEating.signal();
            this.rightNeighbour.noNeighbourEating.signal();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Philosopher.class.getName()).log(Level.FINE, null, ex);
        } finally {
            table.unlock();
        }
    }

    public void stopEating() {
        this.isEating = false;
    }

    public void think(int time) {
        System.out.println(getWhitespace() + Thread.currentThread().getName() + " starts thinking");
        try {

            Thread.sleep(random.nextInt(time));
            System.out.println(getWhitespace() + Thread.currentThread().getName() + " done thinking ");
        } catch (InterruptedException ex) {
            Logger.getLogger(Philosopher.class.getName()).log(Level.FINE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            while (!this.isStopped) {
                eat(PhilosopherExperiment.MAX_TAKING_TIME_MS);
                think(PhilosopherExperiment.MAX_THINKING_DURATION_MS);
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + " finished");
            System.out.println(Thread.currentThread().getName() + "Times eaten: " + this.timesEaten);
        }
    }

    @Override
    public void setLeft(IPhilosopher left) {
        if (this.getClass() == left.getClass()) {
            setLeft((Philosopher) left);
        }
    }

    @Override
    public void setRight(IPhilosopher right) {
        if (this.getClass() == right.getClass()) {
            setRight((Philosopher) right);
        }
    }

    public void setLeft(Philosopher left) {
        this.leftNeighbour = left;
    }

    public void setRight(Philosopher right) {
        this.rightNeighbour = right;
    }

    @Override
    public void setTable(Lock newTable) {
        this.table = newTable;
        noNeighbourEating = this.table.newCondition();
        neighbourEating = this.table.newCondition();
    }

    @Override
    public void stopPhilosopher() {
        this.isStopped = true;
        this.interrupt();
    }

    public boolean isEating() {
        return this.isEating;
    }

    public String getWhitespace() {
        long length = Thread.currentThread().getId() * 2;
        String temp = "";
        for (long i = 0; i < length; i++) {
            temp = temp + " ";
        }
        return temp;
    }
}
