package worker;

import capyfile.rmi.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class App
{
	public static void main (String[] args)
	{
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
