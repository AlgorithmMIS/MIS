package ant;

import java.util.ArrayList;

public class Move {
	
	int [] path=new int[200];
	int top=-1;
	int L=TabuList.tabuinit;//禁忌表初值
	public void addStep(int v){
		path[++top]=v;
	}
	public boolean includeIn(Move big){
		for(int i=0;i<=this.top;i++){
			if(this.path[i]!=big.path[i])
				return false;
		}
		return true;
	}
}
