import java.io.*;

public class objectFile {
	public static void main(String[] args) {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream("objfile.txt"));

			DataObject d = new DataObject();
			d.setMessage("Some thing....");
			oos.writeObject(d);
			oos.close();
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					"objfile.txt"));
			DataObject d2 = (DataObject) ois.readObject();

			System.out.println(d2.getMessage());

			ois.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

class DataObject implements Serializable {
	static final long serialVersionUID = 1;
	String message;

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
