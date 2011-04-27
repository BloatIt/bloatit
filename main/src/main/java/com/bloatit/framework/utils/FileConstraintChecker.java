package com.bloatit.framework.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.bloatit.common.Log;
import com.bloatit.framework.webprocessor.context.Context;

/**
 * A class that can check various constraints on files
 */
public class FileConstraintChecker {
    private final File file;
    private Boolean isImage;
    private BufferedImage image;

    public enum SizeUnit {
        BYTE(1), //
        KBYTE(1024), //
        MBYTE(KBYTE.getSize() * KBYTE.getSize()), //
        GBYTE(MBYTE.getSize() * MBYTE.getSize());

        private long size;

        SizeUnit(final long size) {
            this.size = size;
        }

        protected long getSize() {
            return size;
        }
    }

    /**
     * Creates a new ConstraintCheck
     * 
     * @param file the file to check
     */
    private FileConstraintChecker(final File file) {
        this.file = file;
    }

    /**
     * Creates a new ConstraintCheck
     * 
     * @param url the <code>url</code> of the file to check
     */
    public FileConstraintChecker(final String url) {
        this(new File(url));
    }

    /**
     * @return <i>true</i> if file exists, <i>false</i> otherwise
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Check if the file is smaller than a given <code>length</code>
     * 
     * @param length the expected maximum <code>length</code> of the file
     * @param unit the unit of the <code>length</code>
     * @return <i>true</i> if the file is smaller than <code>length</code> *
     *         <code>unit</code>, <i>false</i> otherwise
     */
    public boolean isFileSmaller(final long length, final SizeUnit unit) {
        if (!exists()) {
            return false;
        }
        final long fileLength = file.length();
        if ((length * unit.getSize()) >= fileLength) {
            return true;
        }
        return false;
    }

    /**
     * @return <i>true</i> if the file is an image, <i>false</i> otherwise.
     */
    public boolean isImage() {
        if (isImage != null) {
            return isImage.booleanValue();
        }
        if (!file.exists()) {
            return false;
        }

        try {
            image = ImageIO.read(file);
            if (image != null) {
                isImage = true;
            } else {
                isImage = false;
            }
        } catch (final IOException e) {
            Log.framework().error("",e);
            isImage = false;
        }
        return isImage != null && isImage.booleanValue();
    }

    /**
     * Checks whether an image is smaller than and a given <code>width</code>
     * and <code>height</code>
     * 
     * @param width the maximum expected width of the image
     * @param height the maximum expected height of the image
     * @return <i>true</i> if image width is smaller than <code>width</code> and
     *         image height is smaller than <code>height</code>. <i>false</i>
     *         otherwise. Note if file is not an image, returns <i>false</i>.
     */
    private boolean isImageSmaller(final int width, final int height) {
        if (!file.exists()) {
            return false;
        }
        if (!isImage()) {
            return false;
        }
        if (image.getWidth() > width) {
            return false;
        }
        if (image.getHeight() > height) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether the image corresponds to the set of rules defined for an
     * avatar
     * 
     * @return a list of all the test that failed, or <i>null</i> if no test
     *         failed.
     */
    public List<String> isImageAvatar() {
        final ArrayList<String> failures = new ArrayList<String>();
        if (!exists()) {
            failures.add(Context.tr("Sorry we messed up and lost the file you uploaded. Please try again."));
            return failures;
        }
        if (!isImage()) {
            failures.add(Context.tr("Avatars can only be images. Please upload another file."));
            return failures;
        }
        if (!isFileSmaller(50, SizeUnit.KBYTE)) {
            failures.add(Context.tr("Avatars have to be smaller than 50Kb. Please upload another file."));
        }
        if (!isImageSmaller(64, 64)) {
            failures.add(Context.tr("Avatars have to be smaller than 64x64. Please upload another file."));
        }
        if (failures.size() > 0) {
            return failures;
        }
        return null;
    }
}
