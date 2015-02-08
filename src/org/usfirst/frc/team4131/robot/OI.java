package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI{
	private static final double deadband = 0.05;
	private Joystick controller;
	public OI(int port){
		controller = new Joystick(port);
	}
	public double getX(){return controller.getRawAxis(0) - (controller.getRawAxis(0) % deadband);}
	public double getY(){return controller.getRawAxis(1) - (controller.getRawAxis(1) % deadband);}
	public double getStrafe(){return controller.getRawAxis(4) - (controller.getRawAxis(4) % deadband);}
	public boolean getButton(int button){return controller.getRawButton(button);}
	public boolean getButton(Button button){return getButton(button.button);}
	public static enum Button{
		A(1), B(2), X(3), Y(4), LEFT_BUMPER(5), RIGHT_BUMPER(6), SCREEN_SELECT(7), MENU(8), LEFT_STICK(9), RIGHT_STICK(10);
		public final int button;
		private Button(int button){this.button = button;}
	}
}
