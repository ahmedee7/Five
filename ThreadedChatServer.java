import java.io.*;
import java.net.*;
import java.util.*;

public class ThreadedChatServer {

	public static void main(String[] args) {
		try {

			ArrayList<ThreadedChatHandler> handlers = new ArrayList<ThreadedChatHandler>();

			ServerSocket ss = new ServerSocket(1999);
			System.out.println("Server is waiting for clients");
			for (;;) {
				Socket incoming = ss.accept();
				String clientName = "Client "+(handlers.size() + 1);
				new ThreadedChatHandler(clientName, incoming, handlers).start();
		    System.out.println(clientName + " from " + incoming.getInetAddress().getHostName()
            + " has connected");
		    
		   
			}

		} catch (Exception e) {
			System.out.println("ERROR 123");
			System.out.println(e.getMessage());
		}
	}
}

class ThreadedChatHandler extends Thread {

	public ThreadedChatHandler(String name, Socket s, ArrayList<ThreadedChatHandler> list) {
		this.s = s;
		this.list = list;
		this.list.add(this);
		this.name = name;
		try {
      ois = new ObjectInputStream(s.getInputStream());
      oos = new ObjectOutputStream(s.getOutputStream());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}

	public synchronized void broadcast(DataObject d) throws IOException {
	  System.out.println("Broadcasting\t" + d.message);
	  d.setMessage(name + ":\t" + d.message);
		for (ThreadedChatHandler handler : list) {
		  if (!handler.name.equalsIgnoreCase(name))
		    handler.oos.writeObject(d);
		}
	}

	public void run() {
		try {
			while (true) {
			  DataObject input = (DataObject) ois.readObject();
				broadcast(input);
			}
		} catch (Exception e) {
		  System.out.println(e.getMessage());
		} finally {
			try {
				System.out.println(name + " got disconnected!");
				list.remove(this);
				ois.close();
				oos.close();
				s.close();
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
	}

	DataObject obj;
	Socket s;
	ArrayList<ThreadedChatHandler> list;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	String name;
}