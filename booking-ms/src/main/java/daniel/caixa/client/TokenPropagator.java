package daniel.caixa.client;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import java.io.IOException;

@Provider
@RegisterProvider(TokenPropagator.class)
public class TokenPropagator implements ClientRequestFilter {

    @Inject
    JsonWebToken jwt;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (jwt == null || jwt.getRawToken() == null) return;

        String token = jwt.getRawToken();
        requestContext.getHeaders().add("Authorization", "Bearer " + token);
    }
}