package jdg.graph;

public class GridPoint_2_double {
	  public double x,y;

	  public GridPoint_2_double() {}
	  
	  public GridPoint_2_double(double x,double y) { 
	  	this.x=x; 
	  	this.y=y;
	  }

	  public GridPoint_2_double(GridPoint_2_double p) { 
	  	this.x=p.x; 
	  	this.y=p.y; 
	  }

	  public double getX() {return x; }
	  public double getY() {return y; }
	  
	  public void setX(double x) {this.x=x; }
	  public void setY(double y) {this.y=y; }
	    
	  public void translateOf(GridVector_2 v) {
	    this.x=this.x+v.x;
	    this.y=this.y+v.y;
	  }

	  public boolean equals(Object o) {
		  if (o instanceof GridPoint_2) {
			  GridPoint_2 p = (GridPoint_2) o;
			  return this.x==p.x && this.y==p.y; 
		  }
		  throw new RuntimeException ("Method equals: comparing Point_2 with object of type " + o.getClass());  	
	  }

	  public int hashCode () {
		 return (int)(this.x*this.x + this.y);
	  }

	  public double distanceFrom(GridPoint_2 p) {
	    return Math.sqrt((double)this.squareDistance(p));
	  }
	  
	  public double squareDistance(GridPoint_2 p) {
	    double dX=p.getCartesian(0)-x;
	    double dY=p.getCartesian(1)-y;
	    return dX*dX+dY*dY;
	  }

	  public String toString() {return "("+x+","+y+")"; }
	  public int dimension() { return 2;}
	  
	  public double getCartesian(int i) {
	  	if(i==0) return x;
	  	return y;
	  } 
	  public void setCartesian(int i, double x) {
	  	if(i==0) this.x=x;
	  	else this.y=x;
	  }

	  public void setOrigin() {
		  	this.x=0;
		  	this.y=0;
		  }
	    
	  //public GridVector_2 minus(GridPoint_2_double b){
	  //	return new GridVector_2(b.x-x, 
	  //						b.y-y);
	  //}
	  
	 // public GridPoint_2 sum(GridVector_2 v) {
	//	  	return new GridPoint_2(this.x+v.x,
	//	  						this.y+v.y);  	
	  //}
	  /** Verifies if the point is in the interior of the segment */
	 public boolean is_in(GridPoint_2 a, GridPoint_2 b )
	 {
		 if ((y-a.y)*(y-b.y)<0 && (x-a.x)*(y-b.y)-(y-a.y)*(x-b.x)==0)
			 return true;
		 return false;
			 
	 }
		/**
		 * Compare two points (lexicographic order on coordinates)
		 * @param o the point to compare
		 */
	  /*public int compareTo(Point_2 o) {
		  if (o instanceof Point_2) {	  
			  Point_2 p = (Point_2) o;
			  if(this.x<p.getX().doubleValue())
				  return -1;
			  if(this.x>p.getX().doubleValue())
				  return 1;
			  if(this.y<p.getY().doubleValue())
				  return -1;
			  if(this.y>p.getY().doubleValue())
				  return 1;
			  return 0;
		  }
		  throw new RuntimeException ("Method compareTo: comparing Point_2 with object of type " + o.getClass());  	
	  }*/
}
