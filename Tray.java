public class Tray {
	boolean availableLetter;
	private int numberIn;
	String managerNm;
	char secretaryNm;
	
	public Tray (){	
	}
	
	public int numberOfLetters(){
		return numberIn;
	}
	
	public synchronized void addingTo (char secretaryNm){
		if (numberIn==5){
			try{
				System.out.println("The tray is full, secretary " + secretaryNm + " has to wait");
				wait ();
			}catch (Exception e){};
		}
		
			try {
				notify();	
				numberIn++; 
				System.out.println("The tray has " + numberIn + " letters (after secretary " 
				+ secretaryNm + " has put one in)");
			}catch (Exception e){};
		
	}
	
	public synchronized void removingFrom (String managerNm){
		if (numberIn==0){
				try{
					System.out.println("The tray is empty, " + managerNm + " manager has to wait");
					wait();
				}catch (Exception e){};				
			}
		
			try{
				notify();
				numberIn--; 
				System.out.println("The tray has " + numberIn + " letters (after " 
				+ managerNm + " manager has signed one out)");
			}catch (Exception e){};
		}
	}
}