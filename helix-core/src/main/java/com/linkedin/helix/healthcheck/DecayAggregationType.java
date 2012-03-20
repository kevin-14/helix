package com.linkedin.helix.healthcheck;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public class DecayAggregationType implements AggregationType
{

  private static final Logger logger = Logger
      .getLogger(DecayAggregationType.class);

  public final static String TYPE_NAME = "decay";

  double _decayFactor = 0.1;

  public DecayAggregationType(double df)
  {
    super();
    _decayFactor = df;
  }

  @Override
  public String getName()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(TYPE_NAME);
    sb.append(DELIM);
    sb.append(_decayFactor);
    return sb.toString();
  }

  @Override
  public String merge(String iv, String ev, long prevTimestamp)
  {
    double incomingVal = Double.parseDouble(iv);
    double existingVal = Double.parseDouble(ev);
    long currTimestamp = System.currentTimeMillis();
    double minutesOld = (currTimestamp - prevTimestamp) / 60000.0;
    // come up with decay coeff for old value. More time passed, the more it
    // decays
    double oldDecayCoeff = Math.pow((1 - _decayFactor), minutesOld);
    return String
        .valueOf((double) (oldDecayCoeff * existingVal + (1 - oldDecayCoeff)
            * incomingVal));
  }
}
