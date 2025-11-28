package com.mycompany.computer.network.programming.lab3;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class RPServer {
    private static final int PORT = 8888;
    private static final int THREAD_POOL_SIZE = 5; // 可根据负载调整

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务器启动，监听端口：" + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端连接成功：" + clientSocket.getRemoteSocketAddress());

                // 提交任务到线程池
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) serverSocket.close();
                executor.shutdown(); // 关闭线程池
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 每个客户端请求由一个独立线程处理
    static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // 1. 接收客户端选择（石头/剪刀/布）
                String clientChoice = in.readLine();

                // 2. 服务器随机出拳
                String serverChoice = getRandomChoice();

                // 3. 判断胜负
                String result = determineWinner(clientChoice, serverChoice);

                // 4. 返回结果给客户端
                out.println(serverChoice); // 服务器出拳
                out.println(result);       // 胜负结果

                System.out.println("客户端：" + clientChoice + " vs 服务器：" + serverChoice + " → " + result);

            } catch (IOException e) {
                System.err.println("客户端通信异常：" + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getRandomChoice() {
            String[] choices = {"石头", "剪刀", "布"};
            return choices[(int)(Math.random() * 3)];
        }

        private String determineWinner(String client, String server) {
            if (client.equals(server)) return "平局！";
            if ((client.equals("石头") && server.equals("剪刀")) ||
                    (client.equals("剪刀") && server.equals("布")) ||
                    (client.equals("布") && server.equals("石头"))) {
                return "你赢了！";
            } else {
                return "你输了！";
            }
        }
    }
}