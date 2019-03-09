/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.IMU;
import frc.robot.subsystems.BeltSolenoid;
import frc.robot.subsystems.BeltTrain;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Interruptable;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.RearSolenoid;
import frc.robot.subsystems.Mast;
import frc.robot.Diagnostics;
import frc.robot.Vision;
import frc.robot.commands.driverassistance.SendItBro;

import java.util.Arrays;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    public static boolean overrideLimits = false;

    private Diagnostics _diagnostics;

    private SendItBro _sendItBro;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        DriveTrain.getInstance();
        Intake.getInstance();
        Elevator.getInstance();
        Mast.getInstance();
        OI.getInstance();
        IMU.getInstance();
        Vision.getInstance();
        BeltSolenoid.getInstance();
        BeltTrain.getInstance();
        RearSolenoid.getInstance();
        Interruptable.getInstance();

        //_diagnostics = new Diagnostics();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
    }

    @Override
    public void autonomousInit() {
        teleopInit();

        //_sendItBro = new SendItBro();
        //_sendItBro.start();
    }

    /**
     * This function is called periodically during ̶a̶u̶t̶o̶n̶o̶m̶o̶u̶s̶  sandstorm.
     */
    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
        
        //_diagnostics.log();
        //Scheduler.getInstance().run();
        //Variables.getInstance().outputVariables();
    }

    @Override
    public void teleopInit() {
        IMU.getInstance().setFusedHeading(0);

        Scheduler.getInstance().removeAll();

        // Pneumatics

        BeltSolenoid.getInstance().setBeltSolenoid(false);
        RearSolenoid.getInstance().setRearSolenoid(false);

        // init the pid stuff 

        Arrays.fill(OI.getInstance().firstRun, true);
        Arrays.fill(OI.getInstance().errorSum, 0.0);
        Arrays.fill(OI.getInstance().lastActual, 0.0);
        
        Arrays.fill(OI.getInstance().stopArray, 0.0);

        super.teleopInit();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        Variables.getInstance().outputVariables();

        //_diagnostics.log();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledPeriodic() {
        Elevator.getInstance().setTargetPosition(Elevator.getInstance().getVerticalPosition());
        Mast.getInstance().setTargetPosition(Mast.getInstance().getHorizontalPosition());
        Intake.getInstance().setTargetPos(Intake.getInstance().getWristPosition());

        //_diagnostics.close();
    }

}