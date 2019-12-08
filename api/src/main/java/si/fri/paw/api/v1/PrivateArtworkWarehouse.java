package si.fri.paw.api.v1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import si.fri.paw.api.v1.ExceptionMappers.InvalidParameterExceptionMapper;
import si.fri.paw.api.v1.sources.PostSource;
import si.fri.paw.api.v1.sources.TagSource;
import si.fri.paw.api.v1.sources.UserSource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@OpenAPIDefinition(info = @Info(
        title = "Rest API",
        version = "v1",
        contact = @Contact(),
        license = @License(),
        description = "JavaSI API for the Private Artwork Warehouse."),
        servers = @Server(url ="http://localhost:8080/v1"))
@ApplicationPath("v1")
public class PrivateArtworkWarehouse extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(PostSource.class);
        classes.add(UserSource.class);
        classes.add(TagSource.class);
        classes.add(MultiPartFeature.class);
        classes.add(InvalidParameterExceptionMapper.class);

        return classes;
    }
}