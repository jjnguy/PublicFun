package client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

	private String host;
	private int port;

	public Client() {
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
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			handleConnection(server);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleConnection(Socket s) throws IOException {
		PrintStream serverWriter = new PrintStream(s.getOutputStream());
		BufferedInputStream serverReader = new BufferedInputStream(s.getInputStream());

		System.out.println("Enter the directory that you would like to search:");
		Scanner stdin = new Scanner(System.in);
		String directory = stdin.nextLine();
		serverWriter.println(directory);
		serverWriter.flush();

		byte[] buffer = new byte[32];
		int cols = 0;
		while (serverReader.read(buffer) > 0) {
			System.out.print(new String(buffer).trim());
			cols += buffer.length;
			if (cols > 100) {
				System.out.println();
				cols = 0;
			}
		}
		System.out.println();

		System.out.println("Select file to download:");
		String requestedFile = stdin.nextLine();
		serverWriter.println("GET " + requestedFile);
		serverWriter.flush();

		while (serverReader.read(buffer) > 0) {
			System.out.print(new String(buffer));
		}
		System.out.println();

		System.out.print("Do you want to quit?(Y/N): ");
		String response = stdin.nextLine();
		if (response.trim().equalsIgnoreCase("y"))
			serverWriter.println("QUIT");
		serverWriter.flush();
	}

	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
