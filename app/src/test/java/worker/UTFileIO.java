package worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import capyfile.rmi.DownloadFileArgs;
import capyfile.rmi.DownloadFileRes;
import capyfile.rmi.UploadFileArgs;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import worker.config.Config;
import worker.services.MetadataService;

@TestMethodOrder (OrderAnnotation.class) class UTFileIO
{
	static class State
	{
		public static UUID fileUUID;
		public static int fileVolume;
	}

	@BeforeEach void setup () throws Exception
	{
		try {
			TestUtil.updateEnv ("AVAILABLE_VOLUMES", "1,2,3");
		} catch (Exception e) {
			System.err.println (e);
		}
		Config.initializeFromEnv ();
	}

	@Test @Order (1) void uploadFile ()
	{
		WorkerServiceImpl server = new WorkerServiceImpl ();

		try {
			// get file uuid

			State.fileUUID = MetadataService.saveFile (
				UUID.randomUUID (), null, "txt", UUID.randomUUID ().toString (), 32);

			UploadFileArgs req = new UploadFileArgs (State.fileUUID, TestUtil.randomBytes (32));
			server.uploadFile (req);

			// wait until it's written

			Thread.sleep (2_000);
			MetadataService.ResGetMetadata resM =
				MetadataService.getMetadata (req.uuid.toString ());
			assertTrue (resM.isReady, () -> "File is ready");
			State.fileVolume = resM.volume;

			return;
		} catch (Exception e) {
			System.err.println (e);
		}
		fail ("Couldn't check file");
	}

	int copyDataFromStream (InputStream istream, byte[] bytes) throws IOException
	{
		byte[] buf = new byte[102400]; // 100KB
		int bytesPos = 0;
		int bytesRead = 0;

		while ((bytesRead = istream.read (buf)) >= 0) {
			System.arraycopy (buf, 0, bytes, bytesPos, bytesRead);
			bytesPos += bytesRead;
		}

		return bytesPos;
	}

	@Test @Order (2) void downloadFile () throws Exception
	{
		WorkerServiceImpl server = new WorkerServiceImpl ();
		InputStream istream = null;

		try {

			try {
				server.downloadFile (new DownloadFileArgs (State.fileUUID, 5));
				fail ("Should throw");
			} catch (Exception e) {
				assertTrue (e instanceof InvalidParameterException, "Specified volume not found");
			}

			try {
				server.downloadFile (new DownloadFileArgs (UUID.randomUUID (), State.fileVolume));
				fail ("Should throw");
			} catch (Exception e) {
				assertTrue (e instanceof FileNotFoundException, "File not found");
			}

			// download file normaly

			DownloadFileRes res =
				server.downloadFile (new DownloadFileArgs (State.fileUUID, State.fileVolume));

			istream = RemoteInputStreamClient.wrap (res.stream);
			byte[] bytes = new byte[(int)res.size];
			assertEquals (
				res.size, copyDataFromStream (istream, bytes), "Downloaded file of correct size");

			// force download from backup folder

			File file = new File (String.format (
				"%1$s/files/volume%2$d/%3$s", Config.getVolumeBasePath (), State.fileVolume,
				State.fileUUID.toString ()));

			Files.deleteIfExists (file.toPath ());

			res = server.downloadFile (new DownloadFileArgs (State.fileUUID, State.fileVolume));

			istream = RemoteInputStreamClient.wrap (res.stream);
			bytes = new byte[(int)res.size];
			assertEquals (
				res.size, copyDataFromStream (istream, bytes), "Downloaded file of correct size");
		} finally {
			if (istream != null)
				istream.close ();
		}
	}
}
