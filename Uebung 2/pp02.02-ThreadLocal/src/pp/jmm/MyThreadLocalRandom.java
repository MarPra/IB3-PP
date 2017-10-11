package pp.jmm;

import java.util.Random;

public class MyThreadLocalRandom implements Runnable {
	public static long now = System.currentTimeMillis();
	public int rng;
//	public Random rand = new Random(now);
	public ThreadLocal<Random> rand = new ThreadLocal<Random>(){
		@Override
		protected Random initialValue(){
			return new Random(now);
		}
	};
	@Override
	public void run() {
		final StringBuffer strBuf = new StringBuffer();
		strBuf.append(Thread.currentThread().getName() + ": ");
		for (int j = 0; j < 20; j++) {
			strBuf.append(String.format("%2d ", rand.get().nextInt(100)));
		}
		System.out.println(strBuf);
	}

	public static void main(final String[] args) {
		Runnable runner = new MyThreadLocalRandom();
		for (int i = 0; i < 10; i++) {
			new Thread(runner, String.format("Runner-%02d", i)).start();
		}
	}
}