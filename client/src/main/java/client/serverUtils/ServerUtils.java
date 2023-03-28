/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.serverUtils;

import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

import java.net.ConnectException;


public class ServerUtils {

    private String server = "http://localhost:8080/";

    /**
     * Sets the server's IP
     *
     * @param serverIP The new server IP
     */
    public void setServer(String serverIP) {
        server = serverIP;
    }

    /**
     * Gets the current server IP
     *
     * @return The server's IP
     */
    public String getServer() {
        return server;
    }

    /**
     * Tries to ping the current server.
     *
     * @return True if the ping was successful, else false.
     * Adapted from
     * <a href="https://stackoverflow.com/questions/11506321/how-to-ping-an-ip-address">
     *     StackOverflow
     * </a>
     */
    public boolean pingServer() {
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .request()
                    .get();
            return true;
        } catch (jakarta.ws.rs.ProcessingException e) {
            if (e.getCause() instanceof ConnectException) {
                System.out.println("Turn on the server before trying to connect");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
