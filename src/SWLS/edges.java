import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class edges {
	private HashSet<edge> myedges;

	public edges() {
		myedges = new HashSet<edge>();
	}

	public edges(edges a) {
		this.myedges = new HashSet<edge>(a.myedges);
	}

	public void add(edge e) {
		if (!myedges.contains(e))
			myedges.add(e);
	}

	public ArrayList<edge> getEdges() {
		return new ArrayList<>(myedges);
	}

	public void remove(edge e) {
		myedges.remove(e);
	}

	public int size() {
		return myedges.size();
	}

	public boolean isEmpty() {
		return myedges.isEmpty();
	}

	public edge getFirst() {
		Iterator<edge> itr = myedges.iterator();
		if (itr.hasNext()) {
			edge tm = itr.next();
			itr.remove();
			return tm;
		}
		return null;
	}
}
