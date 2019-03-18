package ARGuide - Location;

import String;
import UnlimitedInteger;
import Integer;

public abstract class Location {

  protected Integer Floor;

  protected String Identifier;

  protected String Type;

  public String Photo;

  protected Edge Neighbours;

  protected abstract void getLocation();

}