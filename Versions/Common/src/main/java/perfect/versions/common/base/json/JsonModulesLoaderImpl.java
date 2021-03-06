package perfect.versions.common.base.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import perfect.versions.common.base.MinecraftVersion;
import perfect.versions.common.base.VersionModule;
import perfect.versions.common.base.VersionProviderRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class JsonModulesLoaderImpl implements JsonModulesLoader {

    @NotNull private final DefaultJsonModuleClassLoader defaultClassLoader = new DefaultJsonModuleClassLoader();
    @NotNull private final Map<Class<?>, JsonModuleClassLoader> classLoaders = new HashMap<>();

    @Override
    public void load(@NotNull VersionProviderRegistry registry, @NotNull MinecraftVersion version, @NotNull ObjectMapper objectMapper, @NotNull InputStream jsonInputStream)
            throws IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ClassNotFoundException,
                NoSuchMethodException, InvocationTargetException {
        JsonVersionModule[] modulesArray = objectMapper.readValue(jsonInputStream, JsonVersionModule[].class);
        for (JsonVersionModule jsonModule : modulesArray) {
            if (jsonModule.getVersion() == version) {
                Class<?> moduleClass = Class.forName(jsonModule.getClassPath());
                if (jsonModule.getLoaderClassPath() == null) {
                    VersionModule versionModule = this.defaultClassLoader.loadFromClass(moduleClass);
                    versionModule.configure(registry);
                } else {
                    Class<?> loaderClass = Class.forName(jsonModule.getLoaderClassPath());
                    if (!JsonModuleClassLoader.class.isAssignableFrom(loaderClass)) {
                        throw new IllegalArgumentException(jsonModule.getLoaderClassPath() + " is not a class loader");
                    }
                    JsonModuleClassLoader classLoader;
                    if (this.classLoaders.containsKey(loaderClass)) {
                        classLoader = this.classLoaders.get(loaderClass);
                    } else {
                        classLoader = (JsonModuleClassLoader) loaderClass.newInstance();
                        this.classLoaders.put(loaderClass, classLoader);
                    }
                    VersionModule versionModule = classLoader.loadFromClass(Class.forName(jsonModule.getClassPath()));
                    versionModule.configure(registry);
                }
            }
        }
    }

    @Override
    public void registerModuleClassLoader(@NotNull JsonModuleClassLoader classLoader) {
        this.classLoaders.put(classLoader.getClass(), classLoader);
    }
}