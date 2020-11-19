package org.ping.oidc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Authorization {

	public static boolean authorization(String idToken) throws JSONException {
		JSONObject jsonObject = DecodeJwt.testDecodeJWT(idToken);
		JSONArray arr = jsonObject.getJSONArray("AD_Groups");
		for (int i = 0; i < arr.length(); i++) {
			if (arr.get(i).equals("CN=IAM_WAM_SSO_AdminConsole_Access,CN=Users,DC=ms,DC=ds,DC=uhc,DC=com")) {
				return true;
			}
		}
		return false;
	}
}
