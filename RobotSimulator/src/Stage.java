import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
public class Stage extends JPanel {

	Robot r0 = new Robot(10,10, this, "R2-D2", 1);
	Robot r1 = new Robot(500,10, this, "C3-PO", 1);
	Robot r2 = new Robot(500,450, this, "Megatron", 20);
	Robot r3 = new Robot(10, 450, this, "T-1000", 2);
	Robot r4 = new Robot(250, 250, this, "Optimus Prime", 4);
	Food f = new Food(300,300,500, this);
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
		robots.add(f);
		robots.add(r0);
		robots.add(r1);
		robots.add(r2);
		robots.add(r3);
		robots.add(new Robot(25,25, this, "Doomsday", 1));
		robots.add(r4);
		
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
		return r0;
	}
	
	public Entity getObjectInRange(int x1, int x2, int y1, int y2)
	{
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
				return r;
			}
		}
		// If no robot is found, return null.
		return null;
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
		Robot r = new Robot(10 + rand.nextInt(500), 10 + rand.nextInt(440), this,name, 1 + rand.nextInt(5));
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
