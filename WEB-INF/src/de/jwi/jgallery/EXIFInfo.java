package de.jwi.jgallery;

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
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

/**
 * @author Jürgen Weber
 * Source file created on 29.02.2004
 *  
 */
public class EXIFInfo implements Serializable
{

    private static String[] exposureMeteringMethods = { "unknown", // 0
            "average", // 1
            "center weighted average", // 2
            "spot", // 3
            "multi-spot", // 4
            "multi-segment", // 5
            "partial" // 6
    };

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

    private int meteringMode;

    private String cameraMake;

    private String cameraModel; //	Camera model

    private String sensorType; // Camera sensor type

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
        catch (MetadataException e)
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

    private void setAperture(float apertureApex)
    {
        double fStop = Math.pow(ROOTTWO, apertureApex);
        this.aperture = decimalFormatter.format(fStop);
    }

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
        if ((meteringMode > 0) && (meteringMode < 6))
        {
            return exposureMeteringMethods[meteringMode];
        }
        else
        {
            return "other";
        }
    }

    private void setMeteringMode(int meteringMode)
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

    public String getSensorType()
    {
        return sensorType;
    }

    private void setSensorType(String sensorType)
    {
        this.sensorType = sensorType;
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

    private void setEXIFVariables(InputStream jpeg) throws MetadataException,
            JpegProcessingException
    {
        Metadata metadata;
        Directory exifDirectory;

        
        metadata = JpegMetadataReader.readMetadata(jpeg);

        exifDirectory = metadata.getDirectory(ExifDirectory.class);

        setOriginalWidth(exifDirectory
                .getString(ExifDirectory.TAG_EXIF_IMAGE_WIDTH)); //TODO
        setOriginalHeight(exifDirectory
                .getString(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT)); //TODO

        setCompressionLevel(exifDirectory
                .getString(ExifDirectory.TAG_COMPRESSION_LEVEL));
        setOriginalDate(exifDirectory
                .getString(ExifDirectory.TAG_DATETIME_ORIGINAL));

        setResolution(exifDirectory.getString(ExifDirectory.TAG_X_RESOLUTION));
        
        setFlash(exifDirectory.getString(ExifDirectory.TAG_FLASH));

        setFocalLength(exifDirectory.getString(ExifDirectory.TAG_FOCAL_LENGTH));

        setIsoEquivalent(exifDirectory
                .getString(ExifDirectory.TAG_ISO_EQUIVALENT));

        setExposureTime(exifDirectory
                .getString(ExifDirectory.TAG_EXPOSURE_TIME));

        setAperture(exifDirectory.getFloat(ExifDirectory.TAG_APERTURE));

        setFocusDistance(exifDirectory
                .getString(ExifDirectory.TAG_SUBJECT_DISTANCE));

        setMeteringMode(exifDirectory.getInt(ExifDirectory.TAG_METERING_MODE));

        setCameraMake(exifDirectory.getString(ExifDirectory.TAG_MAKE));

        setCameraModel(exifDirectory.getString(ExifDirectory.TAG_MODEL));

        setSensorType(exifDirectory.getString(ExifDirectory.TAG_SENSING_METHOD));

        setComment(exifDirectory.getString(ExifDirectory.TAG_USER_COMMENT));

    }

}
