package controllers;
import java.awt.Point;
import java.util.Random;

import models.*;


public class RobotPathFinder {

	private RobotConsciousness robot;
	// The RobotConsciousness that accesses the methods in this class.
	private int decisionGapX;
	// The amount of cycles before a new decision on movement on the X axis.
	private int decisionGapY;
	// The amount of cycles before a new decision on movement on the Y axis.
	private Random randNum;
	// The randum number generator for this class.
	
	// Constructor. Takes one @param, robot.
	
	public RobotPathFinder(RobotConsciousness robot)
	{
		this.robot = robot;
		randNum = new Random();
		// Initialise the starting decision gaps.
		decisionGapX = randNum.nextInt(200) + 200;
		decisionGapY = randNum.nextInt(200) + 200;
	}

	// Puts the robot in the right direction to it's goal. Takes one @param, goal.
	public boolean setDirectionToGoal(Entity goal)
	{
		if(goal instanceof Robot)
		{
			// Add code for robot based decision. We need to implement personalities before we can do this.
			return false;
		}
		else if(goal instanceof Food)
		{
				Point foodPos = goal.getLocation();
				// Get the difference between the two entities.
				Point difference = new Point(robot.getLocation().x - foodPos.x, robot.getLocation().y - foodPos.y);
				
				if(difference.x > 0)
				{
					// Entity is on the left of the robot, so set direction to go 
					robot.setDx(-1);
				}
				else
				{
					// Entity is on the right of the robot.
					robot.setDx(1);
				}
				
				if(difference.y > 0)
				{
					// Entity is above the robot, so set direction to go up.
					robot.setDy(-1);
				}
				else
				{
					// Entity is below the robot, so set direction to go down.
					robot.setDy(1);
				}
				return true;
			}
		return false;
		}
	
	public void changeDirection(int stepCount) {

		if (stepCount % decisionGapX == 0) {
			// If it is time for the robot to make a movement decision..
			if (decisionGapX % 2 == 0) {
				// If the randomly generated number is even...
				int difference = randNum.nextInt(2);
				robot.setDx(robot.getDx() - difference != 0 ? difference : -1);
				// Set the new direction of the robot based on it's movement minus the change in motion.

			} else {
				// If the randomly generated number is odd...
				int difference = randNum.nextInt(2);
				// Set the new direction of the robot based on it's movement PLUS the change in motion.
				robot.setDx(robot.getDx() + difference != 0 ? difference : -1);
			}
			// Reset a new decision point for the X co-ordinate.
			decisionGapX = 200 + randNum.nextInt(200);
		}

		if (stepCount % decisionGapY == 0) {
			// If it is time to alter the Y coordinate..
			if (decisionGapY % 2 == 0) {
				// Set the up and down direction by minusing the current direction from a random number (1,2).
				robot.setDy(robot.getDy() - randNum.nextInt(2));
			} else {
				// Same but with addition instead of subtraction.
				robot.setDy(robot.getDy() + randNum.nextInt(2));
			}

			// Reset the Y axis decision point.
			decisionGapY = 200 + randNum.nextInt(200);
		}
	}
    }
