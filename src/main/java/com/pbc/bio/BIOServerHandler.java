package com.pbc.bio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Alex on 2018/1/22.
 */
public class BIOServerHandler implements Runnable {
    private Socket socket;

    public BIOServerHandler(Socket s) {
        this.socket = s;
    }

    public void run() {
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);
            String word = br.readLine();
            System.out.println("客户端发送的消息" + word);//打印服务器接受到的消息  下一步转发给客户端处理器

            Thread.sleep(10000);

            pw.println("你好，客户端");//输出给客户端接收器
        } catch (Exception e) {
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
        }
    }
}
