package gameClient;
import api.DWGraph_DS;
import api.edge_data;
import api.game_service;
import gameClient.util.Point3D;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	private int id;
	private boolean taken = false;

	public CL_Pokemon() {

	}

	/**
	 * This is one of the methods that help so spare the Fruit between the Robots
	 * @return the maximum value between all the fruits
	 */
//	public CL_Pokemon getMaxValue() {
//		Iterator<CL_Pokemon> fruitIterator =  myPokemons().iterator();
//		if (fruitIterator.hasNext()) {
//			Fruit max = fruitIterator.next();
////            while (!max.isTaken()&&fruitIterator.hasNext()){
////                max = fruitIterator.next();
////            }
//			while (fruitIterator.hasNext()){
//				Fruit temp = fruitIterator.next();
//
//				if(max.getValue()<temp.getValue()&&!temp.isTaken()){
//					max = temp;
//				}
//			}
//			// max.take();
//			return max;
//		}
//		return null;
//	}
	public ArrayList<CL_Pokemon> myPokemons(game_service game, DWGraph_DS g) {
		//graph = g;
		ArrayList<CL_Pokemon> myPoc = new ArrayList<>();
		int i = 0;
		Iterator<CL_Pokemon> p_iter = Arena.json2Pokemons(game.getPokemons()).iterator();
		while (p_iter.hasNext()) {
				CL_Pokemon p = p_iter.next();
				double value = p.getValue();
				int type = p.getType();

				Point3D position = new Point3D(p.getLocation().x(), p.getLocation().y());
				CL_Pokemon pokemon = new CL_Pokemon(p.getLocation(), type, value, i, p.get_edge());
				i++;
				myPoc.add(pokemon);
				pokemon.set_edge((pokemon.get_edge()));
			}
		return myPoc;

	}

	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}
	/**
	 *
	 * @return id of the fruit
	 */
	public int getId() {
		return id;
	}
	/**
	 * false if Not Taken and true if Taken
	 * @return boolean
	 */
	public boolean isTaken() {
		return taken;
	}

	/**
	 * if you take the fruit
	 */
	public void take(){
		this.taken = true;
	}
	public boolean inRange(int rx,int ry){
		return (_edge.getSrc()>rx && _edge.getSrc()<ry)||(_edge.getDest()>rx && _edge.getDest()<ry);
	}


}
