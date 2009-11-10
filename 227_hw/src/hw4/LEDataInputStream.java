package hw4;
 
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A class for reading primitives from a file in little endian format. 
 */
public class LEDataInputStream {

  /** The underlying big endian file writer. */
  private DataInputStream in;
  
  /** A collection of bytes used for an endian switcharoo. */
  private byte[] bytes;
  
  /** An endian switcher. */
  private ByteBuffer buffer;
  
  /**
   * Create a new little endian file reader for the specified file.
   * 
   * @param file
   * Name of file to reader.
   * 
   * @throws FileNotFoundException
   */
  public LEDataInputStream(String file) throws FileNotFoundException {
    in = new DataInputStream(new FileInputStream(file));
    bytes = new byte[4];
    buffer = ByteBuffer.wrap(bytes);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
  }
  
  /**
   * Read the next int from the file.
   * 
   * @return
   * Value read.
   * 
   * @throws IOException
   */
  public int readInt() throws IOException {
    in.read(bytes, 0, 4);
    return buffer.getInt(0);
  }
  
  /**
   * Read the next short from the file.
   * 
   * @return
   * Value read.
   * 
   * @throws IOException
   */
  public short readShort() throws IOException {
    in.read(bytes, 0, 2);
    return buffer.getShort(0);
  }
  
  /**
   * Read a sequence of bytes from the file. The number of bytes read is determined by the length of the array.
   * 
   * @param bytes
   * Array in which read bytes are stored.
   * 
   * @throws IOException
   */
  public void read(byte[] bytes) throws IOException {
    in.read(bytes);
  }
  
  /**
   * Read a sequence of shorts from the file. The number of shorts read is determined by the length of the array.
   *  
   * @param shorts
   * Array in which shorts are stored.
   * 
   * @throws IOException
   */
  public void read(short[] shorts) throws IOException {
    for (int i = 0; i < shorts.length; ++i) {
      shorts[i] = readShort();
    }
  }
  
  /**
   * Close the file.
   * 
   * @throws IOException
   */
  public void close() throws IOException {
    in.close();
  }
}
