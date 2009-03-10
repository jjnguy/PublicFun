

/**
 * File node type to be used as a placeholder
 * for children of unexpanded directory nodes.
 * These nodes are normally not displayed unless
 * an error prevents the actual children from
 * being found.
 */
public class DummyNode extends FileNode
{
  /**
   * Creates a DummyNode object.
   */
  public DummyNode()
  {
    super("(error)", false);
  }
}
