package cviko07.uloha2_ForkJoinPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.RecursiveTask;

public class SizeTask extends RecursiveTask<DirSize> {

	private File dir;

	public SizeTask(File dir) {
		this.dir = dir;

	}

	@Override
	public DirSize compute() {
		
		return new DirSize(dir,analyzeDir(dir));
	}

	private long analyzeDir(File dir) throws DirectoryForbiddenException{
		long sumSize = 0;
		File[] files = dir.listFiles();
		if(files == null) {
			throw new DirectoryForbiddenException(dir);
		}
		List<SizeTask> tasks = new ArrayList<SizeTask>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				sumSize += files[i].length();
			if (files[i].isDirectory()) {
				SizeTask task = new SizeTask(files[i]);
				task.fork();
				tasks.add(task);
				
			}
				
		}
		for (SizeTask task:tasks) {
			sumSize += task.join().getSize();
		}
		return sumSize;
	}
}
