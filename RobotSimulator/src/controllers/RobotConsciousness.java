package controllers;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import views.Stage;

import models.Entity;
import models.Food;
import models.Robot;

public class RobotConsciousness extends Thread {

	// Random number generator.
	private Robot robot;
	// The robot who's consciousness this thread represents.
	private int dx;
	// The distance each step is on the x axis.
	private int dy;
	// The distance each step is on the y axis.
	private int previousEnergy;
	// The robot's energy level on it's last iteration. For feeding.
	private Entity goal;
	// When a goal is found to move to, it is set here.
	private RobotPathFinder pathFinder;
	// The pathfinding class used to traverse the stage to get to goals.
    private RobotPerception perception;
    // The robot's interface with the external world.
    
	public RobotConsciousness(Robot r, Stage world) {
		this.robot = r;
		// Sets initial direction for the robots.
		previousEnergy = robot.getEnergy();
		goal = null;
		pathFinder = new RobotPathFinder(this);
		perception = new RobotPerception(this,world);
		pathFinder.changeDirection(0);
	}

	public void run() {
		int stepCount = 0;


		while (robot.isAlive()) {

			int robotX = robot.getLocation().x;
			int robotY = robot.getLocation().y;
			// Grab the current location of the robot.

			stepCount++;

			if(!setEntityGoal(robot.getLocation().x, robot.getLocation().y))
			{
				// if no goal was found, walk in random direction.
				pathFinder.changeDirection(stepCount);
			}
			checkProximity(robotX, robotY);
			// Change direction if timer is up.
			if ((robotX >= 950 || robotX <= 0)) {
				dx *= -1;
				// Reverse X direction if reaching boundaries.
			}
			if (robotY >= 450 || robotY <= 0) {
				dy *= -1;
				// Reverse Y direction if reaching boundaries.
			}
			// Set the new location of the robot.
			robot.setLocation(new Point(robotX + dx, robotY + dy));
			if (dx != 0 || dy != 0) {
				robot.decrementEnergy();
				// If robot is moving, then robot is using energy.
			}
			if (!robot.isAlive()) {
				robot.setColor(Color.BLACK);
			}
			previousEnergy = robot.getEnergy();
			try {
				// Sleep the thread for 40ms, so they don't move too quickly.
				// Can be adjusted.
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void checkProximity(int robotX, int robotY) {
		// Will grab all entities that can be interacted with.
		int perceptionBoundary = robot.getPerception() / 2;
		
		ArrayList<Entity> entities = perception.getNearEntities(
										robotX - perceptionBoundary,
										robotX + perceptionBoundary,
										robotY - perceptionBoundary,
										robotY + perceptionBoundary);
		
		robot.checkInteractions(entities);
		
		for (int x = 0; x < entities.size(); x++) {
			Entity e = entities.get(x);

			// Cycle through all entities nearby.
			if (e instanceof Robot) {
				Robot r = (Robot) e;
				if (r != null && !r.equals(robot)) {
					// If r is null, no robot was there. if r.equals(robot) then
					// we're just looking at ourselves.
					if (r.isAlive()) {
						// Apply changes.
						robot.setIsNear(true);
						r.setIsNear(true);
						robot.interact(r);
						r.interact(robot);
					}
				} 
				else {
					// If robots are not nearby ensure the colour value is
					// correct.
					robot.setIsNear(false);
				}
			} else if (e instanceof Food) {
				if(e == goal)
				{
					goal = null;
				}
				if (robot.needsFood()) {
					Food f = (Food) e;
					if (f.getStoredEnergy() > 0) {
						f.interact(robot);
						if (robot.getEnergy() > previousEnergy) {
							robot.setColor(Color.PINK);
							dx = 0;
							dy = 0;
							// Robot is still feeding so will stay still.
						}
					}
				}
			}
		}
		
		
	}

	public boolean setEntityGoal(int robotX, int robotY)
	{
		if(goal == null)
		{
			// No goal has been set, so it's time to look for one.
			int perceptionBoundary = 200;
			ArrayList<Entity> entities = perception.getNearEntities(
					robotX - perceptionBoundary,
					robotX + perceptionBoundary,
					robotY - perceptionBoundary,
					robotY + perceptionBoundary);
			
			if(entities.size() > 0)
			{
				int index = 0;
				while(index < entities.size() && !(entities.get(index) instanceof Food))
				{
					index++;
				}
				if(index < entities.size()) {
					// Grab the food goal. No point hording with other robots.
					goal = entities.get(index);
				}
				// Set the direction to the food goal.
				return pathFinder.setDirectionToGoal(goal);
			}
		}
		else
		{
			// Goal has already been found, and we need to get close to it.
			return pathFinder.setDirectionToGoal(goal);
		}
		
		return false;
	}
		
	
	
	public void setDx(int dx)
	{
		this.dx = dx;
	}
	
	public int getDx()
	{
		return dx;
	}
	
	public void setDy(int dy)
	{
		this.dy = dy;
	}
	
	public int getDy()
	{
		return dy;
	}
	
	public Point getLocation()
	{
		return robot.getLocation();
	}
}
