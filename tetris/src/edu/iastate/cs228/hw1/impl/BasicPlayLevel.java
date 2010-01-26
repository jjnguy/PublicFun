package edu.iastate.cs228.hw1.impl;
import edu.iastate.cs228.hw1.IPlayLevel;

/**
 * Minimal implementation of IPlayLevel.
 */
public class BasicPlayLevel implements IPlayLevel
{
  @Override
  public int fastDropSpeed(int rowCount)
  {
    return 75;
  }

  @Override
  public int speed(int rowCount)
  {
    return 1000;
  }

}
