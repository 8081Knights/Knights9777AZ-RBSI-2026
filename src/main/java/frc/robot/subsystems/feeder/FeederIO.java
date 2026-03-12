package frc.robot.subsystems.feeder;

import frc.robot.util.RBSIIO;
import org.littletonrobotics.junction.AutoLog;

public interface FeederIO extends RBSIIO {

  @AutoLog
  public static class FeederIOInputs {
    public double[] supplyCurrent = new double[] {};
  }

  public default void updateInputs(FeederIOInputs io) {}

  public default void setOutputExtender(double output) {}

  public default void setFeedRoller() {}

  public default void setBlockRoller() {}

  public default void stopRoller() {}

  public default void setMode() {}

  public default double getPosition() {
    return 0.0;
  }
}
