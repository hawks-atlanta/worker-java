package worker;

import worker.config.Config;

public class App
{
	public static void main (String[] args)
	{
		Config.initializeFromEnv ();
		System.out.println ("Worker: Serving RMI");

		try {
			// serve RMI
			WorkerServiceImpl server = new WorkerServiceImpl ();
			server.createStubAndBind ();

		} catch (Exception e) {
			System.out.println (e);
		}
	}
}
