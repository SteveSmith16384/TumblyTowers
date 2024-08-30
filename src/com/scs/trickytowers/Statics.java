package com.scs.trickytowers;

import java.util.Random;

import ssmith.awt.ImageCache;

public class Statics {

	public static final boolean FULL_SCREEN = false;// Define se o jogo roda em tela cheia
    public static final boolean RELEASE_MODE = false; // Define se o jogo está em modo de liberação
	
	public static final boolean SHOW_CONTROLLER_EVENTS = !RELEASE_MODE && false; // Mostra eventos do controlador apenas em modo de desenvolvimento

	// Configurações de desempenho
    public static final int FPS = 30; // Frames por segundo

	// Configurações de tela
    public static final float STD_CELL_SIZE = 5f; // Tamanho padrão das células em unidades lógicas
    public static final int WINDOW_HEIGHT = 900; // Altura da janela quando não está em tela cheia
    public static final int WORLD_WIDTH_LOGICAL = 700; // Largura lógica do mundo do jogo
    public static final int WORLD_HEIGHT_LOGICAL = 200; // Altura lógica do mundo do jogo
    public static final float LOGICAL_WINNING_HEIGHT = WORLD_HEIGHT_LOGICAL * 0.2f; // Altura lógica para vitória

     // Conversão de unidades lógicas para pixels
	public static float LOGICAL_TO_PIXELS;
	
	public static final Random rnd = new Random();

	public static ImageCache img_cache;

	private Statics() {

	}
    

	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


}
