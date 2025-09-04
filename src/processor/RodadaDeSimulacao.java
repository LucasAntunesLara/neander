package processor;

import primitive.Primitive;

public class RodadaDeSimulacao extends Primitive{
	
	public RodadaDeSimulacao(){
		inicializa();
	}
	
	public void inicializa(){
		tempoDeSimulacao = 0;
		nroInstructionsFetched = 0;
		executionCycles = 0;
	}
	
	public void setTempoDeSimulacao(double tempo){
		tempoDeSimulacao = tempo;
	}
	
	public double getTempoDeSimulacao(){
		return tempoDeSimulacao;
	}
	
	public void setNroInstructionsFetched(int nro){
		nroInstructionsFetched = nro;
	}
	
	public int getNroInstructionsFetched(){
		return nroInstructionsFetched;
	}
	
	public void setExecutionCycles(int cycles){
		executionCycles = cycles;
	}
	
	public int getExecutionCycles(){
		return executionCycles;
	}
	
	public void debug() {
		
	}
	
	public void list() {
		
	}
	int nroInstructionsFetched, executionCycles;
	double tempoDeSimulacao;
}
