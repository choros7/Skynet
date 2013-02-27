import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;
public class Stage extends JPanel {

	
	boolean painting;
	int foodIndex = 0;
	
	ArrayList<Entity> robots;
	private World world;
	
	public Stage(World world)
	{
		painting = false;
		this.world = world;
		robots = new ArrayList<Entity>();
		this.setSize(1000,500);
		
		//Adding a number of robots to the array...
		for(int i = 1; i < 20; i++){
			Random rX = new Random();
			Random rY = new Random();
			int initX = rX.nextInt(this.getWidth());
			int initY = rY.nextInt(this.getHeight());
			robots.add(new Robot(initX, initY, this, "R" + Integer.toString(i), 1, 50));
		}
		// Add all robots to a collection.
		this.setBackground(Color.WHITE);
		
		//Activating the robots
		for(Entity r: robots){
			if(r instanceof Robot)
				((Robot) r).activateRobot();
		}
		this.addMouseListener(new StageMouseListener(this));
	}
	public void paint(Graphics g)
	{
		painting = true;
		super.paint(g);
		for(Entity rob : robots)
		{
			// Cycle through each robot and call their paint method.
			rob.paint(g);
		}
		painting = false;
	}
	
	public Robot getRobot()
	{
		return null;
	}
	
	public ArrayList<Entity> getObjectInRange(int x1, int x2, int y1, int y2)
	{
		ArrayList<Entity>robotsInRange = new ArrayList<Entity>();
		for(int x = 0; x < robots.size(); x++)
		{
			// Can't use foreach. Gives a nullpointerException for some reason. This is just as good.
			Entity r = robots.get(x);
			Point p = r.getLocation();
			// Grabs the robot's location, and tests it against the robot's perception.
			if((p.x >= x1 && p.x <= x2) && (p.y >= y1 && p.y <= y2))
			{
				
				// If the robot is within the current robot's perceptive radius,
				// then return the robot.
				robotsInRange.add(r);
			}
		}
		// If no robot is found, return null.
		return robotsInRange;
	}
	
	public void showEntityAtCoordsDetails(int x, int y)
	{
		for (int pos = 0; pos < robots.size(); pos++) {
			Entity r = robots.get(pos);
			Point p = r.getLocation();

			if ((x >= p.x && x <= p.x + 50) && (y >= p.y && y <= p.y + 50)) {
				r.setShowDetails(!r.getShowDetails());
			}
		}
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public void insertRobot(String name)
	{
		Random rand = new Random();
		Robot r = new Robot(10 + rand.nextInt(500), 10 + rand.nextInt(440), this,name, 1 + rand.nextInt(5), 10);
		r.activateRobot();
		robots.add(r);
	}
	
	public void insertFood()
	{
		Random rand = new Random();
		Food f = new Food(10 + rand.nextInt(900), 10 + rand.nextInt(400),rand.nextInt(1000), this);
		robots.add(0, f);
	}
	
	public void removeEntity(Entity e)
	{
		while(painting) {}
		// Wait until not painting.
		if(robots.contains(e))
		robots.remove(e);
	}

}
