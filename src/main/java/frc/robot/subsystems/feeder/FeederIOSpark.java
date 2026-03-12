// Copyright (c) 2024-2026 Az-FIRST
// http://github.com/AZ-First
// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.feeder;

import static frc.robot.Constants.FlywheelConstants.*;
import static frc.robot.Constants.RobotDevices.FLYWHEEL_FEEDER;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.Constants;
import frc.robot.Constants.DrivebaseConstants;
import frc.robot.subsystems.drive.SwerveConstants;
import frc.robot.util.SparkUtil;

/**
 * NOTE: To use the Spark Flex / NEO Vortex, replace all instances of "CANSparkMax" with
 * "CANSparkFlex".
 */
public class FeederIOSpark implements FeederIO {

  // Define the feeder motor from the RobotDevices section of RobotContainer
  private final SparkFlex feeder =
      new SparkFlex(FLYWHEEL_FEEDER.getDeviceNumber(), MotorType.kBrushless);

  private final RelativeEncoder encoder = feeder.getEncoder();
  private final SparkClosedLoopController pid = feeder.getClosedLoopController();
  // IMPORTANT: Include here all devices listed above that are part of this mechanism!
  public final int[] powerPorts = {FLYWHEEL_FEEDER.getPowerPort()};
  private final SimpleMotorFeedforward ff = new SimpleMotorFeedforward(kSreal, kVreal, kAreal);

  public FeederIOSpark() {

    // Configure feeder motor
    var feederConfig = new SparkFlexConfig();
    feederConfig
        .idleMode(
            switch (kFeederIdleMode) {
              case COAST -> IdleMode.kCoast;
              case BRAKE -> IdleMode.kBrake;
            })
        .smartCurrentLimit((int) SwerveConstants.kDriveCurrentLimit)
        .voltageCompensation(DrivebaseConstants.kOptimalVoltage);
    feederConfig.encoder.uvwMeasurementPeriod(10).uvwAverageDepth(2);
    feederConfig
        .closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(kPreal, 0.0, kDreal)
        .feedForward
        .kS(kSreal)
        .kV(kVreal)
        .kA(kAreal);
    feederConfig
        .signals
        .primaryEncoderPositionAlwaysOn(true)
        .primaryEncoderPositionPeriodMs((int) (1000.0 / SwerveConstants.kOdometryFrequency))
        .primaryEncoderVelocityAlwaysOn(true)
        .primaryEncoderVelocityPeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .appliedOutputPeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .busVoltagePeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .outputCurrentPeriodMs((int) (Constants.loopPeriodSecs * 1000.));
    feederConfig
        .openLoopRampRate(DrivebaseConstants.kDriveOpenLoopRampPeriod)
        .closedLoopRampRate(DrivebaseConstants.kDriveClosedLoopRampPeriod);
    SparkUtil.tryUntilOk(
        feeder,
        5,
        () ->
            feeder.configure(
                feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters));
    SparkUtil.tryUntilOk(feeder, 5, () -> encoder.setPosition(0.0));
  }

  @Override
  public void setVoltage(double volts) {
    feeder.setVoltage(volts);
  }

  @Override
  public void stop() {
    feeder.stopMotor();
  }

  @Override
  public void updateInputs(FeederIOInputs inputs) {
    inputs.supplyCurrent = new double[] {feeder.getOutputCurrent()};
  }

  @Override
  public void setFeedRoller() {
    feeder.set(0.3);
  }

  @Override
  public void setBlockRoller() {
    feeder.set(-0.15);
  }
}
