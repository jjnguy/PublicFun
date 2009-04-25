package edu.cs319.dataobjects.database;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="coLabRoom")
public class DBCoLabRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)         
	private Long id;
	
	@Basic
	@Column(name="admin")
	private String admin;
	
	@Basic
	@Column(name="roomname", unique=true)
	private String roomname;

	@Cascade(value=CascadeType.ALL)
	@OneToMany(fetch=FetchType.EAGER)
	private Set<SectionizedDBDocument> documents;

	/**
	 * @return the roomname
	 */
	public String getRoomname() {
		return roomname;
	}

	/**
	 * @param roomname the roomname to set
	 */
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}

	/**
	 * @return the documents
	 */
	public Set<SectionizedDBDocument> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(Set<SectionizedDBDocument> documents) {
		this.documents = documents;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	/**
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
}
