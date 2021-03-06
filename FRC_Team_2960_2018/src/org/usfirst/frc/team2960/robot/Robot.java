/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2960.robot;

import org.usfirst.frc.team2960.robot.Commands.Auto.SwitchRightSide;
import org.usfirst.frc.team2960.robot.Subsytems.Drive;
import org.usfirst.frc.team2960.robot.Subsytems.Elevator;
import org.usfirst.frc.team2960.robot.Subsytems.Intake;
import org.usfirst.frc.team2960.robot.Subsytems.LEDs;
import org.usfirst.frc.team2960.robot.Subsytems.SubsystemBase;
import org.usfirst.frc.team2960.robot.Subsytems.Winch;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Trajectory;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	//Look at
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private Command kAutonomousCommand = null;
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();


	private OI oi;

	private Joystick driveJoystick;
	private Joystick operateJoystick;

	private SubsystemBase[] mSubsytemArray;

	private Trajectory trajectory;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);

		oi = new OI();
		driveJoystick = new Joystick(0);
		operateJoystick = new Joystick(1);



		//TalonSRX _tal = new TalonSRX(9);

		//System.out.println("Talon constructed ");

		mSubsytemArray = new SubsystemBase[]{Drive.getInstance(), Elevator.getInstance(), Intake.getInstance(), Winch.getInstance(), LEDs.getInstance()};
        UsbCamera camera;
		camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(320, 240);
		Drive.getInstance().setNeturalMode(NeutralMode.Brake);

		Drive.getInstance().zeroSensors();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//m_autoSelected = m_chooser.getSelected();
		//switch (m_autoSelected) {
			//case kCustomAuto:
				kAutonomousCommand = new SwitchRightSide();
		//}
		//JAmes was here #Changes
		Drive.getInstance().zeroSensors();
		Elevator.getInstance().zeroSensors();
		System.out.println("Auto selected: " + m_autoSelected);
		if(kAutonomousCommand != null) kAutonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		toSmartDashboard();
	}
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		//Elevator.getInstance().zeroSensors();
		oi.driveRobot(driveJoystick);
		oi.operateRobot(operateJoystick);
		SmartDashboard.putNumber("Joystick Value", driveJoystick.getRawAxis(1));

		toSmartDashboard();
		LEDs.getInstance().sendData("BlueBanner");

		Timer.delay(.005);

		for (SubsystemBase subsystem: mSubsytemArray) {
			subsystem.update();

		}
	}

	/**
	 * Reports all things to smartDashboard
	 */
	private void toSmartDashboard() {
		for (SubsystemBase subsystem: mSubsytemArray) {
			subsystem.toSmartDashboard();

		}

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

}
