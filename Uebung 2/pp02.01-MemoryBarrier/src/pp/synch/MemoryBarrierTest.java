package pp.synch;

public class MemoryBarrierTest extends Thread {

	public volatile boolean stopped = false;
	public boolean isStopped(){
		synchronized (this) {
		return this.stopped;
		}
	}
	public void stopRequest() {
		synchronized (this) {
		this.stopped = true;
		}
	}
	public void run() {
		while (isStopped()) {
			// work
		}
		System.out.println("MemoryBarrierTest-Thread actually stopped.");
	}



	public static void main(String[] args) throws InterruptedException {
		MemoryBarrierTest t = new MemoryBarrierTest();
		t.start();
		Thread.sleep(1000);
		MemoryBarrierTest.sleep(1000);
		t.stopRequest();
//		Thread thread = new Thread(()->	{
//			t.stopRequest();
//		});
//		thread.start();
		System.out.println("Main thread set stopped on MemoryBarrierTest-Thread.");
	}

}
