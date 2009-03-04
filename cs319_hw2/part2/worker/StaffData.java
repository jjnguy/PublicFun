package worker;

/**
 * Simple data container represents a record for
 * data about a staff member.
 */
public class StaffData implements Comparable<StaffData>
{
  /**
   * Staff member's name
   */
  String name;
  
  /**
   * Staff member's title
   */
  String title;
  
  /**
   * Staff member's employee id number
   */
  int id;
  
  /**
   * Constructs a new StaffData record.
   * @param name
   *   staff member's name
   * @param id
   *   staff member's employee id number
   * @param title
   *   staff member's title
   */
  public StaffData(String name, int id, String title)
  {
    this.name = name;
    this.title = title;
    this.id = id;
  }
  
  public String getName()
  {
    return name;
  }
  
  public int getId()
  {
    return id;
  }
  
  public String getTitle()
  {
    return title;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append('\n');
    sb.append("ID=" + id);
    sb.append('\n');
    sb.append(title);
    return sb.toString();
  }

  @Override
  public int compareTo(StaffData other)
  {
    return name.compareTo(other.getName());
  }

  
}
