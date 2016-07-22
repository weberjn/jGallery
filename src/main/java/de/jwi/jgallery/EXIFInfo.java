package de.jwi.jgallery;

import java.io.IOException;

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

import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 * @author Jürgen Weber
 * Source file created on 29.02.2004
 *  
 */
public class EXIFInfo implements Serializable
{

    
    // EXIF Data

    private String imageWidth; //	Width of image in Pixels

    private String imageHeight; //	Height of image in pixels

    private String originalWidth; //	Width of original image in pixels

    private String originalHeight; //	Height of original image in pixels

    private String originalWidthDpi; //

    private String originalHeightDpi; //

    private String formatName; // Type of image

    private String compressionLevel; //	

    private String originalDate; //	Date written by camera

    private String resolution; // Original image resolution as written by camera

    private String flash; //	If flash was used

    private String focalLength; //	Focal length

    private String exposureTime;

    private String isoEquivalent;

    private String aperture = null; //	Aperture

    private String focusDistance;

    private String meteringMode;

    private String cameraMake;

    private String cameraModel; //	Camera model

    private String comment; //	Comment.

    private static double ROOTTWO = Math.sqrt(2);

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    DecimalFormat decimalFormatter = new DecimalFormat("0.#");

    static
    {
        numberFormat.setMaximumFractionDigits(1);
    }

    EXIFInfo(InputStream jpeg) throws GalleryException
    {
        try
        {
            setEXIFVariables(jpeg);
        }
        catch (IOException e)
        {
            throw new GalleryException(e.getMessage());
        }
        catch (JpegProcessingException e)
        {
            throw new GalleryException(e.getMessage());
        }
    }

    public String getFlash()
    {
        return flash;
    }

    private void setFlash(String flash)
    {
        this.flash = flash;
    }

    public String getImageHeight()
    {
        return imageHeight;
    }

    public String getImageWidth()
    {
        return imageWidth;
    }

    public String getComment()
    {
        return comment;
    }

    public String getAperture()
    {
        return aperture;
    }

    private void setAperture(String aperture)
    {
        this.aperture = aperture;
    }    
    
 /*   private void setAperture(float apertureApex)
    {
        double fStop = Math.pow(ROOTTWO, apertureApex);
        this.aperture = decimalFormatter.format(fStop);
    }
*/
    public String getCameraMake()
    {
        return cameraMake;
    }

    private void setCameraMake(String cameraMake)
    {
        this.cameraMake = cameraMake;
    }

    public String getCameraModel()
    {
        return cameraModel;
    }

    private void setCameraModel(String cameraModel)
    {
        this.cameraModel = cameraModel;
    }

    public String getCompressionLevel()
    {
        return compressionLevel;
    }

    private void setCompressionLevel(String compressionLevel)
    {
        this.compressionLevel = compressionLevel;
    }

    public String getExposureTime()
    {
        return exposureTime;
    }

    private void setExposureTime(String exposureTime)
    {
        this.exposureTime = exposureTime;
    }

    public String getFocalLength()
    {
        return focalLength;
    }

    private void setFocalLength(String focalLength)
    {
        this.focalLength = focalLength;
    }

    public String getFocusDistance()
    {
        return focusDistance;
    }

    private void setFocusDistance(String focusDistance)
    {
        this.focusDistance = focusDistance;
    }

    public String getFormatName()
    {
        return formatName;
    }

    private void setFormatName(String formatName)
    {
        this.formatName = formatName;
    }

    public String getIsoEquivalent()
    {
        return isoEquivalent;
    }

    private void setIsoEquivalent(String isoEquivalent)
    {
        this.isoEquivalent = isoEquivalent;
    }

    public String getMeteringMode()
    {
    	return meteringMode;
    }

    private void setMeteringMode(String meteringMode)
    {
        this.meteringMode = meteringMode;
    }

    public String getOriginalDate()
    {
        return originalDate;
    }

    private void setOriginalDate(String originalDate)
    {
        this.originalDate = originalDate;
    }

    public String getOriginalHeight()
    {
        return originalHeight;
    }

    private void setOriginalHeight(String originalHeight)
    {
        this.originalHeight = originalHeight;
    }

    public String getOriginalHeightDpi()
    {
        return originalHeightDpi;
    }

    private void setOriginalHeightDpi(String originalHeightDpi)
    {
        this.originalHeightDpi = originalHeightDpi;
    }

    public String getOriginalWidth()
    {
        return originalWidth;
    }

    private void setOriginalWidth(String originalWidth)
    {
        this.originalWidth = originalWidth;
    }

    public String getOriginalWidthDpi()
    {
        return originalWidthDpi;
    }

    private void setOriginalWidthDpi(String originalWidthDpi)
    {
        this.originalWidthDpi = originalWidthDpi;
    }

    public String getResolution()
    {
        return resolution;
    }

    private void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    private void setComment(String comment)
    {
        if ("ASCII".equals(comment))
        {
            comment = "";
            // Bug in metadata-extractor ?
        }
        else
        {
            this.comment = comment;
        }
    }

    private void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    private void setImageWidth(String imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    private void setEXIFVariables(InputStream jpeg) throws JpegProcessingException, IOException
    {
        Metadata metadata = JpegMetadataReader.readMetadata(jpeg);

        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        setOriginalWidth(directory.getString(ExifSubIFDDirectory.TAG_IMAGE_WIDTH));
        
        setOriginalHeight(directory.getString(ExifSubIFDDirectory.TAG_IMAGE_HEIGHT));

        setCompressionLevel(directory.getString(ExifSubIFDDirectory.TAG_COMPRESSION));
        
        setOriginalDate(directory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));

        setResolution(directory.getString(ExifSubIFDDirectory.TAG_X_RESOLUTION));
        
        setFlash(directory.getString(ExifSubIFDDirectory.TAG_FLASH));
        
        setFocalLength(directory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));

        setIsoEquivalent(directory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));

        setExposureTime(directory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));

        setAperture(directory.getString(ExifSubIFDDirectory.TAG_APERTURE));

        setFocusDistance(directory.getString(ExifSubIFDDirectory.TAG_SUBJECT_DISTANCE));

        setMeteringMode(directory.getString(ExifSubIFDDirectory.TAG_METERING_MODE));

        setCameraMake(directory.getString(ExifSubIFDDirectory.TAG_MAKE));

        setCameraModel(directory.getString(ExifSubIFDDirectory.TAG_MODEL));

        setComment(directory.getString(ExifSubIFDDirectory.TAG_USER_COMMENT));

    }

}
