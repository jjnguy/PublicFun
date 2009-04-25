package edu.cs319.dataobjects.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import edu.cs319.dataobjects.DocumentSubSection;
import edu.cs319.dataobjects.SectionizedDocument;

@Entity
@Table(name="sectionizedDocs")
public class SectionizedDBDocument implements SectionizedDocument{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)         
	private Long id;
	
	@Basic
	@Column(name="documentName")
	private String name;
	
	@OneToMany
	@Cascade(value=CascadeType.ALL)
	private List<DBDocumentSubSection> subSections;
	
	public SectionizedDBDocument(){
		subSections = new ArrayList<DBDocumentSubSection>();
	}
	
	@Override
	public void addAllSubSections(List<DocumentSubSection> ss) {
		
		DBDocumentSubSection newSection;
		
		for(DocumentSubSection section : ss){
			newSection = new DBDocumentSubSection();
			newSection.setName(section.getName());
			newSection.setText(section.getText());
			
			subSections.add(newSection);
		}
		
	}

	@Override
	public boolean addSubSection(DocumentSubSection ds, int index) {
		DBDocumentSubSection newSection = new DBDocumentSubSection();
		newSection.setName(ds.getName());
		newSection.setText(ds.getText());
		
		subSections.add(index, newSection);
		
		return false;
	}

	@Override
	public boolean combineSubSections(String partA, String partB, String combinedName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean flopSubSections(int idx1, int idx2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DocumentSubSection> getAllSubSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DocumentSubSection getSection(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentSubSection getSectionAt(int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSubSectionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubSectionIndex(String sectionID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeAllSubSections() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeSubSection(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean splitSubSection(String name, String partA, String partB, int splitIndex,
			String userName) {
		// TODO Auto-generated method stub
		return false;
	}

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

	/**
	 * @return the subSections
	 */
	public List<DBDocumentSubSection> getSubSections() {
		return subSections;
	}

	/**
	 * @param subSections the subSections to set
	 */
	public void setSubSections(List<DBDocumentSubSection> subSections) {
		this.subSections = subSections;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}