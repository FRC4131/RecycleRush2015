package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI{
	private Joystick controller, enhancedIO;
	public OI(int controller, int enhancedIO){
		this.controller = new Joystick(controller);
		this.enhancedIO = new Joystick(enhancedIO);
	}
	public double getSpeed(){return controller.getRawAxis(1);}//Movement speed during operator-controlled driving
	public double getStrafe(){return controller.getRawAxis(0);}//Strafing speed during operator-controlled driving
	public double getRotation(){return controller.getRawAxis(4);}//Turning speed during operator-controlled driving
	public boolean getButton(int button){return controller.getRawButton(button);}
	public boolean getButton(char button){
		switch(button){
			case('a'): case('A'): return getButton(1);
			case('b'): case('B'): return getButton(2);
			case('x'): case('X'): return getButton(3);
			case('y'): case('Y'): return getButton(4);
			case('l'): case('L'): return getButton(5);//Left bumper
			case('r'): case('R'): return getButton(6);//Right bumper
			default: return false;
		}
	}
	public boolean getIOSwitch(int port){return enhancedIO.getRawButton(port);}
	public double getIOPot(int port){return enhancedIO.getRawAxis(port);}
}
