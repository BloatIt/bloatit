package com.bloatit.framework.scgiserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.bloatit.framework.utils.ConfigurationManager;

/**
 * <p>
 * A class that represents a part of a mime multipart/form-data
 * </p>
 */
public class MimeElement {
    private final static String UPLOAD_TEMP_DIRECTORY = ConfigurationManager.SHARE_DIR + "uploads_temp/";
    private final static String DEFAULT_CONTENT_TYPE = "text/plain";

    /**
     * The stream used to write content
     */
    private OutputStream content;

    /**
     * The pairs reprsenting the header of the mime
     */
    private final Map<String, String> header;

    /**
     * The file where the data is saved (when it's a file)
     */
    private File destination;

    /**
     * Creates a new empty mime element
     */
    protected MimeElement() {
        content = new ByteArrayOutputStream();
        header = new HashMap<String, String>();
    }

    /**
     * <p>
     * A a new header to the element
     * </p>
     * <p>
     * If the header indicates the content is a file, a new file is created to
     * dump content into it. If content had previoulsy been added, it will be
     * beforehand pushed into the new file
     * </p>
     *
     * @param key
     *            the name of the header
     * @param value
     *            the value of the header
     * @throws IOException
     *             when the header indicates a file is contained in the mime,
     *             and the file where this content will be written can't be
     *             created/Accessed
     */
    protected void addHeader(String key, String value) throws IOException {
        header.put(key, value);
        if (isFile()) {
            File uploadedFileDir = new File(UPLOAD_TEMP_DIRECTORY);
            uploadedFileDir.mkdirs();

            final UUID uuid = UUID.randomUUID();
            destination = new File(UPLOAD_TEMP_DIRECTORY + uuid.toString());
            FileOutputStream fos = new FileOutputStream(destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 4096);
            if (content.getClass() == ByteArrayOutputStream.class && ((ByteArrayOutputStream) content).size() > 0) {
                bos.write(((ByteArrayOutputStream) content).toByteArray());
            }
            content = bos;
        }
    }

    /**
     * adds a new byte of content to the mime
     */
    protected void addContent(byte b) throws IOException {
        content.write(b);
    }

    /**
     * <p>
     * Return a Stream allowing to read the content of the mime
     * </p>
     *
     * @return the stream to read the content
     * @throws FileNotFoundException
     *             if the content is a file, and the file where it's stored is
     *             not accessible
     */
    public InputStream getContent() throws FileNotFoundException {
        if (destination == null) {
            return new ByteArrayInputStream(((ByteArrayOutputStream) content).toByteArray());
        }
        return new FileInputStream(destination);
    }

    /**
     * <p>
     * Gets the map containing the header
     * </p>
     *
     * @return a map containing <code>key->value</code> for each header element
     */
    public Map<String, String> getHeader() {
        return header;
    }

    /**
     * Finds the string content type for the mime
     *
     * @return
     */
    public String getContentType() {
        if (header.containsKey("Content-Type")) {
            return header.get("Content-Type");
        }
        return DEFAULT_CONTENT_TYPE;
    }

    /**
     * Indicates wether the mime is used to store a file or not
     *
     * @return <code>true</code> if the mime is used to store a file
     *         <code>false</code> otherwise
     */
    public boolean isFile() {
        return header.containsKey("filename");
    }

    /**
     * Finds a given header for the the mime
     *
     * @param key
     *            the name of the header field
     * @return the value of the header field
     */
    public String getHeaderField(String key) {
        return header.get(key);
    }

    @Override
    public String toString() {
        String result = "";

        for (Entry<String, String> headerField : header.entrySet()) {
            result += "[" + headerField.getKey() + "]: " + headerField.getValue() + "\n";
        }
        if (isFile()) {
            result += "[FILE]: " + destination.getAbsolutePath() + "\n";
        } else {
            // try {
            // while (this.getContent().available() > 0) {
            // char c = (char) ((byte) getContent().read() & 0xff);
            // result += c;
            // }
            // System.out.print('\n');
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
        }

        return result;
    }

    public void close() throws IOException {
        content.close();
    }
}
