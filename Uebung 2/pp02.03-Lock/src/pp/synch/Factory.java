package pp.synch;

import pp.synch.model.Type;

public class Factory {

	private static Type instance;

	public static Type getInstance() {
		Type.prepare();
		synchronized (Factory.class) {
			if (instance == null) {
				instance = new Type();
			}
			return instance;
		}
	}

	// private static Factory myInstance = new Factory();
	//
	// public static Type getInstance() {
	// Type.prepare();
	// synchronized(myInstance) {
	// if (instance == null) {
	// instance = new Type();
	// }
	// return instance;
	// }
	// }
	public static void main(final String[] args) throws InterruptedException {
		final long now = System.currentTimeMillis();
		final Thread[] threads = new Thread[100];
		for (int i = 0; i < 100; i++) {
			threads[i] = new Thread(() -> {
				final Type object = Factory.getInstance();
				System.out.println(Thread.currentThread().getName() + ": serial of instance = " + object.getSerial());
			} , String.format("InstanceGrabber-%02d", i));
			threads[i].start();
		}
		for (int i = 0; i < 100; i++) {
			threads[i].join();
		}
		final long time = System.currentTimeMillis() - now;
		System.out.println("Dauer: " + time + "ms");
	}

}