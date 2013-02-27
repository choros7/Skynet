import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class World extends JFrame {

	public static void main(String[] args)
	{
		new World();
	}
	
	 private Stage stage;
	 RepaintThread thread;
	 private JButton button;
	 
	public World()
	{
		this.setBackground(Color.BLACK);
		stage = new Stage(this);
		thread = new RepaintThread(stage);
		this.setSize(1000,600);
		this.setLayout(null);
		this.add(stage);
		stage.setBackground(Color.WHITE);
		stage.setVisible(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thread.start();
		
		button = new JButton("Add Robot!");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				stage.insertRobot(JOptionPane.showInputDialog("Insert the robot's name!"));
			}
		});
		button.setBounds(600, 500, 100, 50);
		button.setVisible(true);
		this.add(button);
		
		JButton foodButton = new JButton("Add Food!");
		foodButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				stage.insertFood();
			}
		});
		foodButton.setBounds(400,  500, 100, 50);
		foodButton.setVisible(true);
		this.add(foodButton);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		stage.paint(g);
	}
	
	public Robot getRobot()
	{
		return stage.getRobot();
	}

}
