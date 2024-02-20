package Classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkIO {
  private InputStream is;
  private DataInputStream dis;
  private OutputStream os;
  private DataOutputStream dos;
  
  public NetworkIO(Socket sock) throws IOException {
    this.is = sock.getInputStream();
    this.os = sock.getOutputStream();
    this.dis = new DataInputStream(is);
    this.dos = new DataOutputStream(os);
  }

  public String read() throws IOException {
    return dis.readUTF();
  }

  public void write(String str) {
    try {
      this.dos.writeUTF(str);
      this.dos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() throws IOException {
    this.is.close();
    this.os.close();
    this.dis.close();
    this.dos.close();
  }
}
