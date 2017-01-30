package data;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONObject;

public class RemoteMicroAnnotator {

	static HttpClient client = new HttpClient();
	static String host = "http://rtw.ml.cmu.edu/rtw/api/mod2015";

	public static String sample(String primeText, String host) {

		String generatedResponse = "";
		if (primeText == null || primeText.length() == 0) {
			return generatedResponse;
		}

		try {
			JSONObject request = new JSONObject();
			request.put("text", primeText);

			StringRequestEntity requestEntity = new StringRequestEntity(request.toString(), "application/json", "UTF-8");
			PostMethod postMethod = new PostMethod(host);
			postMethod.setRequestEntity(requestEntity);
			
			try {
				int statusCode = client.executeMethod(postMethod);
				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + postMethod.getStatusLine());
				}
				byte[] responseBody = postMethod.getResponseBody();
				System.out.println(new String(responseBody));
				generatedResponse = new String(responseBody);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Exception: " + e.getMessage() + " on URL: " + host);
			} finally {
				postMethod.releaseConnection();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("\nReturning annotated document");
		return generatedResponse;
	}

	public static void main(String[] args) throws IOException {
		System.out.println("RS: " + RemoteMicroAnnotator.sample("Barack Obama thinks it is great to visit New York City", host));
	}
}
