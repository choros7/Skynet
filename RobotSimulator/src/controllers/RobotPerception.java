package controllers;

import java.util.ArrayList;
import views.Stage;
import models.*;

public class RobotPerception {

	RobotConsciousness robot;
	
	Stage stage;
	
	public RobotPerception(RobotConsciousness robot, Stage stage)
	{
		this.robot = robot;
		this.stage = stage;
	}
	
	public ArrayList<Entity> getNearEntities(int x1, int x2, int y1, int y2)
	{
		return stage.getObjectInRange(x1, x2, y1, y2);
	}
}
