package worker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A custom implementation of the ListModel interface
 * stores StaffData records and presents a "view"
 * of the data to an associated JList.
 */
public class DemoModel implements ListModel
{
  /**
   * List of listeners to be notified of changes
   * in this model's data.
   */
  private List<ListDataListener> listeners = 
    new ArrayList<ListDataListener>();

  /**
   * Determines whether the view of the list is 
   * in ascending or descending order of the indices.
   */
  private boolean ascending = true;
  
  /**
   * Indicates whether the list items are currently sorted.
   */
  private boolean sorted = false;
  
  /**
   * The actual data
   */
  private List<StaffData> data = new ArrayList<StaffData>();
  
  /**
   * Constructs a model with the given initial data.
   * @param initialData
   *   The initial data for the model.
   */
  public DemoModel(List<StaffData> initialData)
  {
    if (initialData != null)
    {
      data = initialData;
    }
    else
    {
      data = new ArrayList<StaffData>();
    }
  }
  
  /**
   * Adds a new record to this model.
   * @param name
   *    the new staff member's name
   * @param id
   *    the new staff member's id number
   * @param addr
   *    the new staff member's title
   */
  public void addElement(String name, int id, String title)
  {
    StaffData newRecord = new StaffData(name, id, title);
    
    // if the current view is ascending, add new data to actual end of list
    // in descending mode, "end" of list is actual position 0
    int actualIndex = ascending ? data.size() : 0;
    data.add(actualIndex, newRecord);
    sorted = false;
    
    // notify listeners of new data at index
    int index = data.size() - 1;
    fireIntervalAdded(this, index, index);
  }
  
  /**
   * Deletes the record at the given index.
   * @param index
   *   the index at which to delete
   */
  public void removeElement(int index)
  {
    int actualIndex = ascending ? index : data.size() - 1 - index;
    data.remove(actualIndex);
    
    // notify listeners of removed data at index
    fireIntervalRemoved(this, index, index);
  }
  
  /**
   * Returns the record at the given index.
   * @param index
   *   the index of the desired record
   * @return
   *   the record at the given index
   */
  public StaffData getElement(int index)
  {
    int actualIndex = ascending ? index : data.size() - 1 - index;
    return data.get(actualIndex);
  }
  
  /**
   * Sort this model's data in ascending order.
   */
  public void sortUp()
  {
    if (!sorted)
    {
      Collections.sort(data);
      sorted = true;
    }
    ascending = true;
    
    // notify listeners
    fireContentsChanged(this, 0, data.size() - 1);
  }
  
  /**
   * Sort this model's data in descending order.
   */
  public void sortDown()
  {
    if (!sorted)
    {
      Collections.sort(data);
      sorted = true;
    }
    ascending = false;
    
    // notify listeners
    fireContentsChanged(this, 0, data.size() - 1);
  }
  
  
  // The next four methods are specified by the 
  // ListModel interface and are only called by the 
  // JList implementation.  The JList uses getSize
  // and getElementAt to determine what to display at
  // each position in the list box.  The method
  // addListDataListeners is initially called by
  // the JList (actually a related inner class)
  // to make itself a listener on this model.
  
  @Override
  public Object getElementAt(int index)
  {
    //System.out.println("getElementAt " + index);
    return getElement(index).getName();
  }

  @Override
  public int getSize()
  {
    //System.out.println("getSize");
    return data.size();
  }

  @Override
  public void addListDataListener(ListDataListener arg0)
  {
    System.out.println("addListDataListener() executing in " + Thread.currentThread().getName());

    System.out.println("DemoModel adding listener: " + arg0.toString());
    listeners.add(arg0);
  }

  @Override
  public void removeListDataListener(ListDataListener arg0)
  {
    listeners.remove(arg0);
  }

  //
  // private helper methods for notifying listeners
  //
  
  /**
   * Invokes the <code>intervalAdded</code> method of the 
   * ListDataListener interface with an appropriate event.
   */
  private void fireIntervalAdded(ListModel model, int start, int end)
  {
    ListDataEvent event = new ListDataEvent(model, ListDataEvent.INTERVAL_ADDED, start, end);
    for (ListDataListener listener : listeners)
    {
      listener.intervalAdded(event);
    }
  }

  /**
   * Invokes the <code>intervalRemoved</code> method of the 
   * ListDataListener interface with an appropriate event.
   */  
  private void fireIntervalRemoved(ListModel model, int start, int end)
  {
    ListDataEvent event = new ListDataEvent(model, ListDataEvent.INTERVAL_REMOVED, start, end);
    for (ListDataListener listener : listeners)
    {
      listener.intervalRemoved(event);
    }
  }

  /**
   * Invokes the <code>contentsChanged</code> method of the 
   * ListDataListener interface with an appropriate event.
   */    
  private void fireContentsChanged(ListModel model, int start, int end)
  {
    ListDataEvent event = new ListDataEvent(model, ListDataEvent.CONTENTS_CHANGED, start, end);
    for (ListDataListener listener : listeners)
    {
      listener.contentsChanged(event);
    }

  }

}
