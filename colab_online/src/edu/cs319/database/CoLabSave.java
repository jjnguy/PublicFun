package edu.cs319.database;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.cs319.dataobjects.impl.SectionizedDocumentImpl;

@Entity
@Table(name="COLABS")
public class CoLabSave {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long coLabId;
	
	@Basic
	@Column(name="admin", unique=false, nullable=false)
	private String admin;
	
	@Basic
	@Column(name="roomName", unique=true, nullable=false)
	private String roomName;
	
	@OneToMany
	@JoinTable(
			name="coLabToDocumentJoin",
			joinColumns= @JoinColumn(name="colab_id"),
			inverseJoinColumns = @JoinColumn(name="document_id")
			)
	private List<SectionizedDocumentImpl> documents;
	
	/**
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}
	
	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}
	
	/**
	 * @param roomName the roomName to set
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	/**
	 * @return the documents
	 */
	public List<SectionizedDocumentImpl> getDocuments() {
		return documents;
	}
	
	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<SectionizedDocumentImpl> documents) {
		this.documents = documents;
	}

	/**
	 * @param coLabId the coLabId to set
	 */
	public void setCoLabId(Long coLabId) {
		this.coLabId = coLabId;
	}

	/**
	 * @return the coLabId
	 */
	public Long getCoLabId() {
		return coLabId;
	}
	
}
