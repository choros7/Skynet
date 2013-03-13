package controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import views.Stage;


public class StageMouseListener implements MouseListener {

	private Stage stage;
	
	
	public StageMouseListener(Stage stage)
	{
		this.stage = stage;
	}

	/**
	 * Possible action:
	 * 0. Default click;
	 * 1. Add food;
	 * +++ robots are added at random locations and we do not need a mouse action for that.
	 * 2. Hammer;
	 * 3. ???
	 * 4. Profit.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		switch(stage.getMouseAction()){
			case 0:
				stage.showEntityAtCoordsDetails(e.getX(), e.getY());
				break;
			case 1:
				stage.insertFood(e.getX() - 25, e.getY() - 25);
				stage.setMouseAction(0);
				break;
			case 2:
				break;
			default:
				break;
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
