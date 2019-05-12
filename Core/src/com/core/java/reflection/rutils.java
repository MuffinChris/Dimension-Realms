package com.core.java.reflection;

import org.bukkit.Bukkit;

public class rutils {

	private final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	public Class<?> getNMSClass(String name) {
		try {
			  return Class.forName("net.minecraft.server." + version + "." + name);
		  } catch(ClassNotFoundException e) {
			  e.printStackTrace();
			  return null;
		  }
	}
	
}
