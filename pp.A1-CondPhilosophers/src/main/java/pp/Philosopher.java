package pp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread implements IPhilosopher {
	private int seat;
	private Philosopher left;
	private Philosopher right;
	private Lock table;
	private volatile boolean running = true;
	private final AtomicInteger eaten = new AtomicInteger();
	private final java.util.Random rand = new java.util.Random();

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
	}

	@Override
	public void stopPhilosopher() {
		// TODO Auto-generated method stub
		running = false;
		this.interrupt();
	}

	@Override
	public void run(){
		while(running){
			try {
				int think = rand.nextInt(PhilosopherExperiment.MAX_THINKING_DURATION_MS + 1);
				log (seat, eaten, "thinking for " + think + "ms.");
				Thread.sleep(think);

			} catch (InterruptedException e) {
				if (!running) break;
			}
			
			boolean acquired = false;
			try {
				acquired = table.tryLock(PhilosopherExperiment.MAX_TAKING_TIME_MS, TimeUnit.MILLISECONDS);

				if (acquired) {
					int eat = rand.nextInt(PhilosopherExperiment.MAX_EATING_DURATION_MS + 1);
					log(seat, eaten, "eating for " + eat + "ms.");

					try {
						Thread.sleep(eat);
						eaten.incrementAndGet();
					} catch (InterruptedException ex) {
						if (!running) break;
					}
				} else {
					log(seat, eaten, "couldn't get the table lock.");
				}
			
			} catch (InterruptedException e) {
				if (!running) break;
			} finally {
				if (acquired) {
					table.unlock();
					log(seat, eaten, "released table.");
				}
			}
		}
		log(seat, eaten, "stopped.");
	}
}