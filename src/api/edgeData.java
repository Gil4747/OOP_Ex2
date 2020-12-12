package api;

public class edgeData implements edge_data {
private node_data src;
private node_data dest;
private String info;
private int tag;
private double weight;

public edgeData(node_data src, node_data dest) {
	this.src=src;
	this.dest=dest;
	this.weight=src.getLocation().distance(dest.getLocation());
	this.tag=0;
	this.info="";
	
}
	@Override
	public int getSrc() {
		return this.src.getKey();
	}

	@Override
	public int getDest() {
		return this.dest.getKey();
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public String getInfo() {
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		this.info=s;
		
	}

	@Override
	public int getTag() {
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag=t;
		
	}

}
