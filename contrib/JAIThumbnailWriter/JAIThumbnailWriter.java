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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.sun.media.jai.codec.FileSeekableStream;

import de.jwi.jgallery.IThumbnailWriter;

/**
 * @author Daniel Kottow
 * Source file created on 3.11.2004
 */

public class JAIThumbnailWriter
implements IThumbnailWriter, Serializable
{
    public void write(File infile, File outfile, float compressionQuality,
            int thumbBounds) throws IOException
    {


		/* Create an operator to decode the image file. */
        RenderedOp image = JAI.create("stream", new FileSeekableStream(infile));

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        float thumbRatio = (float) thumbBounds
                / Math.max(imageWidth, imageHeight);

			 /*
              * Create a standard bilinear interpolation object to be
              * used with the "scale" operator.
              */
			  
             Interpolation interp = Interpolation.getInstance(
                                        Interpolation.INTERP_BILINEAR);

             /**
              * Stores the required input source and parameters in a
              * ParameterBlock to be sent to the operation registry,
              * and eventually to the "scale" operator.
              */
			  
             ParameterBlock params = new ParameterBlock();
             params.addSource(image);
             params.add(thumbRatio);         // x scale factor
             params.add(thumbRatio);         // y scale factor
             params.add(0.0F);         // x translate
             params.add(0.0F);         // y translate
             params.add(interp);       // interpolation method

             /* Create an operator to scale image1. */
             RenderedOp thumbImage = JAI.create("scale", params);


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
