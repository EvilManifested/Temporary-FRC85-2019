/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.sensors.IMU;
import frc.robot.subsystems.LiftVertical;
import frc.robot.subsystems.LiftHorizontal;
import frc.robot.commands.lift.VerticalShift;
import frc.robot.commands.lift.HorizontalShift;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.lift.VerticalShift;
import frc.robot.subsystems.DriveTrain;
import frc.robot.commands.drivetrain.FollowTarget;
import frc.robot.commands.lift.HorizontalShift;

public class OI {

    private static OI _instance;

    private Joystick _driverController;
    private Joystick _operatorController;

    private JoystickButton _driverLeftBumper, _driverAButton, _driverBButton;
    private JoystickButton _operatorPhaseZero, _operatorPhaseOne, _operatorPhaseTwo, _operatorPhaseThree, _operatorLeftBumper, _operatorRightBumper;

    private double _xSpeed = 0, _ySpeed = 0, _zRotation = 0;
    private double _liftSpeed = 0;

    private double _gyroAngle;

    private OI() {
        _driverController = new Joystick(Addresses.CONTROLLER_DRIVER);
        _operatorController = new Joystick(Addresses.CONTROLLER_OPERATOR);

        _operatorPhaseZero = new JoystickButton(_operatorController, 1); //Change values if needed
        _operatorPhaseOne = new JoystickButton(_operatorController, 2); //Change values if needed
        _operatorPhaseTwo = new JoystickButton(_operatorController, 3); //Change values if needed
        _operatorPhaseThree = new JoystickButton(_operatorController, 4); //Change values if needed

        _operatorLeftBumper = new JoystickButton(_operatorController, 1);
        _operatorRightBumper = new JoystickButton(_operatorController, 2);

        _driverAButton = new JoystickButton(_driverController, 4); // Change to A button
        _driverAButton = new JoystickButton(_driverController, 5); // Change to B button

        _operatorPhaseZero.whenPressed(new VerticalShift(0, 1)); //go to phase 0
        _operatorPhaseOne.whenPressed(new VerticalShift(1, 1)); //go to phase 1
        _operatorPhaseTwo.whenPressed(new VerticalShift(2, 1)); //go to phase 2
        _operatorPhaseThree.whenPressed(new VerticalShift(3, 1)); //go to phase 3

        _operatorLeftBumper.whenActive(new HorizontalShift(0, 1)); //1 means to go left (or backward) hopefully
        _operatorRightBumper.whenActive(new HorizontalShift(1, -1)); //-1 means to go right (or forward) hopefully

        FollowTarget followTarget;
        _driverAButton.whenPressed(followTarget = new FollowTarget()); //follows when pressed
        _driverBButton.cancelWhenPressed(followTarget); //cancels following when pressed
    }

    public static OI getInstance() {
        if (_instance == null) {
            _instance = new OI();
        }
        return _instance;
    }

    public double[] getJoystickInput() {
        if (isHeadless()) {
            headlessDrive();
            // 1 or 0 is passed to identify whether the array is headless or headed drive.
            return new double[] {1, _xSpeed, _ySpeed, _zRotation, _gyroAngle};
        } else {
            headedDrive();
            return new double[] {0, _xSpeed, _ySpeed, _zRotation, 0};
        }
    }

    private void headedDrive() {
        // Deadband is initialized in subsystem DriveTrain with the mecanum drive constructor.
        _xSpeed = -_driverController.getRawAxis(0);
        _ySpeed = _driverController.getRawAxis(1);
        _zRotation = -_driverController.getRawAxis(4);
        //_gyroAngle = 0.0;
    }

    private void headlessDrive() {
        _xSpeed = -_driverController.getRawAxis(0);
        _ySpeed = _driverController.getRawAxis(1);
        _zRotation = -_driverController.getRawAxis(4);
        _gyroAngle = IMU.getInstance().getFusedHeading();
    }

    public double getXInput() {
        return _driverController.getRawAxis(0);
    }

    public double getYInput() {
        return _driverController.getRawAxis(1);
    }

    /**
     * Called in commands to return the joystick axis which is converted into the set speed of the motor
     */
    public double getLiftVertical() {
        if (!LiftVertical.getInstance().checkVerticalLift(_operatorController.getRawAxis(1))) {
            return _liftSpeed = 0;
        } else {
            return _liftSpeed = _operatorController.getRawAxis(1) * 0.5;
        }
    }

    public double getLiftHorizontal() {
        if (!LiftHorizontal.getInstance().checkHorizontalLift(_operatorController.getRawAxis(0))) {
            return _liftSpeed = 0;
        } else {
            return _liftSpeed = _operatorController.getRawAxis(0) * 0.5;
        }
    }

    public boolean isHeadless() {
        SmartDashboard.putBoolean("Headless", _driverController.getRawButton(6));
        return _driverController.getRawButton(6);
    }

}