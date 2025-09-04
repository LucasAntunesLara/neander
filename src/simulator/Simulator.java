package simulator;

import message.Msg;
import message.MsgInterpreter;

public class Simulator {

	public Simulator ( ) {
		ldeLista = new EventList ( );
	}

	public EventList getListaDeEventos ( ) {
		return ( ldeLista);
	}

	public void advanceTime ( ) {
		Clock.advance ( );		
	}

	public void resetTime ( ) {
		Clock.resetTime();		
	}
	
	public float getTime ( ) {
		return ( Clock.getTime ( ));	
	}

	public void Simulate ( Object parInt) {
		Event evAux;
		Msg msgAux;
		int iNeventos = 0;
		boolean bEnd = false;
		MsgInterpreter miAux = ( MsgInterpreter) parInt;

		// Reseta informaao de uso de circuitos e barramentos e alteraoes em conteudos de componentes
		miAux.resetCircuitAndBusAndContents ( );
		
		while ( true) {
			evAux = ldeLista.remove ( Clock.getTime ( ));
			if ( evAux != null) {
				msgAux = evAux.getMsg ( );
				miAux.execute ( msgAux);
				iNeventos ++;
			} else {
				//miAux.execute ( );
				if ( miAux.mustStop()) return;
				bEnd = miAux.behavior ( );
				if ( bEnd == true) {
					if ( bEndProgram == false) {
						int iTime = ( int) this.getTime ( );
						System.out.println ( "NADA A SIMULAR no ciclo "+iTime+"!!!");
						bEndProgram = true;
					}
					return;
				}
				if ( iNeventos != 0)
					System.out.println ( "SIMULADOR: PROCESSOU EVENTOS PARA ESTE TEMPO!!!");
				break;
			}
		}
	}
	
	public double getTimeStatistics ( Object parInt) {
		Event evAux;
		Msg msgAux;
		int iNeventos = 0;
		boolean bEnd = false;
		MsgInterpreter miAux = ( MsgInterpreter) parInt;
		// Para calculo do tempo de processamento
		long lMSecsB = 0, lMSecsE = 0, lMSecsDelta;
		double deltaTime = 0;

		// Reseta informaao de uso de circuitos e barramentos e alteraoes em conteudos de componentes
		miAux.resetCircuitAndBusAndContents ( );
		
		evAux = ldeLista.remove ( Clock.getTime ( ));
		if ( evAux != null) {
			msgAux = evAux.getMsg ( );
			miAux.execute ( msgAux);
			iNeventos ++;
		}
		if ( iNeventos != 0)
			System.out.println ( "SIMULADOR: PROCESSOU EVENTOS PARA ESTE TEMPO!!!");
		bEnd = false;
		lMSecsB = System.currentTimeMillis ( );
System.out.println ( "Initial time: "+lMSecsB);	
lMSecsDelta = lMSecsB + 60000;
		while ( ! bEnd) {
			if ( miAux.mustStop()) break;
			bEnd = miAux.behavior ( );
			if ( System.currentTimeMillis ( ) > lMSecsDelta) {
				System.out.print ( " . ");
				lMSecsDelta += 60000;
			}
		}
		lMSecsE = System.currentTimeMillis ( );
System.out.println ( "End time: "+lMSecsE);
		deltaTime = (double) (lMSecsE - lMSecsB) / 1000;
System.out.println ( "Elapsed time: "+ deltaTime);
		
		return ( deltaTime);
	}
	
	public void simultateToTheEnd(Object parInt){
		Event evAux;
		Msg msgAux;
		int iNeventos = 0;
		boolean bEnd = false;
		MsgInterpreter miAux = ( MsgInterpreter) parInt;
		// Para calculo do tempo de processamento
		long lMSecsB = 0, lMSecsE = 0;
		double deltaTime = 0;

		// Reseta informaao de uso de circuitos e barramentos e alteraoes em conteudos de componentes
		miAux.resetCircuitAndBusAndContents ( );
		
		evAux = ldeLista.remove ( Clock.getTime ( ));
		if ( evAux != null) {
			msgAux = evAux.getMsg ( );
			miAux.execute ( msgAux);
			iNeventos ++;
		}
		if ( iNeventos != 0)
			System.out.println ( "SIMULADOR: PROCESSOU EVENTOS PARA ESTE TEMPO!!!");
		bEnd = false;
		lMSecsB = System.currentTimeMillis ( );
		System.out.println ( "Initial time: "+lMSecsB);	
		while ( ! bEnd) {
			if ( miAux.mustStop()) break;
			bEnd = miAux.behavior ( );
		}
		lMSecsE = System.currentTimeMillis ( );
		System.out.println ( "End time: "+lMSecsE);
		deltaTime = (double) (lMSecsE - lMSecsB) / 1000;
		System.out.println ( "Elapsed time: "+ deltaTime);
	}
	
	public void Initializations ( Object parInt) {
		Event evAux;
		Msg msgAux;
		int iNeventos = 0;
		MsgInterpreter miAux = ( MsgInterpreter) parInt;

		bEndProgram = false;
		while ( true) {
			evAux = ldeLista.remove ( Clock.getTime ( ));
			if ( evAux != null) {
				msgAux = evAux.getMsg ( );
				miAux.execute ( msgAux);
				iNeventos ++;
			} else {
				if ( iNeventos != 0);
//System.out.println ( "SIMULADOR: PROCESSOU EVENTOS NA INICIALIZACAO!!!");
				break;
			}
		}
		
		// Reseta informaao de uso de circuitos e barramentos
		miAux.resetCircuitAndBusAndContents ( );
	}

	public void Interactive ( Object parInt) {
		Event evAux;
		Msg msgAux;
		int iNeventos = 0;
		MsgInterpreter miAux = ( MsgInterpreter) parInt;

		while ( true) {
			evAux = ldeLista.remove ( Clock.getTime ( ));
			if ( evAux != null) {
				msgAux = evAux.getMsg ( );
				miAux.execute ( msgAux);
				iNeventos ++;
			} else {
				if ( iNeventos != 0);
//System.out.println ( "SIMULADOR: PROCESSOU EVENTOS GERADOS INTERATIVAMENTE!!!");
				break;
			}
		}
		
		// Reseta informaao de uso de circuitos e barramentos
		miAux.resetCircuitAndBusAndContents ( );
	}

	public void debug ( ) {
		System.out.println ( "*** Simulador.debug:...BEGIN");
		System.out.println ( "O tempo e: "+Clock.getTime ( ));
		System.out.println ( "H os seguintes eventos na fila: ");
		ldeLista.debug ( );
		System.out.println ( "*** Simulador.debug:...END");
		System.out.println ( );
	}

	EventList ldeLista;
	boolean bEndProgram = false;
}
