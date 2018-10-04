import java.io.IOException;

public class Main {
	public static final int run = 10;
	public static void main(String[] args) {
		for(int i =0; i< run; i++) {
			Controller m_controller = new Controller("C:\\temp\\directpool3(21)l1h106alpha12.txt");
			try {
				while(!m_controller.updateState()) {
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
