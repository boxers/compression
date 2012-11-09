import java.io.*;
import java.util.Scanner;

public class Compression {
    
    final static int SHIFT = 97;
    static int[] freq = new int[27];
    static String[] enc = new String[27];
    static KHeap heap = new KHeap(2);
    static Node trie;
    
    //gets the frequency of characters
    public static void getFreq(String s) throws IOException{
        FileReader reader = new FileReader(s);
        int nchar = 0;
        while((nchar = reader.read()) != -1){
            char c = (char)nchar;
            if (c != ' ')
                freq[(c-SHIFT)]++;
            else
                freq[26]++;
        }
    }
    
    //hEncode() creates the encoding trie
    public static void hEncode(){
        //loading heap with distict characters and their frequency
        for(int i = 0; i < freq.length; i++){
            if (freq[i] > 0){
                char c;
                int convert = i+SHIFT;
                if (i != 26)
                    c = (char)convert;
                else
                    c = ' ';
                Node dchar = new Node(freq[i],c);
                heap.insert(dchar);
                //System.out.println("Inserted "+c);
            }
        }
        //end loading
        while(heap.n > 1){
            Node t1 = heap.extract_min();
            Node t2 = heap.extract_min();
            Node t = join(t1,t2);
            heap.insert(t);
        }
        trie = heap.extract_min();
    }
    
    //encodeO creates the codes for the characters for 0th Order Compression
    static public void encodeO(Node x){
        if (x == null)
            return;
        Node y = x.parent;
        
        if (y!=null){
            if(x == y.left)
                x.code = y.code+"0";
            else
                x.code = y.code+"1";
        }
        encodeO(x.left);
        if (x.val != 'Z'){
            //the first if statement is for the case
            //where there is only one distinct character
            if (x.code.equals(""))
                x.code+="0";
            if(x.val != ' '){
                enc[x.val-SHIFT] = x.code;
                System.out.println(x.val+" "+x.code);
            }
            else{
                enc[26] = x.code;
                System.out.println(x.val+" "+x.code);
            }
        }
        encodeO(x.right);
        
    }
    
    public static Node join(Node t1, Node t2){
        int nfreq = t1.key + t2.key;
        Node t = new Node(nfreq,'Z');
        t1.parent = t;
        t2.parent = t;
        if(t1.key < t2.key){
            t.left = t1;
            t.right = t2;
        }
        else{
            t.left = t2;
            t.right = t1;
        }
        return t;
    }

    //0th order compression
    public static void oCompress(String s, String n) throws IOException{
        getFreq(s);
        hEncode();
        encodeO(trie);
        //We're supposed to be outputting in bits...
        FileWriter fw = new FileWriter(n+".bz");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        FileReader fr = new FileReader(s);
        
        FileOutputStream fos = new FileOutputStream(n+".key");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        int nchar = 0;
        while((nchar = fr.read()) != -1){
            char c = (char)nchar;
            if (c != ' '){
                pw.print(enc[c-SHIFT]);
            }
            else{
                pw.write(enc[26]);
            }
        }
        //we save the encoding trie here
        oos.writeObject(trie);

        pw.close();
        fr.close();
        oos.close();
    }

    //0th order decompression
    public static void decompress(String s, String n) throws IOException,ClassNotFoundException{
        //We're supposed to read bits...
        FileReader fr = new FileReader(s);
        FileWriter fw = new FileWriter(n+"2.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        FileInputStream fis = new FileInputStream(n+".key");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object encodingTrie = ois.readObject();
        trie = (Node)encodingTrie;

        int nchar = 0;
        Node trav = trie;
        while((nchar = fr.read()) != -1){
            if (!trav.hasChild()){
                pw.print(trav.val);
            }
            else{
                char c = (char)nchar;
                if(c == '1')
                    trav = trav.right;
                else
                    trav = trav.left;
                if (trav.val != 'Z'){
                    pw.print(trav.val);
                    trav = trie;
                }
            }
        }
        pw.close();
        fr.close();
        ois.close();
    }
    

    //main method
    public static void main(String[] args) {
        try{
            //just the basic setup for testing
            String input = "input.txt";
            Scanner scan = new Scanner(System.in);
            //System.out.print("Enter File Name: ");
            //input = scan.nextLine();
            String[] processInput = input.split("\\.");
            String name = processInput[0];
            String ext = "";
            if (processInput.length > 1)
                ext = processInput[1];
            if(ext.equals("bz")){
                //decompress using 0th order trie
                decompress(input,name);
            }
            else{
                //compress using 0th order
                oCompress(input,name);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
