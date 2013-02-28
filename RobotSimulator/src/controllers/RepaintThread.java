package controllers;
import views.Stage;


public class RepaintThread extends Thread {

	private Stage parent;
	
	public RepaintThread(Stage parent)
	{
		this.parent = parent;
	}
	
	public void run()
	{
		while(true) {
			parent.repaint();
			try {
				Thread.sleep(10);
				// The repaint has to refresh quickly so the animation is smooth.
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
