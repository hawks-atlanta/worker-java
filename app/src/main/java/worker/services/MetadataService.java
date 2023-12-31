package worker.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.UUID;
import org.json.JSONObject;
import worker.config.Config;

public class MetadataService
{
	public static boolean fileReady (UUID fileUUID, int volume)
	{
		JSONObject body = new JSONObject ();
		body.put ("volume", String.valueOf (volume));

		String uri =
			String.format ("%s/files/ready/%s", Config.getMetadataBaseUrl (), fileUUID.toString ());

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
			System.err.println (UtilLog.format (e.toString ()));
		}

		return false;
	}

	public static class ResGetMetadata
	{
		public int volume;
		public boolean isReady = false;
	}

	public static ResGetMetadata getMetadata (String fileUUID)
	{
		ResGetMetadata r = new ResGetMetadata ();
		String uri = String.format (
			"%s/files/metadata/%s", Config.getMetadataBaseUrl (), fileUUID.toString ());

		// GET

		try {
			HttpResponse<String> res = HttpClient.newHttpClient ().send (
				HttpRequest.newBuilder ().uri (URI.create (uri)).GET ().build (),
				HttpResponse.BodyHandlers.ofString ());

			if (res.statusCode () == 200) {
				r.volume = new JSONObject (res.body ()).getInt ("volume");
				r.isReady = true;
			}
		} catch (Exception e) {
			System.err.println (UtilLog.format (e.toString ()));
		}

		return r;
	}

	public static UUID
	saveFile (UUID userUUID, UUID directoryUUID, String filetype, String filename, int filesize)
		throws Exception
	{

		JSONObject body = new JSONObject ();
		body.put ("userUUID", userUUID);
		body.put ("parentUUID", directoryUUID == null ? JSONObject.NULL : directoryUUID);
		body.put ("fileType", "archive");
		body.put ("fileName", filename);
		body.put ("fileExtension", filetype == null ? JSONObject.NULL : filetype);
		body.put ("fileSize", filesize);

		// post

		try {
			HttpResponse<String> res = HttpClient.newHttpClient ().send (
				HttpRequest.newBuilder ()
					.uri (URI.create (Config.getMetadataBaseUrl () + "/files"))
					.POST (BodyPublishers.ofString (body.toString ()))
					.build (),
				HttpResponse.BodyHandlers.ofString ());

			JSONObject resBody = new JSONObject (res.body ());
			if (res.statusCode () == 201) {
				return UUID.fromString (resBody.getString ("uuid"));
			}
		} catch (Exception e) {
			System.err.println (UtilLog.format (e.toString ()));
		}

		throw new Exception (UtilLog.format ("Couldn't save file metadata"));
	}
}
