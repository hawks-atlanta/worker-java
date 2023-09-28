package worker;

import capyfile.rmi.DownloadFileArgs;
import capyfile.rmi.DownloadFileRes;
import capyfile.rmi.IWorkerService;
import capyfile.rmi.UploadFileArgs;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import worker.file.DownloadFile;
import worker.file.ThreadUploadFile;

public class WorkerServiceImpl implements IWorkerService
{

	public void createStubAndBind () throws RemoteException
	{

		IWorkerService stub =
			(IWorkerService)UnicastRemoteObject.exportObject ((IWorkerService)this, 0);

		// RMI registry
		Registry registry = LocateRegistry.createRegistry (1099);

		// bind remote object
		registry.rebind ("WorkerService", stub);
	}

	public void uploadFile (UploadFileArgs args) throws RemoteException
	{
		// delegate to thread

		ThreadUploadFile thread = new ThreadUploadFile (args.uuid, args.contents);
		thread.start ();

		return;
	}

	public DownloadFileRes downloadFile (DownloadFileArgs args) throws FileNotFoundException
	{
		return DownloadFile.downloadFile (args);
	}
}
