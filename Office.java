/*
	This program shows how threads 
	can be implemented and synchronized
*/


//class office is the one that has threads and objects created and run
public class Office {
	//main method:
	public static void main (String[] args) {
	
		//creating instances of classes and passing parameters
		Tray tray1 = new Tray();
		Lounge lounge1 = new Lounge();
		Thread m1 = new Thread (new Manager(lounge1, tray1, 2000, 20000, "Junior", 19));		
		Thread m2 = new Thread (new Manager(lounge1, tray1, 4000, 60000, "Senior", 9));
		Thread s1 = new Thread (new Secretary(tray1, 1000, 'A', 12));
		Thread s2 = new Thread (new Secretary(tray1, 2000, 'B', 8));
		Thread s3 = new Thread (new Secretary(tray1, 6000, 'C', 4));
		Thread s4 = new Thread (new Secretary(tray1, 6000, 'D', 4));
		
		//runnimg the threads
		s1.start();
		s2.start();
		s3.start();
		s4.start();
		m1.start();
		m2.start();		
	} //the end of main method
} //the end of office class 


// - - - - - - - - - - - - -  - - - - CLASS TRAY - - - - - - - - - - - - - - - - - - - -- 
//class tray acts like a buffer for producers and consumers
// the letters are "stored" in a tray
class Tray {
	
	//declaring variables
	private boolean availableLetter;
	private int numberIn;
	private String managerNm;
	private char secretaryNm;
	
	//constructor
	public Tray (){	
	}
	
	//get-method to get the number of letters is the tray
	public int numberOfLetters(){
		return numberIn;
	}
	
	//method addintTo is called by secretaries to "add" a letter to the tray
	//it is synchronized, because it cannot be accessed by multiple threads simultaneously
	public synchronized void addingTo (char secretaryNm){
		if (numberIn==5){ //if the number of letters in the tray is 5 - it is full
			try{
				System.out.println("The tray is full, secretary " + secretaryNm + " has to wait");
				wait (); //wait for the tray to have space available
			}catch (Exception e){};
		}
		//when the tray has space letters available, the following code is executed 
			try {
				notify(); //the thread is notified
				numberIn++; //the number of letters in the tray is increased
				System.out.println("The tray has " + numberIn + " letters (after secretary " 
				+ secretaryNm + " has put one in)");
			}catch (Exception e){};
	}//the end of method addingTo
	
	//method removingFrom is called by managers to "remove" letters from the tray for signing
	//it is synchronized, because it cannot be accessed by multiple threads simultaneously
	public synchronized void removingFrom (String managerNm){
		if (numberIn==0){ //if the number of letters in the tray is 0 - it is empty 
			try{
				System.out.println("The tray is empty, " + managerNm + " manager has to wait");
				wait(); //wait for the tray to have letters available
			}catch (Exception e){};				
		}
		//when the tray has letters available, the following code is executed
			try{
				notify(); //the tread is notified
				numberIn--; //the number of letters in the tray is decreased
				System.out.println("The tray has " + numberIn + " letters (after " 
				+ managerNm + " manager has signed one out)");
			}catch (Exception e){};	
	}//the end of method removingFrom
}//the end of class Tray

// - - - - - - - - - - - - - - - -  CLASS LOUNGE - - - - - - - - - - - - - - - - - - -
//class lounge acts like a "resting area" for threads
//managers rest in the lounge every time they sign 7 letters
class Lounge{
	
	//declaring variables
	private int NumberIn = 0;
	private int sleeptime;
	private String nameM;
	
	//constructor
	public Lounge (){
	}
	
	//get-method to get the number of managers in the lounge
	public int getNumberLounging(){
		return NumberIn;
	}
	
	//method that assigns the name of a manager from a different 
	//class to a variable of the Lounge class
	public String managerName (String nameMangr){
		return nameM = nameMangr;
	}
	
	//this method assigns 0 to the people inside the lounge when a manager leaves, 
	//since there cann only be 1 manager in the lounge at a time
	public int leavingTheLounge(){
		return NumberIn = 0;
	}
	
	//method inTheLounge is invoked in other classes 
	//in order to represent a manager resting in the lounge
	public synchronized void inTheLounge (){
			if (NumberIn == 1){ //if there is another manager in the lounge already
				try{
					System.out.println("The lounge is occupied, " + nameM + " manager has to wait");
					wait(); //wait for the manager to leave 
				}catch (Exception e){};
			}
			
			if (NumberIn == 0){//if the lounge is empty
				try{
					notify();//notify the thread
					NumberIn++;	//increase number in the lounge, after a manager enters
				}catch (Exception e){};
			}
	}//the end of method inTheLounge
}//the end of class Lounge


// - - - - - - - - - - - - - - CLASS SECRETARY - - - - - - - - - - - - - - - 
//class secretary acts as a producer of letters 
//since it is a thread, it is said to "implement Runnable"
class Secretary implements Runnable {
	
	//declaring variables
	private Tray trayObj;
	private char sLetter;
	private int typingTime;
	private int numOfLetters;
	
	//constructor
	public Secretary (Tray tray1, int typing, char secLetter, int nLetters){
		trayObj = tray1;
		typingTime = typing;
		sLetter = secLetter;
		numOfLetters = nLetters;
	}
	
	//method run, that runs when the thread is called 
	public void run (){
		try{
			//secretary has to write letters under the same conditions until he reaches the daily limit:
			for (int letter = 1; letter <= numOfLetters; letter ++){ 
				//if there is no space in the tray, to put new letters, the thread goes to sleep briefly 
				//and then checks whether the tray is still full
				//this is repeated until there is space in the tray
				while (trayObj.numberOfLetters()>=5) {		
					Thread.sleep(500 + Character.getNumericValue(sLetter));
					//the time that the thread goes to sleep has the numberical value of the character in it
					//in order to prevent threads from accessing the tray at the same time, 
					//in case they want to do it simultaneously (like in case with secretaries C and D)
				}
				//when there is space in the tray, the following code is executed:
				System.out.println("Secretary " + sLetter + " is typing letter " + letter);
				Thread.sleep(typingTime);//thread sleeps while the letter is being "written"
				System.out.println("Secretary " + sLetter +" has typed a letter, "
				+ "number now typed: " + letter);
				trayObj.addingTo(sLetter);//the letter is added to the tray
			}			
		}catch (InterruptedException e){}
	}//the end of method run
}// the end of class Secretary


//- - - - - - - - - - - - - - - - - - - CLASS MANAGER - - - - - - - - - - - - - - - - - - 
//class manager acts a consumer in the program 
//since it is a thread, it is said to "implement Runnable"
class Manager implements Runnable{
	
	//declaring variables
	private int timeToSign;
	private int loungeTime;
	private Tray trayObj;
	private Lounge loungeObj;
	private String name;
	private int dailyLimit;
	
	//constructor
	public Manager(Lounge lounge1, Tray tray1, int signing, int lounging, String nm, int limit){
		loungeObj = lounge1;
		trayObj = tray1;
		timeToSign = signing;
		loungeTime = lounging;
		name = nm;
		dailyLimit = limit;
	}
	
	//method run, that runs when the thread is called 
	public void run (){
		try{
			//manager has to sign the daily limit worth of letters
			//therefore the same actions are repeated that number of times
			for (int letter = 1; letter <= dailyLimit; letter ++){
				//while the tray is empty, the thread goes to sleep briefly
				// and wakes up again to check whther the tray is still empty
				//it is repeated multiple times until the tray has some letter 
				while(trayObj.numberOfLetters()==0){
					Thread.sleep(timeToSign);
					//the thread sleeps for the "time to sign" number of milliseconds
					//in order to avoid concurrency issues (both managers have different signing time)
				}
				System.out.println(name + " manager is signing a letter from the tray");
				Thread.sleep(timeToSign);//thread goes to sleep for the time that the letter is "signed"
				System.out.println(name + " manager has signed a letter."
				+ " Letters signed so far: " + letter);
				trayObj.removingFrom(name); //the removingFrom method is called to simulate the process of signing
				//if the manager has signed 7 letters, he goes to the lounge (providing the lounge is free)
				if (letter%7 == 0 && loungeObj.getNumberLounging() == 1){
					System.out.println("The lounge is occupied, the " + name + " manager has to wait");
					// the print statement is not inside the while-loop,
					// because the it should not repeat multiple times
					while (letter%7 == 0 && loungeObj.getNumberLounging() == 1){
						//the thread chacks if the lounge is free and sleeps briefly
						//to wake up again to check the number of people in the lounge
						//the process is repeated 
						Thread.sleep(1000);
					}
				}
				//it the manager has signed 7 letters and there is space in the lounge, he goes there
				if (letter%7 == 0 && loungeObj.getNumberLounging()!= 1 ){
					loungeObj.managerName(name);// method managerName is called 
					//to pass the name of the manager in the lounge
					System.out.println("The " + name + " manager has signed " + letter + 
					" letters and is going to the lounge");
					loungeObj.inTheLounge();// method in the lounge is called to simulate the process of being in lounge
					Thread.sleep(loungeTime);//the thread goes to sleep while "in the lounge"
					loungeObj.leavingTheLounge();//the manager leaves the lounge by setting the number of people in the lounge to 0
					System.out.println("The " + name + " manager is out of the lounge");
				}
			}
		}catch (Exception e){}
	}//the end of run method
}//the end of Manager class



