package capyfile.rmi;

import capyfile.rmi.interfaces.File;
import capyfile.rmi.interfaces.FileDownload;
import capyfile.rmi.interfaces.IWorkerService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class WorkerServiceImpl implements IWorkerService
{

	public void createStubAndBind () throws RemoteException
	{

		IWorkerService stub =
			(IWorkerService)UnicastRemoteObject.exportObject ((IWorkerService)this, 0);

		// RMI registry
		Registry registry = LocateRegistry.createRegistry (1900);

		// bind remote object
		registry.rebind ("WorkerService", stub);
	}

	public void uploadFile (File upload) throws RemoteException { return; }

	public File downloadFile (FileDownload download) throws RemoteException
	{
		File file = new File ("----", null);
		return file;
	}
}
