package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private final IntakeIOSparkFlex io;

  public Intake(IntakeIOSparkFlex io) {
    this.io = io;
    io.setMode();
  }

  public void runIntake() {
    io.setOutputRoller();
  }

  public void stopIntake() {
    io.stopRoller();
  }

  @Override
  public void periodic() {}
}