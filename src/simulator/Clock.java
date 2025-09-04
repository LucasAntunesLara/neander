package simulator;


public class Clock {

	public static void setInitialTime ( float parTime) {
		fTime = parTime;
	}

	public static void resetTime ( ) {
		fTime = 0;
	}

	public static float getTime ( ) {
		return ( fTime);
	}

	public static float advance ( ) {
		fTime ++;
		
		return ( fTime);
	}

	private static float fTime = 0.0F;
}
