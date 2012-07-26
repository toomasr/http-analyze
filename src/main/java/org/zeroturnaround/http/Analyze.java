package org.zeroturnaround.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Analyze {
  public static void main(String[] args) throws Exception {
    String host = System.getProperty("host");
    if (host == null) {
      System.out.println("Please provide host with -Dhost=google.com");
      System.exit(0);
    }
    Socket sock = new Socket(host, 80);
    DataInputStream input = new DataInputStream(sock.getInputStream());
    DataOutputStream out = new DataOutputStream(sock.getOutputStream());

    // great place to start timing
    // DNS has been resolved
    long startTime = System.nanoTime();
    // a very simple request
    out.writeBytes("GET / HTTP/1.1\n");
    out.writeBytes("Host: " + host + "\n");
    out.writeBytes("\n");

    // lets keep the byte small but not too small
    byte[] bytes = new byte[1024];
    int rtrn = 0;
    String headers = "";
    while ((rtrn = input.read(bytes)) != -1) {
      long firstByteTime = System.nanoTime();
      System.out.println("First byte: " + (firstByteTime - startTime) / (1000 * 1000.0) + " ms");
      headers = new String(bytes, 0, rtrn);
      break;
    }

    if (System.getProperty("headers") != null) {
      System.out.println();
      String[] lines = headers.split("\n");
      for (int i = 0; i < lines.length; i++) {
        if (lines[i].length() == 1)
          break;
        System.out.println(lines[i]);
      }
    }
  }
}
