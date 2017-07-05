package com.scs.trickytowers.entity.systems;

import com.scs.trickytowers.Main_TumblyTowers;
import com.scs.trickytowers.Player;

public class PlayerInputSystem { // todo - delete this

	private Main_TumblyTowers main;

	public PlayerInputSystem(Main_TumblyTowers _main) {
		main = _main;
	}

	
	public void process(Player player) {
		player.process();
	}

}
