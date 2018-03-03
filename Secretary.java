public class Secretary implements Runnable {
	Tray trayObj;
	char sLetter;
	int typingTime;
	int numOfLetters;
	
	public Secretary (Tray tray1, int typing, char secLetter, int nLetters){
		trayObj = tray1;
		typingTime = typing;
		sLetter = secLetter;
		numOfLetters = nLetters;
	}
	
	public void run (){
		try{
			for (int letter = 1; letter <= numOfLetters; letter ++){
				while (trayObj.numberOfLetters()>=5) {
					Thread.sleep(500 + Character.getNumericValue(sLetter));
				}
				System.out.println("Secretary " + sLetter + " is typing letter " + letter);
				Thread.sleep(typingTime);
				System.out.println("Secretary " + sLetter +" has typed a letter, "
				+ "number now typed: " + letter);
				trayObj.addingTo(sLetter);
			}			
		}catch (InterruptedException e){}
	}
}