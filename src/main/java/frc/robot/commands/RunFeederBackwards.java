package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.feeder.Feeder;

public class RunFeederBackwards extends Command {
  private final Feeder feeder;

  public RunFeederBackwards(Feeder feeder) {
    this.feeder = feeder;
    addRequirements(feeder);
  }

  @Override
  public void execute() {
    feeder.RunFeederBackwards();
    System.out.println("intake running");
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
