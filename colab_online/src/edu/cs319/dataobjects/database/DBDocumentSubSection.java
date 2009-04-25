package edu.cs319.dataobjects.database;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.cs319.dataobjects.DocumentSubSection;

/**
 * Document Sub Section implementation for database persistence
 * 
 * @author admin
 *
 */
@Entity
@Table(name="docSubSection")
public class DBDocumentSubSection extends DocumentSubSection{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)         
	private Long id;
	
	@Basic
	@Column(name="name", nullable=true, unique=false)
	private String name;
	
	@Basic
	@Column(name="text", nullable=true, unique=false, length=Integer.MAX_VALUE)
	private String text;
	
//	@Basic
//	@Column(name="locked", nullable=true, unique=false)
//	private boolean locked;
//	
//	@Basic
//	@Column(name="lockHolder", nullable=true, unique=false)
//	private String lockHolder;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean isLocked() {
		
		return false;
	}

	@Override
	public String lockedByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocked(boolean lock, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setText(String username, String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toDelimmitedString() {
		// TODO Auto-generated method stub
		return null;
	}

//	/**
//	 * @return the lockHolder
//	 */
//	public String getLockHolder() {
//		return lockHolder;
//	}
//
//	/**
//	 * @param lockHolder the lockHolder to set
//	 */
//	public void setLockHolder(String lockHolder) {
//		this.lockHolder = lockHolder;
//	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

//	public boolean getLocked(){
//		return locked;
//	}
//	
//	/**
//	 * @param locked the locked to set
//	 */
//	public void setLocked(boolean locked) {
//		this.locked = locked;
//	}

}
