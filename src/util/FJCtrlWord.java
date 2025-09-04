package util;

public class FJCtrlWord  {

	public FJCtrlWord ( 	String parCWord) {
		System.out.println ( "PC_ENABLE_CTRL   = "+parCWord.substring ( 30, FCWW));
		System.out.println ( "SP_ENABLE_CTRL   = "+parCWord.substring ( 29, 30));
		System.out.println ( "MAR_ENABLE_CTRL  = "+parCWord.substring ( 28, 29));
		System.out.println ( "VARS_ENABLE_CTRL = "+parCWord.substring ( 27, 28));
		System.out.println ( "FRM_ENABLE_CTRL  = "+parCWord.substring ( 26, 27));
		System.out.println ( "A_ENABLE_CTRL    = "+parCWord.substring ( 25, 26));
		System.out.println ( "B_ENABLE_CTRL    = "+parCWord.substring ( 24, 25));
		System.out.println ( "A_SRC_CTRL       = "+parCWord.substring ( 22, 24));
		System.out.println ( "B_SRC_CTRL       = "+parCWord.substring ( 20, 22));
		System.out.println ( "ADDR_MUX_CTRL    = "+parCWord.substring ( 18, 20));
		System.out.println ( "PC_MUX_CTRL      = "+parCWord.substring ( 17, 18));
		System.out.println ( "ICONST_CTRL      = "+parCWord.substring ( 14, 17));
		System.out.println ( "MUX_FLAG_CTRL    = "+parCWord.substring ( 11, 14));
		System.out.println ( "ADDR_ALU_CTRL    = "+parCWord.substring ( 10, 11));
		System.out.println ( "PC_ALU_CTRL      = "+parCWord.substring ( 9, 10));
		System.out.println ( "ALU_FUNC_CTRL    = "+parCWord.substring ( 5, 9));
		System.out.println ( "IMMED_CTRL       = "+parCWord.substring ( 3, 5));
		System.out.println ( "MEM_RW           = "+parCWord.substring ( 2, 3));
		System.out.println ( "INTA             = "+parCWord.substring ( 1, 2));
		System.out.println ( "IR_ENABLE        = "+parCWord.substring ( 0, 1));

	}

	public final int FCWW = 31;	// FJ_CTRL_WORD_WIDTH
}
