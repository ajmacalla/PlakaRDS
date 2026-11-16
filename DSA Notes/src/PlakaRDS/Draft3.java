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
	static final String STAR = "\u2605", STAR_EMPTY = "\u2606";
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
		for (String n : new String[] {"Segoe UI Symbol", "Helvetica Neue", "DejaVu Sans", "Arial Unicode MS"}) {
			if (!have.contains(n))
				continue;
				Font f = new Font(n, Font.PLAIN, 14);
			if (f.canDisplay('\u2605') && f.canDisplay('\u2606'))
				return n;
		}
		return Font.DIALOG;	
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
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception e) {
			}
			UIManager.put("Button.font", font(Font.BOLD, 14));
			UIManager.put("OptionPane.buttonFont", font(Font.BOLD, 14));
			UIManager.put("ComboBox.font", font(Font.PLAIN, 14));
			UIManager.put("TextField.font", font(Font.PLAIN, 14));
			UIManager.put("RadioButton.font", font(Font.PLAIN, 16));
			UIManager.put("Label.font", font(Font.PLAIN, 14));
			
			loadCatalog();
			if (catalog.isEmpty()) {
				info("No artists found in " + CATALOG + ".\n\nLooking here:\n"
						+ Paths.get(CATALOG).toAbsolutePath()
						+ "\n\nPut catalog.txt in that folder (for Eclipse: the project root).");
				return;
			}
			loadRatings();
			mainMenu();
		}
		
		static int artistSum(String a) {
			int s = 0;
			for (Song x : catalog.get(a))
				s += x.sumRate;
			return s;
		}
		
		static int artistCount(String a) {
			int n = 0;
			for (Song x : catalog.get(a))
				n += x.numRate;
			return n;
		}
		
		static double artistAvg(String a) {
			int n = artistCount(a);
			return n == 0 ? 0 : (double) artistSum(a) / n;
		}
		
		static String artistAvgText(String a) {
			int n = artistCount(a);
			return n == 0 ? "No ratings yet." 
					: String.format("%s %.1f / 5 (%d rating%s", STAR, artistAvg(a),
					n, n == 1 ? "" : "s");
		}
		
		static Comparator<String> artistOrder(int mode) {
			Comparator<String> name = String.CASE_INSENSITIVE_ORDER;
			if (mode == 0)
				return name;
			Comparator<String> avg = (a, b) -> {
				boolean ra = artistCount(a) > 0, rb = artistCount(b) > 0;
				if (ra != rb)
					return ra ? -1 : 1;
				int c = Double.compare(artistAvg(a), artistAvg(b));
				return mode == 1 ? -c : c;
			};
			return avg.thenComparing(name);
		}
		
		static List<Song> topRated() {
			List<Song> all = new ArrayList<>();
			for (List<Song> l : catalog.values())
				for (Song s : l) 
					if (s.numRate > 0) all.add(s);
			all.sort((a, b) -> {
				int c = Double.compare(b.average(), a.average());
				return c != 0 ? c : Integer.compare(b.numRate, a.numRate);
			});
			return all;
		}
		
		static String stars(int n) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= 5; i++) 
				sb.append(i <= n ? STAR : STAR_EMPTY);
			return sb.toString();
		}
		
		static JPanel window() {
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(W, H));
			p.setBackground(BG);
			return p;
		}
		
		static JPanel body() {
			JPanel p = new JPanel(new BorderLayout(0, 10));
			p.setBackground(BG);
			p.setBorder(new EmptyBorder(14, 20, 14, 20));
			return p;
		}
		
		static JLabel label(String text, int style, int size, Color c) {
			JLabel l = new JLabel(text);
			l.setFont(font(style, size));
			l.setForeground(c);
			return l;
		}
		
		static <T extends JComponent> T center(T c) {
			c.setAlignmentX(Component.CENTER_ALIGNMENT);
			return c;
		}
		
		static JPanel title(String title, String sub) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBackground(DEEP);
			p.setBorder(new EmptyBorder(16, 24, 16, 24));
			
			JLabel a = label(title, Font.BOLD, 26, Color.WHITE);
			JLabel b = label(sub, Font.PLAIN, 14, SKY);
			a.setAlignmentX(Component.LEFT_ALIGNMENT);
			b.setAlignmentX(Component.LEFT_ALIGNMENT);
			p.add(a);
			p.add(Box.createVerticalStrut(4));
			p.add(b);
			return p;
		}
		
		static JPanel windowTop(Icon album, String albumLine, String title, 
				Icon titleIcon, String sub, Icon subIcon, String ratingLine) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBackground(DEEP);
			p.setBorder(new EmptyBorder(10, 20, 10, 20));
			
			JLabel a = new JLabel(album);
			a.setBorder(new LineBorder(Color.WHITE, 3));
			p.add(center(a));
			if (albumLine != null) {
				p.add(Box.createVerticalStrut(4));
				p.add(center(label(albumLine, Font.PLAIN, 14, SKY)));
			}
			p.add(Box.createVerticalStrut(4));
			JLabel t = label(title, Font.BOLD, 24, Color.WHITE);
			if (titleIcon != null) {
				t.setIcon(titleIcon);
				t.setHorizontalTextPosition(SwingConstants.LEFT);
				t.setIconTextGap(10);
			}
			p.add(center(t));
			if (sub != null) {
				p.add(Box.createVerticalStrut(2));
				p.add(center(label(ratingLine, Font.BOLD, 15, SKY)));
			}
			return p;
		}
		
		static JPanel statBottom(String value, String caption) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBackground(Color.WHITE);
			p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(LINE), new EmptyBorder(12, 8, 12, 8)));
			p.add(center(label(value, Font.BOLD, 28, DEEP)));
			p.add(center(label(caption, Font.PLAIN, 13, MUTED)));
			return p;
		}
		
		
	}
	
}