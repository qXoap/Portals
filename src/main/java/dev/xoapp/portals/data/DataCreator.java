package dev.xoapp.portals.data;

import cn.nukkit.utils.Config;
import dev.xoapp.portals.Loader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;

public class DataCreator {

    private final Config config;

    public DataCreator(String path) {
        File file = new File(Loader.getInstance().getDataFolder() + File.separator + path);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.config = new Config(file.getAbsoluteFile(), Config.JSON);
    }

    public void set(String key, Object value) {
        config.set(key, value);
        config.save();
    }

    public void delete(String key) {
        config.remove(key);
        config.save();
    }

    public Map<String, Object> getSavedData() {
        return config.getAll();
    }
}
