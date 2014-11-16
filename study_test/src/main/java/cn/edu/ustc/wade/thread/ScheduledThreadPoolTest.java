package cn.edu.ustc.wade.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolTest {


	public static void main(String[] args) throws InterruptedException, ExecutionException {

		
		
	 ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
		
		scheduledService.scheduleWithFixedDelay(new RunnerTest(), 0, 1, TimeUnit.SECONDS);
		
		System.out.println("ok?");
	}

}


class RunnerTest  implements Runnable{

	int count=0;
	
	public void run() {
		
		System.out.println(count++);
		
		
	}
	
}



