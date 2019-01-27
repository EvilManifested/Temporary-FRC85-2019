/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Addresses;
import frc.robot.OI;
import frc.robot.commands.drivetrain.DriveWithJoystick;
import frc.robot.sensors.IMU;
import frc.robot.Variables;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import java.util.Arrays;

/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private double kPRot = 0.1, kIRot = 0.0, kDRot = 0.0;
    //private double kPForwardOnly = 0.0, kIForwardOnly = 0.0, kDForwardOnly = 0.0; // might end up only using Rot constants
    //private double baseJoystickAngle = 0.0; // don't think we need this for what we are doing yet
    private double targetAngle = 0.0;
    private double[] wheelSpeeds = new double[4];

    private static DriveTrain _instance = null;
	private WPI_TalonSRX _leftFrontMotor, _leftBackMotor, _rightFrontMotor, _rightBackMotor;

    private MecanumDrive _mDrive;

    private DriveTrain() {
        _leftFrontMotor = new WPI_TalonSRX(Addresses.DRIVETRAIN_LEFT_FRONT_MOTOR);
        _leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        _leftBackMotor = new WPI_TalonSRX(Addresses.DRIVETRAIN_LEFT_BACK_MOTOR);
        _leftBackMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        _rightFrontMotor = new WPI_TalonSRX(Addresses.DRIVETRAIN_RIGHT_FRONT_MOTOR);
        _rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        _rightBackMotor = new WPI_TalonSRX(Addresses.DRIVETRAIN_RIGHT_BACK_MOTOR);
        _rightBackMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        // Mecanum Drive constructor
        _mDrive = new MecanumDrive(_leftFrontMotor, _leftBackMotor, _rightFrontMotor, _rightBackMotor);
        _mDrive.setDeadband(.1);
    }

    public static DriveTrain getInstance() {
        if (_instance == null) {
        _instance = new DriveTrain();
        }
        return _instance;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveWithJoystick());
    }

    /** 
     * args for inputs: 
     * 0 - bool for headedness
     * 1 - xSpeed
     * 2 - ySpeed
     * 3 - zRotation
     * 4 - gyroAngle (0 if not really using)
     */

    public void cartDrive(double[] inputs) {
        int i;

        if (Math.abs(inputs[1]) > Variables.getInstance().DEADBAND 
            || Math.abs(inputs[2]) > Variables.getInstance().DEADBAND
            || Math.abs(inputs[3]) > Variables.getInstance().DEADBAND) {
            for (i = 0; i < 3; i++) {
                if (inputs[i] > 1.0) {
                    inputs[i] = 1.0;
                } else if (inputs[i] < -1.0) {
                    inputs[i] = -1.0; 
                }
            }

            Vector2d vector = new Vector2d(inputs[2], inputs[1]);
            if (inputs[0] == 1) { // if headless, account for it
                vector.rotate(inputs[4]);
            }

            if (Math.abs(inputs[3]) < Variables.getInstance().DEADBAND && inputs[0] == 0) { // will deal with headless later
                if (OI.getInstance().forwardOnly() && Math.abs(inputs[4]) < (Variables.getInstance().MAX_TURNS * 360)) { // if we go over the max amount of turns then don't even bother
                    setForwardOnlyTargetAngle();
                    targetAngle = fixTargetAngle(inputs[4]);
                } else {
                    setTargetAngle(inputs[4]);
                }
                inputs[3] = OI.getInstance().applyPID(OI.getInstance().ROT_SYSTEM, inputs[4], targetAngle, kPRot, kIRot, kDRot);
            }

            wheelSpeeds[0] = vector.x + vector.y + inputs[3];
            wheelSpeeds[1] = vector.x - vector.y - inputs[3];
            wheelSpeeds[2] = vector.x - vector.y + inputs[3];
            wheelSpeeds[3] = vector.x + vector.y - inputs[3];

            limitSpeeds(wheelSpeeds);

            _leftFrontMotor.set(wheelSpeeds[0]);
            _rightFrontMotor.set(-wheelSpeeds[1]);
            _leftBackMotor.set(wheelSpeeds[2]);
            _rightBackMotor.set(-wheelSpeeds[3]);
        }
    }

    /**
     * Returns the selected motor's encoder position (count)
     * 1 Rotation = 4096 counts
     */
    public double getLeftFrontPosition() {
        return _leftFrontMotor.getSelectedSensorPosition();
    }

    public double getLeftBackPosition() {
        return _leftBackMotor.getSelectedSensorPosition();
    }

    public double getRightFrontPosition() {
        return _rightFrontMotor.getSelectedSensorPosition();
    }

    public double getRightBackPosition() {
        return _rightBackMotor.getSelectedSensorPosition();
    }

    /**
     * Returns a value of (-1.0,1.0)
     */
    public double getLeftFrontPercent() {
        return _leftFrontMotor.get();
    }

    public double getLeftBackPercent() {
        return _leftBackMotor.get();
    }

    public double getRightFrontPercent() {
        return _rightFrontMotor.get();
    }

    public double getRightBackPercent() {
        return _rightBackMotor.get();
    }

    public WPI_TalonSRX getIMUTalon() {
        return _leftBackMotor;
    }

    private void setTargetAngle(double gyroAngle) { // if necessary, change the target angle
        if (Math.abs(Math.abs(gyroAngle) - Math.abs(targetAngle)) > Variables.getInstance().TOLERANCE_ANGLE) {
            targetAngle = gyroAngle;
        }
    }

    /*
    okay let's do some math explanations & why i thought this was a good idea
    the goal is to get the angle with left from the y axis being positive going counter-clockwise (because that's how gyro goes)
    so we absolute value the whole thing to keep it within range of arctan's use
    joystick angle formula thus becomes:
    arctan(|X / Y|)
    then we fix it using the method in OI to adjust it so that we can use it (i.e. arctan doesn't always return 0-90!), commented there
    note that this is if you want the robot to go FORWARD facing this angle
    */
    private void setForwardOnlyTargetAngle() {
        double joystickAngle = Math.toDegrees(Math.atan(Math.abs(OI.getInstance().getXInput() / OI.getInstance().getYInput()))); // get angle from the top, left being positive
        joystickAngle = OI.getInstance().fixArcTangent(joystickAngle, -OI.getInstance().getXInput(), OI.getInstance().getYInput()); // x is inverted here to make sure that the atan result is matched w positive x
        if (Math.abs((Math.abs(targetAngle) % 360) - Math.abs(joystickAngle)) > Variables.getInstance().TOLERANCE_ANGLE) { // if the new angle differs "significantly"
            targetAngle = joystickAngle;
        }
    }

    private void limitSpeeds(double[] speeds) { // take the highest speed & then adjust everything else proportionally if it is over 1
        double maxMagnitude = Math.abs(speeds[0]);
        int i;
        for (i = 0; i < 3; i++) {
            if (Math.abs(speeds[i]) > maxMagnitude) {
                maxMagnitude = Math.abs(speeds[i]);
            }
        }
        if (maxMagnitude > 1.0) {
            for (i = 0; i < 3; i++) {
                speeds[i] = speeds[i] / maxMagnitude;
            }
        }
    }

    private double fixTargetAngle(double gyroAngle) { // adjust target angle to the closest possible so we don't get stuck rotating for 9 years
        double minDiff = 180.0;
        double newTargetAngle = 0.0;
        int i;
        
        for (i = -Variables.getInstance().MAX_TURNS; i < Variables.getInstance().MAX_TURNS; i++) {
            if ((targetAngle + 360 * i - gyroAngle) < minDiff) {
                minDiff = (targetAngle + 360 * i - gyroAngle);
                newTargetAngle = (targetAngle + 360 * i);
            }
        }
        return newTargetAngle;
    }

}