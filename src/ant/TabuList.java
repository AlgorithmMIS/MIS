package ant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class TabuList {
	 ArrayList<Move> list=new ArrayList<Move>();
	static final int tabuinit=3;
	public void addMove(Move e){
		list.add(e);
	}
	public void update(){
		int size=list.size();
		for(int i=0;i<size;i++){
			list.get(i).L-=1;
			if(list.get(i).L==0){
				list.remove(i);
				i--;
			}
		}
	}
	
	public  void check(Move current,HashSet<Integer> readySet){
		Iterator<Move> itr=list.iterator();
		HashSet<Integer> tabuSet=new HashSet<Integer>();
		int best=0;
		int bestVertex=-1;
		while(itr.hasNext()){
			Move m=itr.next();
			if(current.includeIn(m)){
				tabuSet.add(m.path[current.top+1]);
				if(m.top>best){
					best=m.top;
					bestVertex=m.path[current.top+1];
				}
			}
		}
		readySet.removeAll(tabuSet);
		if(readySet.isEmpty()&&bestVertex!=-1){
			readySet.add(bestVertex);
		}
	}
}
