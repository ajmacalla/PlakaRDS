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
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.function.IntConsumer;

public class Theme {
	
	public static final String TITLE = "PlakaRDS";
	public static final String CATALOG = "catalog.txt";
	public static final String RATINGS = "ratings.txt";
	public static final String LOGO = "PlakaRDSLogo.png";
	public static final int W = 900, H = 640; // consistent window size yan sa lahat ocd kicking in
	public static final double ALBUM = 2.0; // album photo size inches
	public static final double ARTIST = 10; // artist photo size millimeter
	public static final String STAR = "\u2605", STAR_EMPTY = "\u2606";
	public static final String[] SORTS = {"A-z", "Highest rated", "Lowest rated"};
	public static final String[] RATE_MEANING = {
			"I don't like it.", "It's not for me.", "It's okay.", "I like it.", "I absolutely love it."
			};
	
	public static final Color DEEP = new Color(31, 90, 122);
	public static final Color SKY = new Color(147, 199, 221);
	public static final Color BG = new Color(240, 247, 251);
	public static final Color ZEBRA = new Color(227, 240, 248);
	public static final Color SELECT = new Color(178, 217, 236);
	public static final Color TEXT = new Color(24, 48, 64);
	public static final Color MUTED = new Color(90, 122, 142);
	public static final Color LINE = new Color(176, 204, 220);
	public static final String FONT = pickFont();

	static final Map<String, Icon> iconCache = new HashMap<>();
	
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
	
	public static Font font(int style, int size) {
		return new Font(FONT, style, size);
	}
	
	public static void btn() {
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
	}
	
	public static String stars(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 5; i++)
			sb.append(i <= n ? STAR : STAR_EMPTY);
		return sb.toString();
	}
	
	public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, TITLE, JOptionPane.PLAIN_MESSAGE);
	}
	
	public static int pxInch(double inches) {
        return (int) Math.round(inches * Toolkit.getDefaultToolkit().getScreenResolution());
	}
	
	public static Border titled(String text) {
		TitledBorder tb = BorderFactory.createTitledBorder(new LineBorder(LINE), " " + text + " ");
		tb.setTitleFont(font(Font.BOLD, 14));
		tb.setTitleColor(DEEP);
		return BorderFactory.createCompoundBorder(tb, new EmptyBorder(6, 12, 8, 12));
	}
	
	public static class Change implements DocumentListener {
		final Runnable r;
		public Change(Runnable r) {
			this.r = r;
		}
		public void insertUpd(DocumentEvent e) {
			r.run();
		}
		public void removeUpd(DocumentEvent e) {
			r.run();
		}
		public void changedUpd(DocumentEvent e) {
			r.run();
		}
		
		public static Icon img(String file, int size) {
			String key = file + "@" + size;
			Icon ic = iconCache.get(key);
			if (ic == null)	{
				ic = loadImg(file, size);
				iconCache.put(key, ic);
			}
			
		static Icon loadImg(String f, int size) {
			if (f != null && !f.isEmpty()) {
				File file = new File("covers", f);
				if (!file.exists()) 
					file = new File(f);
				if (file.exists()) {
					try {
						BufferedImage bi = ImageIO.read(file);
						if (bi != null) {
							int s = Math.min(bi.getWidth(), bi.getHeight());
							BufferedImage sq = bi.getSubimage((bi.getWidth() - s) / 2, (bi.getHeight() - s) / 2, s, s);
	                        return new ImageIcon(sq.getScaledInstance(size, size, Image.SCALE_SMOOTH));
						}
					} catch (Exception e) {
						}
					}
				}
	        BufferedImage ph = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = ph.createGraphics();
	        g.setColor(SKY);
	        g.fillRect(0, 0, size, size);
	        g.setColor(DEEP);
	        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(10, size / 3)));
	        FontMetrics fm = g.getFontMetrics();
	        String note = "\u266A";
	        g.drawString(note, (size - fm.stringWidth(note)) / 2, (size + fm.getAscent() - fm.getDescent()) / 2);
	        g.dispose();
	        return new ImageIcon(ph);
		}
		
	}
}