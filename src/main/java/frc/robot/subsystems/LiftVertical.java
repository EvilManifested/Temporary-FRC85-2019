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
import frc.robot.commands.lift.LiftVerticalWithJoystick;
import frc.robot.sensors.ProxSensors;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class LiftVertical extends Subsystem {

    private static LiftVertical _instance = null;

    private TalonSRX _liftLeftMotor, _liftRightMotor;

    private double targetPos;

    private boolean adjusting;
  
    private LiftVertical() {
        _liftLeftMotor = new TalonSRX(Addresses.LIFT_LEFT_MOTOR);
        _liftLeftMotor.setNeutralMode(NeutralMode.Brake);
        _liftRightMotor = new TalonSRX(Addresses.LIFT_RIGHT_MOTOR);
        _liftRightMotor.setNeutralMode(NeutralMode.Brake);
        _liftRightMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    }

    public static LiftVertical getInstance() {
        if (_instance == null) {
            _instance = new LiftVertical();
        }
        return _instance;
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new LiftVerticalWithJoystick());
    }

    public void verticalShift(double speed) {
        if (!OI.getInstance().getOperatorLiftVertical() || adjusting) {
            speed = OI.getInstance().applyPID(OI.getInstance().LIFT_VERTICAL_SYSTEM, 
                                              getVerticalPosition(), 
                                              targetPos, 
                                              Variables.getInstance().getVerticalLiftKP(), 
                                              Variables.getInstance().getVerticalLiftKI(), 
                                              Variables.getInstance().getVerticalLiftKD(), 
                                              0.5, 
                                              -0.2);
        } else if (speed > Variables.getInstance().DEADBAND_OPERATORSTICK) {
            speed = 0.5;
            targetPos = getVerticalPosition();
        } else if (speed < -Variables.getInstance().DEADBAND_OPERATORSTICK) {
            speed = -0.2;
            targetPos = getVerticalPosition();
        }

        if ((ProxSensors.getInstance().getLiftTopLimit() && speed > 0.0)
             || (ProxSensors.getInstance().getLiftBottomLimit() && speed < 0.0)
             || softLimits(speed)) {
            speed = 0.0;
        }

        if (ProxSensors.getInstance().getLiftBottomLimit()) {
            setVerticalPosition(0);
        }

        _liftLeftMotor.set(ControlMode.PercentOutput, speed);
        _liftRightMotor.set(ControlMode.PercentOutput, speed);
    }

    private boolean softLimits(double speed) {
        double mastPosition = LiftHorizontal.getInstance().getHorizontalPosition();
        double verticalPosition = getVerticalPosition();
        double intakePosition = Intake.getInstance().getFlipperPosition();

        if (mastPosition < Variables.getInstance().MAST_PROTECTED) { // below mast protected, the vertical lift should have a different set of things
            if (verticalPosition < Variables.getInstance().LIFT_MIN_FOR_MAST
                && intakePosition < OI.getInstance().convertDegreesToIntake(10)
                && speed < 0.0) {
                return true;
            }
        } else if (mastPosition > Variables.getInstance().MAST_PROTECTED) { // above, it should be fine
            if (verticalPosition < Variables.getInstance().CARGO_FLOOR
                && intakePosition < OI.getInstance().convertDegreesToIntake(10)
                && speed < 0.0) {
                return true;
            }
        } else if (verticalPosition > Variables.getInstance().CARGO_HIGH) {
            return true;
        }
        return false;
    }

    public void stopMotors() {
        _liftLeftMotor.set(ControlMode.PercentOutput, 0.0);
        _liftRightMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void setVerticalPosition(int position) {
        _liftRightMotor.setSelectedSensorPosition(position);
    }

    public int getVerticalPosition() {
        return _liftRightMotor.getSelectedSensorPosition();
    }

    public void setTargetPosition(double target) {
        targetPos = target;
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

    public TalonSRX getIMUTalon() {
        return _liftLeftMotor;
    }
   
}
