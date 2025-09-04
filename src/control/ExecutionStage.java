package control;

import primitive.Primitive;
import util.Define;

public class ExecutionStage extends Primitive implements Define {

	public ExecutionStage ( String parName, int parOrg, int parTarg) {
		sbName = new StringBuffer ( ).append (parName);

		iOriginalPipe = parOrg;
		iTargetPipe = parTarg;
		iCurrent = null;
		bActive = true;
	}

	public void setCurrentInst ( Instruction parInst) {
		iCurrent = parInst;
	}

	public Instruction getCurrentInst ( ) {
		return ( iCurrent);
	}

	public int getExecutionStageId ( ) {
		return ( iOriginalPipe);
	}

	public int getExecutionStageTargetId ( ) {
		return ( iTargetPipe);
	}

	public void setActiveCondition ( boolean parActive) {
		bActive = parActive;
	}
	
	public boolean isActive ( ) {
		return bActive;
	}

	public void debug ( ) {
//		System.out.println ( );
//		System.out.println ( "*** PipeStage.debug:...BEGIN");
		System.out.println ( "---->	NOME DO PIPESTAGE: "+sbName+" e traducao: "+iOriginalPipe+","+iTargetPipe+","+bActive);
		if ( iCurrent != null) iCurrent.debug ( );
		else System.out.println ( "No Instruction");
//		System.out.println ( "*** PipeStage.debug:...END");
//		System.out.println ( );
	}

	public void list ( ) {
//		System.out.println ( );
//		System.out.println ( "*** PipeStage.debug:...BEGIN");
		System.out.println ( "---->	NOME DO PIPESTAGE: "+sbName);
		if ( iCurrent != null) iCurrent.list ( );
		else System.out.println ( "No Instruction");
//		System.out.println ( "*** PipeStage.debug:...END");
//		System.out.println ( );
	}

//	iTargetPipe nao esta' sendo usado
	protected int iOriginalPipe, iTargetPipe;
	protected Instruction iCurrent;
	protected boolean bActive;
}
