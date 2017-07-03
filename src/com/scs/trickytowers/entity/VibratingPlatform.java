package com.scs.trickytowers.entity;

import com.scs.trickytowers.Main;
import com.scs.trickytowers.entity.components.IProcessable;

public class VibratingPlatform extends PhysicalEntity implements IProcessable {

	public VibratingPlatform(Main main) {
		super(main, "VibratingPlatform");
	}

	
	@Override
	public void preprocess(long interpol) {
		// TODO - vibrate
		
	}

	@Override
	public void postprocess(long interpol) {
		
	}

}
