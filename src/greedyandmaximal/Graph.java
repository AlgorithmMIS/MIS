package greedyandmaximal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;



public class Graph {
	private HashMap<Integer,HashSet<Integer>> adTable;
	public ArrayList<Item> adList=new ArrayList<Item>(); //按度排序的邻接表
	int numberOfVertex;
	public Graph(){
		this.adTable=new HashMap<Integer,HashSet<Integer>>();
		File file=new File("src/frb.mis");
		String line;
		String [] strs;
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			line=br.readLine();
			strs=line.split(" ");
			this.numberOfVertex=Integer.valueOf(strs[2]);
			while((line=br.readLine())!=null){
				strs=line.split(" ");
				
				//The id of vertex starts from zero
				int v1=Integer.valueOf(strs[1])-1;
				int v2=Integer.valueOf(strs[2])-1;
				//also set  v2 -v1
				this.addEdge(v1,v2);
				this.addEdge(v2,v1);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		//adTable is finished
		Iterator<Integer> itr=this.adTable.keySet().iterator();
		while(itr.hasNext()){
			int i=itr.next();
			this.adList.add(new Item(i,this.adTable.get(i)));
		}
		Collections.sort(adList,new Item());
	}
	
	private void addEdge(int v1,int v2){
		if(this.adTable.containsKey(v1)){
			this.adTable.get(v1).add(v2);
		}else{
			this.adTable.put(v1, new HashSet<Integer>());
			this.adTable.get(v1).add(v2);
		}
		
	}

}

class Item implements Comparator<Item>{
	int vertex;
	HashSet<Integer> list;
	Item(int i,HashSet<Integer> l){
		this.vertex=i;
		this.list=l;
	}
	Item(){
		
	}

	public int compare(Item arg0, Item arg1) {
		if(arg0.list.size()<arg1.list.size())
			return -1;
		else if(arg0.list.size()==arg1.list.size())
			return 0;
		else return 1;
	}
}