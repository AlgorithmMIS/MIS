package EWLS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class edges {
	private LinkedList<edge> myedges;

	public edges() {
		myedges = new LinkedList<edge>();
	}

	public edges(edges a) {
		this.myedges = new LinkedList<edge>(a.myedges);
	}

	public void add(edge e) {
		if (!myedges.contains(e))
			myedges.add(e);
	}

	public LinkedList<edge> getEdges() {
		return (myedges);//needn't return a clone
	}

	public void remove(edge e) {
		myedges.remove(e);
	}
	public void remove(ArrayList<edge> a){
		myedges.removeAll(a);
	}
	public int size() {
		return myedges.size();
	}

	public boolean isEmpty() {
		return myedges.isEmpty();
	}

	public edge poll() {
		return this.myedges.poll();
	}

	public edge peek() {
		// TODO Auto-generated method stub
		return this.myedges.peek();
	}

	public edge getLast() {
		// TODO Auto-generated method stub
		return this.myedges.getLast();
	}

	public edge get(int i) {
		// TODO Auto-generated method stub
		return this.myedges.get(i);
	}

	public void addFirst(edge t) {
		// TODO Auto-generated method stub
		this.myedges.addFirst(t);
	}
}
