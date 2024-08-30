package com.scs.trickytowers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    public BufferStrategy BS;

    public MainWindow(Main_TumblyTowers main) {
        if (Statics.FULL_SCREEN) {
            this.setUndecorated(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
            device.setFullScreenWindow(this);
        } else {
            float frac = (float) Statics.WORLD_WIDTH_LOGICAL / (float) Statics.WORLD_HEIGHT_LOGICAL;
            this.setSize((int) (Statics.WINDOW_HEIGHT * frac), Statics.WINDOW_HEIGHT);
        }

        // Ajustar a escala de acordo com o tamanho da janela
        Statics.LOGICAL_TO_PIXELS = (float) this.getHeight() / (float) Statics.WORLD_HEIGHT_LOGICAL;

        this.setVisible(true);
        this.addKeyListener(main);

        this.createBufferStrategy(2);
        BS = this.getBufferStrategy();

        this.requestFocus();

        // Adicionar um listener para redimensionamento
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Em vez de recriar o BufferStrategy, apenas ajustar a escala
                Statics.LOGICAL_TO_PIXELS = (float) getHeight() / (float) Statics.WORLD_HEIGHT_LOGICAL;
            }
        });
    }
    
    // Método para verificar se o buffer strategy está funcionando e recriá-lo se necessário
    public void checkBufferStrategy() {
        if (BS == null || BS.contentsLost()) {
            this.createBufferStrategy(2);
            BS = this.getBufferStrategy();
        }
    }

}
