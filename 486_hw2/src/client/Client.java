package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * A file transfer client for cs486
 * 
 * @author Justin
 */
public class Client {

    /**
     * Byte representing that we have reached the end of a file
     */
    public static final byte EOF = 4;

    /**
     * The connection to the server
     */
    private Socket connection;

    /**
     * Stream for sending data to a server
     */
    private OutputStream serverOut;
    /**
     * Stream for reading data from a sever
     */
    private InputStream serverIn;

    /**
     * The current directory the client is looking at
     */
    private String currentDirectory;

    /**
     * The current list of files in the current directory
     */
    private String[] listOfFiles;

    /**
     * Connects to a file server host
     * 
     * @param host
     *            the address of the host
     * @param port
     *            the port of the host
     * @throws UnknownHostException
     *             if the host cannot be found
     * @throws IOException
     *             if there is an error retrieving the streams from the connection
     */
    public void connect(String host, int port) throws UnknownHostException, IOException {
        connection = new Socket(host, port);
        serverOut = connection.getOutputStream();
        serverIn = connection.getInputStream();
    }

    /**
     * Sends a request to the server to change the current directory
     * 
     * @param newDirectory
     *            the path to the new directory
     * @throws IOException
     *             if there is an error in communication
     */
    public void changeDirectory(String newDirectory) throws IOException {
        if (newDirectory == null)
            return;
        String dirList = readDirectoryListingFromServer(newDirectory);
        listOfFiles = dirList.split("\\s+");
    }

    /**
     * Deals with creating request and parsing output of a directory listing request
     * 
     * @param newDirectory
     *            the path to the new directory on the server
     * @return the space delimited list of files in the requested directory
     * @throws IOException
     *             if there is an error in communication
     */
    private String readDirectoryListingFromServer(String newDirectory) throws IOException {
        serverOut.write(newDirectory.getBytes());
        serverOut.flush();
        currentDirectory = newDirectory.endsWith(File.separator) ? newDirectory : newDirectory
                + File.separator;
        byte[] buffer = new byte[1024];
        int bytesRead;
        System.out.print("Reading directory listing from server...");
        String dirList = "";
        while ((bytesRead = serverIn.read(buffer)) > 0) {
            dirList += new String(buffer, 0, bytesRead);
            System.out.println(dirList);
            if (dirList.charAt(dirList.length() - 1) == EOF)
                break;
        }
        System.out.println("Done!");
        return dirList.substring(0, dirList.length() - 1);
    }

    /**
     * @return a File[] of all of the files in the current directory
     */
    public File[] listFiles() {
        if (listOfFiles == null)
            throw new IllegalArgumentException("You must first choose a directory");
        List<File> ret = new ArrayList<File>(listOfFiles.length);
        for (String s : listOfFiles) {
            ret.add(new File(currentDirectory + s));
        }
        return ret.toArray(new File[ret.size()]);
    }

    /**
     * Performs a GET request on a server, and saves the file in the requested location
     * 
     * @param fullPath
     *            the path to the requested file
     * @param location
     *            the location to save the downloaded file
     * @throws IOException
     *             if there is an error in communication
     */
    public void downloadFile(String fullPath, File location) throws IOException {
        File reguestedFile = new File(fullPath);
        // create the request string
        String getReq = "GET " + reguestedFile.getName() + ((char) EOF);
        serverOut.write(getReq.getBytes());
        serverOut.flush();
        // create a stram to save the file to
        FileOutputStream fout = new FileOutputStream(location);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = serverIn.read(buffer)) > 0) {
            if (buffer[bytesRead - 1] == EOF) {
                fout.write(buffer, 0, bytesRead - 1);
                break;
            } else {
                fout.write(buffer, 0, bytesRead);
            }
        }
        fout.close();
    }

    /**
     * Sends a QUIT request to the server
     * 
     * @throws IOException
     *             if there is an error communicating
     */
    public void disconnect() throws IOException {
        serverOut.write(("QUIT" + (char) EOF).getBytes());
        connection.close();
        connection = null;
    }

    /**
     * Getter for the current directory
     * 
     * @return
     */
    public String getDirectory() {
        return currentDirectory;
    }
}
