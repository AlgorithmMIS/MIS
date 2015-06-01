package ant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Ant {
	static double alpha=2;
	static double beta=1;
	HashSet<Integer> mis=new HashSet<Integer>();
	HashSet<Integer> allVertex;
	ArrayList<Item> adList;
	int total;
	Move move;
	HashSet<Integer> readySet;
	TabuList tabu;
	public Ant(ArrayList<Item > adList,TabuList tabu){
		this.adList=adList;
		this.total=adList.size();
		this.tabu=tabu;

	}
	public void run(){
		//System.out.println("run start");
		move=new Move();
		Random rd=new Random();
		int first=Math.abs(rd.nextInt())%total;
		mis.clear();
		allVertex=new HashSet<Integer>();
		for(int i=0;i<total;i++){
			allVertex.add(i);
		}
		readySet=new HashSet<Integer>();
		mis.add(first);
		move.addStep(first);
		allVertex.remove(first);
		//System.out.println("first"+first);
		this.updateReadySet();
		
		int i=0;
		while(!readySet.isEmpty()){
			//System.out.println("choose time"+(i++)+" ready size"+readySet.size()+"allVertex.size"+allVertex.size()+"mis "+mis.size());
			this.chooseOne();
		}
		//System.out.println("run end");
	}
	private void chooseOne(){
		
		ArrayWithPosibility a=new ArrayWithPosibility();
		Iterator<Integer> i=readySet.iterator();
		while(i.hasNext()){

			int v=i.next();			//System.out.println(v);
			a.add(v,Math.pow(adList.get(v).S,alpha)*Math.pow(adList.get(v).G, beta));
		}
		int s=a.randomSelect();
		mis.add(s);
		move.addStep(s);
		allVertex.remove(s);
		this.updateReadySet();
		//System.out.println(s);
	}
	private void updateReadySet(){
		
		readySet.clear();
		readySet.addAll(allVertex);
		Iterator<Integer> itr=mis.iterator();
		readySet.removeAll(mis);
		while(itr.hasNext()){
			int i=itr.next();
			readySet.removeAll(adList.get(i).list);
			
		}
		tabu.check(move, readySet);
		
	}
	public void release(int max,int latestMax){
		Iterator<Integer> itr=this.mis.iterator();
		int i;
		while(itr.hasNext()){
			i=itr.next();
			this.adList.get(i).updateStrength(max, latestMax);
		}
		
	}
}

class ArrayWithPosibility{
	ArrayList<Integer> list=new ArrayList<Integer>();
	ArrayList<Double> posibility=new ArrayList<Double>();
	Random rd=new Random();
	public void add(int i,double p){
		list.add(i);
		if(this.posibility.isEmpty())
			posibility.add(p);
		else posibility.add(p+posibility.get(posibility.size()-1));
	}
	
	public int randomSelect(){
		double base=posibility.get(posibility.size()-1);
		double p=rd.nextDouble()*base;
		double lastP=0;
		for(int i=0;i<posibility.size();i++){
			if(p>=lastP&&p<posibility.get(i)){
				return list.get(i);
			}else{
				lastP=posibility.get(i);
				continue;
			}
		}
		return -1;
	}
	
}
