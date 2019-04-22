/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.OI;
import frc.robot.Addresses;
import frc.robot.Variables;
import frc.robot.sensors.Sensors;
import frc.robot.subsystems.ClimbRear;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Elevator extends Subsystem {

    private static Elevator _instance = null;

    private TalonSRX _liftLeftMotor, _liftRightMotor;

    private Servo _liftServo;

    private Timer _timer, _timer2;

    private double targetPos, _servoAngle;

    private boolean adjusting;
  
    private Elevator() {
        _liftLeftMotor = new TalonSRX(Addresses.LIFT_LEFT_MOTOR);
        _liftLeftMotor.setNeutralMode(NeutralMode.Brake);
        _liftRightMotor = new TalonSRX(Addresses.LIFT_RIGHT_MOTOR);
        _liftRightMotor.setNeutralMode(NeutralMode.Brake);
        _liftRightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        _liftLeftMotor.configOpenloopRamp(0.5);
        _liftRightMotor.configOpenloopRamp(0.5);

        _timer = new Timer();
        _timer2 = new Timer();

        _liftServo = new Servo(Addresses.LIFT_SERVO);
    }

    public static Elevator getInstance() {
        if (_instance == null) {
            _instance = new Elevator();
        }
        return _instance;
    }

    @Override
    public void initDefaultCommand() {
    }

    public void verticalShift(double speed) {
        if (adjusting
            || speed == 0.0
            || (softLimits(speed) && !SmartDashboard.getBoolean("Disable Elevator Soft Limits", false))) {
            speed = OI.getInstance().applyPID(OI.ELEVATOR_SYSTEM, 
                                              getElevatorPosition(), 
                                              targetPos, 
                                              0, 
                                              Variables.getInstance().getElevatorKI(),
                                              Variables.getInstance().getElevatorKD(), 
                                              0.7, 
                                              -0.35);
        } else if (speed > 0) {
            speed *= 0.7;
            targetPos = getElevatorPosition();
        } else if (speed < 0) {
            speed *= 0.35;
            targetPos = getElevatorPosition();
        }

        if (getElevatorPosition() < 1000 && speed < 0) {
            speed *= 0.5;
        }

        //if ((adjusting && speed > 0 && speed < 0.22) // there is a better fix for this probably based on acceleration, will work on that later
        //    || (adjusting && speed > -0.1 && speed < 0)) {
        //    adjusting = false;
        //}

        SmartDashboard.putBoolean("Lift Soft Limits Activated", softLimits(speed));

        if ((Sensors.getInstance().getLiftTopLimit() && speed > 0.0)
             || (Sensors.getInstance().getLiftBottomLimit() && speed < 0.0)
             || (_timer.get() < Variables.ELEVATOR_TIMER && _servoAngle == Variables.getInstance().getElevatorUnlocked())) {
            speed = 0.0;
        }

        if (getServo() == Variables.getInstance().getElevatorLocked()
            //&& _timer2.get() > Variables.ELEVATOR_TIMER
            ) {
            speed = 0.0;
        }

        _liftLeftMotor.set(ControlMode.PercentOutput, speed);
        _liftRightMotor.set(ControlMode.PercentOutput, speed);
    }

    private boolean softLimits(double speed) {
        double mastPosition = Mast.getInstance().getMastPosition();
        double verticalPosition = getElevatorPosition();
        double intakePosition = Intake.getInstance().getWristPosition();

        // lift needs a top limit, a bottom limit,
        // if the wrist is down and the mast is back
        // if the wrist is down and the mast is forward
        if (verticalPosition > Variables.ELEVATOR_MAX_POS[Variables.getInstance().isPracticeBot()] // top limit
            && speed > 0) {
            return true;
            } /*else if (mastPosition < Variables.MAST_BREAKPOINT // mast is back & wrist is down
                   && intakePosition < Variables.WRIST_MIN_POS_MAST_BACK
                   && verticalPosition < Variables.ELEVATOR_MIN_POS_MAST_PROTECTED
                   && speed < 0) {
            return true;
        } else if (mastPosition >= Variables.MAST_BREAKPOINT // mast is forward & wrist is down
                   && intakePosition < Variables.WRIST_MIN_POS_MAST_BACK
                   && verticalPosition < Variables.ELEVATOR_MIN_POS_MAST_FORWARD_CARGO
                   && speed < 0) {
            return true;
        } */else if (verticalPosition < Variables.ELEVATOR_MIN_POS_MAST_FORWARD_HATCH // general bottom limit
                   && speed < 0) {
            return true;
        }
    
        return false;
    }

    public void setElevatorMotors(double speed) {
        if (getElevatorPosition() < 1000 && speed < 0)
            speed *= 0.5;
        _liftLeftMotor.set(ControlMode.PercentOutput, speed);
        _liftRightMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopMotors() {
        _liftLeftMotor.set(ControlMode.PercentOutput, 0.0);
        _liftRightMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void setVerticalPosition(int position) {
        _liftRightMotor.setSelectedSensorPosition(position);
    }

    public int getElevatorPosition() {
        return _liftRightMotor.getSelectedSensorPosition();
    }

    public double getElevatorLeftVoltage() {
        return _liftLeftMotor.getMotorOutputVoltage();
    }

    public double getElevatorRightVoltage() {
        return _liftRightMotor.getMotorOutputVoltage();
    }

    public double getElevatorLeftCurrent() {
        return _liftLeftMotor.getOutputCurrent();
    }

    public double getElevatorRightCurrent() {
        return _liftRightMotor.getOutputCurrent();
    }

    public void setTargetPosition(double target) {
        //max is always the same for the elevator
        //if(target > Variables.ELEVATOR_MAX_POS) {
        //    target = Variables.ELEVATOR_MAX_POS;
        //}
        //if mast is protected, use a special minimum value for the elevator
        /*else if(Mast.getInstance().getHorizontalPosition() < Variables.MAST_BREAKPOINT){
            if(target < Variables.ELEVATOR_MIN_POS_MAST_PROTECTED)
                target = Variables.ELEVATOR_MIN_POS_MAST_PROTECTED;
        }
        //otherwise, minimum value is determined by the wrist
        else{
            if(Intake.getInstance().getWristPosition() > Variables.WRIST_MIN_POS_MAST_BACK) {
                if(target < Variables.ELEVATOR_MIN_POS_MAST_FORWARD_HATCH) 
                    target = Variables.ELEVATOR_MIN_POS_MAST_FORWARD_HATCH;
            }
            else {
                if(target < Variables.ELEVATOR_MIN_POS_MAST_FORWARD_CARGO) 
                    target = Variables.ELEVATOR_MIN_POS_MAST_FORWARD_CARGO;
            }
        } else {*/
        if (ClimbRear.getInstance().getClimbRearPosition() > Variables.CLIMB_REAR_SLOW_DOWN_MIN) { // if the rear climb is ever working
            targetPos = getElevatorPosition();
        } else {
            targetPos = target;
        }
    }

    public double getTargetPosition() {
        return targetPos;
    }

    public void changeAdjustingBool(boolean bool) {
        adjusting = bool;
    }

    public boolean getAdjustingBool() {
        return adjusting;
    }

    public void setServo(double degree) {
        _liftServo.setAngle(degree);
        _servoAngle = degree;
    }

    public double getServo() {
        return _servoAngle;
    }

    public void startTimer() {
        _timer.start();
    }

    public void resetTimer() {
        _timer.reset();
    }

    public void stopTimer() {
        _timer.stop();
    }

    public void startTimer2() {
        _timer2.start();
    }

    public void resetTimer2() {
        _timer2.reset();
    }

    public void stopTimer2() {
        _timer2.stop();
    }    

    public double getTimer2() {
        return _timer2.get();
    }

    public TalonSRX getIMUTalon() {
        return _liftLeftMotor;
    }
   
}
