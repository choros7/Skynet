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

	public RobotConsciousness(Robot r) {
		this.robot = r;
		randNum = new Random();
		decisionGapX = 200;
		decisionGapY = 200;
		// Add some initial values for decision gaps.
		changeDirection(0);
		// Sets initial direction for the robots.
		previousEnergy = robot.getEnergy();
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

			changeDirection(stepCount);
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
		// Will grab all entities if they are nearby
		ArrayList<Entity> entities = robot.getNearRobots(
				robotX - robot.getPerception() / 2,
				robotX + robot.getPerception() / 2,
				robotY - robot.getPerception() / 2,
				robotY + robot.getPerception() / 2);
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
					} else {
						// doesn't work yet. Couldn't be bothered.
						// Supposed to turn orange when a robot gets close to a
						// dead robot.
						robot.setColor(Color.ORANGE);
					}
				} else {
					// If robots are not nearby ensure the colour value is
					// correct.
					robot.setIsNear(false);
				}
			} else if (e instanceof Food) {
				if (robot.foodNeed()) {
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
