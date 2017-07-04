package com.scs.trickytowers.entity.systems;

import com.scs.trickytowers.Main;
import com.scs.trickytowers.Player;

public class PlayerInputSystem { // todo - delete this

	private Main main;

	public PlayerInputSystem(Main _main) {
		main = _main;
	}

	
	public void process(Player player) {
		player.process();
	}

}
