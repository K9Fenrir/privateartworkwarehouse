package si.fir.paw.utility.beans.security;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import si.fir.paw.utility.beans.entity.UserBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthorizationBean {

    @Inject
    UserBean userBean;

    private static final Logger log = Logger.getLogger(AuthorizationBean.class.getName());

    public String generateJWT(String username, boolean admin){
        try {
            RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

            // Give the JWK a Key ID (kid), which is just the polite thing to do
            rsaJsonWebKey.setKeyId("k1");

            // Create the Claims, which will be the content of the JWT
            JwtClaims claims = new JwtClaims();
            claims.setIssuer("PrivateArtworkWarehouse");  // who creates the token and signs it
            claims.setAudience("PAW-Client"); // to whom the token is intended to be sent
            claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
            claims.setGeneratedJwtId(); // a unique identifier for the token
            claims.setIssuedAtToNow(); // when the token was issued/created (now)
            claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
            claims.setSubject("login"); // the subject/principal is whom the token is about
            claims.setClaim("admin", admin);
            claims.setClaim("username", username);

            JsonWebSignature jws = new JsonWebSignature();

            jws.setPayload(claims.toJson());

            jws.setKey(rsaJsonWebKey.getPrivateKey());

            jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA512);

            String jwt = jws.getCompactSerialization();

            log.info("Generated JWT: " + jwt);

            return jwt;
        }
        catch (JoseException je){
            log.info("Error generating JWT token");
            return "ERROR";
        }
    }

    public JwtClaims decodeJWT(String jwt){
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipSignatureVerification()
                .build();
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);

            return jwtClaims;
        }
        catch (InvalidJwtException ije){
            return null;
        }
    }



}