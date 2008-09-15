package edu.iastate.cs309.guiElements.mainGuiTabs;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import edu.iastate.cs309.communication.TorrentInfo;
import edu.iastate.cs309.communication.TorrentProp;
import edu.iastate.cs309.guiElements.PieceProgressPane;
import edu.iastate.cs309.guiElements.guiUTil.GUIUtil;
import edu.iastate.cs309.guiElements.lowerlevel.HBox;
import edu.iastate.cs309.guiElements.lowerlevel.HStretcher;
import edu.iastate.cs309.guiElements.lowerlevel.VBox;

/**
 * An UberTab for displaying detailed information about a Torrent
 * 
 * @author Justin
 * 
 */
@SuppressWarnings("serial")
public class TorrentPropertiesTab extends UberTab
{

	private TorrentInfo info;
	private TorrentProp prop;

	private JLabel transferInfoSubTitleLabel;
	private JLabel ammountDownloadedLabel;
	private JLabel ammountRemainingLabel;
	private JLabel downloadSpeedLabel;
	private JLabel uploadSpeedLabel;
	private JLabel numSeedsLabel;
	private JLabel trackerInfoSubTitleLabel;
	private JLabel generalInfoSubTitleLabel;

	private JLabel transferInfoSubTitleLabelData;
	private JLabel timeRemainingLabelData;
	private JLabel ammountDownloadedLabelData;
	private JLabel ammountRemainingLabelData;
	private JLabel downloadSpeedLabelData;
	private JLabel uploadSpeedLabelData;
	private JLabel numSeedsLabelData;
	private JLabel trackerInfoSubTitleLabelData;
	private JLabel generalInfoSubTitleLabelData;

	private JLabel progressLabel;
	private PieceProgressPane progress;

	/**
	 * Creates a simple tab.
	 * 
	 * @param propP
	 * @param infoP
	 */
	public TorrentPropertiesTab(TorrentProp propP, TorrentInfo infoP)
	{
		setLayout(new BorderLayout());
		createComponents();
		layoutComponents();
		setProperties(infoP, propP);
	}

	/**
	 * 
	 */
	private void layoutComponents()
	{
		//		add(new
		//		VBox(Alignment.LEADING, true).addComp(new
		//			HBox("Transfer", Alignment.LEADING).addComp(new 
		//				VBox(Alignment.LEADING).addComp(
		//					timeElapsedLabel).addComp(
		//					ammountDownloadedLabel).addComp(
		//					ammountRemainingLabel).addComp(
		//					downloadSpeedLabel).addComp(
		//					uploadSpeedLabel).addComp(
		//					numSeedsLabel)
		//				).addComp(new
		//				VBox(Alignment.LEADING).addComp(
		//					timeElapsedLabelData).addComp(
		//					ammountDownloadedLabelData).addComp(
		//					ammountRemainingLabelData).addComp(
		//					downloadSpeedLabelData).addComp(
		//					uploadSpeedLabelData).addComp(
		//					numSeedsLabelData)
		//				).addComp(
		//					new HStretcher()
		//				)
		//			)
		//		);
		add(new VBox(Alignment.LEADING, true).addComp(new HBox(Alignment.LEADING).addComp(progressLabel).addComp(progress)).addComp(new HBox("Transfer", Alignment.LEADING).addComp(new VBox(Alignment.LEADING).addComp(ammountDownloadedLabel).addComp(ammountRemainingLabel).addComp(downloadSpeedLabel).addComp(uploadSpeedLabel).addComp(numSeedsLabel)).addComp(new VBox(Alignment.LEADING).addComp(ammountDownloadedLabelData).addComp(ammountRemainingLabelData).addComp(downloadSpeedLabelData).addComp(uploadSpeedLabelData).addComp(numSeedsLabelData)).addComp(new HStretcher())));
	}

	public void setProperties(TorrentInfo toInfo, TorrentProp toProp)
	{
		if (toInfo != null)
			updateInfo(toInfo);
		if (toProp != null)
			updateProps(toProp);
	}

	/**
	 * @param toProp
	 */
	private void updateProps(TorrentProp toProp)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param toInfo
	 */
	private void updateInfo(TorrentInfo toInfo)
	{
		ammountDownloadedLabelData.setText(Long.toString(toInfo.getBytesDownloaded()));
		ammountRemainingLabelData.setText(Long.toString(toInfo.getSize() - toInfo.getBytesDownloaded()));
		downloadSpeedLabelData.setText(toInfo.getDownloadSpeed() + " B/s");
		uploadSpeedLabelData.setText(toInfo.getUploadSpeed() + " B/s");
		numSeedsLabelData.setText(Integer.toString(toInfo.getNumSeeders()));
		progress.updatePieces(toInfo.getProgress(), toInfo.getNumPieces());
		repaint();
	}

	private final void createComponents()
	{
		progressLabel = new JLabel("Pieces:");
		if (info != null)
			progress = new PieceProgressPane(info.getProgress(), info.getNumPieces());
		else
			progress = new PieceProgressPane(null, 0);

		transferInfoSubTitleLabel = new JLabel("Transfer Information:");
		//timeElapsedLabel = new JLabel("Time Elapsed:");
		ammountDownloadedLabel = new JLabel("Bytes Downloaded:");
		ammountRemainingLabel = new JLabel("Bytes Left:");
		downloadSpeedLabel = new JLabel("Download Speed:");
		uploadSpeedLabel = new JLabel("Upload Speed:");
		numSeedsLabel = new JLabel("Number of Seeders Connected:");

		transferInfoSubTitleLabelData = new JLabel();
		//timeElapsedLabelData = new JLabel();
		ammountDownloadedLabelData = new JLabel();
		ammountRemainingLabelData = new JLabel();
		downloadSpeedLabelData = new JLabel();
		uploadSpeedLabelData = new JLabel();
		numSeedsLabelData = new JLabel();
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabName()
	 */
	@Override
	public String getTabName()
	{
		return "Torrent Info";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText()
	{
		return "Info about the selected torrent";
	}

	/**
	 * @see edu.iastate.cs309.guiElements.mainGuiTabs.UberTab#getTabIcon()
	 */
	@Override
	public Icon getTabIcon()
	{
		return new ImageIcon(GUIUtil.tabImageFOlder + File.separator + "TorrentInfoLogo.png");
	}
}
