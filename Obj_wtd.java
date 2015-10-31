import java.util.*;
 public class Obj_wtd implements Comparable <Obj_wtd>, Comparator<Obj_wtd>
    {
       int freq;
       String doc_id;
       @Override
       public int compareTo(Obj_wtd a) {
 
            if (this.freq < a.freq)
                return 1;
            else if (this.freq > a.freq)
                return -1;
            else
                return 0;
        }
        @Override
        public int compare(Obj_wtd a,Obj_wtd b)
        {
            return (Integer.parseInt(a.doc_id)-Integer.parseInt(b.doc_id));
        }
        
    }