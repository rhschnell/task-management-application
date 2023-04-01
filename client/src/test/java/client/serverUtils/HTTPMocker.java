package client.serverUtils;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class HTTPMocker {
    protected final Client clientMock;
    protected final WebTarget targetMock;
    protected final Invocation.Builder builderMock;
    protected final Invocation invocationMock;

    public HTTPMocker(){
        clientMock = Mockito.mock(Client.class);
        targetMock = Mockito.mock(WebTarget.class);
        builderMock = Mockito.mock(Invocation.Builder.class);
        invocationMock = Mockito.mock(Invocation.class);
        initialize();
    }

    public void initialize(){
        when(clientMock.target(anyString())).thenReturn(targetMock);
        when(targetMock.path(anyString())).thenReturn(targetMock);
        when(targetMock.request(anyString())).thenReturn(builderMock);
        when(targetMock.queryParam(anyString(), any(Object.class))).thenReturn(targetMock);
        when(builderMock.accept(anyString())).thenReturn(builderMock);
        when(builderMock.post(any(Entity.class), any(Class.class))).thenReturn(invocationMock);
    }



}
