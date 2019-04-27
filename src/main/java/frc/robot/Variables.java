 /*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Mast;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.ClimbFront;
import frc.robot.subsystems.ClimbRear;
import frc.robot.sensors.Sensors;
import frc.robot.sensors.IMU;
import frc.robot.sensors.RangeFinder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Variables {

    private static Variables _instance;

    // PID

    private static final double kP_ELEVATOR = 0.0008, kI_ELEVATOR = 0.00000003, kD_ELEVATOR_UP = 0.001, kD_ELEVATOR_DOWN = 0.003;

    private static final double kP_MAST = 0.00004, kI_MAST = 0.0, kD_MAST = 0.00003;

    private static final double kP_INTAKE = 0.000025, kI_INTAKE = 0.0, kD_INTAKE = 0.000015;

    private static final double kP_DRIVE = 0.01, kI_DRIVE = 0.0, kD_DRIVE = 0.0;

    private static final double kP_VISION = 0.01, kI_VISION = 0.0, kD_VISION = 0.001;

    private static final double kP_VISION_ROT = 0.05, kI_VISION_ROT = 0.0, kD_VISION_ROT = 0.0;

    private static final double kP_CLIMB = 0.034, kI_CLIMB = 0.0, kD_CLIMB = 0.0;

    private static final double kP_CLIMB_POS = 0.1, kI_CLIMB_POS = 0.0, kD_CLIMB_POS = 0.0;

    private static final double kP_CLIMB_PITCH = 0.034, kI_CLIMB_PITCH = 0.0, kD_CLIMB_PITCH = 0.0;

    private static final double VISION_MAX_SPEED = 1;
    
    // OI

    public static final double DEADBAND_DRIVERSTICK = 0.15;
    public static final double DEADBAND_Z_DRIVERSTICK = 0.3;  
    public static final double DEADBAND_OPERATORSTICK = 0.15;
    public static final double TOLERANCE_ANGLE = 3.0;

    public static final int MAX_TURNS = 7; // if we go over 2520 degrees in either direction in one match then help.

    public static double A_POLYNOMIAL = .60929309069026; // polynomial stuff
    public static double B_POLYNOMIAL = 0;
    public static double C_POLYNOMIAL = .377931548393;
    public static double D_POLYNOMIAL = 0;

    // ANGLES FOR VISION

    public static final double ROT_POS_1 = 90;
    public static final double ROT_POS_2 = -90;
    public static final double ROT_POS_3 = 60;
    public static final double ROT_POS_4 = 120;

    // ELEVATOR

    // Elevator positions

    // 12"
    public static final int HATCH_ONE_COMP = 1144;
    public static final int HATCH_ONE_PRACTICE = 1331;
    public static final int[] HATCH_ONE = {HATCH_ONE_COMP, HATCH_ONE_PRACTICE};

    // 43.5"
    public static final int HATCH_TWO_COMP = 9923;
    public static final int HATCH_TWO_PRACTICE = 10226;
    public static final int[] HATCH_TWO = {HATCH_TWO_COMP, HATCH_TWO_PRACTICE};

    // 70"
    public static final int HATCH_THREE_COMP = 17623;
    public static final int HATCH_THREE_PRACTICE = 17862;
    public static final int[] HATCH_THREE = {HATCH_THREE_COMP, HATCH_THREE_PRACTICE};

    public static final int HATCH_FLOOR_COMP = 1230;
    public static final int HATCH_FLOOR_PRACTICE = 2232;
    public static final int[] HATCH_FLOOR = {HATCH_FLOOR_COMP, HATCH_FLOOR_PRACTICE};

    // 18"
    public static final int HATCH_STATION_COMP = 2539;
    public static final int HATCH_STATION_PRACTICE = 2200;
    public static final int[] HATCH_STATION = {HATCH_STATION_COMP, HATCH_STATION_PRACTICE};
    
    public static final int CARGO_ONE_COMP = 6552 - 200;
    public static final int CARGO_ONE_PRACTICE = 6387;
    public static final int[] CARGO_ONE = {CARGO_ONE_COMP, CARGO_ONE_PRACTICE};

    // 56 5/8"
    public static final int CARGO_TWO_COMP = 13854;
    public static final int CARGO_TWO_PRACTICE = 13740;
    public static final int[] CARGO_TWO = {CARGO_TWO_COMP, CARGO_TWO_PRACTICE};

    // 88 5/8"
    public static final int CARGO_THREE_COMP = 22600;
    public static final int CARGO_THREE_PRACTICE = 23055;
    public static final int[] CARGO_THREE = {CARGO_THREE_COMP, CARGO_THREE_PRACTICE};

    // 44"
    public static final int CARGO_SHIP_COMP = 10193;
    public static final int CARGO_SHIP_PRACTICE = 11958;
    public static final int[] CARGO_SHIP = {CARGO_SHIP_COMP, CARGO_SHIP_PRACTICE};

    public static final int CARGO_FLOOR_COMP = 1991;
    public static final int CARGO_FLOOR_PRACTICE = 2662;
    public static final int[] CARGO_FLOOR = {CARGO_FLOOR_COMP, CARGO_FLOOR_PRACTICE};

    public static final int CARGO_STATION_COMP = 10677;
    public static final int CARGO_STATION_PRACTICE = 11777;
    public static final int[] CARGO_STATION = {CARGO_STATION_COMP, CARGO_STATION_PRACTICE};

    public static final int ELEVATOR_CURRENT_POS = -1;

    // Elevator position limits
    public static final int[] ELEVATOR_MAX_POS = {CARGO_THREE_COMP + 2000, CARGO_THREE_PRACTICE + 2000};
    public static final int ELEVATOR_MIN_POS_MAST_PROTECTED = 3200;
    public static final int ELEVATOR_MIN_POS_MAST_FORWARD_CARGO = 1250;
    public static final int ELEVATOR_MIN_POS_MAST_FORWARD_HATCH = 0;
    public static final int ELEVATOR_MIN_POS_FOR_WRIST_LIFT_HIGH = ELEVATOR_MIN_POS_MAST_PROTECTED;

    // Elevator speed Limits 
    public static final double ELEVATOR_MAX_SPEED = .5;
    public static final double ELEVATOR_MIN_SPEED = -.2;

    // Servo angles for elevator lock (clutch)
    public static final double ELEVATOR_LOCKED_COMP = 80;
    public static final double ELEVATOR_LOCKED_PRACTICE = 80;
    public static final double[] ELEVATOR_LOCKED = {ELEVATOR_LOCKED_COMP, ELEVATOR_LOCKED_PRACTICE};
    public static final double ELEVATOR_UNLOCKED_COMP = 180;
    public static final double ELEVATOR_UNLOCKED_PRACTICE = 180;
    public static final double[] ELEVATOR_UNLOCKED = {ELEVATOR_UNLOCKED_COMP, ELEVATOR_UNLOCKED_PRACTICE};

    public static final double ELEVATOR_TIMER = 0.4;

    // MAST

    // Mast Positions
    public static final int MAST_FORWARD_POS = 700000;
    //public static final int MAST_FORWARD_FOR_HATCH = 441398;
    public static final int MAST_FORWARD_FOR_HATCH = 429798;
    public static final int MAST_FORWARD_FOR_CARGO = 131659;
    public static final int MAST_PROTECTED = 100000; // behind bumpers
    public static final int MAST_FOR_HATCH_SHIP = 82390;

    public static final int MAST_CURRENT_POS = -1;

    public static final int MAST_CARGO_SHIP = 0;

    public static final double MAST_PER_RANGEFINDER = (870047 - 323914) / (1.0498688 - .82021);

    // Mast position limits
    public static final int MAST_MIN_POS = 0;
    public static final int MAST_BREAKPOINT = 571100;
    public static final int MAST_MAX_POS = 871000;

    // Mast speed limits
    public static final double MAST_MAX_SPEED = .5;
    public static final double MAST_MIN_SPEED = -.5;

    // WRIST

    // Wrist positions
    public static final int WRIST_0 = -53714;
    public static final int WRIST_30 = -333333;
    public static final int WRIST_45 = -500000;
    public static final int WRIST_60 = -666666;
    public static final int WRIST_90 = -908000;

    public static final int WRIST_CARGO_HIGH_COMP = -675615;
    public static final int WRIST_CARGO_HIGH_PRACTICE = -750860;
    public static final int[] WRIST_CARGO_HIGH = {WRIST_CARGO_HIGH_COMP, WRIST_CARGO_HIGH_PRACTICE};
    public static final int WRIST_CARGO_COMP = -767000;
    public static final int WRIST_CARGO_PRACTICE = -767000;
    public static final int[] WRIST_CARGO = {WRIST_CARGO_COMP, WRIST_CARGO_PRACTICE};
    public static final int WRIST_CARGO_ONE_COMP = -768170;
    public static final int WRIST_CARGO_ONE_PRACTICE = -768170;
    public static final int[] WRIST_CARGO_ONE = {WRIST_CARGO_ONE_COMP, WRIST_CARGO_ONE_PRACTICE};
    public static final int WRIST_CARGO_SHIP_COMP = -900000;
    public static final int WRIST_CARGO_SHIP_PRACTICE = -975850;
    public static final int[] WRIST_CARGO_SHIP = {WRIST_CARGO_SHIP_COMP, WRIST_CARGO_SHIP_PRACTICE};
    public static final int WRIST_CARGO_FLOOR_COMP = -1060346;
    public static final int WRIST_CARGO_FLOOR_PRACTICE = -1142068 + 50000;
    public static final int[] WRIST_CARGO_FLOOR = {WRIST_CARGO_FLOOR_COMP, WRIST_CARGO_FLOOR_PRACTICE};

    public static final int WRIST_HATCH_STATION_COMP = -392327;
    public static final int WRIST_HATCH_STATION_PRACTICE = -392327;
    public static final int[] WRIST_HATCH_STATION = {WRIST_HATCH_STATION_COMP, WRIST_HATCH_STATION_PRACTICE};
    public static final int WRIST_HATCH_FLOOR_COMP = -988993;
    public static final int WRIST_HATCH_FLOOR_PRACTICE = -1140762;
    public static final int[] WRIST_HATCH_FLOOR = {WRIST_HATCH_FLOOR_COMP, WRIST_HATCH_FLOOR_PRACTICE};

    public static final int WRIST_CURR_POSITION = 1;

    // Wrist position limits
    public static final int WRIST_MAX_POS = 0;
    public static final int WRIST_MIN_POS_MAST_BACK = -200000;
    public static final int WRIST_MIN_POS_LIFT_UP = WRIST_90;
    public static final int WRIST_MIN_POS = -1170000;

    // Wrist speed limits
    public static final double WRIST_MAX_SPEED_UP = 0.8;
    public static final double WRIST_MAX_SPEED_DOWN = -0.8;

    // CLIMB
    public static final double CLIMB_LEFT_LEVEL_THREE_COMP = 82.05069732666016;
    public static final double CLIMB_LEFT_LEVEL_THREE_PRACTICE = 82.05069732666016;
    public static final double[] CLIMB_LEFT_LEVEL_THREE = {CLIMB_LEFT_LEVEL_THREE_COMP, CLIMB_LEFT_LEVEL_THREE_PRACTICE};
    public static final double CLIMB_RIGHT_LEVEL_THREE_COMP = 79.62188720703125 + 0.5;
    public static final double CLIMB_RIGHT_LEVEL_THREE_PRACTICE = 79.62188720703125;
    public static final double[] CLIMB_RIGHT_LEVEL_THREE = {CLIMB_RIGHT_LEVEL_THREE_COMP, CLIMB_RIGHT_LEVEL_THREE_PRACTICE};
    public static final double CLIMB_REAR_LEVEL_THREE = 87.88459777832031;

    public static final double CLIMB_LEFT_LEVEL_TW0 = 38.428192138671875;
    public static final double CLIMB_RIGHT_LEVEL_TWO = 34.57111358642578;
    public static final double CLIMB_REAR_LEVEL_TWO = 39.78531265258789;

    // climb position limits
    public static final double CLIMB_LEFT_MAX = 83;
    public static final double CLIMB_RIGHT_MAX = 82;
    public static final double CLIMB_REAR_MAX = 89;

    public static final double CLIMB_LEFT_SLOW_DOWN_MIN = 6.357143878936768;
    public static final double CLIMB_RIGHT_SLOW_DOWN_MIN = 3.1666641235351562;
    public static final double CLIMB_REAR_SLOW_DOWN_MIN = 5.261898517608643;

    public static final double[] CLIMB_LEFT_SLOW_DOWN_MAX = CLIMB_LEFT_LEVEL_THREE;
    public static final double[] CLIMB_RIGHT_SLOW_DOWN_MAX = CLIMB_RIGHT_LEVEL_THREE;
    public static final double CLIMB_REAR_SLOW_DOWN_MAX = CLIMB_REAR_LEVEL_THREE;

    // climb speed limits
    public static final double CLIMB_MAX_SPEED_UP = 0.6;
    public static final double CLIMB_MAX_SPEED_DOWN = -0.8;

    // servo angles for climb
    public static final double CLIMB_LOCKED_COMP = 110;
    public static final double CLIMB_LOCKED_PRACTICE = 0;
    public static final double[] CLIMB_LOCKED = {CLIMB_LOCKED_COMP, CLIMB_LOCKED_PRACTICE};
    public static final double CLIMB_UNLOCKED_COMP = 70;
    public static final double CLIMB_UNLOCKED_PRACTICE = 25;
    public static final double[] CLIMB_UNLOCKED = {CLIMB_UNLOCKED_COMP, CLIMB_UNLOCKED_PRACTICE};

    // servo angles for the drop
    public static final double CLIMB_LEFT_UP_COMP = 0;
    public static final double CLIMB_LEFT_UP_PRACTICE = 0;
    public static final double[] CLIMB_LEFT_UP = {CLIMB_LEFT_UP_COMP, CLIMB_LEFT_UP_PRACTICE};
    public static final double CLIMB_LEFT_DOWN_COMP = 90;
    public static final double CLIMB_LEFT_DOWN_PRACTICE = 90;
    public static final double[] CLIMB_LEFT_DOWN = {CLIMB_LEFT_DOWN_COMP, CLIMB_LEFT_DOWN_PRACTICE};
    public static final double CLIMB_RIGHT_UP_COMP = 180;
    public static final double CLIMB_RIGHT_UP_PRACTICE = 0;
    public static final double[] CLIMB_RIGHT_UP = {CLIMB_RIGHT_UP_COMP, CLIMB_RIGHT_UP_PRACTICE};
    public static final double CLIMB_RIGHT_DOWN_COMP = 90;
    public static final double CLIMB_RIGHT_DOWN_PRACTICE = 90;
    public static final double[] CLIMB_RIGHT_DOWN = {CLIMB_RIGHT_DOWN_COMP, CLIMB_RIGHT_DOWN_PRACTICE};

    // ROLLER

    // Roller speeds
    public static final double ROLLER_IN = 0.80;
    public static final double ROLLER_OUT = -0.80;

    public static final double CAMERA_CENTER = 160.0;

    /**
     * Put variables here that should be changeable on the fly.
     */
    private Variables() {
        SmartDashboard.putNumber("kP_VLIFT", kP_ELEVATOR);
        SmartDashboard.putNumber("kI_VLIFT", kI_ELEVATOR);
        SmartDashboard.putNumber("kD_VLIFT_DOWN", kD_ELEVATOR_DOWN);
        SmartDashboard.putNumber("kD_VLIFT_UP", kD_ELEVATOR_UP);

        SmartDashboard.putNumber("kP_HLIFT", kP_MAST);
        SmartDashboard.putNumber("kI_HLIFT", kI_MAST);
        SmartDashboard.putNumber("kD_HLIFT", kD_MAST);
        
        SmartDashboard.putNumber("kP_INTAKE", kP_INTAKE);
        SmartDashboard.putNumber("kI_INTAKE", kI_INTAKE);
        SmartDashboard.putNumber("kD_INTAKE", kD_INTAKE);

        SmartDashboard.putNumber("kP_DRIVE", kP_DRIVE);
        SmartDashboard.putNumber("kI_DRIVE", kI_DRIVE);
        SmartDashboard.putNumber("kD_DRIVE", kD_DRIVE);

        SmartDashboard.putNumber("kP_VISION", kP_VISION);
        SmartDashboard.putNumber("kI_VISION", kI_VISION);
        SmartDashboard.putNumber("kD_VISION", kD_VISION);

        SmartDashboard.putNumber("kP_VISION_ROT", kP_VISION_ROT);
        SmartDashboard.putNumber("kI_VISION_ROT", kI_VISION_ROT);
        SmartDashboard.putNumber("kD_VISION_ROT", kD_VISION_ROT);

        SmartDashboard.putNumber("Vision Max Speed", VISION_MAX_SPEED);

        SmartDashboard.putNumber("kP_CLIMB", kP_CLIMB);
        SmartDashboard.putNumber("kI_CLIMB", kI_CLIMB);
        SmartDashboard.putNumber("kD_CLIMB", kD_CLIMB);

        SmartDashboard.putNumber("kP_CLIMB_POS", kP_CLIMB_POS);
        SmartDashboard.putNumber("kI_CLIMB_POS", kI_CLIMB_POS);
        SmartDashboard.putNumber("kD_CLIMB_POS", kD_CLIMB_POS);

        SmartDashboard.putNumber("kP_CLIMB_PITCH", kP_CLIMB_PITCH);
        SmartDashboard.putNumber("kI_CLIMB_PITCH", kI_CLIMB_PITCH);
        SmartDashboard.putNumber("kD_CLIMB_PITCH", kD_CLIMB_PITCH);

        SmartDashboard.putNumber("MAX_SPEED_UP_INTAKE", WRIST_MAX_SPEED_UP);
        SmartDashboard.putNumber("MAX_SPEED_DOWN_INTAKE", WRIST_MAX_SPEED_DOWN);

        SmartDashboard.putNumber("ELEVATOR_UNLOCKED", ELEVATOR_UNLOCKED[isPracticeBot()]);
        SmartDashboard.putNumber("ELEVATOR_LOCKED", ELEVATOR_LOCKED[isPracticeBot()]);

        SmartDashboard.putNumber("CLIMB_UNLOCKED", CLIMB_UNLOCKED[isPracticeBot()]);
        SmartDashboard.putNumber("CLIMB_LOCKED", CLIMB_LOCKED[isPracticeBot()]);

        SmartDashboard.putNumber("CLIMB_LEFT_UP", CLIMB_LEFT_UP[isPracticeBot()]);
        SmartDashboard.putNumber("CLIMB_LEFT_DOWN", CLIMB_LEFT_DOWN[isPracticeBot()]);
        SmartDashboard.putNumber("CLIMB_RIGHT_UP", CLIMB_RIGHT_UP[isPracticeBot()]);
        SmartDashboard.putNumber("CLIMB_RIGHT_DOWN", CLIMB_RIGHT_DOWN[isPracticeBot()]);

        SmartDashboard.putNumber("CLIMB_MAX_SPEED_UP", CLIMB_MAX_SPEED_UP);
        SmartDashboard.putNumber("CLIMB_MAX_SPEED_DOWN", CLIMB_MAX_SPEED_DOWN);

        SmartDashboard.putBoolean("Joysticks Enabled", true);

        SmartDashboard.putBoolean("Disable Intake Top Limit", true);
        SmartDashboard.putBoolean("Disable Intake Prox Limit", false);

        SmartDashboard.putBoolean("Disable Intake Soft Limits", false);
        SmartDashboard.putBoolean("Disable Mast Soft Limits", false);
        SmartDashboard.putBoolean("Disable Elevator Soft Limits", false);

        SmartDashboard.putBoolean("Reset Wrist Encoder", false);
        SmartDashboard.putBoolean("Reset Elevator Encoder", false);

        SmartDashboard.putBoolean("Run Diagnostics?", false);
        SmartDashboard.putBoolean("Close Diagnostics?", true);

        SmartDashboard.putBoolean("Practice Bot?", false);

        SmartDashboard.putBoolean("Relay?", false);
    }

    public static Variables getInstance() {
        if (_instance == null) {
			_instance = new Variables();
        }
        return _instance;
    }

    // ELEVATOR

    public double getElevatorKP() {
        return SmartDashboard.getNumber("kP_VLIFT", kP_ELEVATOR); // these are gonna have to be small af
    }

    public double getElevatorKI() {
        return SmartDashboard.getNumber("kI_VLIFT", kI_ELEVATOR);
    }

    public double getElevatorUpKD() {
        return SmartDashboard.getNumber("kD_VLIFT_UP", kD_ELEVATOR_UP);
    }

    public double getElevatorDownKD() {
        return SmartDashboard.getNumber("kD_VLIFT_DOWN", kD_ELEVATOR_DOWN);
    }

    // SERVO

    public double getElevatorUnlocked() {
        return SmartDashboard.getNumber("ELEVATOR_UNLOCKED", ELEVATOR_UNLOCKED[isPracticeBot()]);
    }

    public double getElevatorLocked() {
        return SmartDashboard.getNumber("ELEVATOR_LOCKED", ELEVATOR_LOCKED[isPracticeBot()]);
    }

    // MAST

    public double getMastKP() {
        return SmartDashboard.getNumber("kP_HLIFT", kP_MAST);
    }

    public double getMastKI() {
        return SmartDashboard.getNumber("kI_HLIFT", kI_MAST);
    }

    public double getMastKD() {
        return SmartDashboard.getNumber("kD_HLIFT", kD_MAST);
    }

    // WRIST

    public double getWristKP() {
        return SmartDashboard.getNumber("kP_INTAKE", kP_INTAKE);
    }

    public double getWristKI() {
        return SmartDashboard.getNumber("kI_INTAKE", kI_INTAKE);
    }

    public double getWristKD() {
        return SmartDashboard.getNumber("kD_INTAKE", kD_INTAKE);
    }

    public double getMaxSpeedUpIntake() {
        return SmartDashboard.getNumber("MAX_SPEED_UP_INTAKE", WRIST_MAX_SPEED_UP);
    }

    public double getMaxSpeedDownIntake() {
        return SmartDashboard.getNumber("MAX_SPEED_DOWN_INTAKE", WRIST_MAX_SPEED_DOWN);
    }

    // DRIVE

    public double getDriveKP() {
        return SmartDashboard.getNumber("kP_DRIVE", kP_DRIVE);
    }

    public double getDriveKI() {
        return SmartDashboard.getNumber("kI_DRIVE", kI_DRIVE);
    }

    public double getDriveKD() {
        return SmartDashboard.getNumber("kD_DRIVE", kD_DRIVE);
    }

    // VISION

    public double getVisionKP() {
        return SmartDashboard.getNumber("kP_VISION", kP_VISION);
    }

    public double getVisionKI() {
        return SmartDashboard.getNumber("kI_VISION", kI_VISION);
    }

    public double getVisionKD() {
        return SmartDashboard.getNumber("kD_VISION", kD_VISION);
    }

    public double getVisionMaxSpeed() {
        return SmartDashboard.getNumber("Vision Max Speed", VISION_MAX_SPEED);
    }

    // VISION ROT

    public double getVisionRotKP() {
        return SmartDashboard.getNumber("kP_VISION_ROT", kP_VISION_ROT);
    }

    public double getVisionRotKI() {
        return SmartDashboard.getNumber("kI_VISION_ROT", kI_VISION_ROT);
    }

    public double getVisionRotKD() {
        return SmartDashboard.getNumber("kD_VISION_ROT", kD_VISION_ROT);
    }

    // CLIMB

    public double getClimbkP() {
        return SmartDashboard.getNumber("kP_CLIMB", kP_CLIMB);
    }

    public double getClimbkI() {
        return SmartDashboard.getNumber("kI_CLIMB", kI_CLIMB);
    }

    public double getClimbkD() {
        return SmartDashboard.getNumber("kD_CLIMB", kD_CLIMB);
    }

    public double getClimbPoskP() {
        return SmartDashboard.getNumber("kP_CLIMB_POS", kP_CLIMB_POS);
    }

    public double getClimbPoskI() {
        return SmartDashboard.getNumber("kI_CLIMB_POS", kI_CLIMB_POS);
    }

    public double getClimbPoskD() {
        return SmartDashboard.getNumber("kD_CLIMB_POS", kD_CLIMB_POS);
    }

    public double getClimbPitchkP() {
        return SmartDashboard.getNumber("kP_CLIMB_PITCH", kP_CLIMB_PITCH);
    }

    public double getClimbPitchkI() {
        return SmartDashboard.getNumber("kI_CLIMB_PITCH", kI_CLIMB_PITCH);
    }

    public double getClimbPitchkD() {
        return SmartDashboard.getNumber("kD_CLIMB_PITCH", kD_CLIMB_PITCH);
    }

    public double getClimbMaxSpeedUp() {
        return SmartDashboard.getNumber("CLIMB_MAX_SPEED_UP", CLIMB_MAX_SPEED_UP);
    }

    public double getClimbMaxSpeedDown() {
        return SmartDashboard.getNumber("CLIMB_MAX_SPEED_DOWN", CLIMB_MAX_SPEED_DOWN);
    }

    public double getClimbUnlocked() {
        return SmartDashboard.getNumber("CLIMB_UNLOCKED", CLIMB_UNLOCKED[isPracticeBot()]);
    }

    public double getClimbLocked() {
        return SmartDashboard.getNumber("CLIMB_LOCKED", CLIMB_LOCKED[isPracticeBot()]);
    }

    public double getClimbLeftUp() {
        return SmartDashboard.getNumber("CLIMB_LEFT_UP", CLIMB_LEFT_UP[isPracticeBot()]);
    }

    public double getClimbLeftDown() {
        return SmartDashboard.getNumber("CLIMB_LEFT_DOWN", CLIMB_LEFT_DOWN[isPracticeBot()]);
    }

    public double getClimbRightUp() {
        return SmartDashboard.getNumber("CLIMB_RIGHT_UP", CLIMB_RIGHT_UP[isPracticeBot()]);
    }

    public double getClimbRightDown() {
        return SmartDashboard.getNumber("CLIMB_RIGHT_DOWN", CLIMB_RIGHT_DOWN[isPracticeBot()]);
    }

    public double getRangeFinderBasedOnMast(double mastPos) { // rangefinder from the front bumpers of the robot
        return ((-(mastPos - MAST_FORWARD_FOR_HATCH) / MAST_PER_RANGEFINDER) + 1);
    }

    public double getDistanceFromWall() { // from the bumpers
        return (getRangeFinderBasedOnMast(Mast.getInstance().getMastPosition()) - RangeFinder.getInstance().getDistance());
    }

    public int isPracticeBot() {
        if (SmartDashboard.getBoolean("Practice Bot?", false)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Repeatedly called in Robot.java
     */
    public void outputVariables() {

        // OI

        SmartDashboard.putNumber("Operator Joystick", OI.getInstance().getOperatorJoystickY());

        // Drivetrain

        SmartDashboard.putNumber("Left Front Percent", DriveTrain.getInstance().getLeftFrontPercent());
        SmartDashboard.putNumber("Left Back Percent", DriveTrain.getInstance().getLeftBackPercent());
        SmartDashboard.putNumber("Right Front Percent", DriveTrain.getInstance().getRightFrontPercent());
        SmartDashboard.putNumber("Right Back Percent", DriveTrain.getInstance().getRightBackPercent());

        // Vision

        SmartDashboard.putNumber("Distance", Vision.getInstance().twoTargetDistance());

        // Elevator

        SmartDashboard.putNumber("Vertical Lift", Elevator.getInstance().getElevatorPosition());

        SmartDashboard.putBoolean("Lift Top", Sensors.getInstance().getLiftTopLimit());
        SmartDashboard.putBoolean("Lift Center", Sensors.getInstance().getLiftCenterLimit());
        SmartDashboard.putBoolean("Lift Bottom", Sensors.getInstance().getLiftBottomLimit());

        // Intake

        SmartDashboard.putNumber("Intake Encoder", Intake.getInstance().getWristPosition());

        SmartDashboard.putBoolean("Intake Top Prox Sensor", Sensors.getInstance().getIntakeTopLimit());
        
        // Mast
        
        SmartDashboard.putNumber("Horizontal Lift", Mast.getInstance().getMastPosition());

        SmartDashboard.putBoolean("Front Prox Sensor", Sensors.getInstance().getLiftFrontLimit());
        SmartDashboard.putBoolean("Rear Prox Sensor", Sensors.getInstance().getLiftRearLimit());
        SmartDashboard.putBoolean("Intake Top Prox Sensor", Sensors.getInstance().getIntakeTopLimit());

        // Climb

        SmartDashboard.putNumber("Front Climb Left Position", ClimbFront.getInstance().getClimbLeftPosition());
        SmartDashboard.putNumber("Front Climb Right Position", ClimbFront.getInstance().getClimbRightPosition());

        SmartDashboard.putNumber("Rear Climb Position", ClimbRear.getInstance().getClimbRearPosition());

        SmartDashboard.putBoolean("Front Climb Left Limit Switch", Sensors.getInstance().getClimbLeftLimit());
        SmartDashboard.putBoolean("Front Climb Right Limit Switch", Sensors.getInstance().getClimbRightLimit());
        SmartDashboard.putBoolean("Rear Climb Limit Switch", Sensors.getInstance().getClimbRearLimit());

        SmartDashboard.putBoolean("Front Climb Left Photoeye", Sensors.getInstance().getClimbFrontLeftPhotoeye());
        SmartDashboard.putBoolean("Front Climb Right Photoeye", Sensors.getInstance().getClimbFrontRightPhotoeye());
        SmartDashboard.putBoolean("Rear Climb Photoeye", Sensors.getInstance().getClimbRearPhotoeye());

        // IMU

        SmartDashboard.putNumber("Fused", IMU.getInstance().getFusedHeading());

        SmartDashboard.putNumber("Initial Yaw", IMU.getInstance().getInitialYaw());
        SmartDashboard.putNumber("Yaw", IMU.getInstance().getYaw());

        SmartDashboard.putNumber("Initial Pitch", IMU.getInstance().getInitialPitch());
        SmartDashboard.putNumber("Pitch", IMU.getInstance().getPitch());

        SmartDashboard.putNumber("Initial Roll", IMU.getInstance().getInitialRoll());
        SmartDashboard.putNumber("Roll", IMU.getInstance().getRoll());

        //RangeFinder
        SmartDashboard.putNumber("Range Finder Distance", RangeFinder.getInstance().getDistance());
        SmartDashboard.putNumber("Range Finder Based On Mast", getRangeFinderBasedOnMast(Mast.getInstance().getMastPosition()));
    }

}