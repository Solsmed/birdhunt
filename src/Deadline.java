import java.util.Date;

public class Deadline {
    
    Deadline(Date pDate)
    {
        mDate=pDate;
    }

    long TimeUntil()
    {
        return mDate.getTime()-(new Date()).getTime();
    }
        
    private Date mDate;
}
