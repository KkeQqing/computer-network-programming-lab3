/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.computer.network.programming.lab3;

/**
 *
 * @author kq
 */
public class ComputerNetworkProgrammingLab3 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        // 强制使用 Nimbus 主题（确保所有环境一致）
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("无法设置 Nimbus 主题：" + e.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            new RPClientFrame().setVisible(true);
            new RPClientFrame().setVisible(true);
            new RPClientFrame().setVisible(true);
            new RPClientFrame().setVisible(true);
        });
    }
    
}
