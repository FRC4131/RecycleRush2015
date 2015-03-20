package org.usfirst.frc.team4131.robot.oi;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class GameController extends Joystick{
	public static final int A=1, B=2, X=3, Y=4, LEFT_BUMPER=5, RIGHT_BUMPER=6, SCREEN_SELECT=7, MENU=8, LEFT_STICK=9, RIGHT_STICK=10;
	public static final int LEFT_X=0, LEFT_Y=1, LEFT_TRIGGER=2, RIGHT_TRIGGER=3, RIGHT_X=4, RIGHT_Y=5;
	public final int port;
	public GameController(int port){super(port); this.port = port;}
	public boolean getButton(int button){return isConnected() && getRawButton(button);}
	public double getAxis(int axis){return isConnected() ? getRawAxis(axis) : 0;}
	public boolean isConnected(){return DriverStation.getInstance().getStickAxisCount(port) > 0;}//Disconnected joysticks return 0 axes.
}
