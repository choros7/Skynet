package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import models.Robot;

import controllers.RepaintThread;



public class World extends JFrame {

	public static void main(String[] args)
	{
		new World();
	}
	
	 private Stage stage;
	 RepaintThread thread;
	 
	public World()
	{
		this.setTitle("Skynet");
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
		
		buttonAddRobot();
		buttonAddFood();
		buttonHammer();
		
		
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
	
	
	
	private void buttonAddFood(){
		JButton foodButton = new JButton("Add Food!");
		foodButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				stage.setMouseAction(1);
			}
		});
		foodButton.setBounds(10,  500, 120, 50);
		foodButton.setVisible(true);
		this.add(foodButton);
	}
	
	private void buttonAddRobot(){
		JButton button = new JButton("Add Robot!");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				stage.insertRobot(JOptionPane.showInputDialog("Insert the robot's name!"));
			}
		});
		button.setBounds(130, 500, 120, 50);
		button.setVisible(true);
		this.add(button);
	}
	
	private void buttonHammer(){
		JButton hammer = new JButton("Hammer");
		hammer.setBounds(250, 500, 120, 50);
		hammer.setVisible(true);
		hammer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stage.setMouseAction(2);
			}
		});
		
		this.add(hammer);
	}

}
