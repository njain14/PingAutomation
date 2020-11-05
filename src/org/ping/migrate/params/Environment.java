package org.ping.migrate.params;

public class Environment {

	public static String getPingAccEnvironment(String environment, String region) {
		String env = environment;
		String reg = region;

		switch (env) {
		case ("dev"):
			switch (reg) {
			case ("R1"):
				return "https://apvrd24148.uhc.com:9000";
			case ("R2"):
				return "https://apvrd24207.uhc.com:9000";
			case ("R3"):
				return "https://apvrd24157.uhc.com:9000";
			}
			break;
			
		case ("sandbox"):
			switch (reg) {
			case ("R1"):
				return "https://apvrd20770.uhc.com:9000";
			case ("R2"):
				return "https://apvrd20770.uhc.com:9000";
			case ("R3"):
				return "https://apvrd20770.uhc.com:9000";
			}
			break;

		case ("stage"):
			switch (reg) {
			case ("R1"):
				return "https://apvrs24290.uhc.com:9000";
			case ("R2"):
				return "https://apvrs24331.uhc.com:9000";
			case ("R3"):
				return "https://apvrs24334.uhc.com:9000";
			}
			break;

		case ("prod"):
			switch (reg) {
			case ("R1"):
				return "https://authgateway1.entiam.uhg.com";
			case ("R2"):
				return "https://authgateway2.entiam.uhg.com";
			case ("R3"):
				return "https://authgateway3.entiam.uhg.com";
			}
			break;
		}
		return "No matching Environment and Region found";

	}

	public static String getPingFedEnvironment(String Environment) {

		switch (Environment) {

			case ("sandbox"):
				return "https://apvrd20763.uhc.com:9999";				
	
			case ("dev"):
				return "https://apvrd24098.uhc.com:9999";
				
			case ("stage"):
				return "https://apvrs24245.uhc.com:9999";
			}
				return "https://apvrp24350.uhc.com:9999";
	}
}