package worker;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import worker.config.Config;

class UTConfig
{
	@Test void configFromEnv ()
	{

		try {
			Config.initializeFromEnv ();
		} catch (Exception e) {
			fail ("Couldn't initialize vars");
		}

		try {
			TestUtil.updateEnv ("AVAILABLE_VOLUMES", ",");
			Config.initializeFromEnv ();
			fail ();
		} catch (Exception e) {
			assertTrue (e instanceof RuntimeException, "Should throw");
		}

		try {
			TestUtil.updateEnv ("AVAILABLE_VOLUMES", "");
			Config.initializeFromEnv ();
			fail ();
		} catch (Exception e) {
			assertTrue (e instanceof RuntimeException, "Should throw");
		}
	}
}
