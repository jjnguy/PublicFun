package edu.iastate.cs309.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import edu.iastate.cs309.clientserverprotocol.NetUtils;
import edu.iastate.cs309.util.Util;

public class TransferFilesThread extends Thread
{
	private List<Integer> fileIndexes;
	private int refID;
	private List<InputStream> data;
	private TorrentInformationContainer information;
	private Logger log = ClientLog.log;

	/**
	 * 
	 * TODO: this handling (added by David) is a dirty hack
	 * 
	 * @param refIDP
	 *            the refID of the torrent
	 * @param fileIndexesP
	 *            the files that need to be transfered
	 * @param dataP
	 *            the raw dirty data!
	 */
	public TransferFilesThread(int refIDP, List<Integer> fileIndexesP, List<InputStream> dataP, TorrentInformationContainer informationP)
	{
		refID = refIDP;
		data = dataP;
		fileIndexes = fileIndexesP;
		information = informationP;
	}

	@Override
	public void run()
	{
		JOptionPane.showMessageDialog(null, "Transfer beginning.  You will be notified when the transfer is complete.");
		/* number to transfer */
		int num = fileIndexes.size();

		try
		{
			for (int i = 0; i < num; ++i)
			{
				File file = null;

				/* if a single file download */
				if (information.info.getIndividualFileNames().size() == 0)
					file = new File(TheActualClient.fileDirectory + File.separator + information.info.getName());
				else
				{
					String filename = information.info.getIndividualFileNames().get(fileIndexes.get(i));
					file = new File(TheActualClient.fileDirectory + File.separator + information.info.getName() + File.separator + filename);
				}

				/* ensure directory is created */
				file.getParentFile().mkdirs();

				if (Util.TDEBUG)
					System.out.println("Writing " + file.getAbsolutePath());

				OutputStream out = new FileOutputStream(file);
				NetUtils.transfer(data.get(i), out);
			}
		}
		catch (FileNotFoundException e)
		{
			log.log(Level.WARNING, "Couldn't open file for writing. Transferring files failed.");
			if (Util.DEBUG)
				e.printStackTrace();
		}
		catch (IOException e)
		{
			log.log(Level.WARNING, "Transfering files failed.");
			if (Util.DEBUG)
				e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Transfer of " + information.info.getName() + " is complete.\nYou can find the file in " + TheActualClient.fileDirectory + '.');
	}

	/**
	 * @return the refID
	 */
	public int getRefId()
	{
		return refID;
	}
}
