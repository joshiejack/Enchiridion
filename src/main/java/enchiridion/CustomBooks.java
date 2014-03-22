package enchiridion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import enchiridion.api.Formatting;
import enchiridion.api.GuideHandler;
import enchiridion.api.StackHelper;
import enchiridion.api.XMLHelper;
import enchiridion.api.pages.PageImage;
import enchiridion.api.pages.PageImage.LinkedTexture;

public class CustomBooks {
	public static final String id = "booksid";
	public static final ArrayList<String> onWorldStart = new ArrayList();
	public static final HashMap<String, BookInfo> bookInfo = new HashMap();

	public static class BookInfo {
		ItemStack onCrafting;
		ItemStack[] crafting;
		String author, displayName;
		public Integer bookColor;
		public String background;

		public BookInfo(String displayName, String author, Integer bookColor) {
			this.displayName = displayName;
			this.author = author;
			this.bookColor = bookColor;
		}
	}

	public static void preInit() {
		File folder = Enchiridion.root;
		if (!folder.exists()) {
			folder.mkdir();
		}

		for (File file : folder.listFiles()) {
			String zipName = file.getName();
			// Continue if the file i a zip
			if (zipName.substring(zipName.lastIndexOf(".") + 1, zipName.length()).equals("zip")) {
				BookLogHandler.log(Level.INFO, "[Enchiridion] Attempting to read data for the installed Guide Book: " + zipName);
				try {
					ZipFile zipfile = new ZipFile(file);
					Enumeration enumeration = zipfile.entries();
					while (enumeration.hasMoreElements()) {
						ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
						String fileName = zipentry.getName();
						String extension = fileName.substring(fileName.length() - 3, fileName.length());
						if (!zipentry.isDirectory()) {
							if (FMLClientHandler.instance() != null && extension.equals("png")) {
								try {
									String id = fileName.substring(0, fileName.lastIndexOf('.'));
									BufferedImage img = ImageIO.read(zipfile.getInputStream(zipentry));
									DynamicTexture dmTexture = new DynamicTexture(img);
									ResourceLocation texture = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(id, dmTexture);
									LinkedTexture linked = new LinkedTexture(img.getHeight(), img.getWidth(), dmTexture, texture);
									String identifier = (zipName.substring(0, zipName.length() - 4) + "|" + fileName.substring(0, fileName.length() - 4));
									PageImage.addToCache(identifier, linked);
								} catch (Exception e) {
									e.printStackTrace();
									BookLogHandler.log(Level.WARNING, "[Enchiridion] Failed to Read Image Data of " + fileName);
								}
							} else if (extension.equals("xml")) {
								try {
									String id = fileName.substring(0, fileName.lastIndexOf('.'));
									DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
									DocumentBuilder build = factory.newDocumentBuilder();
									Document doc = build.parse(zipfile.getInputStream(zipentry));
									doc.getDocumentElement().normalize();
									ClientProxy.bookCache.put(id, doc);
								} catch (Exception e) {
									e.printStackTrace();
									BookLogHandler.log(Level.WARNING, "[Enchiridion] Failed to Read XML Data of " + fileName);
								}
							}
						}
					}

					zipfile.close();
					FMLLog.getLogger().log(Level.INFO, "[Enchiridion] Sucessfully finished reading the installed Guide Book: " + zipName);
				} catch (Exception e) {
					BookLogHandler.log(Level.WARNING, "[Enchiridion] Failed to read the installed Guide Book: " + zipName);
				}
			}
		}

		if (GuideHandler.DEBUG_ENABLED) {
			File debugFolder = new File(Enchiridion.root + File.separator + "debug");

			if (!debugFolder.exists()) {
				debugFolder.mkdir();
			}

			for (File file : debugFolder.listFiles()) {
				String xmlName = file.getName();
				if (xmlName.substring(xmlName.lastIndexOf(".") + 1, xmlName.length()).equals("xml")) {
					try {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder build = factory.newDocumentBuilder();
						Document doc = build.parse(file);
						doc.getDocumentElement().normalize();
						ClientProxy.bookCache.put(xmlName.substring(0, xmlName.lastIndexOf('.')), doc);
						FMLLog.getLogger().log(Level.INFO, "[Enchiridion] Sucessfully loaded debug mode custom book xml " + xmlName);
					} catch (Exception e) {
						BookLogHandler.log(Level.WARNING, "[Enchiridion] Failed to load debug mode custom book xml " + xmlName);
					}
				}
			}
		}
	}

	public static void setup(String key, Element xml) {
		String displayName = XMLHelper.getElement(xml, "name");
		String author = XMLHelper.getElement(xml, "author");
		Integer color = XMLHelper.getElementAsHex(xml, "color", 0xFFFFFF);
		BookInfo info = new BookInfo(displayName, author, color);
		info.displayName = Formatting.getColor(XMLHelper.getAttribute(XMLHelper.getNode(xml, "name"), "color")) + info.displayName;
		info.author = Formatting.getColor(XMLHelper.getAttribute(XMLHelper.getNode(xml, "author"), "color")) + info.author;
		if (XMLHelper.getAttribAsBoolean(xml, "gen")) onWorldStart.add(key);
		info.background = XMLHelper.getElement(xml, "background");
		String onCrafting = XMLHelper.getElement(xml, "onCrafting");
		if (onCrafting != null && !onCrafting.equals("")) {
			ItemStack stack = StackHelper.getStackFromString(onCrafting);
		}
		
		bookInfo.put(key, info);
	}

	public static Document getDebugMode(String xml) {
		File debugFolder = new File(Enchiridion.root + File.separator + "debug");
		String file = debugFolder + File.separator + xml + ".xml";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = factory.newDocumentBuilder();
			Document doc = build.parse(file);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getID(ItemStack stack) {
		return stack.stackTagCompound.getString(id);
	}
	
	public static BookInfo getBookInfo(ItemStack stack) {
		return bookInfo.get(getID(stack));
	}
}
