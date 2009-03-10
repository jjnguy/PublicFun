package edu.cs319.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import edu.cs319.server.events.CoLabEvent;

public class JustinsQueue implements Queue<CoLabEvent> {

	private List<CoLabEvent> backing;

	public JustinsQueue(){
		backing = new ArrayList<CoLabEvent>();
	}
	
	@Override
	public boolean add(CoLabEvent e) {
		return false;
	}

	@Override
	public CoLabEvent element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(CoLabEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CoLabEvent peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoLabEvent poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoLabEvent remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addAll(Collection<? extends CoLabEvent> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<CoLabEvent> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

}
