package org.ping.oidc;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DecodeJwt {

	public static JSONObject testDecodeJWT(String jwtToken) throws JSONException {
		String[] split_string = jwtToken.split("\\.");
		String base64EncodedBody = split_string[1];
		Base64 base64Url = new Base64(true);
		String body = new String(base64Url.decode(base64EncodedBody));
		JSONObject jsonResponse = new JSONObject(new String(body));
		return jsonResponse;
	}

	public static boolean authorization(String idToken) throws JSONException {
		JSONObject jsonObject = testDecodeJWT(idToken);
		JSONArray arr = jsonObject.getJSONArray("AD_Groups");
		for (int i = 0; i < arr.length(); i++) {
			if (arr.get(i).equals("CN=IAM_WAM_SSO_AdminConsole_Access,CN=Users,DC=ms,DC=ds,DC=uhc,DC=com")) {
				return true;
			}
		}
		return false;
	}

}
