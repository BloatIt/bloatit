package com.bloatit.framework.xcgiserver.mime;

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

import com.bloatit.common.Log;
import com.bloatit.framework.xcgiserver.mime.decoders.MimeBase64Decoder;
import com.bloatit.framework.xcgiserver.mime.decoders.MimeBinaryDecoder;
import com.bloatit.framework.xcgiserver.mime.decoders.MimeDecoder;
import com.bloatit.framework.xcgiserver.mime.filenaming.FileNamingGenerator;

/**
 * <p>
 * A class that represents a part of a mime multipart/form-data
 * </p>
 */
public class MimeElement {
    private static final String DEFAULT_CONTENT_TYPE = "text/plain";
    private static final String FILE_NAME = "filename";
    private static final String CONTENT_ENCODING = "Content-Transfer-Encoding";
    private static final String DEFAULT_CONTENT_ENCODING = "binary";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final Object FIELD_NAME = "name";

    private static final Map<String, MimeDecoder> availableEncodings = new HashMap<String, MimeDecoder>() {
        private static final long serialVersionUID = 6626449431506692683L;
        {
            put("binary", new MimeBinaryDecoder());
            put("8bit", new MimeBinaryDecoder());
            put("base64", new MimeBase64Decoder());
        }
    };

    /**
     * The stream used to write content
     */
    private OutputStream contentOutput;

    private ByteArrayOutputStream nonFileInput;

    /**
     * The pairs reprsenting the header of the mime
     */
    private final Map<String, String> header;

    /**
     * The file where the data is saved (when it's a file)
     */
    private File destination;
    private MimeDecoder decoder;
    private final FileNamingGenerator nameGen;
    private String fileSavingDirectory;

    /**
     * Creates a new empty mime element
     * 
     * @param fileSavingDirectory
     */
    protected MimeElement(final FileNamingGenerator nameGen, final String fileSavingDirectory) {
        header = new HashMap<String, String>();
        this.nameGen = nameGen;
        if (fileSavingDirectory.endsWith("/")) {
            this.fileSavingDirectory = fileSavingDirectory;
        } else {
            this.fileSavingDirectory = fileSavingDirectory + "/";
        }

    }

    /**
     * <p>
     * Return a Stream allowing to read the content of the mime
     * </p>
     * 
     * @return the stream to read the content
     * @throws FileNotFoundException if the content is a file, and the file where it's
     *         stored is not accessible
     */
    public InputStream getContent() throws FileNotFoundException {
        if (destination == null) {
            if (nonFileInput == null) {
                return new ByteArrayInputStream(new byte[0]);
            }
            return new ByteArrayInputStream(nonFileInput.toByteArray());
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
        if (header.containsKey(CONTENT_TYPE)) {
            return header.get(CONTENT_TYPE);
        }
        return DEFAULT_CONTENT_TYPE;
    }

    /**
     * Finds a given header for the the mime
     * 
     * @param key the name of the header field
     * @return the value of the header field
     */
    public String getHeaderField(final String key) {
        return header.get(key);
    }

    /**
     * Finds the name of the element or null if no name has been set
     * 
     * @return the name of the element or null if no name has been set
     */
    public String getName() {
        return header.get(FIELD_NAME);
    }

    /**
     * Finds the absolute pathname of the file in which the uploaded file has been stored
     * 
     * @return the absolute filepath or <code>null</code> if it's not a file
     */
    public File getDestination() {
        if (!isFile()) {
            return null;
        }
        return destination;
    }

    /**
     * Finds the original filename for the file
     * 
     * @return the original filename or <code>null</code> if MimeElement is not a file
     */
    public String getFilename() {
        if (isFile()) {
            return getHeaderField(FILE_NAME);
        }
        return null;
    }

    /**
     * Indicates wether the mime is used to store a file or not
     * 
     * @return <code>true</code> if the mime is used to store a file <code>false</code>
     *         otherwise
     */
    public boolean isFile() {
        return header.containsKey(FILE_NAME);
    }

    /**
     * <p>
     * Closes the Underlying stream of the MimeElement
     * </p>
     * 
     * @throws IOException If the stream cannot be closed
     */
    public void close() throws IOException {
        if (contentOutput == null) {
            // Closed before content, we initialize it anyway so we generate a
            // filename and other stuff that can be needed.
            initializeWriter();
        }
        contentOutput.close();
    }

    /**
     * Finds the current encoding of the MimeElement. If no encoding was explicitely
     * defined, the default encoding is used
     * 
     * @return the way content has been encoded.
     * @throws InvalidMimeEncodingException
     */
    public String getEncoding() throws InvalidMimeEncodingException {
        if (header.containsKey(CONTENT_ENCODING)) {
            return header.get(CONTENT_ENCODING);
        }
        return DEFAULT_CONTENT_ENCODING;
    }

    @Override
    public String toString() {
        String result = "";

        for (final Entry<String, String> headerField : header.entrySet()) {
            result += "[" + headerField.getKey() + "]: " + headerField.getValue() + "\n";
        }
        if (isFile()) {
            result += "[FILE]: " + destination.getAbsolutePath() + "\n";
        } else {
            try {
                final InputStream is = getContent();
                while (is.available() > 0) {
                    final char c = (char) ((byte) is.read() & 0xff);
                    result += c;
                }
                result += '\n';
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * <p>
     * A a new header to the element
     * </p>
     * <p>
     * If the header indicates the content is a file, a new file is created to dump
     * content into it. If content had previoulsy been added, it will be beforehand pushed
     * into the new file
     * </p>
     * 
     * @param key the name of the header
     * @param value the value of the header
     * @throws IOException when the header indicates a file is contained in the mime, and
     *         the file where this content will be written can't be created/Accessed
     * @throws InvalidMimeEncodingException
     * @throws MalformedMimeException
     */
    protected void addHeader(final String key, final String value) throws InvalidMimeEncodingException, MalformedMimeException {
        header.put(key, value);
        if (key.equals(CONTENT_ENCODING)) {
            this.decoder = getDecoder();
        }
        if (contentOutput != null) {
            throw new MalformedMimeException("Content should never be added before header.");
        }
    }

    /**
     * adds a new byte of content to the mime
     * 
     * @throws MalformedMimeException
     * @throws InvalidMimeEncodingException
     */
    protected void addContent(final byte b) throws IOException {
        if (contentOutput == null) {
            initializeWriter();
        }
        contentOutput.write(b);
    }

    /**
     * Initializes the MimeElemend to get ready to write
     * 
     * @throws IOException If an IO error occurs when creating the stream that will be
     *         used to save content
     */
    private void initializeWriter() throws IOException {
        if (decoder == null) {
            try {
                decoder = getDecoder();
            } catch (final InvalidMimeEncodingException e) {
                // Does never happen
                Log.framework().fatal("Got an exception that should never happen", e);
            }
        }
        if (isFile()) {
            final File uploadedFileDir = new File(fileSavingDirectory);
            uploadedFileDir.mkdirs();
            destination = new File(fileSavingDirectory + nameGen.generateName(getHeaderField(FILE_NAME)));
            try {
                final FileOutputStream fos = new FileOutputStream(destination);
                contentOutput = new DecodingOuputStream(fos, decoder);
            } catch (final FileNotFoundException e) {
                Log.web().fatal("Couldn't create the output file to store uploaded file: " + destination.getAbsolutePath(), e);
                throw new FileNotFoundException("Couldn't create the output file to store uploaded file: " + destination.getAbsolutePath());
            }
        } else {
            nonFileInput = new ByteArrayOutputStream();
            contentOutput = new DecodingOuputStream(nonFileInput, decoder);
        }
    }

    /**
     * Finds the current decoder to use with this file
     * 
     * @return the decoder to used with the mime
     * @throws InvalidMimeEncodingException When no decoder available can decode this
     *         content
     */
    private MimeDecoder getDecoder() throws InvalidMimeEncodingException {
        return availableEncodings.get(getEncoding());
    }

}
