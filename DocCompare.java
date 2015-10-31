import java.util.*;
public class DocCompare implements Comparator<Obj_wtd>
{
    @Override
    public int compare(Obj_wtd a,Obj_wtd b)
    {
        return Integer.parseInt(a.doc_id)-Integer.parseInt(b.doc_id);
    }
}