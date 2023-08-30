package capyfile.rmi;

import capyfile.rmi.interfaces.Message;
import capyfile.rmi.interfaces.MessengerService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MessengerServiceImpl implements MessengerService
{

	public void createStubAndBind () throws RemoteException
	{

		MessengerService stub =
			(MessengerService)UnicastRemoteObject.exportObject ((MessengerService)this, 0);

		// RMI registry
		Registry registry = LocateRegistry.createRegistry (1900);

		// bind remote object
		registry.rebind ("MessengerService", stub);
	}

	public Message sendMessage (Message clientMessage) throws RemoteException
	{

		Message serverMessage = new Message ("Hi from RMI");
		return serverMessage;
	}
}
