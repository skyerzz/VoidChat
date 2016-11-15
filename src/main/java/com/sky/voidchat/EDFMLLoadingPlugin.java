package com.sky.voidchat;

import java.io.File;
import java.util.Map;


import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class EDFMLLoadingPlugin implements IFMLLoadingPlugin{

	public static File location;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{EDClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
