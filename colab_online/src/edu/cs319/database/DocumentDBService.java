package edu.cs319.database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.cs319.dataobjects.impl.DocumentSubSectionImpl;

public class DocumentDBService {
	
	/**
	 * Will return all of the CoLabSave objects for the specified username or
	 * an empty list if none exist.
	 * 
	 * @param username
	 * @return CoLabSave objects for a username
	 */
	@SuppressWarnings("unchecked")
	public List<CoLabSave> getAllRoomsForUser(String username){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(CoLabSave.class);
		
		criteria.add(Restrictions.eq("admin", username));
		
		List ret = criteria.list();
		
		if(ret == null){
			ret = new ArrayList<CoLabSave>();
		}
		
		return ret;
	}
	
	public void updateCoLabRoom(CoLabSave room){
		Session session = HibernateUtil.getSession();
		
		session.update(room);
		
		session.flush();
		session.close();
	}
	
	public void saveCoLabRoom(CoLabSave room){
		Session session = HibernateUtil.getSession();
		
		session.save(room);
		
		session.flush();
		session.close();
	}
	
	public void deleteAllRoomsForUser(String username){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(CoLabSave.class);
		
		criteria.add(Restrictions.eq("admin", username));
		
		List<CoLabSave> rooms = criteria.list();
		
		for(CoLabSave room : rooms){
			session.delete(room);
		}
		
		session.flush();
		session.close();
	}
	
	public void deleteCoLabRoom(CoLabSave room){
		Session session = HibernateUtil.getSession();
		session.delete(room);
		
		session.flush();
		session.close();
	}

	public void addDocumentSubSection(DocumentSubSectionImpl doc){
		Session session = HibernateUtil.getSession();
		
		session.save(doc);
		
		session.flush();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentSubSectionImpl> getAllDocumentSubSections(){
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(DocumentSubSectionImpl.class);
		
		return criteria.list();
		
	}
	
}
