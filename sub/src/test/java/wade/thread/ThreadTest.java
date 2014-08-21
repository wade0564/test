package wade.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ThreadTest {
	


	ExecutorService subExecutor;
	
	public static Semaphore s ;

	public void init() {
		int nThreads = 5;
		this.subExecutor = Executors.newFixedThreadPool(nThreads);

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ThreadTest t = new ThreadTest();
		t.init();
		

		for (int i = 0; i < 5; i++) {
			t.subExecutor.submit(new RunTask(i), null);

		}
		
		System.out.println("ok?");

	}

}

class RunTask implements Runnable {
	
	int index;
	BufferedWriter bw;
	public RunTask(int index) {
		File  f= new File ("c:/tmp/test"+index);
		try {
//			f.createNewFile();
			bw =new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.index=index;
	}

	public void run() {
		
		for(int i=0;i<5000000;i++)
		{
			try {
				bw.write("Thread"+index+"Today is Friday.line: "+i +"\n" );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
