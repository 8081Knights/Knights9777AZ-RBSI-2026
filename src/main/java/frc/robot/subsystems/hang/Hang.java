package frc.robot.subsystems.hang;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hang extends SubsystemBase {
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
  public void periodic() {}
}