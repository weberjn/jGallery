package de.jwi.jgallery.imageio;

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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * @author Jürgen Weber
 * Source file created on 29.02.2004
 */
public class ImageIOThumbnailWriter
implements IThumbnailWriter, Serializable
{
    public void write(File infile, File outfile, float compressionQuality,
            int thumbBounds) throws IOException
    {
        // Retrieve jpg image to be resized
        Image image = ImageIO.read(infile);

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        float thumbRatio = (float) thumbBounds
                / Math.max(imageWidth, imageHeight);

        int thumbWidth = (int) (imageWidth * thumbRatio);
        int thumbHeight = (int) (imageHeight * thumbRatio);

        // draw original image to thumbnail image object and
        // scale it to the new size on-the-fly
        BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
        if (iter.hasNext())
        {
            writer = (ImageWriter) iter.next();
        }

        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(compressionQuality);

        // Prepare output file
        ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
        writer.setOutput(ios);

        writer.write(thumbImage);

        // Cleanup
        ios.flush();
        writer.dispose();
        ios.close();
    }

}
