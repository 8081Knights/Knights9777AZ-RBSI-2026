package frc.robot.subsystems.feeder;

import frc.robot.util.RBSISubsystem;

public class Feeder extends RBSISubsystem {
  private final FeederIOSpark io;

  public Feeder(FeederIOSpark io) {
    this.io = io;
    io.setMode();
  }

  /** runs the feeder so that we can shoot */
  public void runFeeder() {
    io.setFeedRoller();
  }

  /** runs the feeder backwards so the balls will go up */
  public void RunFeederBackwards() {
    io.setBlockRoller();
  }

  /** stops the feeder */
  public void stopFeeder() {
    io.stopRoller();
  }

  @Override
  public void rbsiPeriodic() {}
}
