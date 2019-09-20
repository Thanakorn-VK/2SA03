import java.util.concurrent.Semaphore;

public class hw3 {

//  private final Semaphore mutexLock=new Semaphore(3); //true: first come, first serve
  private String Buffer = "Buffer is empty";
  private int writer = 1;
  private int readerCount = 0;

    Semaphore mutex = new Semaphore(1, true);  
    Semaphore db = new Semaphore(1, true);

  public static void main(String[] args) {
    hw3 test=new hw3();
    test.mystart(); 
  }
  
  public void mystart() {
      for(int i=0;i<5;i++){
        Reader reader=new Reader();
        reader.start();
      }

      Writer writer=new Writer();
      writer.start();
  }

  public class Reader extends Thread {
    @Override
    public void run() {
      System.out.println("Reader "+this.getId()+" starts **");
   	  while(writer == 1) {
	    try{
		mutex.acquire();
            }
	    catch(InterruptedException e){}
	    ++readerCount;
	    if(readerCount == 1) {
		try{
		db.acquire();
            }
	    catch(InterruptedException e){}
}
   	    mutex.release();
            System.out.println("Reader "+this.getId()+" read ["+Buffer+"] from Buffer. ");
	    try{
		mutex.acquire();
            }
	    catch(InterruptedException e){}

            --readerCount;

	    if(readerCount == 0) db.release();
          
	    mutex.release();
        // must put it to sleep to delay its effect a bit
        try{ Thread.sleep(250);
		}catch(InterruptedException e){}
	  }


	  System.out.println("Reader "+this.getId()+" end.\n");
    }
  }

  public class Writer extends Thread {
    @Override
    public void run() {
      System.out.println("Writer "+this.getId()+" starts **");
      for(int i=0;i<10;i++){
		try{
		db.acquire();
            }
	    catch(InterruptedException e){ }
		System.out.print("Writer "+this.getId()+" write <");
		Buffer = "This is data number" +i;
		System.out.println(Buffer+"> to Buffer. ");
		db.release();

        // must put it to sleep to delay its effect a bit
        try{ Thread.sleep(500);
		}catch(InterruptedException e){}
	  }

	  System.out.println("Writer "+this.getId()+" end.\n");
	  writer = 0;
    }
  }
}
