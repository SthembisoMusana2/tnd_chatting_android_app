import java.util.LinkedList;
import java.util.Queue;

public class Queues {
	
	
	public static void main(String[] args) {
		Queue<Integer> q = new LinkedList<Integer>();
		
		q.add(10);
		q.add(20);
		q.add(0);
		q.add(2);
		
		System.out.println("Elements of the queue: "+ q);
		
		q.remove();
		System.out.print("Elements of queue after removal: " + q);
		
		q.add(100);
		
		System.out.print("Queue after adding: "+ q.size());
	}
}
