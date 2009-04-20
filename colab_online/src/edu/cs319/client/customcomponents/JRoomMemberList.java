package edu.cs319.client.customcomponents;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.cs319.client.IClient;
import edu.cs319.server.CoLabPrivilegeLevel;
import edu.cs319.util.Util;

public class JRoomMemberList extends JList {

	private JFlexibleListModel<RoomMemberLite> model;
	private String username;
	private IClient client;

	public JRoomMemberList(String username, IClient client) {
		this.client = client;
		this.username = username;
		model = new JFlexibleListModel<RoomMemberLite>();
		setModel(model);
		addMouseListener(rightClickListen);
	}

	@Override
	public JFlexibleListModel<RoomMemberLite> getModel() {
		return model;
	}

	public boolean removeMember(String userID) {
		RoomMemberLite dummy = new RoomMemberLite(userID, null);
		return model.remove(dummy);
	}

	public boolean setMemberPriv(String id, CoLabPrivilegeLevel priv) {
		// super admins stay super admins
		if (Util.isSuperAdmin(id))
			return true;
		RoomMemberLite dummy = new RoomMemberLite(id, priv);
		int idx = model.indexOf(dummy);
		if (idx == -1)
			return false;
		RoomMemberLite mem = (RoomMemberLite) model.getElementAt(idx);
		mem.setPriv(priv);
		model.update();
		return true;
	}

	public RoomMemberLite getFromID(String username) {
		RoomMemberLite dummy = new RoomMemberLite(username, CoLabPrivilegeLevel.OBSERVER);
		int idx = model.indexOf(dummy);
		if (idx == -1)
			return null;
		return (RoomMemberLite) model.getElementAt(idx);
	}

	private MouseListener rightClickListen = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		private void doPop(MouseEvent e) {
			JPopupMenu menu = new RoomMemberPopupMenu(username, ((RoomMemberLite)getSelectedValue()).getName());
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	};

	private class RoomMemberPopupMenu extends JPopupMenu {
		private JMenuItem kickOutMember;
		private JMenuItem promoteUser;
		private JMenuItem demoteUser;

		public RoomMemberPopupMenu(String clicker, String clickee) {
			RoomMemberLite clcikerMem = getFromID(clicker);
			RoomMemberLite clickeeMem = getFromID(clickee);
			if (clcikerMem.equals(clickeeMem))return;
			// observers can't do anything, and super admins can't have anything done to them
			if (clcikerMem.getPriv() == CoLabPrivilegeLevel.OBSERVER
					|| clcikerMem.getPriv() == CoLabPrivilegeLevel.SUPER_ADMIN) {
				return;
			}
			// admins can kick any user out
			if (clcikerMem.getPriv() == CoLabPrivilegeLevel.ADMIN
					|| clcikerMem.getPriv() == CoLabPrivilegeLevel.SUPER_ADMIN) {
				kickOutMember = new JMenuItem("Kick Out Member");
				kickOutMember.addActionListener(kickOutAction);
				add(kickOutMember);
				demoteUser = new JMenuItem("Demote Member");
				demoteUser.addActionListener(demoteAction);
				add(demoteUser);
			}
			// Participants can only promote observers
			if (clcikerMem.getPriv() == CoLabPrivilegeLevel.PARTICIPANT
					&& clickeeMem.getPriv() != CoLabPrivilegeLevel.OBSERVER) {
				return;
			}
			promoteUser = new JMenuItem("Promote Member");
			promoteUser.addActionListener(promoteAction);
			add(promoteUser);
		}

		private ActionListener promoteAction;
		private ActionListener demoteAction;
		private ActionListener kickOutAction;
	}

	public void setUser(String username) {
		this.username = username;
	}
}
