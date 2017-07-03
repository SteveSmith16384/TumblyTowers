package ssmith.util;

import java.util.ArrayList;
import java.util.List;

public class TSArrayList<E> extends ArrayList<E> implements List<E> {

	private static final long serialVersionUID = 1L;

	private ArrayList<E> to_add = new ArrayList<E>();
	private ArrayList<E> to_remove = new ArrayList<E>();

	public TSArrayList() {
		super();
	}


	public void refresh() {
		super.removeAll(this.to_remove);
		this.to_remove.clear();
		super.addAll(this.to_add);
		this.to_add.clear();
	}


	@Override
	public boolean add(E o) {
		if (this.to_add.contains(o) || this.contains(o)) {
			//throw new RuntimeException("Object " + o + " has already been added!");
			return false;
		}
		return this.to_add.add(o);
	}


	@Override
	public boolean remove(Object o) {
		return this.to_remove.add((E)o);
	}


	@Override
	public void clear() {
		for (E e : this) {
			this.to_remove.add(e);
		}
	}
	
	
	@Override
	public int size() {
		this.refresh();
		return super.size();// + this.to_add.size() - this.to_remove.size();
	}


	@Override
	public boolean isEmpty() {
		return size() <= 0;
	}
	
	
	@Override
	public boolean contains(Object o) {
		return super.contains(o) || this.to_add.contains(o);
	}
}
