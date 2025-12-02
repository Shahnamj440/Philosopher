package pp;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread implements IPhilosopher {
	private Philosopher left;
	private Philosopher right;
	private Lock table;
	private int seat;
	private int eaten;
	private boolean eating;
	private volatile boolean stop;
	private final Random random;
	private Condition condition;

	private void log(String message){
		synchronized (Philosopher.class) {
			for (int i = 0; i < this.seat; i++){
				System.out.println("                         ");
			}
			System.out.println("P" + this.seat + ":" + message);
		}
	}

	public Philosopher() {
		this.eaten = 0;
		this.eating = false;
		this.stop = false;
		this.random = new Random();
	}

	@Override
	public void setLeft(IPhilosopher left) {
		// TODO Auto-generated method stub
		// Cast auf Philosopher erforderlich Test1 N43
		this.left = (Philosopher) left;
	}

	@Override
	public void setRight(IPhilosopher right) {
		// TODO Auto-generated method stub
		// Cast auf Philosopher erforderlich
		this.right = (Philosopher) right;
	}

	@Override
	public void setSeat(int seat) {
		this.seat = seat;
	}

	@Override
	public void setTable(Lock table) {
		// TODO Auto-generated method stub
		this.table = table;
		this.condition = this.table.newCondition();
	}
	
	@Override
	public void stopPhilosopher() {
		// TODO Auto-generated method stub
		log("stopping.");
		stop = true;
		this.interrupt();
	}

	@Override
	public void run(){
		log("starting");
		try {
			while(!this.stop){
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
				log("left and right released.");
				this.eating = false;
			}
		} finally {
			this.table.unlock();
		}
		Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_EATING_DURATION_MS));
	}

	private void eat() throws InterruptedException {
		
	}
}