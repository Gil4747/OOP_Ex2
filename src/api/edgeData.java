package api;

import java.util.Objects;

public class edgeData implements edge_data {
	private int nothing;
	private node_data src;
	private node_data dest;
	private String info;
	private int tag;
	private double weight;

	public edgeData(node_data src, node_data dest) {
		this.src = src;
		this.dest = dest;
		this.weight = src.getLocation().distance(dest.getLocation());
		this.tag = 0;
		this.info = "";
	}
	/**
	 * connect src to dest in some weight
	 * @param src
	 * @param dest
	 * @param w
	 */
	public edgeData(node_data src, node_data dest, double w) {
		this.src = src;
		this.dest = dest;
		this.weight = w;
		this.tag = 0;
		this.info = "";

	}
	/**
	 *
	 * @return key of src in the graph
	 */
	@Override
	public int getSrc() {
		return this.src.getKey();
	}
	/**
	 *
	 * @return key of dest in the graph
	 */
	@Override
	public int getDest() {
		return this.dest.getKey();
	}
	/**
	 *
	 * @return weight of the edge
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}
	/**
	 *
	 * @return info of the edge
	 */
	@Override
	public String getInfo() {
		return this.info;
	}
	/**
	 *  set info of the edge
	 * @param s
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;

	}
	/**
	 *
	 * @return tag of the edge
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
		this.tag = t;

	}
	/**
	 *Checks whether the arc is equal to the object that the function receives
	 * @param o
	 * @return true if the arches are equal and false if not.
	 *
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		edgeData edgeData = (edgeData) o;
		return nothing == edgeData.nothing && tag == edgeData.tag && Double.compare(edgeData.weight, weight) == 0 && src.equals(edgeData.src) && dest.equals(edgeData.dest) && Objects.equals(info, edgeData.info);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nothing, src, dest, info, tag, weight);
	}
}