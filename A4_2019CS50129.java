import java.util.Vector;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;
public class A4_2019CS50129{
    public static void main(String[] args)  throws Exception{
		Scanner in_node = new Scanner(new File(args[0]));
		Scanner in_edge = new Scanner(new File(args[1]));
		in_node.useDelimiter("\n");
        in_edge.useDelimiter("\n");
        Graph G = new Graph();
        in_node.next();
		while(in_node.hasNext()){
            String[] idlabel = in_node.next().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            idlabel[1]=idlabel[1].replace("\"","");
            Vertex V=new Vertex(idlabel[1]);
            G.addVertex(V);
        }
        in_edge.next();
        while (in_edge.hasNext()){      
            String[] S=in_edge.next().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            S[1]=S[1].replace("\"","");
            S[0]=S[0].replace("\"","");
            Vertex source= G.A.get(S[0]);
            Vertex target= G.A.get(S[1]);
            int weight=Integer.parseInt(S[2]);
            Edge E= new Edge(source, target,weight);
            G.addEdge(E);
        }
        in_node.close();
        in_edge.close();
        switch(args[2]){
            case "average":
                double a=(double) 2*G.getNoEdge()/G.getNoVertex();
                System.out.printf("%.2f", a);
                System.out.println();
                break;
            case "rank":
                Vertex[] arr=G.generateArray();
                ms(arr,G.getNoVertex());
                int k=0;
                while (k<G.getNoVertex()-1){
                    System.out.print(arr[k].getIdlabel());
                    System.out.print(",");
                    k++;
                }
                System.out.println(arr[k].getIdlabel());
                break;
            default:
                break;
        }
    }
    static void ms(Vertex arr[],int n){
        //if the length of the array is 1 then its already sorted
        if (n<2){
            return;
        }
        int middle =n/2;//finding the middle of the array
        Vertex left[]= new Vertex[middle];// bifurcating the array into  the left 
        Vertex right[]= new Vertex[n-middle];// bifurcating the array into  right part
        int i=0;
        //create the left array
        while (i<middle){
            left[i]=arr[i];
            i++;
        }
        i=middle;
        //creating the right array
        while (i<n){
            right[i-middle]=arr[i];
            i++;
        }
        //recusively call the merge sort algo on both the part and merge them
        ms(left,middle);
        ms(right,n-middle);
        merge(arr,left,right,middle,n-middle);
    }

    //helper function to help us merge 2 sorted arrays
    static void merge(Vertex arr[],Vertex left[],Vertex right[],int leftl, int rightl){
        int i=0;
        int j=0;
        int k=0;
        //while i and j remain between the lenght of left and right limit of array
        while (i < leftl && j < rightl) {
            //if left is smaller then add left in array first
            if ((left[i]).totalW > (right[j]).totalW) {
                arr[k++] = left[i++];
            }
            //if left is greater then add right in array first
            else if ((left[i]).totalW < (right[j]).totalW){
                arr[k++] = right[j++];
            }
            else {
                int y=(left[i]).getIdlabel().compareTo((right[j]).getIdlabel());
                if (y>=0) {
                    arr[k++] = left[i++];
                }
                else{
                    arr[k++] = right[j++];                   
                }
            }
        }
        //add the remaining elemets to the array 
        while (i < leftl) {
            arr[k++] = left[i++];
        }
        //add the remaining elemets to the array 
        while (j < rightl) {
            arr[k++] = right[j++];
        }
    }
}
class Graph{
    private int verticeN;
    private int edgeN;
    HashMap<String,Vertex> A;    
    HashMap<Vertex,Vector<Vertex>> vertices;  
    public Graph(){
        this.verticeN=0;
        this.edgeN=0;
        this.vertices=new HashMap<>();
        this.A=new HashMap<>();
    }
    public void addVertex(Vertex V){
        verticeN++;
        vertices.put(V, V.adjlist);
        A.put(V.getIdlabel(),V);
    }
    public int getNoVertex(){
        return verticeN;
    }
    public int getNoEdge(){
        return edgeN;
    }
    public void addEdge(Edge E){
        edgeN++;
        Vertex S=E.getSource();
        Vertex T=E.getTarget();
        S.totalW+=E.getWeight();
        T.totalW+=E.getWeight();
        vertices.get(T).add(S);
        vertices.get(S).add(T);
    }
    public Vertex[] generateArray(){
        int n=this.getNoVertex();
        Vertex[] arr=new Vertex[n];
        int l=0;
        for (Vertex V:this.vertices.keySet()){
            arr[l]=V;
            l++;
        }
        return arr;
    }
}
class Vertex{
    private String idlabel;
    Vector<Vertex> adjlist;
    int totalW;
    public Vertex(String idlabel){
        this.idlabel=idlabel;
        this.adjlist=new Vector<>();
        this.totalW=0;
    }
    public Vertex(Vertex V){
        this.idlabel=V.getIdlabel();
        this.adjlist=V.adjlist;
        this.totalW=V.totalW;
    }
    public String getIdlabel() {
        return idlabel;
    }
}
class Edge{
    private Vertex source;
    private Vertex target;
    private int weight;
    public Edge(Vertex source, Vertex target, int weight){
        this.source=source;
        this.target=target;
        this.weight=weight;
    }
    public Vertex getSource(){
        return this.source;
    }
    public Vertex getTarget(){
        return this.target;
    }
    public int getWeight(){
        return this.weight;
    }
}


