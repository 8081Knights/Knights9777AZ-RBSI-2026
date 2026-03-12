package frc.robot.subsystems.hang;

import static frc.robot.Constants.IntakeConstants.*;
import static frc.robot.Constants.RobotDevices.*;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.robot.Constants;
import frc.robot.Constants.DrivebaseConstants;
import frc.robot.subsystems.drive.SwerveConstants;
import frc.robot.util.SparkUtil;

public class HangIOSpark implements HangIO {

  private final SparkMax hangMotor = new SparkMax(HANG.getDeviceNumber(), MotorType.kBrushless);
  private final RelativeEncoder encoder = hangMotor.getEncoder();
  private final SparkClosedLoopController pid = hangMotor.getClosedLoopController();
  public final int[] powerPorts = {INTAKE.getPowerPort()};

  private final SimpleMotorFeedforward ff = new SimpleMotorFeedforward(kSreal, kVreal, kAreal);

  public HangIOSpark() {

    // Configure leader motor
    var hangConfig = new SparkFlexConfig();
    hangConfig
        .idleMode(
            switch (kIntakeIdleMode) {
              case COAST -> IdleMode.kCoast;
              case BRAKE -> IdleMode.kBrake;
            })
        .smartCurrentLimit((int) SwerveConstants.kDriveCurrentLimit)
        .voltageCompensation(DrivebaseConstants.kOptimalVoltage);
    hangConfig.encoder.uvwMeasurementPeriod(10).uvwAverageDepth(2);
    hangConfig
        .closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(kPreal, 0.0, kDreal)
        .feedForward
        .kS(kSreal)
        .kV(kVreal)
        .kA(kAreal);
    hangConfig
        .signals
        .primaryEncoderPositionAlwaysOn(true)
        .primaryEncoderPositionPeriodMs((int) (1000.0 / SwerveConstants.kOdometryFrequency))
        .primaryEncoderVelocityAlwaysOn(true)
        .primaryEncoderVelocityPeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .appliedOutputPeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .busVoltagePeriodMs((int) (Constants.loopPeriodSecs * 1000.))
        .outputCurrentPeriodMs((int) (Constants.loopPeriodSecs * 1000.));
    hangConfig
        .openLoopRampRate(DrivebaseConstants.kDriveOpenLoopRampPeriod)
        .closedLoopRampRate(DrivebaseConstants.kDriveClosedLoopRampPeriod);
    SparkUtil.tryUntilOk(
        hangMotor,
        5,
        () ->
            hangMotor.configure(
                hangConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters));
    SparkUtil.tryUntilOk(hangMotor, 5, () -> encoder.setPosition(0.0));
  }

  @Override
  public void updateInputs(HangIOInputs inputs) {
    inputs.supplyCurrent = new double[] {hangMotor.getOutputCurrent()};
  }

  @Override
  public void setOutputRoller() {
    hangMotor.set(0.4);
  }

  @Override
  public void stopRoller() {
    hangMotor.stopMotor();
  }
}
