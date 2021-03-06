package ctree.lgraph;
import java.util.Comparator;
import ctree.graph.*;

/**
 * <p> Closure-tree</p>
 *
 * @author Huahai He
 * @version 1.0
 */
public class UnlabeledEdge implements Edge {
  protected boolean containsNull;
  protected int v1, v2;
  public double prob;
  public UnlabeledEdge() {
  }

  public UnlabeledEdge(int _v1, int _v2, boolean _containsNull,double _prob) {
    v1 = _v1;
    v2 = _v2;
    containsNull = _containsNull;
    prob = _prob;
  }

  public int v1() {
    return v1;
  }

  public int v2() {
    return v2;
  }

  /**
   * compatible
   *
   * @return boolean
   */
  public boolean mappable(Edge e) {
    return e instanceof UnlabeledEdge;
  }

  public String toString() {
    String s = v1 + " " + v2;
    if (containsNull) {
      s += "|null";
    }
    return s;

  }

  public static Comparator<UnlabeledEdge> ProbComparator = new Comparator<UnlabeledEdge>() 
  {
	  
      @Override
      public int compare(UnlabeledEdge e1, UnlabeledEdge e2) {
          return (int) (e1.prob - e2.prob);
      }
  };
}
