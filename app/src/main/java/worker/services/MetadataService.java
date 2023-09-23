package worker.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import worker.config.Config;
import worker.config.ConfigMetadata;

public class MetadataService
{
	public static boolean fileReady (String fileUUID, int volume)
	{
		JSONObject body = new JSONObject ();
		body.put ("volume", String.valueOf (volume));

		String uri = String.format (
			"%s/%s/files/ready/%s", Config.getMetadataBaseUrl (), ConfigMetadata.basePath,
			fileUUID);

		System.out.println (uri);

		// PUT request

		try {
			HttpResponse<String> res = HttpClient.newHttpClient ().send (
				HttpRequest.newBuilder ()
					.uri (URI.create (uri))
					.PUT (BodyPublishers.ofString (body.toString ()))
					.build (),
				HttpResponse.BodyHandlers.ofString ());

			if (res.statusCode () == 204) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}

		return false;
	}
}
