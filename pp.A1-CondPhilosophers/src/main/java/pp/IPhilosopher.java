package pp;

import java.util.concurrent.locks.Lock;

public interface IPhilosopher {

	void run();

	void setLeft(IPhilosopher left);

	void setRight(IPhilosopher right);

	void setSeat(int seat);

	void setTable(Lock table);

	void start();

	void stopPhilosopher();
	
	default void log(int seat, int eaten, String message) {
		synchronized (Philosopher.class) {
			for (var i = 1; i <= seat; i++) {
				System.out.print("                         ");
			}
			System.out.println("P" + seat + " - Eaten: " + eaten + " : " + message);
		}
	}
}
