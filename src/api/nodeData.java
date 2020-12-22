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
	private static double maxY=10, maxX=10 , minX=-10, minY=-10;

	public nodeData(){
		this.key=id++;
		this.tag=0;
		this.dist=Integer.MAX_VALUE;
		this.info="";
		//this.location()
	}

	public nodeData(int key, geo_location location ){
		maxX = Math.max(location.x(),maxX);
		maxY = Math.max(location.y(),maxY);
		minX = Math.min(location.x(),minX);
		minY = Math.min(location.y(),minY);
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
	 public nodeData(node_data n){
	        id = n.getKey();
	        if (id <=n.getKey())
	            id = n.getKey()+1;
	        this.location = new geoLocation(n.getLocation().x(),n.getLocation().y());
	        maxX = Math.max(this.location.x(),maxX);
	        maxY = Math.max(this.location.y(),maxY);
	        minX = Math.min(this.location.x(),minX);
	        minY = Math.min(this.location.y(),minY);
	        weight = n.getWeight();
	        info = n.getInfo();
	        tag = n.getTag();
	    }


	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public geo_location getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public void setLocation(geo_location p) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight=w;
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
	public int getDist() {
		return this.dist;	 
	}

	public void setDist(int t) {
		this.dist=t;
	}
	 /**
    *
    * @return return max of location x of all the nodes
    */
   public static double getMaxX() {
       return maxX;
   }
	 /**
    *
    * @return return min of location x of all the nodes
    */
   public static double getMinX() {
       return minX;
   }
   /**
    *
    * @return return max of location y of all the nodes
    */

   public static double getMaxY() {
       return maxY;
   }
   /**
    *
    * @return return min of location y of all the nodes
    */
   public static double getMinY() {
       return minY;
   }

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
