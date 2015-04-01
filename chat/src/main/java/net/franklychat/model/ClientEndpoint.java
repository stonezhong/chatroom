package net.franklychat.model;

import org.apache.commons.lang3.StringUtils;

public class ClientEndpoint  {
    private final String address;
    private final int port;

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        return (address==null)?0:address.hashCode() * 3 + 7 * port;
    }

    @Override
    public boolean equals(Object other) {

        if ((other == null) || !(other instanceof ClientEndpoint)) {
            return false;
        }

        ClientEndpoint otherClientEndpoint = (ClientEndpoint)other;

        return (StringUtils.equals(getAddress(), otherClientEndpoint.getAddress()) &&
                getPort() == otherClientEndpoint.getPort());
    }
    
    public ClientEndpoint(String address, int port) {
        this.address = address;
        this.port = port;
    }

}
