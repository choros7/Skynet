package models;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import views.Stage;



public class Food extends Component implements Entity {
	Stage stage;
	
	int storedEnergy;
	private final int PRIORITY = 10;
	boolean showDetails;
	BufferedImage image;
	
	public Food(int x, int y, int storedEnergy, Stage stage) 
	{
		this.setBounds(x,y,50,50);
		this.storedEnergy = storedEnergy;
		this.setVisible(true);
		this.showDetails = false;
		this.stage = stage;
		try {
			//Windows: 
			//
			if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
				image = ImageIO.read(new File("/home/dreamer/Pictures/Food.png"));
			else
				image = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\cog.png"));
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void interact(Entity other) {
		if(other instanceof Robot && this.storedEnergy >= 1)
		{
			Robot r = (Robot)other;
			r.addEnergy(1);
			this.storedEnergy--;
		}
		
		if(storedEnergy <= 0)
		{
			stage.removeEntity(this);
		}
		
	}
	
	public void paint(Graphics g)
	{
		if(this.storedEnergy > 0) {
			g.setColor(Color.CYAN);
			g.fillRect(this.getLocation().x, this.getLocation().y, 50,50);
			g.drawImage(image,this.getLocation().x, this.getLocation().y,50,50, null);
			if(showDetails)
			{
				g.drawString("Energy:" + storedEnergy, this.getLocation().x + 10, this.getLocation().y - 10);
			}
		}
	}

	@Override
	public int getPriority() {
		return PRIORITY;
	}

	@Override
	public boolean getShowDetails() {
		return showDetails;
	}

	@Override
	public void setShowDetails(boolean newShowDetails) {
		this.showDetails = newShowDetails;
	}

	public int getStoredEnergy()
	{
		return this.storedEnergy;
	}

}
