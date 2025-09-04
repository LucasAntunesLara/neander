package platform;

import java.io.File;

public class Platform {

	public static char getSeparatorPath ( ) {
		return File.separatorChar;
	}
	
	public static String treatPathNames ( String sPathName) {
		String sTmp = new String ( sPathName);
		
		//System.out.println ( "PathName (before): "+ sTmp);

		if ( sTmp.indexOf ( "\\") != - 1) {
			if ( getSeparatorPath ( ) != '\\') {
				sTmp = sTmp.replace ( '\\', getSeparatorPath ( ));
			}
		} else if ( sTmp.indexOf ( "/") != - 1) {
			if ( getSeparatorPath ( ) != '/') {
				sTmp = sTmp.replace ( '/', getSeparatorPath ( ));
			}			
		} else if ( sTmp.indexOf ( ",") != - 1) {
			if ( getSeparatorPath ( ) != ',') {
				sTmp = sTmp.replace ( ',', getSeparatorPath ( ));
			}				
		}
		
		//System.out.println ( "PathName (after): "+ sTmp);
		
		return sTmp;
	}

}

