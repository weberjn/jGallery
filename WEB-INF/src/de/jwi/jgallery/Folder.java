
package de.jwi.jgallery;

/*
 * jGallery - Java web application to display image galleries
 * 
 * Copyright (C) 2004 Juergen Weber
 * 
 * This file is part of jGallery.
 * 
 * jGallery is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * jGallery is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with jGallery; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston
 */

import imageinfo.ImageInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletContext;

import de.jwi.jgallery.db.DBManager;
import de.jwi.jgallery.imageio.ImageIOThumbnailWriter;
import de.jwi.jgallery.toolkit.ToolkitThumbnailWriter;

/**
 * @author Juergen Weber Source file created on 17.02.2004
 *  
 */
public class Folder implements FilenameFilter, Serializable
{

	private static final String FOLDER_KEY = "folder";

	private static final String CAPTIONSFILE = "captions.txt";

	private static final String JGALLERYIGNOREFILE = ".jGalleryIgnore";

	private ConfigData configData;

	private static final String GENERATORURL = "http://www.jwi.de/jgallery/";


	private String thumbsdir = "thumbs";

	private float thumbQuality = 0.98f;

	private int thumbSize = 150;

	private boolean isDirectoryParsed = false;

	private boolean isProcessSubdirectories = true;

	private boolean isCreateThumbs = true;

	private IThumbnailWriter thumbnailWriter = null;

	private File directory;

	private Configuration configuration;

	protected String[] imageFiles;

	private Properties captions = new Properties();

	private int[] imageCounters;

	private int folderCounter = -1;

	String[] subDirectories;

	private String iconWidth;

	private String iconHeight;

	protected String folderPath;

	protected String imagePath;

	protected String jgalleryContextPath;

	private String parentIndexPage;
	private String parentlink;

	private Hashtable images = new Hashtable();

	private Image[] imagesArray;

	private Image image; // the current image

	private int imageNum; // Number of the current image

	private String skin = "Standard" ; // Name (and folder) of skin

	private String indexJsp;

	private String slideJsp;

	private String resPath;

	private String resResourcePath;

	private String stylePath;

	private String style = "Black";

	private int level;

	private int cols = 2; // 	Number of image columns on index pages

	private int currentCols;

	private int currentRows;

	private int rows = 3; // 	Max number of image rows on index pages

	private boolean isShowImageNum = true;

	private boolean isShowDates = true;

	private int totalIndexes; //	Total number of index pages

	private int totalAlbumImages; //	Total number of images in an album

	// (subdirectory images included)
	private int totalImages; //	Total number of images in a directory

	private int indexNum; //  	The number of the current index page

	private String albumPath;

	private String title;

	private HashMap variables = new HashMap();

	private DBManager dBManager;

	private ServletContext appContext;

	public Folder(File directory, ServletContext appContext,
			Configuration configuration, ConfigData configData,
			String jgalleryContextPath, String folderPath, String imagePath,
			DBManager dBManager) throws GalleryException
	{
		this.directory = directory;

		this.appContext = appContext;
		this.configData = configData;

		this.configuration = configuration;

		this.jgalleryContextPath = jgalleryContextPath;
		this.folderPath = folderPath;
		this.imagePath = imagePath;

		this.dBManager = dBManager;

		readConfiguration();

		thumbnailWriter = configuration.getThumbnailWriter();

	}

	public HashMap getVariables()
	{
		return variables;
	}

	private int sortingOrder = ImageComparator.SORTNONE;

	private void readConfiguration() throws GalleryException
	{
		cols = configuration.getInt("index.columns",cols);
		rows = configuration.getInt("index.rows", rows);

		skin = configuration.getString("skin",skin);
		indexJsp = "/skins/" + skin + "/index.jsp";
		slideJsp = "/skins/" + skin + "/slide.jsp";

		style = configuration.getString("style", style);

		String s = configuration.getString("sortingOrder");
		if ("filedate".equals(s))
		{
			sortingOrder = ImageComparator.SORTBYFILEDATE;
		}
		else if ("exifdate".equals(s))
		{
			sortingOrder = ImageComparator.SORTBYEXIFDATE;
		}
		else if ("name".equals(s))
		{
			sortingOrder = ImageComparator.SORTBYNAME;
		}
		else if ("none".equals(s))
		{
			sortingOrder = ImageComparator.SORTNONE;
		}
		else 
		{
			sortingOrder = ImageComparator.SORTNONE;
		}		

		isShowImageNum = configuration.getBoolean("showImageNum", isShowImageNum);

		thumbsdir = configuration.getString("thumbnails.dir",thumbsdir);

		isCreateThumbs = configuration.getBoolean("thumbnails.create",isCreateThumbs);

		thumbSize = configuration.getInt("thumbnails.size", thumbSize);
		thumbQuality = configuration.getFloat("thumbnails.quality",thumbQuality);


		// parentlink.galleries=
		
		resResourcePath = "/skins/" + skin + "/res";
		resPath = jgalleryContextPath + "/skins/" + skin + "/res";
		stylePath = jgalleryContextPath + "/skins/" + skin + "/styles/" + style
				+ ".css";

		s = configuration.getString("parentlink");
		if (s==null)
		{
			String s1=folderPath.substring(1,folderPath.indexOf('/',1));
			s = configuration.getString("parentlink."+s1);
		}
		
		if (s!=null)
		{
			parentlink = s;
		}
		
		setIconDimensions();

		configuration.getUserVariables(variables);
	}


	private void setIconDimensions()
	{
		InputStream is = appContext.getResourceAsStream(resResourcePath
				+ "/folder.gif");
		if (null != is)
		{

			ImageInfo ii = new ImageInfo();
			ii.setInput(is);
			if (ii.check())
			{
				iconWidth = Integer.toString(ii.getWidth());
				iconHeight = Integer.toString(ii.getHeight());
			}
		}
	}

	File getDirectory()
	{
		return directory;
	}

	public String getUrlExtention()
	{
		return configData.urlExtention;
	}

	public String getShowDates()
	{
		return Boolean.toString(isShowDates);
	}

	public String getShowImageNum()
	{
		return Boolean.toString(isShowImageNum);
	}

	public String getComment()
	{
		String s = captions.getProperty(FOLDER_KEY);
		return (s == null) ? "" : s;
	}

	public String getCopyright()
	{
		return "(c) Jürgen Weber";
	}

	public String getIndexJsp()
	{
		return indexJsp;
	}

	public String getSlideJsp()
	{
		return slideJsp;
	}

	public Image getImage()
	{
		return image;
	}

	protected IImageAccessor makeImageAccessor(String name)
	{
		return new FileImageAccessor(name, this);
	}

	public Image getSubDirOrImage(int n) throws GalleryException
	{
		Image image;
		if (isProcessSubdirectories)
		{
			if (n > subDirectories.length)
			{
				// get an image
				image = getImage(n - subDirectories.length);
			}
			else
			{
				// get a subfolder representation
				image = new Image(subDirectories[n - 1], true, this,
						makeImageAccessor(subDirectories[n - 1]));
			}
		}
		else
		{
			image = getImage(n);
		}
		return image;
	}

	// lazy load Image n (1..)
	public Image getImage(int n) throws GalleryException
	{
		if (null == imagesArray[n - 1])
		{
			// create the thumb first, as it is needed in the Image constructor
			checkAndCreateThumb(n - 1);
			imagesArray[n - 1] = new Image(imageFiles[n - 1], false, this,
					makeImageAccessor(imageFiles[n - 1]));
			String s = captions.getProperty(imageFiles[n - 1]);
			if (s != null)
			{
				imagesArray[n - 1].setComment(s);
			}
		}
		return imagesArray[n - 1];
	}

	// n = 0..
	private void checkAndCreateThumb(int n) throws GalleryException
	{
		if (!isCreateThumbs)
		{
			return;
		}

		File thumbsDir = new File(directory, thumbsdir);

		if (!thumbsDir.exists())
		{
			if (!thumbsDir.mkdir())
			{
				throw new GalleryException(
						"Could not create thumbnail directory: " + thumbsDir);
			}
		}

		File thumb = new File(directory, thumbsdir + "/" + imageFiles[n]);
		File original = new File(directory, imageFiles[n]);
		long l1, l2;

		if (thumb.exists()
				&& ((l1 = thumb.lastModified()) > (l2 = original.lastModified())))
		{
			return;
		}

		// if thumb is older than image, then recreate thumb

		try
		{
			thumbnailWriter.write(original, thumb, thumbQuality, thumbSize);
		}
		catch (IOException e)
		{
			throw new GalleryException("Error creating thumbnail" + thumb
					+ " :" + e.getMessage());
		}
	}

	public String getHTMLBase()
	{
		return jgalleryContextPath + folderPath;
	}

	public String toString()
	{
		return getHTMLBase();
	}

	private String getImageHTMLBase(int imageNum)
	{
		String image = imageFiles[imageNum - 1];
		return getHTMLBase() + image.substring(0, image.indexOf('.'));
	}

	private void calculateVariables()
	{
		totalAlbumImages = totalImages = imageFiles.length;

		totalIndexes = totalImages / (cols * rows);
		if (totalImages % (cols * rows) > 0)
		{
			totalIndexes++;
		}

	}

	/**
	 * @return Number of current Index Page (1...)
	 */
	private int calculateIndexNum(int imageNum)
	{
		int n;
		int i = imageNum + subDirectories.length;

		int maxImagesPerIndex = cols * rows;

		if (maxImagesPerIndex > 1)
		{
			n = i / maxImagesPerIndex;
			if ((i % maxImagesPerIndex) != 0)
			{
				n++;
			}
		}
		else
		{
			n = i;
		}
		return n;
	}

	public String getClassName()
	{
		return getClass().getName();
	}

	/**
	 * @return Number of image columns on index pages
	 */
	public String getCols()
	{
		return Integer.toString(cols);
	}

	public int getColsI()
	{
		return cols;
	}

	/**
	 * @return Number of image rows on current index page
	 */
	public String getCurrentRows()
	{
		return Integer.toString(currentRows);
	}

	/**
	 * @return Name and version of jGallery
	 */
	public String getGenerator()
	{
		return "jGallery " + getInternalVersion();
	}

	public String getGeneratorurl()
	{
		return GENERATORURL;
	}

	/**
	 * @return Number of the current image within a slide
	 */
	public String getImageNum()
	{
		return Integer.toString(imageNum);
	}

	public int getImageNumI()
	{
		return imageNum;
	}

	/**
	 * @return Number of images on current index page
	 */
	public String getIndexImageCount()
	{
		return Integer.toString(20);
	}

	/**
	 * @return The number of the current index page
	 */
	public String getIndexNum()
	{
		return Integer.toString(indexNum);
	}

	/**
	 * @return Internal version number
	 */
	public String getInternalVersion()
	{
		return configData.version;
	}

	/**
	 * @return Level of album directory (0 meaning root level)
	 */
	public String getLevel()
	{
		return Integer.toString(level);
	}

	/**
	 * @return Max image width as set by user
	 */
	public String getMaxImageWidth()
	{
		String s = (String) variables.get("maxImageWidth");
		return s != null ? s : "";
	}

	/**
	 * @return Max image height as set by user
	 */
	public String getMaxImageHeight()
	{
		String s = (String) variables.get("maxImageHeight");
		return s != null ? s : "";
	}

	/**
	 * @return Max thumbnail width as set by user
	 */
	public String getMaxThumbWidth()
	{
		String s = (String) variables.get("maxThumbWidth");
		return s != null ? s : "";
	}

	/**
	 * @return Max thumbnail height as set by user
	 */
	public String getMaxThumbHeight()
	{
		String s = (String) variables.get("maxThumbHeight");
		return s != null ? s : "";
	}

	/**
	 * @return Max number of image rows on index pages
	 */
	public String getRows()
	{
		return Integer.toString(rows);
	}

	/**
	 * @return Name of current skin
	 */
	public String getSkin()
	{
		return skin;
	}

	/**
	 * @return Name of current style sheet
	 */
	public String getStyle()
	{
		return style;
	}

	/**
	 * @return Name of album directory
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @return Total number of index pages
	 */
	public String getTotalIndexes()
	{
		return totalIndexes > 1 ? Integer.toString(totalIndexes) : "";
	}

	/**
	 * @return Total number of images in an album (subdirectory images included)
	 */
	public String getTotalAlbumImages()
	{
		return Integer.toString(totalAlbumImages);
	}

	/**
	 * @return Total number of images in a directory
	 */
	public String getTotalImages()
	{
		return Integer.toString(totalImages);
	}

	/**
	 * @return Character set and encoding of generated pages and comments
	 */
	public String getTextEncoding()
	{
		return "UTF-8";
	}

	/**
	 * @return Define as user defined variable to explicitly set a language for
	 *         a multilingual skin (ISO two character language code)
	 */
	public String getLanguage()
	{
		return "en";
	}

	// 1 ..
	public String getIndexPage(int index)
	{
		if ((index < 1) || (index > totalIndexes))
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append(getHTMLBase()).append("index");
		if (index > 1)
		{
			sb.append(Integer.toString(index - 1));
		}
		sb.append(".").append(configData.urlExtention);

		return sb.toString();
	}

	/**
	 * @return Filename of index page for current slide + delta
	 */
	public String getIndexPage()
	{
		int i = calculateIndexNum(imageNum);

		return getIndexPage(i);
	}

	/**
	 * @return Filename of the first index page
	 */
	public String getFirstIndexPage()
	{
		return getIndexPage(1);
	}

	/**
	 * @return Filename of the last index page
	 */
	public String getLastIndexPage()
	{
		return getIndexPage(totalIndexes);
	}

	/**
	 * @return Filename of the previous index page
	 */
	public String getPreviousIndexPage()
	{
		int i = calculateIndexNum(imageNum);

		return getIndexPage(i - 1);
	}

	/**
	 * @return Filename of the next index page
	 */
	public String getNextIndexPage()
	{
		int i = calculateIndexNum(imageNum);

		return getIndexPage(i + 1);
	}

	/**
	 * @return Filename of parent index page
	 */
	public String getParentIndexPage()
	{
		return parentIndexPage;
	}

	/**
	 * used by Image.getIconPath();
	 */
	String getIconPath()
	{
		return getResPath() + "/folder.gif";
	}

	String getIconWidth()
	{
		return iconWidth;
	}

	String getIconHeight()
	{
		return iconHeight;
	}

	public String getThumbsdir()
	{
		return thumbsdir;
	}

	public String getFileContent(String fname)
			throws FileNotFoundException, IOException, GalleryException
	{
		File f = new File(directory, fname);
		StringBuffer sb = new StringBuffer();
		String s;

		if (f.getParentFile().getCanonicalPath().equals(
				directory.getCanonicalPath()))
		{
			// only allow to read in Folder's directory

			BufferedReader in = new BufferedReader(new FileReader(f));
			while ((s = in.readLine()) != null)
			{
				sb.append(s);
			}
		}
		else
		{
			throw new GalleryException("Will only read in current directory.");
		}

		return sb.toString();
	}

	/**
	 * @return Path to get back to the top of a multi directory level album
	 */
	public String getRootPath()
	{
		return folderPath;
	}

	/**
	 * @return Path to the image that is shown in slides
	 */
	/*
	 * TODO
	 * 
	 * public String getImagePath() { return imageNum > 0 ? folderPath +
	 * imageFiles[imageNum - 1] : ""; }
	 */
	/**
	 * @return Path to the selected style file
	 */
	public String getStylePath()
	{
		return stylePath;
	}

	/**
	 * @return Path to the "res" directory containing album resources (support
	 *         files like gif buttons etc)
	 */
	public String getResPath()
	{
		return resPath;
	}

	// 1..
	private String getSlidePage(int n)
	{
		return getImageHTMLBase(n) + "." + configData.urlExtention;
	}

	/**
	 * @return Filename of the first slide page
	 */
	public String getFirstPage()
	{
		return getSlidePage(1);
	}

	/**
	 * @return Filename of the last slide page
	 */
	public String getLastPage()
	{
		return getSlidePage(imageFiles.length + 1);
	}

	public Image getPrevious() throws GalleryException
	{
		image = imageNum > 1 ? getImage(imageNum - 1) : null;
		return image;
	}

	public Image getNext() throws GalleryException
	{
		image = imageNum < totalImages ? getImage(imageNum + 1) : null;
		return image;
	}

	/**
	 * @return Filename of the previous slide page
	 */
	public String getPreviousPage()
	{
		return imageNum > 1 ? getSlidePage(imageNum - 1) : "";
	}

	/**
	 * @return Filename of the current slide page
	 */
	public String getCurrentPage()
	{
		return getSlidePage(imageNum);
	}

	/**
	 * @return Filename of the next slide page
	 */
	public String getNextPage()
	{
		return imageNum < totalImages ? getSlidePage(imageNum + 1) : "";
	}

	public boolean accept(File dir, String name)
	{
		String s = name.toLowerCase();
		return s.endsWith(".jpg") | s.endsWith(".jpeg");
	}

	public static final int INDEX = 1, SLIDE = 2;

	protected String[] getSubDirectories()
	{
		File f = new File(directory, JGALLERYIGNOREFILE);
		if (f.exists())
		{
			return new String[0];
		}

		return directory.list(new DirectoriesFilter(getThumbsdir(),
				JGALLERYIGNOREFILE));
	}

	public int setFileName(String pathInfoFileName) throws GalleryException
	{
		String s = pathInfoFileName.startsWith("/") ? pathInfoFileName
				.substring(1) : pathInfoFileName;
		String s1;
		int n = 0;

		if (null == subDirectories)
		{
			subDirectories = getSubDirectories();
		}

		if (s.startsWith("index"))
		{
			//GalleryException

			if (s.equals("index." + configData.urlExtention))
			{
				indexNum = 1;
			}
			else
			{
				int i = 0;

				s1 = s.substring("index".length(), s.indexOf('.'));
				try
				{
					i = Integer.parseInt(s1);
				}
				catch (NumberFormatException e)
				{
					throw new GalleryNotFoundException("URL not found", e);
				}

				indexNum = 1 + i;

				if (indexNum > totalIndexes)
				{
					throw new GalleryNotFoundException("URL not found");
				}

			}

			int maxImagesPerIndex = cols * rows;

			imageNum = 1 + (indexNum - 1) * maxImagesPerIndex;

			if (indexNum < totalIndexes)
			{
				currentCols = cols;
				currentRows = rows;
			}
			else
			{
				int r = totalImages % maxImagesPerIndex;

				currentRows = r / cols;
				if (r % cols > 0)
					currentRows++;

				currentCols = Math.min(r, cols);
			}

			n = INDEX;
		}
		else
		// a slide page
		{
			s1 = s.substring(0, s.indexOf('.'));

			Integer theImage = (Integer) images.get(s1);
			if (null == theImage)
			{
				throw new GalleryNotFoundException("URL not found");
			}

			imageNum = theImage.intValue();
			indexNum = calculateIndexNum(imageNum);

			image = getImage(imageNum);

			n = SLIDE;
		}

		return n;
	}

	public void loadFolder() throws GalleryException
	{
		if (!isDirectoryParsed)
		{

			// "/testalbum/second/"

			String[] parts = folderPath.split("/");

			StringBuffer sb = new StringBuffer();

			if (parts.length > 2) // [0] is empty, [1..n-2] are parents, [n-1]
			// is current
			{
				for (int i = 1; i < parts.length - 1; i++)
				{
					level = i;
					sb.append(parts[i]);
					sb.append('/');
				}
				String parent = sb.toString();

				String hTMLBase = jgalleryContextPath;
				parentIndexPage = hTMLBase + "/" + parent + "index."
						+ configData.urlExtention;

				// Check, if parent directory is to be ignored

				File parentFile = directory.getParentFile();
				if (null != parentFile)
				{
					File f = new File(parentFile, JGALLERYIGNOREFILE);
					if (f.exists())
					{
						parentIndexPage = "";
					}
				}

			}
			if ("".equals(parentIndexPage))
			{
				parentIndexPage = parentlink!=null ? parentlink : ""; // set to non defined
			}

			File f = new File(directory, JGALLERYIGNOREFILE);
			if (f.exists())
			{
				imageFiles = new String[0];
			}
			else
			{
				imageFiles = directory.list(this);
			}

			f = new File(directory, CAPTIONSFILE);
			InputStream is = null;
			try
			{
				is = new FileInputStream(f);
				captions.load(is);
			}
			catch (FileNotFoundException e)
			{
				// ignore, no captions
			}
			catch (IOException e)
			{
				throw new GalleryException(e.getMessage(), e);
			}

			imageCounters = new int[imageFiles.length];
			Arrays.fill(imageCounters, -1);

			endLoad();
		}
	}

	protected void endLoad() throws GalleryException
	{
		imagesArray = new Image[imageFiles.length];

		// if sorting is wished for, need to load all images first
		// and sort them
		if (sortingOrder != ImageComparator.SORTNONE)
		{
			for (int i = 0; i < imageFiles.length; i++)
			{
				getImage(i + 1);
			}
			Comparator c = new ImageComparator(sortingOrder);

			Arrays.sort(imagesArray, c);

			for (int i = 0; i < imageFiles.length; i++)
			{
				imageFiles[i] = imagesArray[i].getFileName();
			}

		}

		for (int i = 0; i < imageFiles.length; i++)
		{
			images.put(imageFiles[i].substring(0, imageFiles[i].indexOf('.')),
					new Integer(i + 1));

		}
		calculateVariables();
		isDirectoryParsed = true;

		title = folderPath.substring(folderPath.indexOf('/') + 1);
		if (title.endsWith("/"))
		{
			title = title.substring(0, title.lastIndexOf('/'));
		}

	}

	public int getCurrentImagesPerPage()
	{
		if (indexNum < totalIndexes)
		{
			return rows * cols;
		}
		else
		{
			return totalImages + subDirectories.length - (indexNum - 1) * rows
					* cols;
		}
	}

	public String getCurrentCols()
	{
		return Integer.toString(currentCols);
	}

	public String getFolderPath()
	{
		return folderPath;
	}

	public String getImageBasePath()
	{
		return imagePath;
	}

	public String getThumbsPath()
	{
		return getImageBasePath() + thumbsdir;
	}

	public String getCounter()
	{
		if (folderCounter == -1)
		{
			if ((null != dBManager) && this.configData.doCount)
			{
				// increment counter only once per Session

				try
				{
					folderCounter = dBManager
							.getAndIncFolderCounter(folderPath);
				}
				catch (SQLException e)
				{
					appContext.log(e.getMessage(), e);
				}
			}
		}
		String s = folderCounter > -1 ? Integer.toString(folderCounter) : null;

		return s;
	}

	public String getImageCounter(String name)
	{
		// trigger first putting folder into DB
		getCounter();

		String s = name.substring(0, name.indexOf('.'));

		String rc = null;

		Integer theImage = (Integer) images.get(s);
		imageNum = theImage.intValue();

		int c = imageCounters[imageNum - 1];

		if ((null != dBManager) && this.configData.doCount)
		{
			// increment counter only once per Session

			if (c == -1)
			{
				try
				{
					c = dBManager.getAndIncImageCounter(folderPath,
							imageFiles[imageNum - 1]);

					imageCounters[imageNum - 1] = c;

					rc = Integer.toString(c);
				}
				catch (SQLException e)
				{
					appContext.log(e.getMessage(), e);
					rc = null;
				}
			}
			else
			{
				rc = Integer.toString(c);
			}
		}
		return rc;
	}
}