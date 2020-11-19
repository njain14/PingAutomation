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
}
