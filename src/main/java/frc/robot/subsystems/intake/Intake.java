package frc.robot.subsystems.intake;

import frc.robot.util.RBSISubsystem;

public class Intake extends RBSISubsystem {
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
  public void rbsiPeriodic() {}
}
