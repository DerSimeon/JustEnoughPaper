package lol.simeon.jer;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

// Written in Java: Paper loads the PluginLoader before the Kotlin stdlib is on
// the classpath, so this class must not depend on the Kotlin runtime.
public class JustEnoughRecipesLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(
            new RemoteRepository.Builder(
                "central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR
            ).build()
        );
        resolver.addDependency(
            new Dependency(new DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib:" + kotlinVersion()), null)
        );
        classpathBuilder.addLibrary(resolver);
    }

    private String kotlinVersion() {
        try (InputStream stream = getClass().getResourceAsStream("/jer-versions.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties.getProperty("kotlin-version");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
