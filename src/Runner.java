public class Runner{
	public static void main(String[]args) throws InterruptedException {
		DataScan ds = new DataScan(new Groupme_Com());
		ds.readExcel();
		System.out.println("End of Read 1");
		//Thread.sleep(10000);
		System.out.println("Begin Delete 1");
		ds.gCom.deleteGroups();
		System.out.println("End of Delete 1");
		
		ds.readExcel();
		System.out.println("End Read 2\nBegin Delete 2");
		ds.gCom.deleteGroups();
		System.out.println("End Delete 2");
	}
}