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
public class SimpleJavaClient {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("127.0.0.1", 9999);
			InputStream i = s.getInputStream();
			OutputStream o = s.getOutputStream();
			String str;
			do {
				byte[] line = new byte[100];
				System.in.read(line);
				o.write(line);
				i.read(line);
				str = new String(line);
				System.out.println(str.trim());
			} while ( !str.trim().equals("bye") );
			s.close();
		}
		catch (Exception err) {
			System.err.println(err);
		}
	}
}
