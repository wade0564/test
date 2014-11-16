package cn.edu.ustc.wade.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.net.ssl.SSLContext;

public class ThreadTest {
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
		ThreadTest t = new ThreadTest();
		t.init();
		

		for (int i = 0; i < 5; i++) {
			t.handleService.submit(new RunTask(), null);
			t.handleService.submit(new CallTask());

		}
		
		s.acquire();
		System.out.println("ok?");
//		
//		for(int i=0;i<10;i++)
//			
//			System.out.println(t.handleService.take().get());
	}

}

class RunTask implements Runnable {

	public void run() {
		System.out.println("run" + Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ThreadTest.s.release();
	}

}

class CallTask implements Callable {

	public Object call() throws Exception {
		System.out.println("call" + Thread.currentThread().getName());
		ThreadTest.s.release();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
}
