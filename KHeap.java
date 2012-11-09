/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class KHeap {

    Node[] heap;
    int k;
    int n;
    
    public KHeap(){
        heap = new Node[30];
        n = 0;
        k = 2;
    }
    
    public KHeap(int kay){
        heap = new Node[30];
        n = 0;
        k = kay;
    }
    
    public KHeap(Node[] arr, int kay){
        heap = new Node[30];
        k = kay;
        n = arr.length;
        for(int i = 0; i < n; i++)
            heap[i+1] = arr[i];
    }
    
    public void heapify(){
        for(int i = parent(n); i > 0; i--){
            heapify(heap,i,n);
        }
    }
   
    public void heapify(Node[] a, int root, int bottom){
        while((k*root-(k-2)) <= bottom){
            int minChild = k*root-(k-2);
            for (int j = 0; j < k-1; j++){
                if ((k*root-j+1) > bottom)
                        continue;
                else if (a[k*root-j+1].key < a[minChild].key){
                    minChild = k*root-j+1;
                }
            }
            if(a[root].key > a[minChild].key){
                Node temp = a[root];
                a[root] = a[minChild];
                a[minChild] = temp;
                root = minChild;
            }
            else 
                return;
        }
    }
    
    public Node extract_min(){
        if(n == 0)
            return null;
        Node min = heap[1];
        heap[1] = heap[n];
        --n;
        heapify(heap,1,n);
        return min;
    }
    
    public void debug(){
        if(n == 0)
            return;
        for(int j = 1; j <= n; j++){
                System.out.println(heap[j].key+" "+heap[j].val);
        }
        System.out.println("n = "+n+" k = "+k);
    }
    
    public int size(){
        return n;
    }
    
    public int parent(int x){
        int p = x/k;
        int r = x%k;
        if (r > 1)
            p++;
        return p;
    }
    
    public void insert(Node x){
        n++;
        heap[n] = x;
        int in = n;
        int i = parent(in);
        while(i > 0){
            if(heap[in].key < heap[i].key){
                Node temp = heap[i];
                heap[i] = heap[in];
                heap[in] = temp;
                in = i;
                i = parent(in);
            }
            else
                return;
        }           
    }
}
