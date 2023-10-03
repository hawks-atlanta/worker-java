package worker;

import worker.config.Config;

public class App
{
	public static void main (String[] args)
	{
		try {
			Config.initializeFromEnv ();
			System.out.println ("Worker: Serving RMI");

			// serve RMI
			WorkerServiceImpl server = new WorkerServiceImpl ();
			server.createStubAndBind ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
}
