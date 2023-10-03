package worker;

import worker.config.Config;
import worker.services.UtilLog;

public class App
{
	public static void main (String[] args)
	{
		try {
			Config.initializeFromEnv ();

			// serve RMI
			WorkerServiceImpl server = new WorkerServiceImpl ();
			server.createStubAndBind ();

			System.out.println (UtilLog.format ("Worker: Serving RMI on 0.0.0.0:1099"));
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
}
