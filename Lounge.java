public class Lounge{
	private int NumberIn = 0;
	int sleeptime;
	String nameM;
	
	public Lounge (){
	}
	
	public int getNumberLounging(){
		return NumberIn;
	}
	public String managerName (String nameMangr){
		return nameM = nameMangr;
	}
	
	public int leavingTheLounge(){
		return NumberIn = 0;
	}
	
	public synchronized void inTheLounge (){
			if (NumberIn == 1){
				try{
					System.out.println("The lounge is occupied, " + nameM + " manager has to wait");
					wait();
				}catch (Exception e){};
			}
			
			if (NumberIn == 0){
				try{
					notify();
					NumberIn++;	
				}catch (Exception e){};
			}
			
	}
}