/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Addresses;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;

/**
 * The intake subsystem
 */
public class Intake extends Subsystem {

    private static Intake _instance = null;
    private TalonSRX _flipper;
    private TalonSRX _roller;
    private Encoder _intakePos;
    private LimitSwitchNormal _test;
    private LimitSwitchSource _test2;

    private int _loadStationPos = 0; // placeholder
    private int _pickUpPos = 90; // placeholder
    private int _lowerPickUpPos = 95; // placeholder

    private ControlMode mode = ControlMode.PercentOutput;
    private double flipperSpeed = 0.1;
    private double intakeTolerance = 1;

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    private Intake() {
        _flipper = new TalonSRX(Addresses.INTAKE_FLIPPER);
        _roller = new TalonSRX(Addresses.INTAKE_ROLLER);
        _intakePos = new Encoder(Addresses.INTAKE_ENCODER_A, Addresses.INTAKE_ENCODER_B);
    }

    public static Intake getInstance() {
        if (_instance == null) {
        _instance = new Intake();
        }
        return _instance;
    }

    public double getIntakePos() {
        return _intakePos.get();
    }

    /**
     * Sets the intake to vertical position
     */
    public boolean setLoadStation() {
        boolean isThere = false;
        if (getIntakePos() > _loadStationPos) {
            if (!Robot.overrideLimits) {
				_flipper.set(mode, flipperSpeed);
            }
        } else {

        }

        if (Math.abs(getIntakePos() - _loadStationPos) < intakeTolerance) {
            isThere = true;
        }

        return isThere;
    }

    /**
     * Sets the intake to horizontal position
     */
    public void setPickUp() {

    }

    public void intakeCargo(double speed) {
        _roller.set(mode, speed);
    }

    public void shootCargo(double speed) {
        _roller.set(mode, -speed);
    }

    public void stopRoller() {
        _roller.set(mode, 0);
    }

}
