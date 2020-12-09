Please Run the test class with the graph as an argument. That class contains the main function with the right order of things to execute. 
We start by setting tags by the function SetTag. Tags of nodes are attributes that define the length of the longest path to get to this node. Such information helps us to compute a valid initial layout very efficiently.
Please notice that there are 2 versions of compute intersection, using the same idea but different implementations. 
We had some problems with ForceDirected function, you will find however an implementation of it in UpwardDrawing. The best function that reduces the number of intersections for us is the localSearchHeuristic.

