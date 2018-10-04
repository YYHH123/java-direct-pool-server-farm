import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ServerPool {
	private int servedNum;
	private Queue<Job> m_queue = new LinkedList<Job>();
	private List<Server> servers = new ArrayList<Server>();
	public ServerPool(int size) {
		for(int i = 0; i< size; i++) {
			servers.add(new Server());
		}
		servedNum=0;
	}
	public void decreaseProcTime(double time, double ct) {
		for (int i= 0; i< servers.size(); i++) {
			servers.get(i).decreaseTime(time,ct);
			if(servers.get(i).getIdle() && !m_queue.isEmpty())
				servers.get(i).setJob(m_queue.poll());
		}		
	}
	public void insertJob(Job njob) {
		for(int i = 0; i< servers.size(); i++) {
			if(servers.get(i).getIdle() && m_queue.isEmpty()) {
				servers.get(i).setJob(njob);
				return;
			}
		}
		m_queue.add(njob);
	}
	public Double findMinTime() {
		Double mint = Double.MAX_VALUE;
		for(int i = 0; i<servers.size(); i++) {
			if(servers.get(i).getProcTime().compareTo(mint) < 0)
				mint = servers.get(i).getProcTime();
		}
		return mint;
	}
	public Double getMeanWaitingTime(boolean mean) {
		double waitingTime =0.0;
		for(int i= 0; i<servers.size(); i++) {
			waitingTime += servers.get(i).getMeanwaitingTime();
			servedNum += servers.get(i).getServedNum();
		}
		Iterator<Job> it = m_queue.iterator();
		while(it.hasNext()) {
			waitingTime += it.next().getRemainTime();
		}
		servedNum += m_queue.size();
		if(mean)
			return waitingTime = waitingTime / (double) servedNum;
		else 
			return waitingTime;
	}
	public int getServedNum() {
		return servedNum;
	}

}
