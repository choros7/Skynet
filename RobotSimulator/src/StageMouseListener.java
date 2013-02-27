import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class StageMouseListener implements MouseListener {

	private Stage stage;
	
	public StageMouseListener(Stage stage)
	{
		this.stage = stage;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		stage.showEntityAtCoordsDetails(e.getX(), e.getY());
		
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
