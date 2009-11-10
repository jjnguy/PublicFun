package hw4;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A class for writing primitives to a file in little endian format. 
 */
public class LEDataOutputStream {

  /** The underlying big endian file writer. */
  private DataOutputStream out;
  
  /** A collection of bytes used for an endian switcharoo. */
  private byte[] bytes;
  
  /** An endian switcher. */
  private ByteBuffer buffer;
  
  /**
   * Create a new little endian file writer for the specified file.
   * 
   * @param file
   * Name of file to write.
   * 
   * @throws FileNotFoundException
   */
  public LEDataOutputStream(String file) throws FileNotFoundException {
    out = new DataOutputStream(new FileOutputStream(file));
    bytes = new byte[4];
    buffer = ByteBuffer.wrap(bytes);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
  }
  
  /**
   * Write the specified String as a series of bytes.
   * 
   * @param s
   * The String to write.
   * 
   * @throws IOException
   */
  public void writeBytes(String s) throws IOException {
    out.writeBytes(s);
  }
  
  /**
   * Write the specified int.
   * 
   * @param value
   * Value to write.
   * 
   * @throws IOException
   */
  public void writeInt(int value) throws IOException {
    buffer.putInt(0, value);
    out.write(bytes, 0, 4);
  }
  
  /**
   * Writer the specified short.
   * 
   * @param value
   * Value to write.
   * 
   * @throws IOException
   */
  public void writeShort(short value) throws IOException {
    buffer.putShort(0, value);
    out.write(bytes, 0, 2);
  }
  
  /**
   * Write the sequence of shorts.  All values in the array are written in the order in which they appear in the array.
   * 
   * @param shorts
   * The sequence of values to write.
   * 
   * @throws IOException
   */
  public void write(short[] shorts) throws IOException {
    for (int i = 0; i < shorts.length; ++i) {
      writeShort(shorts[i]);
    }
  }
  
  /**
   * Close the file.
   * 
   * @throws IOException
   */
  public void close() throws IOException {
    out.close();
  }
}
