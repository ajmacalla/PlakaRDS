package PlakaRDS;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.IntConsumer;

public class Draft3 {
	
	static final String TITLE = "PlakaRDS";
	static final String CATALOG = "catalog.txt";
	static final String RATINGS = "ratings.txt";
	static final String LOGO = "PlakaRDSLogo.png";
	static final int W = 900, H = 640; // consistent window size yan sa lahat ocd kicking in
	static final double ALBUM = 2.0; // album photo size inches
	static final double ARTIST = 10; // artist photo size millimeter
	static final String STAR = "★", STAR_EMPTY = "☆";
	static final String[] SORTS = {"A-z", "Highest rated", "Lowest rated"};
	static final String[] RATE_MEANING = {
			"I don't like it.", "It's not for me.", "It's okay.", "I like it.", "I absolutely love it."
			};
	
	static final Color DEEP = new Color(31, 90, 122);
	static final Color SKY = new Color(147, 199, 221);
	static final Color BG = new Color(240, 247, 251);
	static final Color ZEBRA = new Color(227, 240, 248);
	static final Color SELECT = new Color(178, 217, 236);
	static final Color TEXT = new Color(24, 48, 64);
	static final Color MUTED = new Color(90, 122, 142);
	static final Color LINE = new Color(176, 204, 220);
	static final String FONT = pickFont();
	
	static String pickFont() {
		Set<String> have = new HashSet<>(Arrays.asList(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
				);
		for (String n : new String[] {"Segoe UI", "Helvetica Neue", "Calibri", "Arial"})
			if (have.contains(n))
				return n;
		return Font.SANS_SERIF;
	}
	
	static Font font(int style, int size) {
		return new Font(FONT, style, size);
	}
	
	static class Review {
		final int stars;
		final String text;
		
		Review(int stars, String text) {
			this.stars = stars;
			this.text = text;
		}
	}
	
	static class Song {
		final String title, artist;
		int sumRate = 0;
		int numRate = 0;
		final int[] starCount = new int[6];
		final List<Review> comments = new ArrayList<>();
		
		Song(String title, String artist) {
			this.title = title;
			this.artist = artist;
		}
		
		void addRating(int stars) {
			sumRate += stars;
			numRate ++;
			starCount[stars]++;
		}
		double average() {
			return numRate == 0 ? 0 : (double) sumRate / numRate;
		}
		String avgText() {
			return numRate == 0 ? "No ratings yet." : String.format("%s %.1f / 5 (%d", STAR, average(), numRate);
		}
		@Override public String toString() {
			return title;
		}
		
		static final Map<String, List<Song>> catalog = new LinkedHashMap<>();
		static final Map<String, String> albumOf = new HashMap<>();
		static final Map<String, String> artistPic = new HashMap<>();
		static final Map<String, String> albumPic = new HashMap<>();
		static final Map<String, Icon> icon = new HashMap<>();
		
		static int artistSort = 0, songSort = 0;
		
		public static void main(String[] args) {
			
		}
	}
	
}