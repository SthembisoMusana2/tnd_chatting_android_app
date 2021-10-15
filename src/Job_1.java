

public class Job_1 extends Thread{
//	private Scanner input = new Scanner(System.in);
	@Override
	public void run() {
		try {
			int j = 0;
			while(j < 100) {
				Thread.sleep(1000);
				System.out.println("The thread executes "+ j);
				j++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
