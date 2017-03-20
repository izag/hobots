package robots.teacher_pack.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.Map;

import javax.swing.JInternalFrame;

public class StateUtils
{
    /**
     * Сохраняет общие свойства графического компонента в словарь
     * @param component
     * @param state
     */
    public static void saveComponentState(Component component, Map<String, String> state)
    {
        Dimension size = component.getSize();
        Point location = component.getLocation();
        state.put("locationX", Integer.toString(location.x));
        state.put("locationY", Integer.toString(location.y));
        state.put("height", Integer.toString(size.height));
        state.put("width", Integer.toString(size.width));
        if (component instanceof JInternalFrame)
        {
            JInternalFrame frame = (JInternalFrame)component;
            state.put("maximized", Boolean.toString(frame.isMaximum()));
            state.put("iconified", Boolean.toString(frame.isIcon()));

            Rectangle normalBounds = frame.getNormalBounds();
            state.put("normalHeight", Integer.toString(normalBounds.height));
            state.put("normalWidth", Integer.toString(normalBounds.width));
            state.put("normalX", Integer.toString(normalBounds.x));
            state.put("normalY", Integer.toString(normalBounds.y));
        }
    }

    /**
     * Восстанавливает общие свойства графического компонента из словаря
     * @param component
     * @param dictionary
     */
    public static void loadComponentState(Component component, Map<String, String> dictionary)
    {
        Dimension size = component.getSize();
        Dimension preferredSize = component.getPreferredSize();
        Point location = component.getLocation();
        location.x = readStateValue(dictionary, "locationX", location.x);
        location.y = readStateValue(dictionary, "locationY", location.y);
        size.width = readStateValue(dictionary, "width", size.width);
        size.height = readStateValue(dictionary, "height", size.height);
        preferredSize.width = readStateValue(dictionary, "preferredWidth", preferredSize.width);
        preferredSize.height = readStateValue(dictionary, "preferredHeight", preferredSize.height);
        component.setLocation(location);
        component.setSize(size);
        component.setPreferredSize(preferredSize);
        
        if (component instanceof JInternalFrame)
        {
            JInternalFrame frame = (JInternalFrame)component;
            suppressExceptions(() -> frame.setMaximum(readStateValue(dictionary, "maximized", frame.isMaximum())));
            suppressExceptions(() -> frame.setIcon(readStateValue(dictionary, "iconified", frame.isIcon())));

            Rectangle normalBounds = frame.getNormalBounds();
            normalBounds.height = readStateValue(dictionary, "normalHeight", normalBounds.height);
            normalBounds.width = readStateValue(dictionary, "normalWidth", normalBounds.width);
            normalBounds.x = readStateValue(dictionary, "normalX", normalBounds.x);
            normalBounds.y = readStateValue(dictionary, "normalY", normalBounds.y);
            frame.setNormalBounds(normalBounds);
        }
    }

    /**
     * Читает значение типа int из словаря. 
     * Если в словаре нет указанного значения, возвращает указанное значение по умолчанию.
     * @param dictionary
     * @param key
     * @param defaultValue
     * @return
     */
    private static int readStateValue(Map<String, String> dictionary, String key, int defaultValue)
    {
        String value = dictionary.get(key);
        if (value != null)
        {
            try
            {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException ignored)
            {
                System.out.println(MessageFormat.format("Невозможно преобразовать значение {0} в целое число", value));
            }
        }
        return defaultValue;
    }
    
    /**
     * Читает значение типа bool из словаря. 
     * Если в словаре нет указанного значения, возвращает указанное значение по умолчанию.
     * @param dictionary
     * @param key
     * @param defaultValue
     * @return
     */
    private static boolean readStateValue(Map<String, String> dictionary, String key, boolean defaultValue)
    {
        String value = dictionary.get(key);
        if (value != null)
        {
            try
            {
                return Boolean.parseBoolean(value);
            }
            catch (NumberFormatException ignored)
            {
                System.out.println(MessageFormat.format("Невозможно преобразовать значение {0} в логический тип", value));
            }
        }
        return defaultValue;
    }
    
    /**
     * Вспомогательный интерфейс для описания действий, которые завершаются с исключениями 
     *
     */
    private static interface ThrowingAction
    {
        public void run() throws Exception;
    }

    /**
     * Вспомогательный метод для выполнения действий с перехватом и подавлением исключений
     * @param action
     */
    private static void suppressExceptions(ThrowingAction action)
    {
        try
        {
            action.run();
        }
        catch (Exception ignored)
        {
            // Исключение сознательно подавляется
        }
    }
}
