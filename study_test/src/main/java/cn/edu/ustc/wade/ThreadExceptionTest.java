package cn.edu.ustc.wade;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ThreadExceptionTest {
	static CompletionService handleService;

	ExecutorService subExecutor;
	
	public static Semaphore s ;

	public void init() {
		int nThreads = 2;
		this.subExecutor = Executors.newFixedThreadPool(nThreads);
		handleService = new ExecutorCompletionService(subExecutor);

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

//		s = new Semaphore(-9);
		ThreadExceptionTest t = new ThreadExceptionTest();
		t.init();
		

		try {

			for (int i = 0; i < 5; i++) {
				t.subExecutor.submit(new RunnerTask());
			}
		} catch (Exception e) {
			System.out.println("----------");
		}
		
		System.out.println("ok?");
//		
//		for(int i=0;i<10;i++)
//			
//			System.out.println(t.handleService.take().get());
	}

}

class RunnerTask implements Runnable {

	public void run() {
		System.out.println("run" + Thread.currentThread().getName());
		try {
			double a = 1 / 0;
			System.out.println("I'm here"+a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
