package ctree.lgraph;

import java.util.*;

import ctree.graph.*;
import ctree.index.*;

/**
 * <p> Closure-tree</p>
 *
 * @author Huahai He
 * @version 1.0
 */
public class LGraph implements Graph {
  protected final static boolean isDirected=false;
  protected LVertex[] V;
  protected UnlabeledEdge[] E;
  protected String id;
  public double probOfGraph;
  
  public Map<String,Double> probMap = null;
  
  public LGraph() {
  }

  public LGraph(LVertex[] _V, UnlabeledEdge[] _E, String _id) {
    V = _V;
    E = _E;
    id = _id;
  }

  /**
   * @return Vertex[]
   */
  public Vertex[] V() {
    return V;
  }

  /**
   * @return Edge[]
   */
  public Edge[] E() {
    return E;
  }

  /**
   * @return id
   */
  public String id() {
    return id;
  }
  
  /**
   * adjMatrix
   *
   * @return int[][]
   */
  public int[][] adjMatrix() {
  	    
  	int[][] adj = new int[V.length][V.length];
    for (int i = 0; i < V.length; i++) {
      Arrays.fill(adj[i], 0);
    }
    for (int i = 0; i < E.length; i++) {
      adj[E[i].v1()][E[i].v2()] = 1;
    }
    if (!isDirected) {
      for (int i = 0; i < E.length; i++) {
        adj[E[i].v2()][E[i].v1()] = 1;
      }
    }
    return adj;
  }

  /**
   * adjList
   *
   * @return int[][]
   */
  public int[][] adjList() {
  	
    int n = V.length;
    LinkedList<Integer> [] llist = new LinkedList[n];
    for (int i = 0; i < n; i++) {
      llist[i] = new LinkedList<Integer> ();
    }
    for (UnlabeledEdge e : E) {
      llist[e.v1].add(e.v2);
      if (!isDirected) {
        llist[e.v2].add(e.v1);
      }
    }
    int[][] adjlist = new int[n][];
    for (int i = 0; i < n; i++) {
      adjlist[i] = new int[llist[i].size()];
      Iterator<Integer> it = llist[i].listIterator();
      int cnt = 0;
      while (it.hasNext()) {
        adjlist[i][cnt++] = it.next();
      }
    }
    return adjlist;

  }

  public Edge[][] adjEdges() {
    int n = V.length;
    LinkedList<Edge> [] llist = new LinkedList[n];
    for (int i = 0; i < n; i++) {
      llist[i] = new LinkedList<Edge> ();
    }
    for (UnlabeledEdge e : E) {
      llist[e.v1].add(e);
      if (!isDirected) {
        llist[e.v2].add(e);
      }
    }
    Edge[][] adjEdges = new Edge[n][];
    for (int i = 0; i < n; i++) {
      adjEdges[i] = new UnlabeledEdge[llist[i].size()];
      Iterator<Edge> it = llist[i].listIterator();
      int cnt = 0;
      while (it.hasNext()) {
        adjEdges[i][cnt++] = it.next();
      }
    }
    return adjEdges;

  }

  public LGraph GPRepresentative()
  {
	  Double[] discrepancies=new Double[this.V.length];
	  for(int i=0;i<discrepancies.length;i++)
	  {
		  discrepancies[i]=0.0;
	  }
	  List<UnlabeledEdge> edges=new ArrayList<UnlabeledEdge>();
	  for(UnlabeledEdge edge:this.E)
	  {
		  edges.add(edge);
		  discrepancies[edge.v1]-=edge.prob;
		  discrepancies[edge.v2]-=edge.prob;
	  }
	  Collections.sort(edges, new Comparator<UnlabeledEdge>() {
	        @Override
	        public int compare(UnlabeledEdge  e1, UnlabeledEdge e2)
	        {

	            if(e2.prob<e1.prob)
	            {
	            	return -1;
	            }
	            else
	            {
	            	return 1;
	            }
	        }
	    });
	  List<UnlabeledEdge> selectedEdges=new ArrayList<UnlabeledEdge>();
	  for(UnlabeledEdge e:edges)
	  {
		  //System.out.println("Edge Probability"+e.prob);
		  double disv1=discrepancies[e.v1];
		  double disv2=discrepancies[e.v2];
		  if(Math.abs(disv1+1)+Math.abs(disv2+1)<Math.abs(disv1)+Math.abs(disv2))
		  {
			  selectedEdges.add(e);
			  System.out.println("Edge Added to Rep:"+e.v1+" "+e.v2);
			  discrepancies[e.v1]+=1;
			  discrepancies[e.v2]+=1;
		  }
	  }
	  UnlabeledEdge[] edgesArray = new UnlabeledEdge[selectedEdges.size()];
	  LGraph GPRepresentative = new LGraph(this.V, selectedEdges.toArray(edgesArray) ,this.id+"*");
	  GPRepresentative.probMap=this.probMap;
	  return GPRepresentative;
  }
  /*
  public LGraph ADRRepresentative()
  {
	  Double[] discrepancies=new Double[this.V.length];
	  double probSum = 0.0;
	  for(int i=0;i<discrepancies.length;i++)
	  {
		  discrepancies[i]=0.0;
	  }
	  List<UnlabeledEdge> edges=new ArrayList<UnlabeledEdge>();
	  for(UnlabeledEdge edge:this.E)
	  {
		  edges.add(edge);
		  discrepancies[edge.v1]-=edge.prob;
		  discrepancies[edge.v2]-=edge.prob;
		  probSum+=edge.prob;
	  }
	  Collections.sort(edges, new Comparator<UnlabeledEdge>() {
	        @Override
	        public int compare(UnlabeledEdge  e1, UnlabeledEdge e2)
	        {

	            if(e2.prob<e1.prob)
	            {
	            	return -1;
	            }
	            else
	            {
	            	return 1;
	            }
	        }
	    });
	  List<UnlabeledEdge> selectedEdges=new ArrayList<UnlabeledEdge>();
	  for(UnlabeledEdge e:edges)
	  {
		 //System.out.println("Edge Probability"+e.prob);
		 double r=Math.random();
		 if(r<=e.prob)
		 {
			 selectedEdges.add(e);
			 discrepancies[e.v1]+=1;
			 discrepancies[e.v2]+=1;
		 }
		 if(selectedEdges.size()>probSum)
		 {
			 break;
		 }
	  }
	  UnlabeledEdge[] edgesArray = new UnlabeledEdge[selectedEdges.size()];
	  LGraph GPRepresentative = new LGraph(this.V, selectedEdges.toArray(edgesArray) ,this.id+"*");
	  GPRepresentative.probMap=this.probMap;
	  return GPRepresentative;
  }
  */
  public LGraph[] sampledGraphs(int maxNoOfSampledGraphs, double probThresh){
  	LGraph[] graphs = new LGraph[maxNoOfSampledGraphs];
  	int cnt = 0;
  	
  	for(int i=0; i<maxNoOfSampledGraphs; i++){
  		double prob = 1.0;
  		
  		List<UnlabeledEdge> edges = new ArrayList<UnlabeledEdge>();
  		for(UnlabeledEdge edge: this.E){
  			if(Math.random()<=edge.prob){
  				edges.add(edge);
  				prob = prob * edge.prob;
  			}
  		}
  		
  		UnlabeledEdge[] edgesArray = new UnlabeledEdge[edges.size()];
  		
  		if(prob>=probThresh){ // only if prob of graph >= probThresh, use it to compute weighted sim
    		LGraph graph = new LGraph(this.V, edges.toArray(edgesArray) ,this.id+"_"+i);
    		graph.probOfGraph = prob;
    		graph.probMap = this.probMap;
    		graphs[cnt]=graph;
    		cnt++;
  		}

  	}
  	
  	return graphs;
  }
  

  /**
   * @return int
   */
  public int numE() {
    return E.length;
  }

  /**
   * @return int
   */
  public int numV() {
    return V.length;
  }

  public String getId() {
    return id;
  }

  public void setId(String _id) {
    id = _id;
  }

  public String toString() {
    String s = "#" + id + "\n";
    s += V.length + "\n";
    for (int i = 0; i < V.length; i++) {
      s += V[i] + "\n";
    }
    s += E.length + "\n";
    for (int i = 0; i < E.length; i++) {
      s += E[i] + "\n";
    }
    return s;

  }

}
