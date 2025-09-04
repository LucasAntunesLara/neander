/*
 * Created on 06/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package testes;
import java.net.*;
import java.io.*;
/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleJavaServer {

	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(9999);
			String str;
			while (true) {
				Socket c = s.accept();
				InputStream i = c.getInputStream();
				OutputStream o = c.getOutputStream();
				do {
					byte[] line = new byte[100];
					i.read(line);
					o.write(new String ("sockets test real").getBytes());
					str = new String(line);
				} while ( !str.trim().equals("bye") );
				if ( str.trim().equals("bye")) {
					System.out.println ( "Nao sirvo mais ninguem...");
					break;
				}
				c.close();
			}
		}
		catch (Exception err){
		   System.err.println(err);
		}
	}
}
