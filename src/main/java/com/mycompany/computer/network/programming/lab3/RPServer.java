package com.mycompany.computer.network.programming.lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPServer {

    private static final int PORT = 7;
    private static final int THREAD_POOL_SIZE = 3; // 线程池大小
    private ExecutorService executorService; // 线程池

    public RPServer() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器启动，监听端口：" + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端连接：" + clientSocket.getRemoteSocketAddress());

                // 提交任务给线程池处理
                executorService.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("服务器异常：" + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    // 客户端处理器类
    private class ClientHandler implements Runnable {
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
                String clientChoice;
                while ((clientChoice = in.readLine()) != null) {
                    if (clientChoice.trim().isEmpty()) continue;

                    Thread.sleep(3000);

                    String[] choices = {"石头", "剪刀", "布"};
                    String serverChoice = choices[(int) (Math.random() * 3)];
                    String result = determineWinner(clientChoice, serverChoice);

                    out.println("服务器选择：" + serverChoice);
                    out.println(result);

                    System.out.println("客户端：" + clientChoice + " vs 服务器：" + serverChoice + " → " + result);
                }
            } catch (IOException e) {
                System.err.println("处理客户端异常：" + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 判断胜负规则
        private String determineWinner(String player, String computer) {
            if (player.equals(computer)) {
                return "平局！";
            }
            if ((player.equals("石头") && computer.equals("剪刀")) ||
                    (player.equals("剪刀") && computer.equals("布")) ||
                    (player.equals("布") && computer.equals("石头"))) {
                return "你赢了！";
            } else {
                return "你输了！";
            }
        }
    }

    public static void main(String[] args) {
        new RPServer().start();
    }
}