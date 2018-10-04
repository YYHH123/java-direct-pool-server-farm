import java.util.Iterator;
import java.util.Vector;

public class Server {
	private boolean busy;
	private Job m_job;
	private Vector<Double> m_processList;
	public Server() {
		busy = false;
		m_job = null;
		m_processList = new Vector<Double>();
	}
	public boolean setJob(double time, double at) {
		if(!busy) {
		m_job = new Job(time,at);
		busy = true;
		}
		return busy;
	}
	public boolean setJob(Job newjob) {
		if(!busy) {
			m_job = newjob;
			busy = true;
			}
		else {
			System.out.println("replacing JOBs!!!!!!!!!");
		}
		return busy;
	}
	public int decreaseTime(double time, double ct) {
		if(m_job != null) { 
			if(!m_job.decreaseTime(time)) {
				m_processList.add(m_job.getResponseTime(ct));
				m_job = null;
				setIdle();
				return 1;
			}
			return 0;
		}
		return -1;
	}
	public void setIdle()
	{
		busy = false;
	}
	public boolean getIdle()
	{
		return !busy;
	}
	public Double getProcTime() {
		return m_job == null ? Double.MAX_VALUE : m_job.getRemainTime();
	}
	public Double getMeanwaitingTime() {
		Double meanWaiting = new Double(0.0);
		Iterator<Double> it = m_processList.iterator();
		while(it.hasNext()) {
			meanWaiting += it.next();
		}
		return meanWaiting;
	}
	public int getServedNum() {
		return m_processList.size();
	}
}
