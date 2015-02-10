package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class OI{
	private static final double deadband = 0.05;
	private Joystick controller;
	public OI(int port){
		controller = new Joystick(port);
	}
	public double getX(){return controller.getRawAxis(0) - (controller.getRawAxis(0) % deadband);}
	public double getY(){return -(controller.getRawAxis(1) - (controller.getRawAxis(1) % deadband));}
	public int getPOV(){return controller.getPOV();}
	public double getRotation(){return controller.getRawAxis(4) - (controller.getRawAxis(4) % deadband);}
	public boolean getButton(Button button){return controller.getRawButton(button.button);}
	/**
	 * Wait for a button to be pressed (and optionally released).
	 * @param button The button to wait for.
	 * @param release If true, the method will return control when the button has been pressed and released. If false, it will only wait
	 * for a press.
	 */
	public void waitFor(Button button, boolean release){
		while(!getButton(button)) Timer.delay(0.005);//Block until the button is pressed,
		if(!release) return;
		while(getButton(button)) Timer.delay(0.005);//then until it's released.
	}
	public static enum Button{
		A(1), B(2), X(3), Y(4), LEFT_BUMPER(5), RIGHT_BUMPER(6), SCREEN_SELECT(7), MENU(8), LEFT_STICK(9), RIGHT_STICK(10);
		public final int button;
		private Button(int button){this.button = button;}
	}
}
