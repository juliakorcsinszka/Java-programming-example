public class Manager implements Runnable{
	int timeToSign;
	int loungeTime;
	Tray trayObj;
	Lounge loungeObj;
	String name;
	int dailyLimit;
	
	public Manager(Lounge lounge1, Tray tray1, int signing, int lounging, String nm, int limit){
		loungeObj = lounge1;
		trayObj = tray1;
		timeToSign = signing;
		loungeTime = lounging;
		name = nm;
		dailyLimit = limit;
	}
	
	public void run (){
		try{
			for (int letter = 1; letter <= dailyLimit; letter ++){
				while(trayObj.numberOfLetters()==0){
					Thread.sleep(timeToSign);
				}
				System.out.println(name + " manager is signing a letter from the tray");
				Thread.sleep(timeToSign);
				System.out.println(name + " manager has signed a letter."
				+ " Letters signed so far: " + letter);
				trayObj.removingFrom(name); 
				if (letter%7 == 0 && loungeObj.getNumberLounging() == 1){
					System.out.println("The lounge is occupied, the " + name + " manager has to wait");
					while (letter%7 == 0 && loungeObj.getNumberLounging() == 1){
						Thread.sleep(1000);
					}
				}
				if (letter%7 == 0 && loungeObj.getNumberLounging()!= 1 ){
					loungeObj.managerName(name);
					System.out.println("The " + name + " manager has signed " + letter + 
					" letters and is going to the lounge");
					loungeObj.inTheLounge();	
					Thread.sleep(loungeTime);	
					loungeObj.leavingTheLounge();
					System.out.println("The " + name + " manager is out of the lounge");
				}
			}
		}catch (Exception e){}
	}
}