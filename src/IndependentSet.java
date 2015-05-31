import java.util.ArrayList;


public class IndependentSet {
	Graph graph;
	ArrayList<Integer> set=new ArrayList<Integer>();
	public IndependentSet(Graph graph){
		this.graph=graph;
	}
	private IndependentSet(){
		
	}
	//return true only when the set remains independent set after vertex is added
	public boolean checkAdd(int vertex){
		for(Integer i:set){
			if(graph.adjacencyMatrix[i][vertex]!=true){
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
	//return the number of vertex in this set
	public int size(){
		return this.set.size();
	}
	//return a new object of IndependentSet which adds vertex. The origin set will not be changed.
	public IndependentSet add(int vertex){
		IndependentSet set=new IndependentSet();
		set.set.addAll(this.set);
		set.graph=this.graph;
		set.set.add(vertex);
		return set;
	}
	//return a new object
	public IndependentSet clone(){
		IndependentSet newSet=new IndependentSet();
		newSet.graph=this.graph;
		newSet.set.addAll(this.set);
		return newSet;
	}
}
