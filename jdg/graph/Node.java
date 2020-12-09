package jdg.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for representing a node of a directed graph
 *
 * @author Luca Castelli Aleardi (INF421, 2019)
 */
public class Node {
	public GridPoint_2 p; // geometric coordinates
	public ArrayList<Node> successors=null; // list of successors vertices
	public ArrayList<Node> predecessors=null; // list of predecessors vertices
	public String label; // label of the node: vertex label can differ from vertex index
	public int tag; // an integer tag: useful for adding properties to nodes
	public int index; // an index from 0..n-1 (useful to index nodes)
	public Color color= new Color(100,100,100);
	public GridPoint_2_double disp; //The displacement vecotr at each iteration for the force directed algorithm
	    public Node(int index) { 
	    	this.successors=new ArrayList<Node>();
	    	this.predecessors=new ArrayList<Node>();
	    	this.p=new GridPoint_2();
	    	this.disp= new GridPoint_2_double();
	    	this.index=index;
	    	this.label=null;
	    	this.color=new Color(100,100,100);// no label defined
	    }
	    
	    public Node(int index, GridPoint_2 p, Color color) { 
	    	this.successors=new ArrayList<Node>();
	    	this.predecessors=new ArrayList<Node>();
	    	this.index=index;
	    	this.p=p;
	    	this.disp= new GridPoint_2_double();
	    	this.color=color;
	    	this.label=null; // no label defined
	    }

	    public Node(int index, GridPoint_2 p, Color color, String label) { 
	    	this.successors=new ArrayList<Node>();
	    	this.predecessors=new ArrayList<Node>();
	    	this.index=index;
	    	this.p=p;
	    	this.disp= new GridPoint_2_double();
	    	this.color=color;
	    	this.label=label;
	    }

	    public void addSuccessor(Node v) {
	    	if(v!=this && this.successors.contains(v)==false)
	    		this.successors.add(v);
	    }

	    public void addPredecessor(Node v) {
	    	if(v!=this && this.predecessors.contains(v)==false)
	    		this.predecessors.add(v);
	    }

	    /** check whether the current node is a predecessor of 'v' */
	    public boolean isPredecessorsOf(Node v) {
	    	return this.successors.contains(v);
	    }

	    /** check whether the current node is a successor of 'v' */
	    public boolean isSuccessorOf(Node v) {
	    	return this.predecessors.contains(v);
	    }
	    
	    public boolean isIsolated() {
	    	if(this.successors==null || this.predecessors==null)
	    		return true;
	    	if(this.predecessors.size()==0 && this.successors.size()==0)
	    		return true;
	    	return false;
	    }
	    
	    public void setColor(int r, int g, int b) {
	    	this.color=new Color(r, g, b);
	    }
	    
	    public List<Node> successorsList() {
	    	return this.successors;
	    }
	    
	    public List<Node> predecessorsList() {
	    	return this.predecessors;
	    }
	    
	    public void setTag(int tag) { 
	    	this.tag=tag;
	    }  
	    
	    public int getTag() { 
	    	return this.tag; 
	    }
	    
	    public GridPoint_2 getPoint() { 
	    	return this.p; 
	    }

	    public GridPoint_2 setPoint(GridPoint_2 q) { 
	    	return this.p=q; 
	    }

	    public void setLabel(String label) {
	    	this.label=label;
	    }
	    
	    public String getLabel() {
	    	return this.label;
	    }

	    public int degree() {
	    	return this.inDegree()+this.outDegree();
	    }
	    
	    public int outDegree() {
	    	return this.successors.size();
	    }
	    
	    public int inDegree() {
	    	return this.predecessors.size();
	    }
	    
	    public String toString(){
	        return "v: tag"+tag+" - id"+this.index+" - "+this.p;
	    }

	    public String fullToString(){
	        return "v"+this.index+" - "+this.p+" - label: "+this.label;
	    }

	    public int hashCode() {
	    	return this.index;
	    }
	    
	    public boolean compareTo(Node v) {
	    	/*
	   	* compare l'emplacement des noeuds
	   	*/
	    	int x = this.p.getX() ;
	    	int y = this.p.getY() ;
	    	int z = v.p.getX() ;
	    	int t = v.p.getY() ;
	    	return((x == z) && (y == t)) ;
	    }




}
