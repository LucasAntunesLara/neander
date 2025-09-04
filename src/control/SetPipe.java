package control;

import primitive.SetPrimitive;

public class SetPipe extends SetPrimitive {

	public int translate ( int parPSorg) {
		ExecutionStage psAux;
		int psTarget = - 1;
		int i;
		
		for ( i = 0; i < this.getNelems ( ); i ++) {
			psAux = ( ExecutionStage) this.traverse ( i);
			if ( psAux.iOriginalPipe == parPSorg) return psAux.iTargetPipe;
		}
		
		return psTarget;
	}
}
