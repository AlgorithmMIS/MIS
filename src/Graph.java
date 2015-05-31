import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Graph {
	int numberOfVertex;
	boolean [][] adjacencyMatrix;
	
	
	//Construct the graph. Read the test file.
	public Graph(){
		File file=new File("src/frb.mis");
		String line;
		String [] strs;
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			line=br.readLine();
			strs=line.split(" ");
			this.numberOfVertex=Integer.valueOf(strs[2]);
			this.adjacencyMatrix=new boolean[this.numberOfVertex][this.numberOfVertex];
			this.initMatrix();
			while((line=br.readLine())!=null){
				strs=line.split(" ");
				
				//The id of vertex starts from zero
				int v1=Integer.valueOf(strs[1])-1;
				int v2=Integer.valueOf(strs[2])-1;
				//also set  v2 -v1
				this.adjacencyMatrix[v1][v2]=true;
				this.adjacencyMatrix[v2][v1]=true;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
				
	}
	
	//set all matrix false
	private void initMatrix(){
		for(int i=0;i<this.numberOfVertex;i++){
			for(int j=0;j<this.numberOfVertex;j++){
				this.adjacencyMatrix[i][j]=false;
			}
		}
	}
}
