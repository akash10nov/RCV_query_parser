import java.io.*;
import java.util.*;
/*
 * Document: words
 * Words: Document : Make and object of document id and frequency
 * Word:frequency
 */
public class CSE535Assignment
{
    public static PrintWriter writer;
    static int comparison_made=0;
    public static void main(String[] akash) throws IOException
    {
        /*
         * Get a file;
         */
         String data = akash[0];

        // This will reference one line at a time
        String line = null;

        
            
        /*
         * word to document
         */
        HashMap<String, LinkedList<Obj_wtd>> wordTodoc=new HashMap<String,LinkedList<Obj_wtd>>();
        HashMap<String, LinkedList<Obj_wtd>> wordTodocFreqSorted=new HashMap<String,LinkedList<Obj_wtd>>();
        writer=new PrintWriter(akash[1]);
        /*
         * Document to Word
         */
        HashMap<String,LinkedList<Obj_dtw>> docToword=new HashMap<String,LinkedList<Obj_dtw>>();
        
        /*
         * word and total freq
         */
        
        HashMap<String,Integer> wordTofreq=new HashMap<String,Integer>();
        int count=0;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =  new FileReader(data);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
                line.replace("\\","\\\\"); 
                String[] s=line.split("\\\\");
                String s0=s[0];
                String s1=s[1];
                String s2=s[2];
                Parsing(s,wordTofreq,wordTodoc,wordTodocFreqSorted,docToword);
                count++;
            }   
            
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            writer.println("Unable to open file '" + data + "'");                
        }
        catch(IOException ex) {
            writer.println("Error reading file '"+ data + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
       line=null;
       
       int k=Integer.parseInt(akash[2]);
       String input=akash[3];
       try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =  new FileReader(input);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
                if(line.contains("getTopK"))
                {
                    writer.println("FUNCTION: "+line);
                    String topK=getTopK(k,wordTofreq);
                    writer.print("Result: ");
                    writer.println(topK);
                }
                else if(line.contains("getPostings"))
                {
                    writer.println("FUNCTION: "+line);
                    String s=line.substring(12);
                    if(wordTodoc.containsKey(s))
                        getPostings(s,wordTodoc,wordTodocFreqSorted);
                    else
                        writer.println("term not found");
                }
                else if(line.contains("termAtAQueryAnd"))
                {
                    writer.println("FUNCTION: "+line);
                    String s=line.substring(16);
                    termAtaTime(s,"AND",wordTodocFreqSorted);
                    
                }
                else if(line.contains("termAtAQueryOr"))
                {
                    writer.println("FUNCTION: "+line);
                    String s=line.substring(15);
                    termAtaTime(s,"OR",wordTodocFreqSorted);
                }
                else if(line.contains("docAtAQueryAnd"))
                {
                    writer.println("FUNCTION: "+line);
                    String s=line.substring(15);
                    docAtaTime(s,"AND",wordTodoc);
                }
                else if(line.contains("docAtAQueryOr"))
                {
                    writer.println("FUNCTION: "+line);
                    String s=line.substring(14);
                    docAtaTime(s,"OR",wordTodoc);
                }
            }   
            
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            writer.println("Unable to open file '" + input + "'");                
        }
        catch(IOException ex) {
            writer.println("Error reading file '"+ input + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        } 
       writer.close();
      
       /*
       docAtaTime("April 18, Atlantic Coast, British, Caribbean","AND",wordTodoc);
       docAtaTime("Atlantic, Attack, Atlanta","OR",wordTodoc);
       termAtaTime("April 18, Atlantic Coast, British, Caribbean","AND",wordTodocFreqSorted);
       termAtaTime("Atlantic, Attack, Atlanta","OR",wordTodocFreqSorted);
       */
       
    }
    
    
    public static void Parsing(String[] s,HashMap<String,Integer> wordTofreq,HashMap<String, LinkedList<Obj_wtd>> wordTodoc,HashMap<String, LinkedList<Obj_wtd>> wordTodocFreqSorted,
        HashMap<String,LinkedList<Obj_dtw>> docToword)
    {
        LinkedList<Obj_wtd> temp_wtd=new LinkedList<Obj_wtd>();
        Parsewtd(s[0],s[2],temp_wtd,docToword);
        wordTodoc.put(s[0],temp_wtd);
         LinkedList<Obj_wtd> temp_wtd_Freq=new LinkedList<Obj_wtd>(temp_wtd);
        Collections.sort(temp_wtd_Freq);
        wordTodocFreqSorted.put(s[0],temp_wtd_Freq);
        wordTofreq.put(s[0],Integer.parseInt(s[1].substring(1)));
        
        
    }
    
    
    public static void Parsewtd(String word,String s, LinkedList<Obj_wtd> l, HashMap<String,LinkedList<Obj_dtw>> docToword)
    {
        String str=s.substring(2,s.length()-1);
        String[] doc_freq=str.split(",");
        int len=doc_freq.length;
        for(int i=0;i<len;i++)
        {
            String[] temp=doc_freq[i].split("/");
            String s0=temp[0];
            String s1=temp[1];
            Obj_wtd obj=new Obj_wtd();
            obj.doc_id=temp[0].trim();
            obj.freq=Integer.parseInt(temp[1]);
            if(docToword.containsKey(obj.doc_id))
            {
                LinkedList<Obj_dtw> temp_dtw=docToword.get(obj.doc_id);
                Obj_dtw temp_obj_dtw=new Obj_dtw();
                temp_obj_dtw.freq=obj.freq;
                temp_obj_dtw.word=word;
                temp_dtw.add(temp_obj_dtw);
                docToword.put(obj.doc_id,temp_dtw);
            }
            else
            {
                LinkedList<Obj_dtw> temp_dtw=new LinkedList<Obj_dtw>();
                Obj_dtw temp_obj_dtw=new Obj_dtw();
                temp_obj_dtw.freq=obj.freq;
                temp_obj_dtw.word=word;
                docToword.put(obj.doc_id,temp_dtw);
            }
            l.add(obj);
            
        }
    }
    
    public static String getTopK(int k,HashMap<String,Integer> wordTofreq)
    {
        Set st=wordTofreq.entrySet();
        Iterator i=st.iterator();
       // Obj_dtw[] list=new Obj_dtw[wordTofreq.size()];
        PriorityQueue<Obj_dtw> list = new PriorityQueue<Obj_dtw>();
        while(i.hasNext())
        {
            Map.Entry m=(Map.Entry)i.next();
            int freq=(int)(m.getValue());
            String word=(String)m.getKey();
            Obj_dtw temp=new Obj_dtw();
            temp.freq=freq;
            temp.word=word;
            list.offer(temp);
        }
        String topK="";
        for(int j=0;j<k;j++)
        {
            topK+=list.poll().word;
            if(j==k-1)
                break;
            topK+=", ";
        }
        return topK;
    }
    
    public static void getPostings(String word,HashMap<String, LinkedList<Obj_wtd>> wordTodoc,HashMap<String, LinkedList<Obj_wtd>> wordTodocFreqSorted)
    {
        LinkedList<Obj_wtd> list=wordTodoc.get(word);
        Iterator i=list.listIterator(0);
        writer.print("Ordered by doc IDs: ");
        while(i.hasNext())
        {
            Obj_wtd temp=(Obj_wtd)i.next();
            writer.print(temp.doc_id);
            if(i.hasNext())
                writer.print(", ");
        }
        writer.println("");
        writer.print("Ordered by TF: ");
        LinkedList<Obj_wtd> list1=wordTodocFreqSorted.get(word);
        
        Iterator j=list1.listIterator(0);
        while(j.hasNext())
        {
            Obj_wtd temp=(Obj_wtd)j.next();
            writer.print(temp.doc_id);
            if(j.hasNext())
                writer.print(", ");
        }
        
    }
    
    
    
    
    
    /*
     * 
     * 
     * 
     * Term at a time
     * 
     * 
     * 
     * 
     */
     
    public static void termAtaTime(String s, String operation,HashMap<String, LinkedList<Obj_wtd>> wordTodocFreqSorted)
    {
        String[] words=s.split(", ");
        if(operation.equals("AND"))
        {
            int count=0;
            int flag=0;
            LinkedList<Obj_wtd> temp=new LinkedList<Obj_wtd>();
            long start = System.nanoTime();  
            for(int i=0;i<words.length;i++)
            {
                if(!wordTodocFreqSorted.containsKey(words[i]))
                {
                    flag=1;
                    
                    break;
                }
            }
            if(flag==1)
            {
                writer.println("terms not found");
            }
            else
            {
                LinkedList<Obj_wtd> result=AND(words,wordTodocFreqSorted,0,count,temp);
                long elapsedTime = System.nanoTime() - start;
                writer.println(result.size()+" documents are found");
                writer.println(comparison_made+" comparisons are made");
                writer.println(elapsedTime/1000000000+" seconds are used");
                comparison_made=0;
                writer.print("Results: ");
                Collections.sort(result,new DocCompare());
                Iterator j=result.listIterator(0);
                while(j.hasNext())
                {
                    Obj_wtd temp1=(Obj_wtd)j.next();
                    writer.print(temp1.doc_id);
                    if(j.hasNext())
                        writer.print(", ");
                }
                writer.println("");
            }
        }
        else
        {
            int count=0;
            int flag=1;
            LinkedList<Obj_wtd> temp=new LinkedList<Obj_wtd>();
            long start = System.nanoTime();
            for(int i=0;i<words.length;i++)
            {
                if(wordTodocFreqSorted.containsKey(words[i]))
                {
                    flag=0;
                    break;
                }
            }
            if(flag==1)
            {
                writer.println("terms not found");
            }
            else
            {
                LinkedList<Obj_wtd> result=OR(words,wordTodocFreqSorted,0,count,temp);
                long elapsedTime = System.nanoTime() - start;
                writer.println(result.size()+" documents are found");
                writer.println(comparison_made+" comparisons are made");
                writer.println(elapsedTime/1000000000+" seconds are used");
                comparison_made=0;
                writer.print("Results: ");
                Collections.sort(result,new DocCompare());
                Iterator j=result.listIterator(0);
                while(j.hasNext())
                {
                    Obj_wtd temp1=(Obj_wtd)j.next();
                    writer.print(temp1.doc_id);
                    if(j.hasNext())
                        writer.print(", ");
                }
                writer.println("");
            }
        }
        comparison_made=0;
    }
    
    
    
    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
    
    public static void docAtaTime(String s, String operation,HashMap<String, LinkedList<Obj_wtd>> wordTodoc)
    {
        String[] words=s.split(", ");
        if(operation.equals("AND"))
        {
            int count=0;
            int flag=0;
            LinkedList<Obj_wtd> temp=new LinkedList<Obj_wtd>();
            long start = System.nanoTime();  
            for(int i=0;i<words.length;i++)
            {
                if(!wordTodoc.containsKey(words[i]))
                {
                    flag=1;
                    
                    break;
                }
            }
            if(flag==1)
            {
                writer.println("terms not found");
            }
            else
            {
                LinkedList<Obj_wtd> result=ANDdoc(words,wordTodoc,0,count,temp);
                long elapsedTime = System.nanoTime() - start;
                writer.println(result.size()+" documents are found");
                writer.println(comparison_made+" comparisons are made");
                writer.println(elapsedTime/1000000000+" seconds are used");
                comparison_made=0;
                writer.print("Results: ");
                Iterator j=result.listIterator(0);
                while(j.hasNext())
                {
                    Obj_wtd temp1=(Obj_wtd)j.next();
                    writer.print(temp1.doc_id);
                    if(j.hasNext())
                        writer.print(", ");
                }
                writer.println("");
            }
        }
        else
        {
            int count=0;
            int flag=1;
            LinkedList<Obj_wtd> temp=new LinkedList<Obj_wtd>();
            long start = System.nanoTime();
            for(int i=0;i<words.length;i++)
            {
                if(wordTodoc.containsKey(words[i]))
                {
                    flag=0;
                    break;
                }
            }
            if(flag==1)
            {
                writer.println("terms not found");
            }
            else
            {
                LinkedList<Obj_wtd> result=ORdoc(words,wordTodoc,0,count,temp);
                long elapsedTime = System.nanoTime() - start;
                writer.println(result.size()+" documents are found");
                writer.println(comparison_made+" comparisons are made");
                writer.println(elapsedTime/1000000000+" seconds are used");
                comparison_made=0;
                writer.print("Results: ");
                
                Iterator j=result.listIterator(0);
                while(j.hasNext())
                {
                    Obj_wtd temp1=(Obj_wtd)j.next();
                    writer.print(temp1.doc_id);
                    if(j.hasNext())
                        writer.print(", ");
                }
                writer.println("");
            }
        }
        comparison_made=0;
    }
    public static LinkedList<Obj_wtd> AND(String[] words,HashMap<String, LinkedList<Obj_wtd>> wordTodoc,int index,int count,LinkedList<Obj_wtd> result)
    {  
        if(index==0)
        {
            LinkedList<Obj_wtd> temp=wordTodoc.get(words[index]);
            
            return AND(words,wordTodoc,index+1,count,temp);
        }
        else if(index==words.length-1)
        {
            LinkedList<Obj_wtd> temp=wordTodoc.get(words[index]);
            LinkedList<Obj_wtd> ans=new LinkedList<Obj_wtd>();
            ListIterator i=result.listIterator(0);
            
            while(i.hasNext())
            {
                Obj_wtd result_list=(Obj_wtd)i.next();
                ListIterator j=temp.listIterator(0);
                while(j.hasNext())
                {
                   Iterator j_temp=j;
                   Obj_wtd word_list=(Obj_wtd)j.next();
                   if(result_list.doc_id.equals(word_list.doc_id))
                   {
                       ans.add(word_list);
                       comparison_made++;
                       break;
                    }
                   comparison_made++;
                }
                
            }
            //Collections.sort(ans,new DocCompare());
            return ans;
        
        }
        else
        {
            LinkedList<Obj_wtd> temp=wordTodoc.get(words[index]);
            LinkedList<Obj_wtd> ans=new LinkedList<Obj_wtd>();
            ListIterator i=result.listIterator(0);
            
            while(i.hasNext())
            {
                Obj_wtd result_list=(Obj_wtd)i.next();
                ListIterator j=temp.listIterator(0);
                while(j.hasNext())
                {
                   Iterator j_temp=j;
                   Obj_wtd word_list=(Obj_wtd)j.next();
                   if(result_list.doc_id.equals(word_list.doc_id))
                   {
                       ans.add(word_list);
                       comparison_made++;
                       break;
                    }
                   comparison_made++;
                }
            }
            return AND(words,wordTodoc,index+1,count,ans);
        }
    }
    public static LinkedList<Obj_wtd> OR(String[] words,HashMap<String, LinkedList<Obj_wtd>> wordTodoc,int index,int count,LinkedList<Obj_wtd> result)
    {
        if(index==0)
        {
            LinkedList<Obj_wtd> temp;
            while(true)
            {
                if(wordTodoc.containsKey(words[index]))
                {
                    temp=wordTodoc.get(words[index]);
                    break;
                }
                else
                    index++;
            }
            return OR(words,wordTodoc,index+1,count,temp);
        }
        else if(index==words.length-1)
        {
            LinkedList<Obj_wtd> temp=wordTodoc.get(words[index]);
            
            
            ListIterator j=temp.listIterator(0);
            while(j.hasNext())
            {
                ListIterator i=result.listIterator(0);
                Obj_wtd result_list=(Obj_wtd)j.next();
                int flag=0;
                while(i.hasNext())
                {
                   Obj_wtd comp=(Obj_wtd)i.next();
                   if(comp.doc_id.equals(result_list.doc_id))
                   {
                       flag=1;
                       comparison_made++;
                       break;
                    }
                    comparison_made++;
                }
                if(flag==0)
                {
                    result.add(result_list);
                }
                //writer.println(result_list.doc_id);
                
            }
            //Collections.sort(result,new DocCompare());
            return result;
        
        }
        else
        {
           LinkedList<Obj_wtd> temp=wordTodoc.get(words[index]);
            
            
            ListIterator j=temp.listIterator(0);
            while(j.hasNext())
            {
                ListIterator i=result.listIterator(0);
                Obj_wtd result_list=(Obj_wtd)j.next();
                int flag=0;
                while(i.hasNext())
                {
                   Obj_wtd comp=(Obj_wtd)i.next();
                   if(comp.doc_id.equals(result_list.doc_id))
                   {
                       flag=1;
                       comparison_made++;
                       break;
                    }
                    comparison_made++;
                }
                if(flag==0)
                {
                    result.add(result_list);
                }
                //writer.println(result_list.doc_id);
                
            }
            return OR(words,wordTodoc,index+1,count,result);
        }
    }
    
    
    
    
   
    
    public static LinkedList<Obj_wtd> ANDdoc(String[] words,HashMap<String, LinkedList<Obj_wtd>> wordTodoc,int index,int count,LinkedList<Obj_wtd> result)
    {
        int len=words.length;
        ListIterator[] list=new ListIterator[len];
        for(int i=0;i<len;i++)
        {
            LinkedList<Obj_wtd> wordlist=wordTodoc.get(words[i]);
            list[i]=wordlist.listIterator();
        }
        while(list[0].hasNext())
        {
            Obj_wtd element=(Obj_wtd)list[0].next();
            int flag=0;
            for(int i=1;i<len;i++)
            {
               
                while(list[i].hasNext())
                {
                    Obj_wtd temp=(Obj_wtd)list[i].next();
                    if(element.doc_id.equals(temp.doc_id))
                    {
                        comparison_made++;
                        break;
                    }
                    else if(Integer.parseInt(element.doc_id)<Integer.parseInt(temp.doc_id))
                    {
                        flag=1;
                        comparison_made++;
                        list[i].previous();
                        break;
                    }
                    else
                    {
                        comparison_made++;
                    }
                }
                if(flag==1)
                    break;
            }
            if(flag==0)
                result.add(element);
        }
            
        
        return result;
    }
    public static LinkedList<Obj_wtd> ORdoc(String[] words,HashMap<String, LinkedList<Obj_wtd>> wordTodoc,int index,int count,LinkedList<Obj_wtd> result)
    {
        int len=words.length;
        ListIterator[] list=new ListIterator[len];
        HashMap<String,Integer> h=new HashMap<String,Integer>();
        for(int i=0;i<len;i++)
        {
            LinkedList<Obj_wtd> wordlist=wordTodoc.get(words[i]);
            list[i]=wordlist.listIterator();
            //result=wordTodoc.get(words[0]);
        }
        int l=0;
        while(list[l].hasNext())
        {
            Obj_wtd element=(Obj_wtd)list[l].next();
            int flag=0;
            for(int i=1;i<len;i++)
            {
               
                while(list[i].hasNext())
                {
                    Obj_wtd temp=(Obj_wtd)list[i].next();
                    if(Integer.parseInt(element.doc_id)<Integer.parseInt(temp.doc_id))
                    {
                        flag=1;
                        comparison_made++;
                        list[i].previous();
                        break;
                    }
                    else if(Integer.parseInt(element.doc_id)>Integer.parseInt(temp.doc_id))
                    {
                        comparison_made=comparison_made+2;
                        if(!h.containsKey(temp.doc_id))
                        {
                            comparison_made++;
                            result.add(temp);
                            h.put(temp.doc_id,1);
                        }
                    }
                    else
                    {
                        comparison_made++;
                    }
                }
                
            }
                if(!h.containsKey(element.doc_id))
                {
                    comparison_made=comparison_made+1;
                    result.add(element);
                    h.put(element.doc_id,1);
                }
                if(!list[l].hasNext() && l< len-1)
                {
                    l++;
                }
        }
            
        Collections.sort(result,new DocCompare());
        return result;
    }
}