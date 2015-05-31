package backtrack;
public class BackTrack {
	static Graph graph;
	static int bestNumber=0;
	public static void main(String[] args) {
		graph=new Graph();
		backTrack(0,new IndependentSet(graph));
		System.out.println(bestNumber);
	}
	static void backTrack(int vertex,IndependentSet currentSet){
		System.out.println(vertex);
		//the end
		if(vertex==graph.numberOfVertex){
			
			bestNumber=currentSet.size();
			return;
		}
		
		if(currentSet.checkAdd(vertex)){
			backTrack(vertex+1,currentSet.add(vertex));
		}
		if(currentSet.size()+graph.numberOfVertex-vertex>bestNumber){
			backTrack(vertex+1,currentSet.clone());
		}
		
	}

}
