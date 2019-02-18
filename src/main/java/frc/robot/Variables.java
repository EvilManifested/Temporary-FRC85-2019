/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.DriveTrain;
import frc.robot.sensors.ProxSensors;
import frc.robot.sensors.IMU;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Variables {

    private static Variables _instance;
    
    // DRIVETRAIN

    public final double DEADBAND = 0.1;
    public final double TOLERANCE_ANGLE = 6.0;
    public final int MAX_TURNS = 7; // if we go over 2520 degrees in either direction in one match then help.

    // LIFT

    // Lift Vertical PID
    private static final double kP_VLIFT = 0.1, kI_VLIFT = 0.000001, kD_VLIFT = 0.2;

    // Lift Horizontal PID
    private static final double kP_HLIFT = 0.1, kI_HLIFT = 0.000001, kD_HLIFT = 0.2;

    // Lift Vertical Postitions
    public static final int HATCH_LOW = 10;
    public static final int HATCH_MIDDLE = 100;
    public static final int HATCH_HIGH = 1000;
    public static final int HATCH_LOADING_STATION = 15;
    public static final int HATCH_FLOOR = 5;
    public static final int HATCH_DROP = 5;
    
    public static final int CARGO_LOW = 20;
    public static final int CARGO_MIDDLE = 120;
    public static final int CARGO_HIGH = 1200;
    public static final int CARGO_LOADING_STATION = 95;
    public static final int CARGO_FLOOR = 5;

    public static final double ROT_POS_1 = 90;
    public static final double ROT_POS_2 = -90;
    public static final double ROT_POS_3 = 60;
    public static final double ROT_POS_4 = 120;

    // Lift Horizontal Positions
    public static int platform = 0;
    public static int bumpers = 0;
    public static int CENTER_DRIVE = 0;
    public static int CLIMB_HAB = 0;

    public static final int HORIZONTAL_PROTECTED = 7;
    public static final int HORIZONTAL_UNPROTECTED = 0;
    public static final int BUMPERS_TWO = 3;
    public static final int range = 0;

    // INTAKE

    // Intake PID

    public static final double kP_INTAKE = 0.1, kI_INTAKE = 0.000001, kD_INTAKE = 0.2;

    // Intake Positions and Degrees
    public static final int INTAKE_DEGREE_ONE = 0;
    public static final int INTAKE_DEGREE_TWO = 90;
    public static final int INTAKE_DEGREE_THREE = 95;


    /**
     * Put variables here that should be changeable on the fly.
     */
    private Variables() {
        SmartDashboard.putNumber("kP_VLIFT", kP_VLIFT);
        SmartDashboard.putNumber("kI_VLIFT", kI_VLIFT);
        SmartDashboard.putNumber("kD_VLIFT", kD_VLIFT);

        SmartDashboard.putNumber("kP_HLIFT", kP_HLIFT);
        SmartDashboard.putNumber("kI_HLIFT", kI_HLIFT);
        SmartDashboard.putNumber("kD_HLIFT", kD_HLIFT);
        
        SmartDashboard.putNumber("kP_INTAKE", kP_INTAKE);
        SmartDashboard.putNumber("kI_INTAKE", kI_INTAKE);
        SmartDashboard.putNumber("kD_INTAKE", kD_INTAKE);

        SmartDashboard.putBoolean("Joysticks Enabled", false);
    }

    public static Variables getInstance() {
        if (_instance == null) {
			_instance = new Variables();
        }
        return _instance;
    }

    /**
     * get methods for changable variables
     */
    public double getVerticalLiftKP() {
        return SmartDashboard.getNumber("kP_VLIFT", kP_VLIFT);
    }

    public double getVerticalLiftKI() {
        return SmartDashboard.getNumber("kI_VLIFT", kI_VLIFT);
    }

    public double getVerticalLiftKD() {
        return SmartDashboard.getNumber("kD_VLIFT", kD_VLIFT);
    }

    public double getHorizontalLiftKP() {
        return SmartDashboard.getNumber("kP_HLIFT", kP_HLIFT);
    }

    public double getHorizontalLiftKI() {
        return SmartDashboard.getNumber("kI_HLIFT", kI_HLIFT);
    }

    public double getHorizontalLiftKD() {
        return SmartDashboard.getNumber("kD_HLIFT", kD_HLIFT);
    }

    public double getIntakeKP() {
        return SmartDashboard.getNumber("kP_INTAKE", kP_INTAKE);
    }

    public double getIntakeKI() {
        return SmartDashboard.getNumber("kI_INTAKE", kI_INTAKE);
    }

    public double getIntakeKD() {
        return SmartDashboard.getNumber("kD_INTAKE", kD_INTAKE);
    }

    /**
     * Repeatedly called in Robot.java
     */
    public void outputVariables() {
        SmartDashboard.putNumber("Left Front Percent", DriveTrain.getInstance().getLeftFrontPercent());
        SmartDashboard.putNumber("Left Back Percent", DriveTrain.getInstance().getLeftBackPercent());
        SmartDashboard.putNumber("Right Front Percent", DriveTrain.getInstance().getRightFrontPercent());
        SmartDashboard.putNumber("Right Back Percent", DriveTrain.getInstance().getRightBackPercent());

        SmartDashboard.putNumber("Distance", Vision.getInstance().twoTargetDistance());

        SmartDashboard.putNumber("Fused", IMU.getInstance().getFusedHeading());

        SmartDashboard.putNumber("Initial Yaw", IMU.getInstance().getInitialYaw());
        SmartDashboard.putNumber("Yaw", IMU.getInstance().getYaw());

        SmartDashboard.putNumber("Initial Pitch", IMU.getInstance().getInitialPitch());
        SmartDashboard.putNumber("Pitch", IMU.getInstance().getPitch());

        SmartDashboard.putNumber("Initial Roll", IMU.getInstance().getInitialRoll());
        SmartDashboard.putNumber("Roll", IMU.getInstance().getRoll());
        //SmartDashboard.putBoolean("Prox me OwO", ProxSensors.getInstance().getTopLimit());
    }

}