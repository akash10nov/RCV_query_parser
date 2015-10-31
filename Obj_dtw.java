 public class Obj_dtw implements Comparable <Obj_dtw>
    {
       int freq;
       String word;
       @Override
       public int compareTo(Obj_dtw a) {
 
            if (this.freq < a.freq)
                return 1;
            else if (this.freq > a.freq)
                return -1;
            else
                return 0;
        }
    }