
public class edge {
	int x, y;

	public edge(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public boolean equals(edge another){
		if(this.x==another.x&&this.y==another.y)
			return true;
		if(this.x==another.y&&this.y==another.x)
			return true;
		return false;
	}
}
