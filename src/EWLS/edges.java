package EWLS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class edges {
	private HashSet<edge> myedges;

	public edges() {
		myedges = new HashSet<edge>();
	}

	public edges(edges a) {
		this.myedges = new HashSet<edge>(a.myedges);
	}
	
	public void addAll(HashSet<edge> l){
		myedges.addAll(l);
	}
	public void addAll(edges l){
		myedges.addAll(l.myedges);
	}
	public void add(edge e) {
			myedges.add(e);
	}

	public List<edge> getEdges() {
		return new ArrayList<edge>(myedges);//needn't return a clone
	}

	public void remove(HashSet<edge> hashSet) {
		myedges.removeAll(hashSet);
	}
	public void remove(edge e){
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

	public edge get(int i) {
		// TODO Auto-generated method stub
		return new ArrayList<edge>(myedges).get(i);
	}

	public void addFirst(edge t) {
		// TODO Auto-generated method stub
		LinkedList<edge>tmp=new LinkedList<edge>(myedges);
		tmp.addFirst(t);
		myedges.clear();
		myedges.addAll(tmp);
	}
}
