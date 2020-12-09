import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.util.Stack;
import jdg.graph.*;
import java.util.HashMap;

/**
 * Main class providing tools for computing an upward grid drawing of a directed graph
 * that minimizes the number of crossings
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class UpwardDrawing {
    /** input graph to draw */
    public DirectedGraph g=null;
    /** height of the grid (input parameter) */
    public int height;
    /** width of the grid (input parameter) */
    public int width;
    
    
    public int iterations=100; //describing the number of iterations in the force directed heuristic search
    
    
    /** Attractive force function relative to the distance 
     * 
     */
    public double attractive_force(double d, double k )
    {
    	return d*d/k;
    }
    
    
    /** Repulsive force function relative to the distance
    */ 
    public double repulsive_function(double d, double k )
    {
    	return (d==0)? k*k/(d) : width*k*k; //The repulsive force is very big if the vertices are in the same place.
    }
    
    
    /**
     * initialize the input of the program
     */
    public UpwardDrawing(DirectedGraph g, int width, int height) {
    	this.g=g;
    	this.width=width;
    	this.height=height;
    }
    
    
    /**Sets the tags for all the vertices of the graph (it indicates the longest path in the graph that finishes in that vertice**/
    public void SetTag() {
    	ArrayList<Node> roots = new ArrayList<Node>() ;  // roots will get exactly all the roots of the graph
    	for (Node n : this.g.vertices) {
    		if (n.predecessors.isEmpty()) {
    			roots.add(n) ;
    		}	
    	}
    	Stack<paire> pile = new Stack<paire>() ;  //stack made for the generalized depth first search (DFS)
    	for (Node n : roots) {
    		pile.add(new paire(n,0)) ;          
    	}                                         //initialized with all the roots in the stack
    	Node e ;
    	int n ;
    	while (!pile.empty()) {
    		e = pile.peek().e ;
    		n = pile.peek().n ;                  //the integer indicates the position in the adjacence list of the next successor to see
    		if (n >= e.successors.size()) {
    			pile.pop();                      //we pop a node if all its successors have been seen
    		}
    		else {
    			Node f = e.successors.get(n) ;
    			pile.peek().n += 1 ;
    			if (f.tag <= e.tag) {
    				f.tag = e.tag + 1 ;
    				pile.push(new paire(f,0)) ;
    			}
    		}
    	}
    	for (Node m : this.g.vertices) {
    		if (g.maxtag<m.tag) g.maxtag=m.tag;  //g.maxtag : attribute of graph g equals to the maximum tag in the graph
    		//System.out.println(m) ;
    	}
    }
    
    
    /**
     * Return the number of edge crossings with an optimisation consisting of considering only edges starting at a certain point
     */
    public int computeCrossings() {
    	// COMPLETE THIS ME
    	int count=0;
    	double y;
    	double x;
    	ArrayList<ArrayList<Node>> etages= new ArrayList<ArrayList<Node>>(); //etages will contain all the nodes, at different position reflecting their y-coordinate
    	/* initializing the array with empty arrays */
    	for (int i=0; i<height+1; i++)
    		etages.add(new ArrayList<Node>());
    	//System.out.println(etages);
    	for ( Node a : this.g.vertices)
    	{
    		etages.get(a.p.y).add(a);                      //we add a in etages at the right place
    		
    	}
    	//System.out.print(etages);
    	for( ArrayList<Node> listetage : etages )
    	{
    		for (Node a : listetage)
    		{
    			for (Node b: a.successors)
    			{ 
    				//We only check the edges whose starting node is between x.p.y+1 and y.p.y
    				for (int i=a.p.y+1 ; i<b.p.y; i++)
    				{   
    					for (Node c : etages.get(i))
    					{
    						for (Node d : c.successors )
    						{
    							y= c.p.x-a.p.x+(a.p.y*(b.p.x-a.p.x)/(double)(b.p.y-a.p.y))-(c.p.y*(d.p.x-c.p.x)/(double)(d.p.y-c.p.y));
    							//System.out.print(y);
    							if ((double)((b.p.x-a.p.x))/(b.p.y-a.p.y)-(double)((d.p.x-c.p.x))/(d.p.y-c.p.y) != 0)
    							{
    							//System.out.print((b.p.x-a.p.x)/(b.p.y-a.p.y)-(d.p.x-c.p.x)/(d.p.y-c.p.y));
    							y= y/((double)((b.p.x-a.p.x))/(b.p.y-a.p.y)-(double)((d.p.x-c.p.x))/(d.p.y-c.p.y) );
    							x = (d.p.x-c.p.x)/(d.p.y-c.p.y)*(y-c.p.y)+c.p.x; 
    							/**
    							System.out.print(a);
    							System.out.print(b);
    							System.out.print(c);
    							System.out.print(d);
    							System.out.print(y);
    							System.out.println(x); 
    							
    							*/
    							
    							if ((y-a.p.y)*(y-b.p.y)<0 & (y-c.p.y)*(y-d.p.y)<0)
    								{count++;
    								//System.out.println(count);
    								}
    							}
    						}
    							
    					}
    				}					
    			
    				for (Node c : listetage)
					{ 
    					if (c.p.x<a.p.x)
					{
						for (Node d : c.successors )
						{
							y= c.p.x-a.p.x+a.p.y*(b.p.x-a.p.x)/(b.p.y-a.p.y)-c.p.y*(d.p.x-c.p.x)/(d.p.y-c.p.y);
							if ((double)((b.p.x-a.p.x))/(b.p.y-a.p.y)-(double)((d.p.x-c.p.x))/(d.p.y-c.p.y)  != 0) // The two edges can't have the same slope
							{
							y=y/((double)((b.p.x-a.p.x))/(b.p.y-a.p.y)-(double)((d.p.x-c.p.x))/(d.p.y-c.p.y) );
							x = (d.p.x-c.p.x)/(d.p.y-c.p.y)*(y-c.p.y)+c.p.x;
							/**
							System.out.print(a);
							System.out.print(b);
							System.out.print(c);
							System.out.print(d);
							System.out.print(y);
							System.out.println(x);
							*/
							if ((y-a.p.y)*(y-b.p.y)<0 & (y-c.p.y)*(y-d.p.y)<0)
							{
								count++;
								//System.out.println(count);
								
							}
							}	
					}
					}
					}
    	 
    		} } }
    	//System.out.println("this was the etages");
    	
    	return count;
    	}
    
    
    /**Another function that computes crossings in the graph using a HashMap that stores the intersection of each edge 
     * 
     * @return
     */
    public int computeCrossingsv2()
    {	int count=0;
    	for (Node n: g.vertices)
    	{
    		for (Node s: n.successors)
    		{
    			count += this.intersections(new Pair<Node,Node> (n,s)).size();
    		}
    			
    	}
    	return count/2;
    }

    
    /**
     * Check whether the graph embedding is a valid upward drawing <br>
     * -) the drawing must be upward <br>
     * -) the integer coordinates of nodes must lie in the prescribed bounds: the drawing area is <em>[0..width] x [0..height]</em> <br>
     * -) non adjacent crossing edges must intersect at their interiors
     */
    public boolean checkValidity() {

    	for ( Node a : g.vertices)
    	{
    		if (a.p.x> width | a.p.y > height )
    		{    
    			
    			System.out.println("The graph is too big");
    			System.out.println(a);
    			return false;
    		}
    	}
    	for (Node a : g.vertices)
    	{
    		for (Node b : a.successors)
    		{
    			if (a.p.y >= b.p.y) 
    				{
    				System.out.println("Some edges don't ascend strictly");
    				return false;
    				}
    		}
    	}
    	for (Node a : g.vertices)
    	{
    		for (Node b : a.successors )
    		{
    			for (Node x: g.vertices)
    			{
    			if (x.p.is_in(a.p,b.p) )    //is_in is a method defined in GridPoint_2, and return whether a gridpoint belongs to the segment defined by two others or not
    			{
    				System.out.println("non adjacent crossing edges must intersect at their interiors");
    				//System.out.print(x.p);			
    				
    				return false;
    			}
    			}
    			
    		}
    	}
    	
    	//Checking if now two node occupy the same place with an unoptimal way without using the table
    	
    	for (Node n: g.vertices)
    	{
    		for (Node m: g.vertices)
    		{
    			if (n!=m & n.compareTo(m))
    				return false;
    		}
    	}
    	return true;
    	//throw new Error("To be completed");
    }
    
    
    /** returns a list of nodes that don't have a predecessor (useful for drawing the graph
     */
    public ArrayList<Node>  root()
    {
   	 ArrayList<Node> result = new ArrayList<Node> ();
   	 //boolean [] mem = new boolean [ g.sizeVertices() ];
   	 for (int i=0; i< g.sizeVertices() ; i++)
   	 {
   		 //mem[i]= true;
   		 if (g.getNode(i).predecessors.isEmpty())
   			 {result.add(g.getNode(i));
   			 g.getNode(i).setColor(100,110,0);
   			 }
   	 }
   	 return result;
   	 
    }
    
    
    /**Initializes the position_map attribute that contains the edges based on their lower and higher node y-coordinates
     * It will be used later
     */
    public void initialize_position()
    {	
    	Pair<Integer, Integer> mapaire;
    	for (Node node: g.vertices)
    	{
    		for (Node b: node.successors)
    		{	
    			mapaire= new Pair<Integer,Integer> (node.p.y, b.p.y);
    			//System.out.print(g.position_map);
    			if ( g.position_map.containsKey(mapaire) && g.position_map.get(mapaire) != null)
    			{	
    				
    				g.position_map.get(mapaire).add(new Pair<Node,Node> (node,b));
    			}
    			else
    			{	
    				g.position_map.put(mapaire,new ArrayList<Pair<Node,Node>>());
        			g.position_map.get(mapaire).add(new Pair<Node,Node> (node,b));
    				
    			}
    		}
    	}
    	//System.out.println(g.position_map);
    }
    

    /**
     * Compute a valid initial layout, satisfying the prescribed requirements according to the problem definition <br>
     * 
     * Remark: the vertex coordinates are stored in the class 'Node' (Point_2 'p' attribute)
     */
    public void computeValidInitialLayout() {
    	int levelheight= (height+1)/(g.maxtag+1); //This variable represents the height allocated for vertices of a given tag (level)
    	//System.out.println(levelheight);
    	this.SetTag(); 
    	for (Node node: g.vertices) //Assigns a uniformly random point depending  on the tag of the point representing its "level"  
    	{
    		node.setPoint(new GridPoint_2((int)(Math.random()*(this.width+1)), (int)((node.tag+1/4)*levelheight+Math.random()*levelheight/4)));
    		System.out.println(node);
    	}
    	
    		for (Node node :g.vertices)
    		{
    			while(this.HasProblem(node))
    			{
    				
    				node.setPoint(new GridPoint_2((int)(Math.random()*(this.width+1)), (int)((node.tag+1/4)*levelheight+Math.random()*levelheight/4)));
    				System.out.println(node);
    			}
    			
    		}
    	

    	//if (!this.checkValidity())
    	//	this.computeValidInitialLayout();
    	//System.out.println(levelheight);
    	//throw new Error("To be completed");
    }
    
    
    
    /**Returns True if node is misplaced either on an edge or on another node, or on of his edges contain a node in their interiors.
     */
    public boolean HasProblem(Node node)
    {
    	//boolean resu=false;
		for (Node x: g.vertices)
		{
			if ( x!=node && node.compareTo(x) )
			{
				return true;
			}
		}
		return !(this.is_over_an_edge(node.p)) || !(this.HasNodeOnPredecessors(node)) || !(this.HasNodeOnSuccessors(node));
    }
    
    
    
    /** Returns true if a point of the Graph is over an edge (with good complexity)
     * 
     * @param p
     * @return boolean if the node is on an edge
     */
    public boolean is_over_an_edge(GridPoint_2 p)
    {
    	for (int y1=0; y1<p.y; y1++ )                     //optimisation in the edges we consider
    	{
    		for (int y2=p.y+1; y2<height+1; y2++)
    		{
    			if (g.position_map.containsKey(new Pair <Integer, Integer>(y1,y2)))
				{ 
    				for (Pair<Node,Node> b : this.g.position_map.get(new Pair<Integer,Integer>(y1,y2))) {
    						if (p.is_in(b.getLeft().p, b.getRight().p)) {
    							return false ;
    						}
			} 
    		}
    	}
    }
    
    	return true;
    }
    
    
    /**Returns True is the edges going to successors of the Node node from it don't contain a vertice in their interior
     * 
     * @param node
     * @return
     */
    public boolean HasNodeOnSuccessors(Node node)
    {
    	for (Node x: node.successors)
    	{
    		for (Node y: g.vertices)
    		{
    			if (y.p.is_in(node.p, x.p)) return false;
    		}
    	}
    	return true;
    }
    
    /**Returns True is the edges going from predecessors of the Node node to it don't contain a vertice in their interior
     * 
     * @param node
     * @return
     */
    public boolean HasNodeOnPredecessors(Node node)
    {
    	for (Node x: node.predecessors)
    	{
    		for (Node y: g.vertices)
    		{
    			if (y.p.is_in(x.p, node.p)) return false;
    		}
    	}
    	return true;
    }
    
    
    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     * tentative is the parameter c in the report ; it is the number of times we can try to move a node without success in a row
     */

    public void localSearchHeuristic(int tentatives) {
    	/* dic : dictionnary edge -> list of edges that intersect it + the number of edges that intersect it
    	* 
    	*/
    	HashMap <Pair<Node,Node>, Pair<ArrayList<Pair<Node,Node>>,Integer >> dic =new HashMap <Pair<Node,Node>, Pair<ArrayList<Pair<Node,Node>>,Integer>> () ;
    	Pair<Node,Node> arrete ;
    	ArrayList<Pair<Node,Node>> liste_inter; // A list that contains the edges intersecting every edge
    	for (Node n : this.g.vertices) {
    		for (Node m : n.successors) {
    			arrete = new Pair<Node, Node> (n,m) ;
    			liste_inter = this.intersections(arrete) ;
    			int p = liste_inter.size() ;
    			dic.put(arrete, new Pair<ArrayList<Pair<Node,Node>>,Integer >(liste_inter,p)) ;
    		}
    	}
    	System.out.println(dic);
    	/*
    	* dic2 : dictionnary node -> number of intersection on "its" edges
    	*/
    	HashMap <Node, Integer> dic2 =new HashMap  <Node, Integer> () ;
    	int M = 0 ;
    	int r; // compteur du nombre d'intersection liée à un sommet
    	for (Node n : this.g.vertices) {
    		r = 0 ;
    		for (Node m : n.successors) {
    			arrete = new Pair<Node,Node>(n,m) ;
    			r += dic.get(arrete).getRight() ;
    		}
    		for (Node m : n.predecessors) {
    			arrete = new Pair<Node,Node> (m,n) ;
    			r += dic.get(arrete).getRight() ;
    		}
    		dic2.put(n,r) ;
    		M = Math.max(M,r) ;
    	}
    	/*
    	* etages_inter contient les sommets du graphe rangés en fonction du nombre r précédemmment défini i.e le nombre d'intersections que font les arrêtes de ce sommet
    	* M est le nombre maximal, utilisé pour construire l
    	*/
    	ArrayList<ArrayList<Node>> etages_inter =new ArrayList<ArrayList<Node>>() ;
    	for (int i=0; i<M+1; i++) {
    		etages_inter.add(new ArrayList<Node>()) ;
    	}
    	for (Node n : this.g.vertices) {
    		r = dic2.get(n) ;
    		etages_inter.get(r).add(n) ;
    	}
    	int d = tentatives ; // sert pour vérifier qu'on ne recommence pas plus de c fois d'affilés
    	ArrayList<Node> exclus = new ArrayList<Node>() ;
    	//System.out.println(etages_inter);
    	while (M>=0) {  
    		//System.out.println(d);
    		//System.out.println(etages_inter);
    		if (etages_inter.get(M).isEmpty()) {
    			M -= 1 ;
    			
    		}
    		else {
    			if (d == 0) {
    				exclus.add(etages_inter.get(M).get(0)) ;
    				etages_inter.get(M).remove(0) ;
    				d = tentatives ;
    			}
    			else {
    				Node n = etages_inter.get(M).get(0) ;
    				int ymin = -1 ; // ordonnées du plus bas des successeurs et du plus haut des prédecesseurs
    				int ymax = this.height + 1 ;
    				for (Node m : n.predecessors) {
    					ymin = Math.max(m.p.y, ymin) ;
    				}
    				for (Node m : n.successors) {
    					ymax = Math.min(m.p.y, ymax) ;
    				} 
    				int x = n.p.getX() ;  //coordonnées actuelles de n
    				int y = n.p.getY() ;
    			/*
    			*  (x,y) tiré au hasard 
    			*  y > ymin et < ymax
    			*  
    			*  puis on vérifie que ce n'est pas un sommet du graphe
    			*/
    				double a = Math.random();
    				double b = Math.random();
    				int z = 1 + ymin + (int) (a*(ymax - ymin - 1)) ; //random int in [ymin +1 ; ymax -1]
    				int t = (int) (b*(this.width+1)) ;               // random int in [0 ; width]
    				n.p.setX(t);
    				n.p.setY(z);

    				//tester s'i y a un autre point dans la même zone de la grid
    				boolean test1 = true ;
    				for (Node m : this.g.vertices) {       // à changer : utiliser un tableau de booléens
    					if ((m != n) && (n.compareTo(m))) {
    						test1 = false ;
    					}
    				}
    				if (!test1) {
    				/*
    				* remttre n comme avant
    				*/
    					n.p.setX(x);
    	    			n.p.setY(y);
    	    			d--;
    				}
    				
    				
    				else {
    					if (! this.HasNodeOnPredecessors(n)|| ! this.HasNodeOnSuccessors(n) || ! this.is_over_an_edge(n.p))
    					{	
    						//remettre n comme avant
    						n.p.setX(x);
        	    			n.p.setY(y);
        	    			d--;
    					}
    					else
    					{
    					int s = 0;
    					//Calculer le nombre d'intersection sur les nouvelles arrêtes après avoir bougé notre point
   						for (Node q : n.successors) {
   							arrete = new Pair<Node,Node>(n,q) ;
    						s += this.intersections(arrete).size() ;
    					}
    					for (Node q : n.predecessors) {
    						arrete = new Pair<Node,Node>(q,n) ;
   							s += this.intersections(arrete).size() ;
    					}
    					if (s >= dic2.get(n)) {
    					/*
    					* remettre n comme avant
    					*/
    						n.p.setX(x);
       	    				n.p.setY(y);
   							d--;
    					}
    					else {
    					/*
    					* tout mettre à jour
    					* + valeur de c "à 0"
    					*/
    						d = tentatives ;
    						etages_inter.get(M).remove(0) ;
   							etages_inter.get(s).add(n) ;
    						dic2.replace(n, s) ;
    						for (Node q : n.successors) {
    							arrete = new Pair<Node,Node>(n,q) ;
    							dic.replace(arrete, new Pair<ArrayList<Pair<Node,Node>>,Integer >(this.intersections(arrete),this.intersections(arrete).size())) ;
    						}
   							for (Node q : n.predecessors) {
   								arrete = new Pair<Node,Node>(q,n) ;
   								dic.replace(arrete,new  Pair<ArrayList<Pair<Node,Node>>,Integer>(this.intersections(arrete),this.intersections(arrete).size())) ;
   							}
    					/*
    					* parcourir le dictionnaire pour mettre à jour les listes
    					*/
    						for (Node q : this.g.vertices) {
    							if (q!=n) {
    								/**for (Node w : q.successors) {
    									if (w!=n) {
   											arrete = new Pair<Node,Node>(q,w) ;  // arrete non liée à n ; à mettre à jour
   											ArrayList<Pair<Node,Node>> lis = dic.get(arrete).getLeft() ;
    										for (Pair<Node,Node> h : lis) { // regarder l'évolution pour les arretes liées à n qui l'intersectaient
    											if (h.getLeft()==n || h.getRight()==n) {
    												if (!dic.get(h).getLeft().contains(arrete)) {
    													lis.remove(h) ;
    												}
    											}
   											}
    									}
    								}*/
    								for (Node w : q.predecessors) {
    									if (w!=n) {
    										arrete = new Pair<Node,Node>(w,q) ;  // arrete non liée à n ; à mettre à jour
    										ArrayList<Pair<Node,Node>> lis = dic.get(arrete).getLeft() ;
    										int ind=0;
    										while (ind < dic.get(arrete).getLeft().size()) { // regarder l'évolution pour les arretes liées à n qui l'intersectaient
   												if (lis.get(ind).getLeft()==n || lis.get(ind).getRight()==n) {
   													//System.out.println(lis.get(ind));
   													//System.out.println(dic.get(lis.get(ind)));
   													if (! dic.get(lis.get(ind)).getLeft().contains(arrete)) {
   														dic.get(arrete).getLeft().remove(lis.get(ind)) ;
    												}
   													else ind++;
    											}
   												else ind++;
    										}
    									}
    								}
    							}
    						}

    							
    							/*
    							* reste à rajouter aux listes les nouvelles intersections
    							*/
    						for (Node w : n.successors) {
    							arrete = new Pair<Node,Node>(n,w) ;
    							ArrayList<Pair<Node,Node>> lis = dic.get(arrete).getLeft() ;
    							for (Pair<Node,Node> h : lis) {
    								if (!dic.get(h).getLeft().contains(arrete)) {
										dic.get(h).getLeft().add(arrete) ;
									}
    							}
    						}
    						for (Node w : n.predecessors) {
    							arrete = new Pair<Node,Node>(w,n) ;
    							ArrayList<Pair<Node,Node>> lis = dic.get(arrete).getLeft() ;
   								for (Pair<Node,Node> h : lis) {
    								if (!dic.get(h).getLeft().contains(arrete)) {
										dic.get(h).getLeft().add(arrete) ;
									}
    							}
    						}
    							/*
    							* mise à jour de la longueur des listes
    							*/
    						for (Node q : this.g.vertices) {
    							for (Node w : q.successors) {
    								arrete = new Pair<Node,Node>(q,w) ;
    								ArrayList<Pair<Node,Node>> lis = dic.get(arrete).getLeft() ;
    								int u = lis.size() ;
    								dic.replace(arrete, new Pair<ArrayList<Pair<Node,Node>>,Integer>(lis,u)) ;
   								}
   								for (Node w : q.predecessors) {
   									arrete = new Pair<Node,Node>(w,q) ;
   									ArrayList<Pair<Node,Node>> lis = dic.get(arrete).getLeft() ;    									int u = lis.size() ;
    								dic.replace(arrete, new Pair<ArrayList<Pair<Node,Node>>,Integer>(lis,u)) ;
    							}
    						}
    							/*
    							* mise à jour des sommets dans le dic2 et dans la liste l
    							*/
    						for (Node q : this.g.vertices) {
    							if (!exclus.contains(q)) {
    								int k = 0 ;
        							for (Node w : q.successors) {
        								arrete = new Pair<Node,Node>(q,w) ;
        								k += dic.get(arrete).getRight() ;
        							}
        							for (Node w : q.predecessors) {
        								arrete = new Pair<Node,Node>(w,q) ;
        								k += dic.get(arrete).getRight() ;
        							}
        							if (dic2.get(q) != k) {
        								etages_inter.get(dic2.get(q)).remove(q) ;
        								if (k<etages_inter.size()) {
       										etages_inter.get(k).add(q) ;
       									}
       									else {
       										ArrayList<Node> lis =new ArrayList<Node> ();    										
       										lis.add(q) ;
        									etages_inter.add(k, lis) ;
        								}
        								dic2.replace(q, k) ;
        								M = Math.max(M, k) ;
        							}
    							}
    						}
    					}
    				}
    			}}
    				
    		}
    	}
    }

    
    
    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     * 
     * This function doesn't work properly for it tends to have big forces and displacement and puts very quickly all the vertices at the extremities. We didn't have time to debug the function. 
     */
    public void forceDirectedHeuristic(double temp) {
    	// YOU ARE FREE to choose to implement this function
    	double norm= 0;
    	double diffx=0;
    	double diffy=0;
    	boolean possible_disp=true;
    	double k=  (Math.sqrt((double)(height)*width/ g.vertices.size()));
    	System.out.println(k);//useful variable in the force_directed heuristic expressing the mean value of the distance between two vertices
    	for (int i=0; i<iterations && temp>0 ; i++)
    	{	//We calculate the forces that we consider as being proportional to the displacement
    		for (Node v : g.vertices)
    		{
    			v.disp.x=0;
    			v.disp.y=0;
    			for (Node u: g.vertices)
    			{
    				if (u != v)
    				{
    					diffx=v.p.x-u.p.x;
    					diffy=v.p.y-u.p.y;
    					norm= Math.sqrt(diffx*diffx+diffy*diffy);
    					v.disp.setX(v.disp.x+diffx/norm*this.repulsive_function(norm,k));
    					v.disp.setY(v.disp.y+diffy/norm*this.repulsive_function(norm,k));
    				}
    				
    			}
    			
    			for (Node u: v.successors)
    			{
    				diffx=v.p.x-u.p.x;
					diffy=v.p.y-u.p.y;
					norm= Math.sqrt(diffx*diffx+diffy*diffy);
    				v.disp.setX(v.disp.x-diffx/norm*this.attractive_force(norm,k));
    				u.disp.setX(v.disp.x+diffx/norm*this.repulsive_function(norm,k));
    				v.disp.setY(v.disp.y-diffy/norm*this.attractive_force(norm,k));
    				u.disp.setY(v.disp.y+diffy/norm*this.repulsive_function(norm,k));
    			}
    		}
    		
    		//We effectively displace the vertices whenever we can
    		for (Node v: g.vertices)
    		{	
    			possible_disp=true;
    			v.disp.setX((int) v.disp.x);
    			System.out.println(v.disp.x);
    			v.disp.setY( (int) v.disp.y);
    			System.out.println(v.disp.y);
    			//We prevent a vertice to get over his successor
    			for (Node u: v.successors)
    			{
    				if (v.p.y+v.disp.y >= u.p.y)
    				{
    					possible_disp=false;
    					break;
    				}
    			}
    			// We prevent a vertice to get under his predecessor
    			for (Node u: v.predecessors)
    			{
    				if (v.p.y+v.disp.y <= u.p.y)
    				{
    					possible_disp=false;
    					break;
    				}
    			}
    			//We prevent from being displaced outside frame
    			if (v.p.y+v.disp.y >= height )
    				{v.p.y=height; possible_disp=false;}
    			if (v.p.y+v.disp.y<=0)
    				{v.p.y=0; possible_disp=false;}
    			if (v.p.x+v.disp.x >= width )
    				{v.p.x=width; possible_disp=false;}
    			if (v.p.x+v.disp.x<=0)
    				{v.p.x=0; possible_disp=false;}
    			if (possible_disp)
    				{	
    					int x=v.p.x;
    					int y= v.p.y;
    					int a= (v.disp.x > temp) ? v.p.x+ (int) temp: v.p.x+ (int) (v.disp.x);
    					int b= (v.disp.y > temp) ? v.p.y+ (int) temp: v.p.y+ (int) (v.disp.y);
    					v.p.x=a;
						v.p.y=b;
						System.out.print(this.HasProblem(v));
    					if  (this.HasProblem(v))
    					{
    						v.p.x=(int)x;
    						v.p.y=(int) y;
    					}
    				}

    		}  
    		temp=temp/2;
    	}
		//We prevent a node to be at the interior of an edge
    	for (Node v: g.vertices)
    	{
		for (Node a: g.vertices)
		{
			for (Node b: a.successors)
			{
				if (v.p.is_in(a.p,b.p))
				{
					v.p.x=( v.p.x<width-1) ? v.p.x+1: v.p.x-1;
					break;
				}
			}
		}
    	}
    }

    
    
    /**
     * Main function that computes a valid upward drawing that minimizes edge crossings. <br>
     * This function choses to run the local heuristic that reduces the number of crossings in the graph
     * You are free to use and combine any algorithm/heuristic to obtain a good upward drawing.
     */
    public void computeUpwardDrawing() {
    	System.out.print("Compute a valid drawing with few crossings: ");
    	long startTime=System.nanoTime(), endTime; // for evaluating time performances
    	
    	// COMPLETE THIS METHOD
    	System.out.println("TO BE COMPLETED");
    	this.localSearchHeuristic(4);
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
    	System.out.println("Elapsed time: "+duration+" seconds");
    }

    
    /**
     * Check whether the current graph is provided with an embedding <br>
     * -) if all nodes are set to (0, 0): the graph has no embedding by definition <br>
     * -) otherwise, the graph has an embedding
     */
    public static boolean hasInitialLayout(DirectedGraph graph) {
    	for(Node v: graph.vertices) {
    		if(v.getPoint().getX()!=0 || v.getPoint().getY()!=0)
    			return true;
    	}
    	return false;
    }
    
    
    
   /** public static  void main( String args[] )
    {
    	
    }*/
    /**Returns True if two edges edges e and f intersect in their interior 
     * 
     * @param e edge formed with two nodes
     * @param f another edge
     * @return true if theu intersect
     */
    public boolean intersect(Pair<Node,Node> e, Pair<Node,Node> f) {
    	Node a = e.getLeft() ;
    	Node b = e.getRight() ;
    	Node c = f.getLeft() ;
    	Node d = f.getRight() ;
    	boolean rep = false ;

    	
    	double y= c.p.x-a.p.x+(a.p.y*(b.p.x-a.p.x)/(double)(b.p.y-a.p.y))-(c.p.y*(d.p.x-c.p.x)/(double)(d.p.y-c.p.y));
    	if ((double)((b.p.x-a.p.x))/(b.p.y-a.p.y)-(double)((d.p.x-c.p.x))/(d.p.y-c.p.y) != 0) {
            y= y/((double)((b.p.x-a.p.x))/(b.p.y-a.p.y)-(double)((d.p.x-c.p.x))/(d.p.y-c.p.y) );
            // x = (d.p.x-c.p.x)/(d.p.y-c.p.y)*(y-c.p.y)+c.p.x; 
            if ((y-a.p.y)*(y-b.p.y)<0 & (y-c.p.y)*(y-d.p.y)<0) {
            	rep = true ;
            }

    		
    	}

    	
    	return rep ;
	
    }
    
    
    /*
     * return the list of all edges that intersect the edge a in the graph
     * 
     */
    public ArrayList<Pair<Node,Node>> intersections(Pair<Node,Node> a) {
    	Node e = a.getLeft() ;
    	Node f = a.getRight() ;
        //assert (f.isSuccessorOf(e)) ;
        int y_start = e.p.getY() ;
        int y_end = f.p.getY() ;
        ArrayList<Pair<Node,Node>> l= new ArrayList<Pair<Node,Node>>() ;
        for (int x = 0; x < y_end; x++) {
        	for (int y = Math.min(x,y_start) + 1; y < this.height+1; y++) { 
        		if (g.position_map.containsKey(new Pair <Integer, Integer>(x,y)))
        				{ 
        			for (Pair<Node,Node> b : this.g.position_map.get(new Pair<Integer,Integer>(x,y))) {
        				if (this.intersect(a, b)) {
        					l.add(b) ;
        				}
        			} 
        				}
        		
        	}
        }
       // System.out.println(a);
        //System.out.println(l);
        return l ;
    }
    

}

