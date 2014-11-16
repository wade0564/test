package cn.edu.ustc.wade.thread;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	
	public static void main(String[] args) throws InterruptedException {
		
		Semaphore semaphore = new Semaphore(0);
		
		semaphore.acquire();
		
		
	}
	

}
