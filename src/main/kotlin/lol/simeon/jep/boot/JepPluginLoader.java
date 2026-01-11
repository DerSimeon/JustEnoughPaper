package lol.simeon.jep.boot;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.jpenilla.gremlin.runtime.DependencyCache;
import xyz.jpenilla.gremlin.runtime.DependencyResolver;
import xyz.jpenilla.gremlin.runtime.DependencySet;
import xyz.jpenilla.gremlin.runtime.logging.Slf4jGremlinLogger;
import xyz.jpenilla.gremlin.runtime.platformsupport.PaperClasspathAppender;

import java.nio.file.Path;
import java.util.Set;

/**
 * JepPluginLoader
 * Used to load the plugin's dependencies and inject them into the classpath.
 */
@SuppressWarnings("UnstableApiUsage")
public class JepPluginLoader implements PluginLoader {

    /**
     * load the plugin's dependencies and inject them into the classpath.
     *
     * @param classpathBuilder the classpath builder
     */
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        new PaperClasspathAppender(classpathBuilder).append(resolve(classpathBuilder.getContext().getDataDirectory().getParent().resolve("../libraries")));
    }

    private Set<Path> resolve(Path cacheDir) {
        DependencySet dependencySet = DependencySet.readFromClasspathResource(this.getClass().getClassLoader(), "jep-dependencies.txt");
        DependencyCache dependencyCache = new DependencyCache(cacheDir);
        Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
        Set<Path> result;
        try(DependencyResolver resolver = new DependencyResolver(new Slf4jGremlinLogger(logger))) {
            result = resolver.resolve(dependencySet, dependencyCache).jarFiles();
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve dependencies", e);
        }
        dependencyCache.cleanup();
        return result;

    }
}