package frc.robot.subsystems.hang;

import frc.robot.util.RBSISubsystem;

public class Hang extends RBSISubsystem {
  private final HangIOSpark io;

  public Hang(HangIOSpark io) {
    this.io = io;
    io.setMode();
  }

  public void liftHang() {
    io.setOutputRoller();
  }

  public void stopIntake() {
    io.stopRoller();
  }

  @Override
  public void rbsiPeriodic() {}
}
