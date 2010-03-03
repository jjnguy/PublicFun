package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Linked list implementation skeleton for Assignment 3.
 */
public class CS228LinkedList<E> extends AbstractSequentialList<E> {
	/**
	 * Dummy node at the head of the list.
	 */
	private final Node head;

	/**
	 * Dummy node at the tail of the list.
	 */
	private final Node tail;

	/**
	 * Constructs an empty list consisting of just the head and tail nodes.
	 */
	public CS228LinkedList() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.prev = head;

		// TODO - any other initialization you need
	}

	@Override
	public boolean add(E item) {
		add(size(), item);
		return true;
	}

	@Override
	public void add(int index, E item) {
		int idxCount = 0;
		for (Node n = head.next; n != tail; n = n.next) {
			for (int i = 0; i < n.data.length; i++) {
				if (idxCount == index) {
					n.add(item, i);
					return;
				}
				idxCount++;
			}
		}
		// This means we are adding onto the end...I think
		Node toAdd = new Node();
		tail.prev.next = toAdd;
		toAdd.next = tail;
		toAdd.prev = tail.prev;
		tail.prev = toAdd;
		toAdd.data[0] = item;
	}

	@Override
	public E get(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("Cannot get element at negative index");
		int idxCount = 0;
		for (Node n = head.next; n != tail; n = n.next) {
			for (int i = 0; i < n.data.length; i++) {
				if (idxCount == index)
					return n.data[i];
				idxCount++;
			}
		}
		throw new IndexOutOfBoundsException("The given index, " + index + " was out of bounds");
	}

	@Override
	public ListIterator<E> listIterator(int startIndex) {
		return new CS228ListIterator(startIndex);
	}

	@Override
	public int size() {
		int size = 0;
		for (Node n = head.next; n != tail; n = n.next) {
			for (int i = 0; i < n.data.length; i++) {
				if (n.data[i] != null)
					size++;
			}
		}
		return size;
	}

	@Override
	public String toString() {
		String ret = "[";
		for (Node n = head.next; n != tail; n = n.next) {
			ret += n.toString();
			ret += ", ";
		}
		return ret.substring(0, ret.length() - 2) + "]";
	}

	/**
	 * Doubly-linked node type for this List implementation.
	 */
	private class Node {
		private static final int DEFAULT_SIZE = 2;

		public Node prev;
		public Node next;

		private E[] data;

		Node() {
			this(DEFAULT_SIZE);
		}

		Node(int dataSize) {
			data = (E[]) new Object[dataSize];
		}

		Node(boolean flag) {
			if (flag)
				data = (E[]) new Object[new Random().nextInt(5) + 1];
			else
				data = (E[]) new Object[DEFAULT_SIZE];
		}

		/**
		 * Adds element e into the specified index in the data array If there is no more room, a new node will be
		 * inserted after this node.
		 * 
		 * @param e
		 * @param index
		 */
		private void add(E e, int index) {
			if (hasRoom(index)) {
				shiftElements(data, index);
				data[index] = e;
			} else {
				Node toAdd = new Node();
				this.next.prev = toAdd;
				toAdd.next = this.next;
				toAdd.prev = this;
				this.next = toAdd;

				toAdd.data[0] = this.data[data.length - 1];
				// Shifts everything over one spot, the end element gets lost
				System.arraycopy(this.data, index, this.data, index + 1, data.length - index - 1);
				this.data[index] = e;
			}
		}

		/**
		 * Shifts all elements in the array so that arr[index] will be empty
		 * 
		 * @param arr
		 * @param index
		 */
		public void shiftElements(E[] arr, int index) {
			if (arr[index] == null)
				return;
			int firstIndexOfNull = -1;
			for (int i = index; i < arr.length; i++) {
				if (arr[i] == null) {
					firstIndexOfNull = i;
					break;
				}
			}
			if (firstIndexOfNull < index)
				throw new IllegalArgumentException();
			for (int i = firstIndexOfNull; i > index; i--) {
				arr[i] = arr[i - 1];
			}
			arr[index] = null;
		}

		/**
		 * Returns whether or not there is room in the node after the given index
		 * 
		 * @param index
		 * @return
		 */
		private boolean hasRoom(int index) {
			for (int i = index; i < data.length; i++) {
				if (data[i] == null)
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			String ret = "(";
			for (int i = 0; i < data.length; i++) {
				ret += data[i] != null ? data[i] : "-";
				if (i == data.length - 1)
					ret += ")";
				else
					ret += ", ";
			}
			return ret;
		}
	}

	/**
	 * Concrete ListIterator for this List implementation.
	 */
	private class CS228ListIterator implements ListIterator<E> {

		private Caret caret;
		private int nextIndex;

		/**
		 * Constructs an iterator whose initial position is the given index.
		 * 
		 * @param startIndex
		 *            initial position of the iterator
		 */
		public CS228ListIterator(int startIndex) {
			// TODO ...test
			caret = new Caret();
			int idxCount = 0;
			for (Node n = head.next; n != tail; n = n.next) {
				for (int i = 0; i < n.data.length; i++) {
					if (n.data[i] != null)
						idxCount++;
					if (idxCount == startIndex) {
						caret.n = n;
						caret.idx = i;
						return;
					}
				}
			}
		}

		@Override
		public void add(E arg0) {
			// TODO test
			CS228LinkedList.this.add(nextIndex, arg0);
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public E next() {
			// TODO some way to tell if next should be the first node cuz we prev'd off the end
			caret = caret.next();
			if (caret == null)
				throw new NoSuchElementException();
			nextIndex++;
			return caret.n.data[caret.idx];
		}

		@Override
		public int nextIndex() {
			return nextIndex;
		}

		@Override
		public E previous() {
			// TODO some way to tell if previous should be the last node cuz we next'd off the end
			caret = caret.prev();
			if (caret == null)
				throw new NoSuchElementException();
			nextIndex--;
			return caret.n.data[caret.idx];
		}

		@Override
		public int previousIndex() {
			return nextIndex - 1;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}

		@Override
		public void set(E arg0) {
			// TODO Auto-generated method stub

		}

		private class Caret {
			private Node n;
			private int idx;

			private Caret next() {
				if (n == null)
					return null;
				Caret ret = new Caret();
				int nextIdx = nextNotNullIdx();
				if (nextIdx < 0) {
					if (n.next == null)
						return null;
					ret.n = n.next;
					ret.idx = -1;
					ret.idx = ret.nextNotNullIdx();
				} else {
					ret.idx = nextIdx;
					ret.n = n;
				}
				return ret;
			}

			private Caret prev() {
				if (n == null)
					return null;
				Caret ret = new Caret();
				int prevNotNull = prevNotNullIdx();
				if (prevNotNull < 0) {
					if (n.prev == null)
						return null;
					ret.n = n.prev;
					ret.idx = ret.n.data.length;
					ret.idx = ret.prevNotNullIdx();
				} else {
					ret.idx = prevNotNull;
					ret.n = n;
				}
				return ret;
			}

			private int prevNotNullIdx() {
				for (int i = idx - 1; i > 0; i--) {
					if (n.data[i] != null)
						return i;
				}
				return -1;
			}

			private int nextNotNullIdx() {
				for (int i = idx + 1; i < n.data.length; i++) {
					if (n.data[i] != null)
						return i;
				}
				return -1;
			}
		}

	}
}
