package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Degrees;
import static frc.robot.Constants.RobotDevices.*;

import org.littletonrobotics.junction.Logger;

import static frc.robot.Constants.IntakeConstants.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
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


public class IntakeIOSparkFlex implements IntakeIO {

  private final SparkFlex intakeRoller = new SparkFlex(INTAKE.getDeviceNumber(), MotorType.kBrushless);
  private final RelativeEncoder encoder = intakeRoller.getEncoder();
  private final SparkClosedLoopController pid = intakeRoller.getClosedLoopController();
  public final int[] powerPorts = {INTAKE.getPowerPort()};

  private final SimpleMotorFeedforward ff = new SimpleMotorFeedforward(kSreal, kVreal, kAreal);


 public IntakeIOSparkFlex() {

    // Configure leader motor
    var intakeConfig = new SparkFlexConfig();
    intakeConfig
        .idleMode(
            switch (kIntakeIdleMode) {
              case COAST -> IdleMode.kCoast;
              case BRAKE -> IdleMode.kBrake;
            })
        .smartCurrentLimit((int) SwerveConstants.kDriveCurrentLimit)
        .voltageCompensation(DrivebaseConstants.kOptimalVoltage);
    intakeConfig.encoder.uvwMeasurementPeriod(10).uvwAverageDepth(2);
    intakeConfig
        .closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(kPreal, 0.0, kDreal)
        .feedForward
        .kS(kSreal)
        .kV(kVreal)
        .kA(kAreal);
    intakeConfig
        .signals
        .primaryEncoderPositionAlwaysOn(true)
        .primaryEncoderPositionPeriodMs((int) (1000.0 / SwerveConstants.kOdometryFrequency))
        .primaryEncoderVelocityAlwaysOn(true)
        .primaryEncoderVelocityPeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .appliedOutputPeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .busVoltagePeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .outputCurrentPeriodMs((int) (Constants.loopPeriodSecs * 1000.));
    intakeConfig
        .openLoopRampRate(DrivebaseConstants.kDriveOpenLoopRampPeriod)
        .closedLoopRampRate(DrivebaseConstants.kDriveClosedLoopRampPeriod);
    SparkUtil.tryUntilOk(
        intakeRoller,
        5,
        () ->
            intakeRoller.configure(
                intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters));
    SparkUtil.tryUntilOk(intakeRoller, 5, () -> encoder.setPosition(0.0));

  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.supplyCurrent =
        new double[] {intakeRoller.getOutputCurrent()};

    
  }

  @Override
  public void setOutputRoller() {
    intakeRoller.set(0.4);
  }



  @Override
  public void stopRoller() {
    intakeRoller.stopMotor();
  }

 

  
}