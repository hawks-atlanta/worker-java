package worker;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import capyfile.rmi.UploadFileArgs;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import worker.config.Config;
import worker.services.MetadataService;

class UTFileIO
{
	@BeforeEach void setup ()
	{
		try {
			TestUtil.updateEnv ("AVAILABLE_VOLUMES", "1,2,3");
		} catch (Exception e) {
			System.err.println (e);
		}
		Config.initializeFromEnv ();
	}

	@Test void uploadFile ()
	{
		WorkerServiceImpl server = new WorkerServiceImpl ();

		try {
			// get file uuid

			UUID uuid = MetadataService.saveFile (
				UUID.randomUUID (), null, "txt", UUID.randomUUID ().toString (), 32);

			UploadFileArgs req = new UploadFileArgs (uuid, TestUtil.randomBytes (32));
			server.uploadFile (req);

			// wait until it's written

			Thread.sleep (2_000);
			assertTrue (MetadataService.checkReady (req.uuid.toString ()), () -> "File is ready");
			return;
		} catch (Exception e) {
			System.err.println (e);
		}
		fail ("Couldn't check file");
	}
}
