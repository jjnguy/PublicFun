package worker;

public class LookupService
{
  public static int lookup(String name)
  {
    long millis = 4000 + (int)(8000 * Math.random());
    lookBusy(millis);
    return name.hashCode();
  }
  
  private static void lookBusy(long millis)
  {
    long interval = 300;
    long stop = System.currentTimeMillis() + millis;
    
    try
    {
      while(!Thread.currentThread().isInterrupted() && 
             System.currentTimeMillis() < stop)
      {
        System.out.print(".");
        Thread.sleep(interval);      
      }
    }
    catch (InterruptedException ie)
    {}
  }
}
