package worker;

import capyfile.rmi.DownloadFileArgs;
import capyfile.rmi.File;
import capyfile.rmi.IWorkerService;
import capyfile.rmi.UploadFileArgs;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
		// send to a new thread

		ThreadUploadFile thread = new ThreadUploadFile (args.uuid, args.contents);
		thread.start ();

		return;
	}

	public File downloadFile (DownloadFileArgs args) throws RemoteException
	{
		File file = new File ("----", null);
		return file;
	}
}
