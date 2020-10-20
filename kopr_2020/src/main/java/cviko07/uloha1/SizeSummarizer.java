package cviko07.uloha1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;


public class SizeSummarizer {

	public static final String START_DIR = "D:\\upjs\\3.rocnik\\KOPR\\workspace";
	public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		File rootDir = new File(START_DIR);
		System.out.println("Pocet vlakien: " + THREAD_COUNT);
		ForkJoinPool executor = new ForkJoinPool();

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		List<SizeTask> tasks = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				executor.submit(task);
				tasks.add(task);

			}
		}
		for (SizeTask task : tasks) {
			DirSize dirSize = task.join();
			System.out.println("Cas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
		}
		executor.shutdown();
	}

}
