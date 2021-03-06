package models;

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

import views.Stage;

import controllers.RobotConsciousness;
public class Robot extends JPanel implements Entity {
	
	private RobotConsciousness thread;
	// The robot's brain. Applies changes to the robot as it moves around.
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
	
	private int maxEnergy;
	private ArrayList<Entity> tempMemory; 
	
	/**
	 * 
	 * @param x location x
	 * @param y location y
	 * @param parent where the robot will be drawn
	 * @param name Name of the robot
	 * @param energyDecrement Robot's energy usages
	 */
	public Robot(int x, int y, Stage parent, String name,  int energyDecrement, int perception) {
		this.setBounds(x,y,30,30);
		thread = new RobotConsciousness(this, parent);
		super.setSize(100,100);
		this.robotColor = Color.GREEN;
		this.name = name;
		this.isNear = false;
		this.energy = 10000;
		this.maxEnergy = 10000;
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
		
			if(showDetails)
			{
				// Prints out the robot's statistics.
				g.setColor(robotColor);
				g.drawString("Name: " + name, this.getLocation().x + 20, this.getLocation().y - 15);
				g.drawString("Energy Left:"  + energy, this.getLocation().x + 20, this.getLocation().y - 5);
				g.drawString("X: " + this.getLocation().x + " Y: " + this.getLocation().y, this.getLocation().x + 20, this.getLocation().y + 5);
			}
		}
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
			this.robotColor = Color.blue;
			// Sets the robot's color if a robot is near.
		else{
			if(this.getEnergy() < this.maxEnergy / 2)
				this.robotColor = Color.orange;
			else if(this.getEnergy() < this.maxEnergy / 10)
				this.robotColor = Color.red;
			else
				this.robotColor = Color.green;
			// Sets the robots color if a robot is not near.
		}
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
				//System.out.println(name + " says Good morning to" + ((Robot) other).getName());
				tempMemory.add(other);
			}
			
			//Robot r = (Robot)other;
		}
	}
	@Override
	public int getPriority() {
		return 0;
	}
	
	public int getMaxEnergy(){
		return this.maxEnergy;
	}
	
	public boolean needsFood(){
		// Editted: This means robots will only feed until they are half full.
		// Ammended so robots will always feed until they are full. 
		
		/**
		 * In the future we can use the "fear" value to override this. 
		 */
		if(this.energy < this.maxEnergy)
			return true;
		else
			return false;
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
