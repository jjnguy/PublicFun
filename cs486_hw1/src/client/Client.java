package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
		InputStream serverReader = s.getInputStream();

		System.out.println("Enter the directory that you would like to search:");
		Scanner stdin = new Scanner(System.in);
		String directory = stdin.nextLine();
		serverWriter.println(directory);
		serverWriter.flush();

		byte[] buffer = new byte[32 * 1024];
		int cols = 0;
		int bytesRead;
		if((bytesRead = serverReader.read(buffer, 0 , buffer.length)) > 0) {
			System.out.print(new String(buffer, 0, bytesRead).trim());
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
		System.err.println("Sent file request: GET " + requestedFile);
		
		FileOutputStream fout = new FileOutputStream(new File("C:\\outputloc.txt"));
		
		bytesRead = 0;
		if ((bytesRead = serverReader.read(buffer, 0, buffer.length)) > 0) {
			fout.write(buffer, 0 , bytesRead);
		}
		
		fout.flush();
		fout.close();

		System.out.print("Do you want to quit?(Y/N): ");
		String response = stdin.nextLine();
		if (response.trim().equalsIgnoreCase("y"))
			serverWriter.println("QUIT");
		serverWriter.flush();
		s.close();
	}

	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
