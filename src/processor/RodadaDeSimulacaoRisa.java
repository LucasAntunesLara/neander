package processor;

public class RodadaDeSimulacaoRisa extends RodadaDeSimulacao{
	
	public RodadaDeSimulacaoRisa(){
		//super();
		//inicializa();
	}
	
	public void inicializa(){
		total = 0;
		numberOfInstructionsMarked = 0;
		numberOfInstructionsStillMarked = 0;
		notMarked = 0;
		overflow = 0;
		overPercent = 0;
		handling = 0;
		handPercent = 0;
		smallsize = 0;
		smallPercent = 0;
		numberOfBlocks = 0;
		averageSizeOfBlocks = 0;
	}
	
	public void setTotalInstructions(int t){
		total = t;
	}
	
	public int getTotalInstructions(){
		return total;
	}
	
	public void setNumberOfInstructionsMarked(int num){
		numberOfInstructionsMarked = num;
	}
	
	public int getNumberOfInstructionsMarked(){
		return numberOfInstructionsMarked;
	}
	
	public void setNumberOfInstructionsStillMarked(int num){
		numberOfInstructionsStillMarked = num;
	}
	
	public int getNumberOfInstructionsStillMarked(){
		return numberOfInstructionsStillMarked;
	}
	
	public void setNumberOfInstructionsNotMarked(int nMarked){
		notMarked = nMarked;
	}
	
	public int getNumberOfInstructionsNotMarked(){
		return notMarked;
	}
	
	public void setOverflow(int over){
		overflow = over;
	}
	
	public int getOverflow(){
		return overflow;
	}
	
	public float getOverflowPercent(){
		overPercent = (float)((overflow*100)/numberOfInstructionsMarked);
		return overPercent;
	}
	
	public void setHandling(int hand){
		handling = hand;
	}
	
	public int getHandling(){
		return handling;
	}
	
	public float getHandlingPercent(){
		handPercent = (float)((handling*100)/numberOfInstructionsMarked);
		return handPercent;
	}
	
	public void setSmallsize(int small){
		smallsize = small;
	}
	
	public int getSmallsize(){
		return smallsize;
	}
	
	public float getSmallPercent(){
		smallPercent = (float)((handling*100)/numberOfInstructionsMarked);
		return smallPercent;
	}
	
	public void setNumberOfBlocks(int num){
		numberOfBlocks = num;
	}
	
	public int getNumberOfBlocks(){
		return numberOfBlocks;
	}
	
	public void setAverageSizeOfBlocks(float average){
		averageSizeOfBlocks = average;
	}
	
	public float getAverageSizeOfBlocks(){
		return averageSizeOfBlocks;
	}
	
	int total, numberOfInstructionsMarked, numberOfInstructionsStillMarked, notMarked, overflow, handling, smallsize, numberOfBlocks;
	float overPercent, handPercent, smallPercent, averageSizeOfBlocks;
}
