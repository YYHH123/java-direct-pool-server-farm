import java.io.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Controller {
	private static final double lambda = 0.5123;
	public static final int server_num = 3;
	public static final int High = 1000000;
	public static final int Low = 1;
	public static final double alpha = 1.2;
	public static final double STIME  = 30000000;
	public static final int arrival = 0;
	public static final int departure =1;
	private static int[] partition = {2,1};
	private String file_name;
	public double current_t;
	public int start;
	public int id_with_mint;
	private double remain_arrival;
	private double min_t;
	private double[] cutoffs = new double[partition.length-1];
	private int system_state;
	private Vector<ServerPool> m_pools;
	private Vector<Double> waitingT_perPool;
	
	public Controller(String f_name) {
		file_name = f_name;
	    current_t = 0.0;
	    start =0;
	    id_with_mint = -1;
	    remain_arrival = getExpRanNum(lambda);
	    min_t = remain_arrival;
	    waitingT_perPool = new Vector<Double> ();
	    m_pools = new Vector<ServerPool> ();
	    double[] site_cutoffs= new double[server_num -1];
	    for(int i=1; i<=server_num-1; i++)
	    {
	    	site_cutoffs[i-1] =Math.pow((i*Math.pow(High,(1-alpha))+server_num-i)/server_num,1/(1-alpha));
	        System.out.println(site_cutoffs[i-1]);
	    }
	    for(int j=0; j<partition.length-1;j++){
	        ServerPool sp =new ServerPool(partition[j]);
	        m_pools.add(sp);
	        cutoffs[j] = site_cutoffs[j+partition[j]-1];
	    }
	    ServerPool sp = new ServerPool(partition[partition.length -1]);
	    m_pools.add(sp);
	    
	}
	public static Double getExpRanNum(double lambda) {
		Double UnivRanNum, ExpRanNum;
		Random random = new Random(System.nanoTime());
		UnivRanNum = random.nextDouble();
		while (UnivRanNum.equals(new Double (1.0)))
			UnivRanNum = random.nextDouble();
			
		ExpRanNum = -1.0*(Math.log(1-UnivRanNum))*(1.0/lambda);
		return ExpRanNum;
		
	}
	public boolean updateState() throws IOException {
		if(start==0)
	    {
	        current_t+=min_t;
	        remain_arrival=getExpRanNum(lambda);
	        double NewProTime=getPareto(Low,High,alpha);
	        SITAE_pool(NewProTime);
	        start=1;
	    }
	    else if(start==1)
	    {
	        
	        current_t+=min_t;
	        if(current_t>STIME)
	        {
	            Double MeanWaitingTime = ComputResult();
	            FileWriter fw = new FileWriter(file_name,true);
	            fw.write(MeanWaitingTime.toString() +"\r\n");
	            fw.close();
	            return true; 
	        }
	        else if(system_state==arrival)
	        {
	            remain_arrival=getExpRanNum(lambda);
	            decreaseTime(min_t);
	            double NewProTime=getPareto(Low,High,alpha);
	            //double NewProTime = 593.00;
	            SITAE_pool(NewProTime);
	        }
	        else if(system_state==departure)
	        {
	            remain_arrival-=min_t;
	            decreaseTime(min_t);
	        }
	    }
	    getNewState();
	    return false;
		
	}
	private void getNewState() {
		min_t = findMinTime();
		system_state = id_with_mint == -1 ? arrival : departure;
	}
	private void decreaseTime(double time) {
		Iterator<ServerPool> it = m_pools.iterator();
		while(it.hasNext()) {
			it.next().decreaseProcTime(time, current_t);;
		}
		
	}
	private double ComputResult() {
		double totalwaitingtime= 0.0;
		int size = 0;
		for(int i =0; i<m_pools.size(); i++) {
			totalwaitingtime += m_pools.get(i).getMeanWaitingTime(false);
			waitingT_perPool.add(m_pools.get(i).getMeanWaitingTime(true));
			size += m_pools.get(i).getServedNum();
		}
		return totalwaitingtime = totalwaitingtime / (double) size;
		
	}
	private void SITAE_pool(double newProTime) {
		Job njob = new Job(newProTime,current_t);
		for(int i = 0; i<cutoffs.length; i++) {
			if(newProTime < cutoffs[i]) {
				m_pools.get(i).insertJob(njob);
				return;
			}
		}
		m_pools.get(m_pools.size()-1).insertJob(njob);
		
	}
	public static double getPareto(int low, int high, double alpha) {
		double UnivRanNum, ParetoRanNum;
		Random random = new Random(System.nanoTime());
		UnivRanNum = random.nextDouble();
		double temp = -(UnivRanNum*Math.pow(high,alpha)-UnivRanNum*Math.pow(low,alpha)-Math.pow(high,alpha))/Math.pow(high*low, alpha);
		ParetoRanNum = Math.pow(temp,-1/alpha);
		return ParetoRanNum;
	}
	public Double findMinTime() {
		Double mint = new Double(remain_arrival);
		id_with_mint = -1;
		for(int i =0; i< m_pools.size() ; i++) {
			if(m_pools.get(i).findMinTime().compareTo(mint) < 0) {
				mint = m_pools.get(i).findMinTime();
				id_with_mint = i;
			}
		}
		return mint;
	}
	public 

}
