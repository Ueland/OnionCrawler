package no.ueland.onionCrawler.objects.configuration;

import no.ueland.onionCrawler.enums.configuration.ConfigurationKey;

import java.util.HashMap;

/**
 * Created by TorHenning on 19.08.2015.
 */
public class Configuration {
    private HashMap<String, Object> values;

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public int getInt(ConfigurationKey key) {
        return Integer.parseInt((String) values.get(key.name()));
    }

    public String getString(ConfigurationKey key) {
        return (String) values.get(key.name());
    }

    public String[] getStrings(ConfigurationKey key) {
        String s = getString(key);
        if(s == null) {
            s = "";
        }
        return s.split(",");
    }

    public Object getObject(ConfigurationKey key) {
        return values.get(key.name());
    }

    public boolean getBoolean(ConfigurationKey key) {
        return Boolean.valueOf((String) values.get(key.name()));
    }

    public void setValue(ConfigurationKey key, Object value) {
        this.values.put(key.name(), value);
    }
}
