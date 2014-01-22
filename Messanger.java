import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

import javax.swing.SwingUtilities;

public class Messanger extends Frame implements ActionListener, TextListener,
		MouseListener, MouseMotionListener, Runnable {
	/////////Client list//////////
	List lst;
	Label lbl;
	////////draw/////////////
	Button r;
	Button bl;
	Button g;
	Button q;
	Button o;
	Button b;
	int lastX;
	int lastY;
	int currX;
	int currY;
	Color color;
	// //////////text//////////////////
	TextField tf;
	TextArea ta;
	Button k;
	Button p;
	DataObject in, out;
	Panel content;
	private FlowLayout layout;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	private DataObject message;
	private Socket connecting;
	private String IP;

	// constructor
	public Messanger() {
		setTitle("AMessanger");
		setSize(670, 600);
		IP = "localhost"; // for now then will ask the user later
		Frame f = new Frame("Client Window");
		layout = new FlowLayout();
		setLayout(layout);
		f.setLayout(new FlowLayout());
		p = new Button("Send");
		p.addActionListener(this);
		tf = new TextField();
		k = new Button("Disconnect");
		k.addActionListener(this);
		tf.setColumns(50);
		ta = new TextArea();
		ta.setEditable(false);
		r = new Button("Red");
		bl = new Button("Black");
		g = new Button("Green");
		q = new Button("Cyan");
		o = new Button("Orange");
		b = new Button("Blue");
		color = Color.black;
		////////client list///////////
		lst = new List(4, false);
		lbl = new Label("Client List:");
 		lst.setSize(new Dimension(300, 40));
		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showMessage("You: " + tf.getText());
				sendMessage(tf.getText());
				tf.setText("");
			}
		});
		add(tf);
		add(p);
		add(k);
		add(ta);
		//////Client list///////////
		add(lbl);
		add(lst);
		// ////draw/////
		add(r);
		add(bl);
		add(g);
		add(q);
		add(o);
		add(b);
		r.addActionListener(this);
		b.addActionListener(this);
		g.addActionListener(this);
		q.addActionListener(this);
		o.addActionListener(this);
		b.addActionListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		Scanner scan = new Scanner (System.in);
		lst.add("You");
	}

	public void start() {
	
		try {
			
			connect();
			showMessage("Welcome to AMessanger!");
			streams();
			String array[] = ta.getText().split("\\n");
	        String s = array[array.length - 1];
				if (s.substring(0,8).equalsIgnoreCase("Client 2")){
					lst.add("Client 2");}
			
		} catch (IOException ioException) {
			showMessage("\n The server is down! ");
		} finally {
			
		}
	}

	public void paint(Graphics g) {
		g.drawLine(lastX, lastY, currX, currY);
		record(currX, currY);
	}

	public void update(Graphics g) {
		g.setColor(color);
		paint(g);

	}

	public void record(int x, int y) {
		lastX = x;
		lastY = y;
	}

	public void connect() throws IOException {
		showMessage("\n Connecting...");

		connecting = new Socket(InetAddress.getByName(IP), 1999);
		System.out.println("Connected");
	}

	private void sendMessage(String actioncommand) {
		try {
			System.out.println("Send message " + actioncommand);
			DataObject message = new DataObject();
			message.setMessage(actioncommand);
			oos.writeObject(message);
		} catch (IOException ioException) {
			ta.append("\n ERROR 1: Can't send message");
		}

	}

	private void showMessage(final String string) {

		ta.append(string + "\n");
	}

	public void streams() throws IOException {
		oos = new ObjectOutputStream(connecting.getOutputStream());
		ois = new ObjectInputStream(connecting.getInputStream()); // read from
																	// server
	}

	public void closing() {
		showMessage("\n Bye!!");
		try {
			oos.close();
			ois.close();
			connecting.close();
			System.exit(0);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// for now then will ask the user later
		Messanger mf = new Messanger();
		mf.setVisible(true);
		mf.start();
		mf.run();

	}

	public void textValueChanged(TextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equalsIgnoreCase("Red"))
			color = Color.red;
		if (ae.getActionCommand().equalsIgnoreCase("Green"))
			color = Color.green;
		if (ae.getActionCommand().equalsIgnoreCase("Blue"))
			color = Color.blue;
		if (ae.getActionCommand().equalsIgnoreCase("Orange"))
			color = Color.orange;
		if (ae.getActionCommand().equalsIgnoreCase("Cyan"))
			color = Color.CYAN;
		else if (ae.getActionCommand().equalsIgnoreCase("Black"))
			color = Color.black;
		
		if (ae.getSource() == p) {
			System.out.println("Send pressed");
			showMessage("You: " + tf.getText());
			sendMessage(tf.getText());
			tf.setText("");
		}if(ae.getSource() == k){
			try {
				showMessage("You: Bye Everyone!");
				sendMessage("Bye Everyone!");
				oos.close();
				ois.close();
				connecting.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}

	public void mouseDragged(MouseEvent e) {
		currX = e.getX();
		currY = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		record(e.getX(), e.getY());

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		record(e.getX(), e.getY());

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		while (true) {
			try {
				message = (DataObject) (ois.readObject());
				if (message != null) {
					System.out.println("Received: " + message.message);
					showMessage(message.message);
				}
			} catch (Exception e) {

			}
		}
	}
}
