import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
public class Robot extends JPanel implements Entity {
	
	private RobotConsciousness thread;
	// The robot's brain. Applies changes to the robot as it moves around.
	private Stage parent;
	// The stage the robot is in.
	private Color robotColor;
	// The color of the robot. 
	private String name;
	// The name of this robot.
	private boolean isNear;
	// Boolean value indicating if a robot is near this robot.
	private int energy;
	// The amount of energy this robot has.
	private int energyDecrement;
	// The amount the energy will decrease by each step.
	private int perception;
	// How far the robot can see an event.
	private boolean showDetails;
	
	private ArrayList<Entity> tempMemory; 
	
	/**
	 * 
	 * @param x location x
	 * @param y location y
	 * @param parent where the robot will be drawn
	 * @param name Name of the robot
	 * @param energyDecrement Robot's energy usages
	 */
	public Robot(int x, int y, Stage parent, String name, int energyDecrement, int perception) {
		this.setBounds(x,y,30,30);
		thread = new RobotConsciousness(this);
		super.setSize(100,100);
		this.parent = parent;
		this.robotColor = Color.GREEN;
		this.name = name;
		this.isNear = false;
		this.energy = 10000;
		this.energyDecrement = energyDecrement;
		this.perception = perception;
		tempMemory = new ArrayList<Entity>();
	}
	
	public void paint(Graphics g)
	{
		if(this.energy > 0) {
			g.setColor(robotColor);
			// This red / green circle is the robot.
			try{
				if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
					g.drawImage(ImageIO.read(new File("/home/dreamer/Pictures/Robo.png")), this.getLocation().x, this.getLocation().y, 30, 30, null);
				else
					g.drawImage(ImageIO.read(new File("C:\\Users\\admin\\Desktop\\robot.gif")),this.getLocation().x, this.getLocation().y, 30, 30, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		
			g.setColor(Color.BLACK);
			if(showDetails)
			{
				// Prints out the robot's statistics.
				g.setColor(Color.BLACK);
				g.drawString("Name: " + name, this.getLocation().x + 20, this.getLocation().y - 15);
				g.drawString("Energy Left:"  + energy, this.getLocation().x + 20, this.getLocation().y - 5);
				g.drawString("X: " + this.getLocation().x + " Y: " + this.getLocation().y, this.getLocation().x + 20, this.getLocation().y + 5);
			}
		}
	}
	
	/**
	 * proxy method to access the method in the stage object.
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public ArrayList<Entity> getNearRobots(int x1, int x2, int y1, int y2)
	{
		return parent.getObjectInRange(x1, x2, y1, y2);
	}
	
	public void setColor(Color robotColor)
	{
		this.robotColor = robotColor;
	}
	
	public void activateRobot()
	{
		thread.start();
	}
	
	public void setIsNear(boolean value)
	{
		this.isNear = value;
		// Sets the new "isNear" value.
		if(isNear)
			this.robotColor = Color.red;
			// Sets the robot's color if a robot is near.
		else
			this.robotColor = Color.green;
			// Sets the robots color if a robot is not near.
	}
	
	public void decrementEnergy()
	{
		// Decreases the robot's energy supply.
		energy -= energyDecrement;
	}
	
	public boolean isAlive()
	{
		// if the robot has more than 0 energy, it is alive.
		return energy > 0;
	}
	
	public int getPerception()
	{
		return perception;
	}
	
	public void setShowDetails(boolean showDetails)
	{
		this.showDetails = showDetails;
	}
	
	public boolean getShowDetails()
	{
		return showDetails;
	}
	
	@Override
	public void interact(Entity other) {
		boolean knowYou = false;
		if(other instanceof Robot)
		{
			//Search the memory for known robots
			for(Entity r: tempMemory){
				if(other.equals(r));
				knowYou = true;
				//System.out.println(this.getName() + ": I know " + ((Robot) other).getName());
				break;
			}
			//If I don't know you... do something.
			if(!knowYou){
				System.out.println(name + " says Good morning to" + ((Robot) other).getName());
				tempMemory.add(other);
			}
			
			//Robot r = (Robot)other;
		}
	}
	@Override
	public int getPriority() {
		return 0;
	}
	
	public void addEnergy(int amountToAdd)
	{
		this.energy += amountToAdd;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
	public void checkInteractions(ArrayList<Entity> entitiesInRange){
		ArrayList<Entity> newMemory = new ArrayList<Entity>();
		for(Entity r: tempMemory){
			//here I am trying to figure out the algorithm for forgetting...
			for(Entity e : entitiesInRange)
			{
				if(e == r)
				{
					// If e and r are the same object, it means we shouldn't forget e.
					newMemory.add(e);
				}
			}
		}
		tempMemory = newMemory;
		// Update our memory with the objects that are still within our perception radius.
	}
}
