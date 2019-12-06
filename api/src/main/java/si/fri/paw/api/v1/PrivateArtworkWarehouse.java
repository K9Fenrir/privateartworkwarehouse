package si.fri.paw.api.v1;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import si.fri.paw.api.v1.sources.PostSource;
import si.fri.paw.api.v1.sources.TagSource;
import si.fri.paw.api.v1.sources.UserSource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("v1")
public class PrivateArtworkWarehouse extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(PostSource.class);
        s.add(UserSource.class);
        s.add(TagSource.class);
        s.add(MultiPartFeature.class);

        return s;
    }
}