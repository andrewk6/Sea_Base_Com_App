import java.io.File;

public class DataScan{
	File conf, sched;
	
	public DataScan() {
		conf = new File("Config.bin");
		if(!conf.exists()) {
			
		}
	}
}