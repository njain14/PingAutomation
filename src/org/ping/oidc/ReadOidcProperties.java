package org.ping.oidc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadOidcProperties {
	String result = "";
	InputStream inputStream;
	Properties prop;

	public Properties getPropValues() throws IOException {

		try {
			prop = new Properties();
			String propFileName = "openidconnect.properties";

			inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return prop;
	}
}