package org.ping.migrate.pf.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;

public class ConnectionStream {
	
	public static StringBuffer getConnectionStream(HttpsURLConnection con) throws IOException {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = inputReader.readLine()) != null) {
			response.append(inputLine);
		}
		inputReader.close();
		return response;
	}

}
