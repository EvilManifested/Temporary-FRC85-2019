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

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.util.Arrays;

/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private double kPRot = 0.05, kIRot = 0.0, kDRot = 0.0;
    private double kMaxOuputRot = 1.0, kMinOuputRot = -1.0;
    //private double kPForwardOnly = 0.0, kIForwardOnly = 0.0, kDForwardOnly = 0.0; // might end up only using Rot constants
    //private double baseJoystickAngle = 0.0; // don't think we need this for what we are doing yet
    private double targetAngle = 0.0;
    private double[] wheelSpeeds = new double[4];

    private static DriveTrain _instance = null;
    private TalonSRX _leftFrontMotor, _leftBackMotor, _rightFrontMotor, _rightBackMotor;

    private DriveTrain() {
        _leftFrontMotor = new TalonSRX(Addresses.DRIVETRAIN_LEFT_FRONT_MOTOR);
        _leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        _leftBackMotor = new TalonSRX(Addresses.DRIVETRAIN_LEFT_BACK_MOTOR);
        _leftBackMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        _rightFrontMotor = new TalonSRX(Addresses.DRIVETRAIN_RIGHT_FRONT_MOTOR);
        _rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        _rightBackMotor = new TalonSRX(Addresses.DRIVETRAIN_RIGHT_BACK_MOTOR);
        _rightBackMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
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
     * 0 - xSpeed - positive is right
     * 1 - ySpeed - positive is forward
     * 2 - zRotation - positive is counter-clockwise
     * 3 - gyroAngle (0 if not really using)
     */
    public void cartDrive(double[] inputs) {
        int i;

        if (Math.abs(inputs[0]) > Variables.getInstance().DEADBAND 
            || Math.abs(inputs[1]) > Variables.getInstance().DEADBAND
            || Math.abs(inputs[2]) > Variables.getInstance().DEADBAND) {
            for (i = 0; i < 2; i++) {
                if (inputs[i] > 1.0) {
                    inputs[i] = 1.0;
                } else if (inputs[i] < -1.0) {
                    inputs[i] = -1.0; 
                }
            }

            Vector2d vector = new Vector2d(inputs[1], -inputs[0]); // invert x because left is negative
            if (OI.getInstance().isHeadless() || OI.getInstance().forwardOnly()) { // if headless, account for it
                vector.rotate(inputs[3]);
            }

            if (Math.abs(inputs[2]) < Variables.getInstance().DEADBAND && !OI.getInstance().isHeadless()) { // we will deal with headless later ahaha
                if (OI.getInstance().forwardOnly()) {
                    setForwardOnlyTargetAngle();
                    fixAngles(inputs[3]);
                    inputs[2] = OI.getInstance().applyPID(OI.getInstance().ROT_SYSTEM, inputs[3], targetAngle, kPRot, kIRot, kDRot, kMaxOuputRot, kMinOuputRot);
                } else {
                    setTargetAngleMoving(inputs[3]);
                    inputs[2] = OI.getInstance().applyPID(OI.getInstance().ROT_SYSTEM, inputs[3], targetAngle, kPRot, kIRot, kDRot);
                }
            }

            wheelSpeeds[0] = vector.x + vector.y + inputs[2];
            wheelSpeeds[1] = vector.x - vector.y - inputs[2];
            wheelSpeeds[2] = vector.x - vector.y + inputs[2];
            wheelSpeeds[3] = vector.x + vector.y - inputs[2];

            limitSpeeds(wheelSpeeds);

            _leftFrontMotor.set(ControlMode.PercentOutput, wheelSpeeds[0]);
            _rightFrontMotor.set(ControlMode.PercentOutput, -wheelSpeeds[1]);
            _leftBackMotor.set(ControlMode.PercentOutput, wheelSpeeds[2]);
            _rightBackMotor.set(ControlMode.PercentOutput, -wheelSpeeds[3]);
        } else {
            _leftFrontMotor.set(ControlMode.PercentOutput, 0.0);
            _rightFrontMotor.set(ControlMode.PercentOutput, 0.0);
            _leftBackMotor.set(ControlMode.PercentOutput, 0.0);
            _rightBackMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }

    public double getTargetAngle(Vector2d vector) {
        double targetAngle;
        targetAngle = Math.atan(OI.getInstance().getYInput() / OI.getInstance().getXInput());

        return targetAngle;
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
        return _leftFrontMotor.getMotorOutputPercent();
    }

    public double getLeftBackPercent() {
        return _leftBackMotor.getMotorOutputPercent();
    }

    public double getRightFrontPercent() {
        return _rightFrontMotor.getMotorOutputPercent();
    }

    public double getRightBackPercent() {
        return _rightBackMotor.getMotorOutputPercent();
    }

    public TalonSRX getIMUTalon() {
        return _leftBackMotor;
    }

    private void setTargetAngleMoving(double gyroAngle) { // if necessary, change the target angle
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
        double joystickAngle = Math.toDegrees(Math.atan(-OI.getInstance().getXInput() / Math.abs(OI.getInstance().getYInput()))); // get angle from the top, making left positive
        joystickAngle = OI.getInstance().fixArcTangent(joystickAngle, OI.getInstance().getXInput(), OI.getInstance().getYInput()); // fix the arctan angle so that we get a full 360 degrees
        if (Math.abs((Math.abs(targetAngle) % 360) - Math.abs(joystickAngle)) > Variables.getInstance().TOLERANCE_ANGLE) { // if the new angle differs "significantly"
            targetAngle = joystickAngle;
        }
    }

    private void setTurn90TargetAngle(boolean[] directions) {

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

    private void fixAngles(double gyroAngle) { // adjust current angle to gyro angle
        double minDiff = 180.0;
        double newTargetAngle = 0.0;
        int i;

        for (i = -Variables.getInstance().MAX_TURNS; i < Variables.getInstance().MAX_TURNS; i++) {
            if (Math.abs(targetAngle + 360 * i - gyroAngle) < minDiff) {
                minDiff = (targetAngle + 360 * i - gyroAngle);
                newTargetAngle = targetAngle + 360 * i; // note that we can't edit targetAngle directly
            }
        }
        targetAngle = newTargetAngle;
    }
}