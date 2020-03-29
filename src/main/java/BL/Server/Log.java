package BL.Server;

/**
 * Description:     X
 * ID:              X
 **/
public class Log
{
    private static Log instance;

    private Log()
    {

    }

    public static Log get()
    {
        if(instance == null) instance = new Log();
        return instance;
    }
}
