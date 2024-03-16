package com.br.fullPvp.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.br.fullPvp.Main;

import lombok.Getter;

public class ClassGetter {
	
	@Getter
	static ClassGetter instance = new ClassGetter();
	
	@SuppressWarnings("deprecation")
	public void commands(String pacote) {
		for (Class<?> classe : getClassesForPackage(Main.getPlugin(Main.class), pacote)) {
			if (CommandExecutor.class.isAssignableFrom(classe)) {
				try {
					CommandExecutor comando = null;
					try {
						Constructor<?> con = classe.getConstructor(new Class[] { Main.class });
						comando = (CommandExecutor) con.newInstance(new Object[] { Main.getPlugin() });
					} catch (Exception ex) {
						comando = (CommandExecutor) classe.newInstance();
					}
					Main.getPlugin(Main.class).getCommand(classe.getSimpleName().replace("comando", "")).setExecutor(comando);
					Bukkit.getLogger().info("Comando '/" + classe.getSimpleName().toLowerCase().replace("comando", "") + "' carregado com sucesso.");
				} catch (Exception e) {
					Bukkit.getLogger().info("Erro ao carregar o comando " + classe.getSimpleName() + ".");
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void events(String pacote) {
		for (Class<?> classe : getClassesForPackage(Main.getPlugin(Main.class), pacote)) {
			if (Listener.class.isAssignableFrom(classe)) {
				try {
					Listener evento;
					try {
						evento = (Listener) classe.getConstructor(new Class[] { Main.class }).newInstance(new Object[] { Main.class });
					} catch (Exception e) {
						evento = (Listener) classe.newInstance();
					}
					Bukkit.getPluginManager().registerEvents(evento, Main.getPlugin());
					Bukkit.getLogger().info("Evento '" + classe.getSimpleName() + "' carregado com sucesso.");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar o evento " + classe.getSimpleName() + ".");
				}
			}
		}
	}

	public ArrayList<Class<?>> getClassesForPackage(JavaPlugin plugin, String pkgname) {
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		final CodeSource src = plugin.getClass().getProtectionDomain().getCodeSource();
		if (src != null) {
			final URL resource = src.getLocation();
			resource.getPath();
			processJarfile(resource, pkgname, classes);
		}
		return classes;
	}

	private static Class<?> loadClass(final String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
		} catch (NoClassDefFoundError e2) {
			return null;
		}
	}

	private static void processJarfile(final URL resource, final String pkgname, final ArrayList<Class<?>> classes) {
		final String relPath = pkgname.replace('.', '/');
		final String resPath = resource.getPath().replace("%20", " ");
		final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
		JarFile jarFile;
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
		}
		final Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			final String entryName = entry.getName();
			String className = null;
			if (entryName.endsWith(".class") && entryName.startsWith(relPath)
					&& entryName.length() > relPath.length() + "/".length()) {
				className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
			}
			if (className != null) {
				final Class<?> c = loadClass(className);
				if (c == null) {
					continue;
				}
				classes.add(c);
			}
		}
		try {
			jarFile.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}
