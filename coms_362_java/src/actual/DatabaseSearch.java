package actual;

import infoExpert.SongData;

import java.util.List;

/**
 * 
 * Interface that includes methods to utilize the database
 *
 */
public interface DatabaseSearch {
	public List<SongData> search(String broadTerm, String title, String artist,
			String album, int sortBy, String usename);

	public List<String> getAllUsers();
}
