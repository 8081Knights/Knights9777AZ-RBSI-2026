package frc.robot.subsystems.hang;

import frc.robot.util.RBSIIO;
import org.littletonrobotics.junction.AutoLog;

public interface HangIO extends RBSIIO {

  @AutoLog
  public static class HangIOInputs {
    public double[] supplyCurrent = new double[] {};
  }

  public default void updateInputs(HangIOInputs io) {}

  public default void setOutputExtender(double output) {}

  public default void setOutputRoller() {}

  public default void stopRoller() {}

  public default void setMode() {}

  public default double getPosition() {
    return 0.0;
  }
}