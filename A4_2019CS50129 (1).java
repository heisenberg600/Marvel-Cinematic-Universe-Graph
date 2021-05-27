import java.util.Vector;
import java.util.Iterator;
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
            String[] idlabel = in_node.next().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)",-1);
            idlabel[1]=idlabel[1].replace("\"","");
            Vertex V=new Vertex(idlabel[1]);
            G.addVertex(V);
        }
        in_edge.next();
        while (in_edge.hasNext()){      
            String[] S=in_edge.next().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)",-1);
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
            case "independent_storylines_dfs":
                Vector<component> C= new Vector<>();
                C=connected(G);
                component[] comp=new component[G.no_of_component];
                Iterator value = C.iterator();
                int o=0;
                while (value.hasNext()){
                    component a1=(component)value.next();
                    comp[o]=a1;
                    o++;
                }
                ms1(comp, G.no_of_component);
                o=0;
                while (o<G.no_of_component){
                    System.out.println(comp[o].getstringid());
                    o++;
                }                
                break;
            default:
                break;
        }
    }

    static void ms1(component arr[],int n){
        //if the length of the array is 1 then its already sorted
        if (n<2){
            return;
        }
        int middle =n/2;//finding the middle of the array
        component left[]= new component[middle];// bifurcating the array into  the left 
        component right[]= new component[n-middle];// bifurcating the array into  right part
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
        ms1(left,middle);
        ms1(right,n-middle);
        merge1(arr,left,right,middle,n-middle);
    }

    //helper function to help us merge 2 sorted arrays
    static void merge1(component arr[],component left[],component right[],int leftl, int rightl){
        int i=0;
        int j=0;
        int k=0;
        //while i and j remain between the lenght of left and right limit of array
        while (i < leftl && j < rightl) {
            if (left[i].no_of_vertex_in>right[j].no_of_vertex_in) {
                arr[k++] = left[i++];
            }
            //if left is greater then add right in array first
            else if (left[i].no_of_vertex_in<right[j].no_of_vertex_in) {
                arr[k++] = right[j++];
            }
            else{
                int y=(left[i].getstringid()).compareTo(right[j].getstringid());
                if (y >0) {
                    arr[k++] = left[i++];
                }
                //if left is greater then add right in array first
                else {
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
    static void ms(String arr[],int n){
        //if the length of the array is 1 then its already sorted
        if (n<2){
            return;
        }
        int middle =n/2;//finding the middle of the array
        String left[]= new String[middle];// bifurcating the array into  the left 
        String right[]= new String[n-middle];// bifurcating the array into  right part
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
    static void merge(String arr[],String left[],String right[],int leftl, int rightl){
        int i=0;
        int j=0;
        int k=0;
        //while i and j remain between the lenght of left and right limit of array
        while (i < leftl && j < rightl) {
            int y=left[i].compareTo(right[j]);
            if (y >0) {
                arr[k++] = left[i++];
            }
            //if left is greater then add right in array first
            else {
                arr[k++] = right[j++];
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
    static Vector<component> connected(Graph G){
        int n=G.getNoVertex();
        Vector<component> C=new Vector<>();
        boolean visited[] = new boolean[n];
        int i=0;
        while (i<n){
            if (!visited[i]){
                component comp=new component(0);
                G.no_of_component++;
                dfs(G,G.B.get(i),visited,comp);
                Vector<String> vertcomp=new Vector();
                vertcomp=comp.nodes;
                Iterator value = vertcomp.iterator();
                String[] arrr=new String[comp.no_of_vertex_in];
                int k=0;
                while (value.hasNext()){
                    arrr[k]=(String)value.next();
                    k++;
                }
                ms(arrr, comp.no_of_vertex_in);
                comp.arr=arrr;               
                C.add(comp);
            }
            i++;
        }
        return C;
    }
    static void dfs(Graph G,Vertex V,boolean[] visited,component comp){
        comp.add(V.getIdlabel());
        visited[V.number]=true;
        Iterator value = V.adjlist.iterator();
        while (value.hasNext()){
            Vertex a=(Vertex)value.next();
            if (!visited[a.number]){
                dfs(G, a, visited, comp);
            }
        }       
    }
}
class Graph{
    private int verticeN;
    private int edgeN;
    int no_of_component;
    HashMap<String,Vertex> A;    
    HashMap<Vertex,Vector<Vertex>> vertices;
    HashMap<Integer,Vertex> B; 
    public Graph(){
        this.verticeN=0;
        this.edgeN=0;
        this.vertices=new HashMap<>();
        this.A=new HashMap<>();
        this.B=new HashMap<>();
        this.no_of_component=0;
    }
    public void addVertex(Vertex V){
        V.number=verticeN;
        B.put(V.number,V);
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
    int number;
    public Vertex(String idlabel){
        this.idlabel=idlabel;
        this.adjlist=new Vector<>();
        this.totalW=0;
        this.number=0;
    }
    public Vertex(Vertex V){
        this.idlabel=V.getIdlabel();
        this.adjlist=V.adjlist;
        this.totalW=V.totalW;
        this.number=V.number;
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

class component{
    int no_of_vertex_in;
    Vector<String> nodes;
    String[] arr;
    public component(int no_of_vertex_in){
        this.no_of_vertex_in=no_of_vertex_in;
        this.nodes=new Vector<>();
    }
    public void add(String label){
        this.no_of_vertex_in++;
        this.nodes.add(label);
    }
    public String getstringid(){
        int i=0;
        String[] arr1=this.arr;
        String S="";
        while (i<this.no_of_vertex_in-1){
            S+=arr1[i]+",";
            i++;
        }
        S+=arr1[this.no_of_vertex_in-1];
        return S;
    }
}
