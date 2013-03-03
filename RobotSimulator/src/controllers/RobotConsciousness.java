package controllers;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import models.Entity;
import models.Food;
import models.Robot;

public class RobotConsciousness extends Thread {

	private Random randNum;
	// Random number generator.
	private Robot robot;
	// The robot who's consciousness this thread represents.
	int dx;
	// The distance each step is on the x axis.
	int dy;
	// The distance each step is on the y axis.
	int decisionGapX;
	// How often the robot decides to change the x direction.
	int decisionGapY;
	// How often the robot decides to change the y direction.
	int previousEnergy;
	// The robot's energy level on it's last iteration. For feeding.
	Robot currentInteraction;
	// The robot this robot is currently interacting with.
	Entity goal;
	// When a goal is found to move to, it is set here.

	public RobotConsciousness(Robot r) {
		this.robot = r;
		randNum = new Random();
		decisionGapX = 200;
		decisionGapY = 200;
		// Add some initial values for decision gaps.
		changeDirection(0);
		// Sets initial direction for the robots.
		previousEnergy = robot.getEnergy();
		goal = null;
	}

	public void run() {
		int stepCount = 0;

		
		decisionGapX = 200 + randNum.nextInt(200);
		decisionGapY = 200 + randNum.nextInt(200);

		while (robot.isAlive()) {

			int robotX = robot.getLocation().x;
			int robotY = robot.getLocation().y;
			// Grab the current location of the robot.

			// Apply the necessary changes if another robot is nearby.
			stepCount++;

			if(!setEntityGoal(robot.getLocation().x, robot.getLocation().y))
			{
				// if no goal was found, walk in random direction.
				changeDirection(stepCount);
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
		
		ArrayList<Entity> entities = robot.getNearEntities(
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
			System.out.println("No goal set. Finding new Goal..");
			// No goal has been set, so it's time to look for one.
			int perceptionBoundary = 200;
			ArrayList<Entity> entities = robot.getNearEntities(
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
				return setDirectionToGoal(goal);
			}
		}
		else
		{
			System.out.println("Goal set. Plotting path to goal");
			// Goal has already been found, and we need to get close to it.
			return setDirectionToGoal(goal);
		}
		
		return false;
	}
	
	private boolean setDirectionToGoal(Entity goal)
	{
		if(goal instanceof Robot)
		{
			// Add code for robot based decision. We need to implement personalities before we can do this.
			return false;
		}
		else if(goal instanceof Food)
		{
			if(robot.needsFood())
			{
				System.out.println("Made it to this point!");
				Point foodPos = goal.getLocation();
				// Get the difference between the two entities.
				Point difference = new Point(robot.getLocation().x - foodPos.x, robot.getLocation().y - foodPos.y);
				
				if(difference.x > 0)
				{
					// Entity is on the left of the robot, so set direction to go 
					dx = -1;
				}
				else
				{
					// Entity is on the right of the robot.
					dx = 1;
				}
				
				if(difference.y > 0)
				{
					// Entity is above the robot, so set direction to go up.
					dy = -1;
				}
				else
				{
					// Entity is below the robot, so set direction to go down.
					dy = 1;
				}
			}
			return true;
		}
		return false;
	}
	
	public void changeDirection(int stepCount) {
		if (stepCount % decisionGapX == 0) {
			dx = 0;
			if (decisionGapX % 2 == 0) {
				int difference = randNum.nextInt(2);
				dx = dx - difference != 0 ? difference : -1;

			} else {
				int difference = randNum.nextInt(2);
				dx = dx + difference != 0 ? difference : -1;
			}

			decisionGapX = 200 + randNum.nextInt(200);
		}

		if (stepCount % decisionGapY == 0) {
			dy = 0;
			if (decisionGapY % 2 == 0) {
				dy = dy - randNum.nextInt(2);
			} else {
				dy = dy + randNum.nextInt(2);
			}

			decisionGapY = 200 + randNum.nextInt(200);
		}
	}

}
