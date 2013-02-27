import java.awt.Graphics;
import java.awt.Point;


public interface Entity {
	public void interact(Entity other);
	public int getPriority();
	public void paint(Graphics g);
	public Point getLocation();
	public boolean getShowDetails();
	public void setShowDetails(boolean newShowDetails);
}
