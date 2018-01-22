package com.pbc.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Alex on 2018/1/22.
 */
public class BIOClient {
    private static int PORT = 8888;
    private static String IP = "127.0.0.1";

    public static void main(String[] args) {
        BufferedReader br = null;
        PrintWriter pw = null;
        Socket socket = null;
        BufferedReader input = null;

        try {
            socket = new Socket(IP, PORT);
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            input = new BufferedReader(new InputStreamReader(System.in));
            pw.println(input.readLine());
            System.out.println("发送完毕");
            String response = br.readLine();
            System.out.println("服务器 传给 客户端服务处理：" + response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (pw != null)
                pw.close();
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
