import java.util.ArrayList;
import jdg.graph.Pair;

import jdg.graph.DirectedGraph;
import jdg.graph.Node;
import jdg.io.GraphLoader;
import jdg.io.GraphReader_Json;
import jdg.io.GraphWriter_Json;
import processing.core.PApplet;
public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Tools for the \"Graph Drawing Contest 2020: Live Challenge\"");
		if(args.length<1) {
			System.out.println("Error: one argument required: input file in JSON format");
			System.exit(0);
		}
		
		String inputFile=args[0]; // input file, encoded as JSON format
		DirectedGraph g=GraphLoader.loadGraph(inputFile); // input graph
		int[] drawingBounds=GraphReader_Json.readDrawingAreaBounds(inputFile); // reading width and height of the drawing area
		UpwardDrawing up = new UpwardDrawing(g,600, 600);
		//System.out.println(up.checkValidity());
		//System.out.print(up.height);
		//up.computeCrossings();
		//System.out.print("Le nombre d'intersection est: ");
		//System.out.println(up.computeCrossings());
		// set the input parameters for drawing the graph layout
		GraphRenderer.sizeX=600; // setting canvas width (number of pixels)
		GraphRenderer.sizeY=600; // setting canvas height (pixels)
		GraphRenderer.inputGraph=g; // set the input graph (for rendering)
		GraphRenderer.drawingWidth=drawingBounds[0]; // setting the width of the drawing area
		GraphRenderer.drawingHeight=drawingBounds[1];  // setting the width of the drawing area
		
		if(UpwardDrawing.hasInitialLayout(g)) // check whether the nodes are provided with an initial embedding
			System.out.println("The input graph has an initial embedding")
			;
		else
			System.out.println("The input graph is not provided with an initial embedding");
		
		// initialize the upward drawing
		
		UpwardDrawing ud=new UpwardDrawing(g, GraphRenderer.drawingWidth, GraphRenderer.drawingHeight);	
		ud.SetTag();
		ud.initialize_position();
		ud.computeValidInitialLayout();
		System.out.println(ud.computeCrossings());// first phase: compute a valid drawing (if necessary)
		//ud.forceDirectedHeuristic(10);
		ud.computeUpwardDrawing();// second phase: minimize the number of crossings
		
		System.out.println(ud.computeCrossings());
		//ud.forceDirectedHeuristic(100);
		//System.out.println(g.vertices);
		//boolean isValid=ud.checkValidity(); // check whether the result is a valid drawing
		//int crossings=ud.computeCrossings(); // count the number of crossings
		
		// write the upward drawing computed by your program to a Json file
		GraphWriter_Json.write(g, GraphRenderer.drawingWidth, GraphRenderer.drawingHeight, "output.json");
		
		// uncomment the line below to show a 2D layout of the graph
		PApplet.main(new String[] { "GraphRenderer" }); 
		// launch the Processing viewer

	}

}
