/*
 * jGallery - Java web application to display image galleries
 * 
 * Copyright (C) 2004 Juergen Weber
 * 
 * This file is part of jGallery.
 * 
 * jGallery is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jGallery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jGallery; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 */

package de.jwi.jgallery.toolkit;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import de.jwi.jgallery.IThumbnailWriter;

/**
 * @author Juergen Weber Created on 28.07.2004
 *  
 */
public class ToolkitThumbnailWriter implements IThumbnailWriter, Serializable {

	public void write(File infile, File outfile, float compressionQuality, int thumbBounds) throws IOException {
		Image image = Toolkit.getDefaultToolkit().getImage(infile.toURL());
	    MediaTracker mediaTracker = new MediaTracker(new Container());
	    mediaTracker.addImage(image, 0);
	    try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException e) {
			return;
		}
		
	    int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        float thumbRatio = (float) thumbBounds
                / Math.max(imageWidth, imageHeight);

        int thumbWidth = (int) (imageWidth * thumbRatio);
        int thumbHeight = (int) (imageHeight * thumbRatio);

        // draw original image to thumbnail image object and
        // scale it to the new size on-the-fly
        BufferedImage thumbImage = new BufferedImage(thumbWidth, 
          thumbHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
        // save thumbnail image to OUTFILE
        
        BufferedOutputStream out = new BufferedOutputStream(new
          FileOutputStream(outfile));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.
          getDefaultJPEGEncodeParam(thumbImage);

        param.setQuality(compressionQuality, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(thumbImage);	    
	    
        out.close();
	}

}