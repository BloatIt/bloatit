package com.bloatit.framework.xcgiserver;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * The class {@link RequestKey} represent a unique identifier for a session.
 */
public class RequestKey {

    public enum Source {
        COOKIE, TOKEN, GENERATED
    }

    private static final int SHA515_HEX_LENGTH = 128;
    private static final int RANDOM_SIZE = 512;
    private static final String SHA1_PRNG = "SHA1PRNG";

    private String id;
    private final String ipAddress;
    private final Source source;

    /**
     * Create a key using already existing id and ip address.
     * 
     * @param id the id of this session. It must be non null, and
     *            {@value RequestKey#SHA515_HEX_LENGTH} char long.
     * @param ipAddress can be null. If it is less than 7 chars long it is
     *            considered has null (because invalid).
     * @throws WrongSessionKeyFormatException
     */
    public RequestKey(final String id, final String ipAddress, final Source source) throws WrongSessionKeyFormatException {
        this(id, ipAddress, source, 0);
        if (id.length() != SHA515_HEX_LENGTH) {
            Log.framework().error("The id must be a 128 char long hex-encoded sha512 string.");
            throw new WrongSessionKeyFormatException("The id must be a 128 char long hex-encoded sha512 string.");
        }
        if (source == Source.GENERATED) {
            throw new BadProgrammerException("To generate a new RequestKey use the RequestKey(final String ipAdress) constructor.");
        }

    }

    private RequestKey(final String id, final String ipAddress, final Source source, final int plop) {
        if (ipAddress != null && ipAddress.length() < 7) {
            this.ipAddress = null;
        } else {
            this.ipAddress = ipAddress;
        }
        if (id == null) {
            throw new NonOptionalParameterException();
        }
        this.source = source;
        this.id = id;
    }

    /**
     * Create a new SessionKey, with a random id, for a user at
     * <i>ipAddress</i>.
     * 
     * @param ipAdress the ip address of the user identified by this
     *            {@link RequestKey}.
     */
    public RequestKey(final String ipAdress) {
        this(generateRandomId(), ipAdress, Source.GENERATED, 0);
    }

    public static String generateRandomId() {
        UUID.randomUUID().toString();
        try {
            final byte[] bytes = new byte[RANDOM_SIZE];
            SecureRandom.getInstance(SHA1_PRNG).nextBytes(bytes);
            return DigestUtils.sha512Hex(DigestUtils.sha512Hex(bytes) + UUID.randomUUID().toString());
        } catch (final NoSuchAlgorithmException e) {
            throw new BadProgrammerException(e);
        }
    }

    /**
     * Randomly generate a new id.
     */
    public void resetId() {
        this.id = generateRandomId();
    }

    /**
     * @return the ip address. Can be null.
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @return the 128 char long random id of this key.
     */
    public String getId() {
        return id;
    }

    public Source getSource() {
        return source;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RequestKey other = (RequestKey) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        if (source != other.source) {
            return false;
        }
        return true;
    }

}
