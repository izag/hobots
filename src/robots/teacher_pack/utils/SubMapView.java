package robots.teacher_pack.utils;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class SubMapView extends AbstractMap<String, String>
{
    protected final Map<String, String> m_map;
    protected final String m_prefix;
    private final PrefixedSetView m_setView;
    
    public SubMapView(Map<String, String> map, String keyPrefix) 
    {
        m_map = map;
        m_prefix = MessageFormat.format("{0}.", keyPrefix);
        m_setView = new PrefixedSetView(m_map, m_prefix);
    }

    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet()
    {
        return m_setView;
    }
    
    private static String NO_KEY = UUID.randomUUID().toString();
    
    private String parentKeySelector(Object key)
    {
        if (key instanceof String)
        {
            return m_prefix + (String)key;
        }
        return NO_KEY;
    }
    
    @Override
    public boolean containsKey(Object key)
    {
        return m_map.containsKey(parentKeySelector(key));
    }

    @Override
    public String get(Object key)
    {
        return m_map.get(parentKeySelector(key));
    }

    @Override
    public String put(String key, String value)
    {
        String parentKey = parentKeySelector(key);
        if (parentKey != NO_KEY)
        {
            return m_map.put(parentKey, value);
        }
        return null;
    }

    private static class PrefixedSetView extends AbstractSet<Map.Entry<String, String>>
    {
        protected final Map<String, String> m_map;
        protected final String m_prefix;
        
        protected PrefixedSetView(Map<String, String> map, String prefix)
        {
            m_map = map;
            m_prefix = prefix;
        }

        private boolean filterFunction(Map.Entry<String, String> entry)
        {
            return entry.getKey().startsWith(m_prefix);
        }
        
        private Stream<Map.Entry<String, String>> streamGenerator()
        {
            return m_map.entrySet().stream().filter(this::filterFunction);
        }

        private static class Entry implements Map.Entry<String, String>
        {
            private final String m_key;
            private String m_value;

            public Entry(String key, String value)
            {
                m_key = key;
                m_value = value;
            }
            
            @Override
            public String getKey()
            {
                return m_key;
            }

            @Override
            public String getValue()
            {
                return m_value;
            }

            @Override
            public String setValue(String value)
            {
                String oldValue = m_value;
                m_value = value;
                return oldValue;
            }
        }
        
        private Map.Entry<String, String> entryMapper(Map.Entry<String, String> entry)
        {
            return new Entry(entry.getKey().substring(m_prefix.length()), entry.getValue());
        }
        
        @Override
        public Iterator<Map.Entry<String, String>> iterator()
        {
            return streamGenerator().map(this::entryMapper).iterator();
        }

        @Override
        public int size()
        {
            return (int)streamGenerator().count();
        }
        
    }
    
}
