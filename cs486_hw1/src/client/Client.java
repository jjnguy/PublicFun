package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

	private String host;
	private int port;

	public Client(){
		this("localhost", 4860);
	}
	
	public Client(String host, int port) {
		this.port = port;
		this.host = host;
	}

	@Override
	public void run() {
		Socket server = null;
		try {
			server = new Socket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			handleConnection(server);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleConnection(Socket s) throws IOException {
		PrintStream serverWriter = new PrintStream(s.getOutputStream());
		Scanner serverReader = new Scanner(s.getInputStream());

		System.out.println("Enter the directory that you would like to search:");
		Scanner stdin = new Scanner(System.in);
		String directory = stdin.nextLine();
		serverWriter.println(directory);

		System.out.println(serverReader.nextLine());

		String requestedFile = stdin.nextLine();
		serverWriter.println("GET " + requestedFile);
		
		while(serverReader.hasNextLine()){
			System.out.println(serverReader.nextLine());
		}
		
		System.out.print("Do you want to quit?(Y/N): ");
		String response = stdin.nextLine();
		if (response.trim().equalsIgnoreCase("y"))
			serverWriter.println("QUIT");

	}
	
	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
