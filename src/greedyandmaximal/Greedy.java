package greedyandmaximal;

import java.util.HashSet;

public class Greedy {
	static HashSet<Integer> maximalSet;
	static Graph graph;
	public static void main(String[] args){
		graph=new Graph();
		maximalSet=new HashSet<Integer>();
		HashSet<Integer> temp=new HashSet<Integer>();
		int max=-1;
		for(int start=0;start<graph.adList.size()-1;start++){
			maximalSet.add(start);
			for(int i=0;i<graph.adList.size();i++){
				temp.clear();
				temp.addAll(maximalSet);
				temp.retainAll(graph.adList.get(i).list);
				if(temp.size()==0){

					maximalSet.add(graph.adList.get(i).vertex);
				}
			}
			
			if(max<0){
				max=maximalSet.size();
			}else{
				if(maximalSet.size()>max){
					max=maximalSet.size();
				}
			}
			maximalSet.clear();
		}
		
		System.out.println(max);
	}
}
