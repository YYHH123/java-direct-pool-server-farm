
public class Job {
	private Double process_t;
	private Double arrive_t;
	private Double remain_t;
	public Job(double p_t, double a_t) {
		process_t = new Double(p_t);
		arrive_t = new Double (a_t);
		remain_t  = new Double(p_t);
	}
	public boolean decreaseTime(double time) {
		remain_t -= time;
		if(remain_t.compareTo(new Double (0.0)) <= 0)
			return false;
		else
			return true;
	}
	public double getRemainTime() {
		return remain_t;
	}
	public double getResponseTime(double ct) {
		return ct-arrive_t;
	}

}
