package montador;

import java.util.Vector;

import primitive.Primitive;

//Classe para guardar as informaoes sobre labels de instrues
class Label {
	String nameLabel;
	int normalAddress;
	int risaAddress;
	boolean isNormal;
	
	public Label ( String name, int normalAddr) {
		nameLabel = name;
		normalAddress = normalAddr;
		isNormal = true;
	}
	
	public void list ( ) {
System.out.println ( "--> nome= "+nameLabel+", normalAddr = "+normalAddress+", risaAddr = "+risaAddress);
	}
}

public class ListOfLabels {

	ListOfLabels ( ) {
		vArrayElems = new Vector ( );
	}
	
	public void add ( String name, int normalAddr) {
		Label label = new Label ( name, normalAddr);
		vArrayElems.add ( label);
	}
	
	private void setRISAAdressForAloneLabels ( int naddr, int raddr, boolean isNormal) {
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.normalAddress == naddr) {
				lAux.risaAddress = raddr;
				lAux.isNormal = isNormal;
			}
		}
	}
	
	public void setRISAAddress ( String name, int addr, boolean isNormal)
	{
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
//System.out.println ( "name = "+ name);
			if ( lAux.nameLabel.compareToIgnoreCase( name) == 0) {
				lAux.risaAddress = addr;
				lAux.isNormal = isNormal;
				setRISAAdressForAloneLabels( lAux.normalAddress, addr, isNormal);
				break;
			}
		}	
	}

	public int getNormalAddress ( String name)
	{
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.nameLabel.compareToIgnoreCase( name) == 0) return ( lAux.normalAddress);
		}	
		
		return ( - 1);			
	}
	
	public int getRISAAddress ( String name)
	{
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.nameLabel.compareToIgnoreCase( name) == 0) return ( lAux.risaAddress);
		}	
		
		return ( - 1);			
	}
	
	public boolean getConditionNormalOrReduced ( String name)
	{
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.nameLabel.compareToIgnoreCase( name) == 0) return ( lAux.isNormal);
		}	
		
		return ( false);			
	}

	public boolean hasLabelAtNormalAddress ( int addr)
	{
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.normalAddress == addr) return ( true);
		}	
		
		return ( false);			
	}
	
	public String getLabelAtNormalAddress ( int addr)
	{
		int i;
		Label lAux;
		String sTmp = null;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.normalAddress == addr) return ( lAux.nameLabel);
		}	
		
		return ( sTmp);			
	}

	public int getNumberOfLabelsAtNormalAddress ( int addr)
	{
		int i;
		Label lAux;
		int cont = 0;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			if ( lAux.normalAddress == addr) cont ++;
		}	
		
//System.out.println ( "cont="+cont);
		return ( cont);			
	}
	
	public void list ( )
	{
		int i;
		Label lAux;

		for ( i = 0; i < vArrayElems.size ( ); i ++) {
			lAux = ( Label) vArrayElems.elementAt ( i);
			lAux.list();
		}	
	}
	
	protected Vector vArrayElems;
	// protected int iNelems;
}
