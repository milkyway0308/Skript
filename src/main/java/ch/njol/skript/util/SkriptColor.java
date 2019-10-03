/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Copyright 2011-2017 Peter Güttinger and contributors
 */
package ch.njol.skript.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.localization.Adjective;
import ch.njol.skript.localization.Language;

@SuppressWarnings("null")
public enum SkriptColor implements Color {

	BLACK(DyeColor.BLACK, ChatColor.BLACK),
	DARK_GREY(DyeColor.GRAY, ChatColor.DARK_GRAY),
	// DyeColor.LIGHT_GRAY on 1.13, DyeColor.SILVER on earlier
	LIGHT_GREY(DyeColor.getByColor(org.bukkit.Color.fromRGB(0x9D9D97)), ChatColor.GRAY),
	WHITE(DyeColor.WHITE, ChatColor.WHITE),
	
	DARK_BLUE(DyeColor.BLUE, ChatColor.DARK_BLUE),
	BROWN(DyeColor.BROWN, ChatColor.BLUE),
	DARK_CYAN(DyeColor.CYAN, ChatColor.DARK_AQUA),
	LIGHT_CYAN(DyeColor.LIGHT_BLUE, ChatColor.AQUA),
	
	DARK_GREEN(DyeColor.GREEN, ChatColor.DARK_GREEN),
	LIGHT_GREEN(DyeColor.LIME, ChatColor.GREEN),
	
	YELLOW(DyeColor.YELLOW, ChatColor.YELLOW),
	ORANGE(DyeColor.ORANGE, ChatColor.GOLD),
	
	DARK_RED(DyeColor.RED, ChatColor.DARK_RED),
	LIGHT_RED(DyeColor.PINK, ChatColor.RED),
	
	DARK_PURPLE(DyeColor.PURPLE, ChatColor.DARK_PURPLE),
	LIGHT_PURPLE(DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE);
	
	private ChatColor chat;
	private DyeColor dye;
	
	@Nullable
	Adjective adjective;
	
	SkriptColor(DyeColor dye, ChatColor chat) {
		this.chat = chat;
		this.dye = dye;
	}
	
	@Override
	public org.bukkit.Color asBukkitColor() {
		return dye.getColor();
	}
	
	// currently only used by SheepData
	@Nullable
	public Adjective getAdjective() {
		return adjective;
	}
	
	@Override
	public ChatColor asChatColor() {
		return chat;
	}

	@Override
	public DyeColor asDyeColor() {
		return dye;
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	@Override
	public String getFormattedChat() {
		return "" + chat;
	}
	
	@Deprecated
	@Override
	public byte getWoolData() {
		return dye.getWoolData();
	}
	
	@Deprecated
	@Override
	public byte getDyeData() {
		return (byte) (15 - dye.getWoolData());
	}
	
	final static Map<String, Color> names = new HashMap<>();
	final static Set<Color> colors = new HashSet<>();
	public final static String LANGUAGE_NODE = "colors";
	
	static {
		colors.addAll(Arrays.asList(values()));
		Language.addListener(() -> {
			names.clear();
			for (SkriptColor color : values()) {
				String node = LANGUAGE_NODE + "." + color.name();
				color.adjective = new Adjective(node + ".adjective");
				for (String name : Language.getList(node + ".names"))
					names.put(name.toLowerCase(), color);
			}
		});
	}
	
	/**
	 * @param name The name of the color defined by Skript's .lang files.
	 * @return Optional if any Skript Color matched up with the defined name
	 */
	@Nullable
	public static Color fromName(String name) {
		return names.get(name);
	}
	
	/**
	 * @param dye DyeColor to match against a defined Skript Color.
	 * @return Optional if any Skript Color matched up with the defined DyeColor
	 */
	public static Color fromDyeColor(DyeColor dye) {
		for (Color color : colors) {
			if (color.asDyeColor().equals(dye))
				return color;
		}
		assert false;
		return null;
	}
	
	public static Color fromBukkitColor(org.bukkit.Color color) {
		for (Color c : colors) {
			if (c.asBukkitColor().equals(color))
				return c;
		}
		assert false;
		return null;
	}
	
	/**
	 * @deprecated Magic numbers
	 * @param data DyeColor to match against a defined Skript Color.
	 * @return Optional if any Skript Color matched up with the defined DyeColor
	 */
	@Deprecated
	@Nullable
	public static Color fromDyeData(short data) {
		if (data < 0 || data >= 16)
			return null;
		
		for (Color color : colors) {
			if (color.asDyeColor().getDyeData() == data)
				return color;
		}
		return null;
	}
	
	/**
	 * @deprecated Magic numbers
	 * @param data DyeColor to match against a defined Skript Color.
	 * @return Optional if any Skript Color matched up with the defined DyeColor
	 */
	@Deprecated
	@Nullable
	public static Color fromWoolData(short data) {
		if (data < 0 || data >= 16)
			return null;
		for (Color color : colors) {
			if (color.asDyeColor().getWoolData() == data)
				return color;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return adjective == null ? "" + name() : adjective.toString(-1, 0);
	}

}
