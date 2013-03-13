package views;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;


import models.Entity;
import models.Food;
import models.Robot;

import controllers.StageMouseListener;
public class Stage extends JPanel {

	
	private boolean painting;
	// Boolean value indicating if the Stage is currently painting it's components.
	private ArrayList<Entity> entities;
	// Every entity that exsits within the Stage.
	private World world;
	// The JFrame the stage is housed in.
	
	//What action should the mouse perform on the stage;
	private int mouseAction;
	
	public Stage(World world)
	{
		this.setSize(1000,500);
		this.setBackground(Color.WHITE);
		this.addMouseListener(new StageMouseListener(this));
		// Set properties of JPanel superclass. Add Mouse Listeneger.
		
		this.world = world;
		this.painting = false;
		this.entities = new ArrayList<Entity>();
		// Initialise class membwers.
		
		this.mouseAction = 0;
		//Adding a number of entities to the array...
		for(int i = 1; i < 20; i++){
			Random rX = new Random();
			Random rY = new Random();
			int initX = rX.nextInt(this.getWidth());
			int initY = rY.nextInt(this.getHeight());
			entities.add(new Robot(initX, initY, this,"R" + Integer.toString(i), 1, 50));
		}
		// Add all entities to a collection.
		//Activating the entities
		for(Entity r: entities){
			if(r instanceof Robot)
				((Robot) r).activateRobot();
		}
	}
	
	public void paint(Graphics g)
	{
		painting = true;
		super.paint(g);
		for(Entity rob : entities)
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
		ArrayList<Entity> entitiesInRange = new ArrayList<Entity>();
		for(int x = 0; x < entities.size(); x++)
		{
			// Can't use foreach. Gives a nullpointerException for some reason. This is just as good.
			Entity r = entities.get(x);
			Point p = r.getLocation();
			// Grabs the robot's location, and tests it against the robot's perception.
			if((p.x >= x1 && p.x <= x2) && (p.y >= y1 && p.y <= y2))
			{
				
				// If the robot is within the current robot's perceptive radius,
				// then return the robot.
				entitiesInRange.add(r);
			}
		}
		// If no robot is found, return null.
		return entitiesInRange;
	}
	
	
	public void showEntityAtCoordsDetails(int x, int y)
	{
		// When the mouse is clicked, this finds the entity under the mouse and displays it's details.
		for (int pos = 0; pos < entities.size(); pos++) {
			Entity r = entities.get(pos);
			Point p = r.getLocation();

			if ((x >= p.x && x <= p.x + 50) && (y >= p.y && y <= p.y + 50)) {
				r.setShowDetails(!r.getShowDetails());
			}
		}
	}
	
	
	public World getWorld()
	{
		// Returns the JFrame the stage is housed in.
		return world;
	}
	
	public void insertRobot(String name)
	{
		// Injects a robot object into the stage with @param name.
		Random rand = new Random();
		Robot r = new Robot(10 + rand.nextInt(500), 10 + rand.nextInt(440), this,name, 1 + rand.nextInt(5), 10);
		r.activateRobot();
		entities.add(r);
	}
	
	public void insertFood(int x, int y)
	{
		// Injects a food object into the stage at a random position.
		Random rand = new Random();
		Food f = new Food(x, y,rand.nextInt(1000), this);
		entities.add(0, f);
	}
	
	public void removeEntity(Entity e)
	{
		while(painting) {}
		// Wait until not painting.
		if(entities.contains(e))
		entities.remove(e);
	}
	
	public void setMouseAction(int action){
		this.mouseAction = action;
	}
	
	public int getMouseAction(){
		return this.mouseAction;
	}

}
