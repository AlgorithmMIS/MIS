package ant;

public class ACO {
	static final int numberOfAnt=30;
	static TabuList tabu=new TabuList();
	public static void main(String []args){
		Ant [] ants=new Ant[numberOfAnt];
		ant.Graph g=new Graph();
		int max=-1;
		int latestMax=-1;
		int latestMaxIndex=-1;
		for(int i=0;i<numberOfAnt;i++){
			ants[i]=new Ant(g.cloneList(),tabu);
		}
		int time=0;
		while(time++<500){
			System.out.println(time);
			for(int i=0;i<numberOfAnt;i++){
				ants[i].run();
				int res=ants[i].mis.size();

				if( latestMax<0||(latestMax>=0&&latestMax<res)){
					latestMax=res;
					latestMaxIndex=i;
				}
			}
			if(max<0||(max>=0&&max<latestMax)){
				max=latestMax;
			}
			
			for(int i=0;i<g.adList.size();i++){
				g.adList.get(i).vanish();
			}
			
			ants[latestMaxIndex].release(max,latestMax);
			tabu.update();
			System.out.println("Max:"+max);
		}
		
	}
}
