package pp;

import java.util.concurrent.locks.Lock;

public class Philosopher extends Thread implements IPhilosopher {
	private int seat;

	@Override
	public void setLeft(IPhilosopher left) {
		// TODO Auto-generated method stub
		// Cast auf Philosopher erforderlich Test1
	}

	@Override
	public void setRight(IPhilosopher right) {
		// TODO Auto-generated method stub
		// Cast auf Philosopher erforderlich
	}

	@Override
	public void setSeat(int seat) {
		this.seat = seat;
	}

	@Override
	public void setTable(Lock table) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopPhilosopher() {
		// TODO Auto-generated method stub

	}
}
