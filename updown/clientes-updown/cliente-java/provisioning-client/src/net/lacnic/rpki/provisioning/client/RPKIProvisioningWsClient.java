package net.lacnic.rpki.provisioning.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import net.lacnic.rpki.provisioning.utils.ChildRequest;

public class RPKIProvisioningWsClient {

	private static final String URL_UPDOWN = "https://rpki-demo.lacnic.net/provisioning";
	private static final String RPKISUGAR_AUTH_TOKEN = "apikey";

	public RPKIProvisioningWsClient() {
	}

	public static String childRequest(String childRequest) {
		try {
			return mandarPostYRecibirRespuestaChild(URL_UPDOWN + "/rpki-setup", childRequest);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] updown(byte[] cms) {
		try {
			return mandarPostYRecibirRespuesta(URL_UPDOWN + "/rpki-updown", cms);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getAuthToken() {
		return ChildRequest.TOKEN;
	}

	private static String mandarPostYRecibirRespuestaChild(String url, String child_Request) {
		try {
			HttpClient cliente = ProvisioningHttpClient.getNewHttpClient();
			HttpPost request = new HttpPost(url);
			if (child_Request != null)
				request.setEntity(new ByteArrayEntity(child_Request.getBytes("UTF-8")));
			request.setHeader(RPKISUGAR_AUTH_TOKEN, getAuthToken());
			HttpResponse response = cliente.execute(request);
			return IOUtils.toString(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] mandarPostYRecibirRespuesta(String url, byte[] cms) {
		try {
			HttpClient cliente = ProvisioningHttpClient.getNewHttpClient();
			HttpPost request = new HttpPost(url);
			if (cms != null)
				request.setEntity(new ByteArrayEntity(cms));
			request.setHeader("Content-Type", "application/rpki-updown");
			HttpResponse response = cliente.execute(request);
			return IOUtils.toByteArray(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
