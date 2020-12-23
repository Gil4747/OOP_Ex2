package api;

import java.util.Objects;

public class nodeData implements node_data{
	private static int id=0;
	private int key;
	private double weight;
	private String info;
	private int tag;
	private int dist=Integer.MAX_VALUE;
	private geo_location location;

	public nodeData(){
		this.key=id++;
		this.tag=0;
		this.dist=Integer.MAX_VALUE;
		this.info="";
		//this.location()
	}

	public nodeData(int key, geo_location location ){
		this.key=key;  
		this.tag=0;
		this.dist=Integer.MAX_VALUE;
		this.info="";
		this.location=location;
	}
	public nodeData(geo_location location ){
		this.key=id++;  
		this.tag=0;
		this.dist=Integer.MAX_VALUE;
		this.info="";
		this.location=location;
	}
	public nodeData(int key){
		this.key=key;  
		this.tag=0;
		this.dist=Integer.MAX_VALUE;
		this.info="";
	}
	/**
	 * Copy constructor
	 * @param n
	 */
	 public nodeData(node_data n){
	        id = n.getKey();
	        if (id <=n.getKey())
	            id = n.getKey()+1;
	        this.location = new geoLocation(n.getLocation().x(),n.getLocation().y());
	        weight = n.getWeight();
	        info = n.getInfo();
	        tag = n.getTag();
	    }

	/**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	@Override
	public int getKey() {
		return this.key;
	}
	/** Returns the location of this node, if
	 * none return null.
	 *
	 * @return the location of the node.
	 */
	@Override
	public geo_location getLocation() {
		return location;
	}
	/**
	 *
	 * @param p - new new location  (position) of this node.
	 */
	@Override
	public void setLocation(geo_location p) {
		this.location=new geoLocation(p);
	}
	/**
	 *
	 * @return the weight of this node
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}
	/**
	 * Set weight to this node
	 * @param w - the new weight
	 */
	@Override
	public void setWeight(double w) {
		this.weight=w;
	}
	/**
	 *
	 * @return info of the node
	 */
	@Override
	public String getInfo() {
		return this.info;
	}
	/**
	 * Set info for the node
	 * @param s
	 */
	@Override
	public void setInfo(String s) {
		this.info=s;	
	}
	/**
	 *
	 * @return tag in int of this node
	 */
	@Override
	public int getTag() {
		return this.tag;
	}
	/**
	 *
	 * @param t - the new value of the tag
	 */
	@Override
	public void setTag(int t) {
		this.tag=t;
	}

	public int getDist() {
		return this.dist;	 
	}

	public void setDist(int t) {
		this.dist=t;
	}
	/**
	 *Checks whether the vertex is equal to the object we got in the function.
	 * @param o
	 * @return true if the vertices are equal and false if not.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		nodeData nodeData = (nodeData) o;
		return key == nodeData.key && Double.compare(nodeData.weight, weight) == 0 && tag == nodeData.tag && dist == nodeData.dist && Objects.equals(info, nodeData.info) && location.equals(nodeData.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, weight, info, tag, dist, location);
	}
}
