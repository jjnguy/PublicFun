package edu.iastate.cs309.client;

import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;

public class TorrentInformationContainer
{
	public TorrentInfo info;
	public TorrentProp prop;
	public int refID;

	public TorrentInformationContainer()
	{
	}

	/**
	 * Creates a new TorrentInfoContainer with the given paramters
	 * 
	 * @param refIDP
	 *            the id number for the torrent
	 * @param infoP
	 *            the information of the torrent
	 * @param propP
	 *            the properties of the torrent
	 */
	public TorrentInformationContainer(int refIDP, TorrentInfo infoP, TorrentProp propP)
	{
		info = infoP;
		prop = propP;
		refID = refIDP;

	}

	public String getName()
	{
		return info.getName();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + refID;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TorrentInformationContainer other = (TorrentInformationContainer) obj;

		return refID == other.refID;
	}
}
