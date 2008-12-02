package controller;

import id3TagStuff.ID3v2_XTag;
import id3TagStuff.frames.ID3v2_XFrame;
import id3TagStuff.id3Data.ID3_Picture;
import infoExpert.SongData;

public class createSongData
{
	
	public static SongData tagToSongData (ID3v2_XTag tag)
	{
		SongData SD = new SongData();
		int commentCount = 0;
		int performerCount = 0;
		
		for (ID3v2_XFrame frame : tag.getAllFrames()) 
		{
			if (frame.getFrameID().matches("COMM?")) 
			{
				if (commentCount < 3)
				{
					SD.setComment(commentCount, frame.getData().toString());
					commentCount++;
				}
			}
			
			else if (frame.getFrameID().matches("A?PIC")) 
				SD.setPictureName(frame.getData().toString());
			
			else if (frame.getFrameID().matches("TALB?")) 
				SD.setAlbum(frame.getData().toString());
				 
			else if (frame.getFrameID().matches("TCO?M")) 
				 SD.setComposer(frame.getData().toString());
			
			else if (frame.getFrameID().matches("TI?T2")) 
				 SD.setTitle(frame.getData().toString());
			
			else if (frame.getFrameID().matches("TRC?K")) 
				SD.setTrackNum(frame.getData().toString());
			
			else if (frame.getFrameID().matches("TYER?")) 
				SD.setYear(frame.getData().toString());
				
			else if (frame.getFrameID().matches("TENC?")) 
				SD.setEncodedBy(frame.getData().toString());
			
			else if (frame.getFrameID().matches("TPE?1")) 
			{
				if (performerCount < 3)
				{	
					SD.setPerformer(performerCount,frame.getData().toString());
					performerCount++;
				}
			} 
		}
		return SD;
	}
	
	
}