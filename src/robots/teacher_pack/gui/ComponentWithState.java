package robots.teacher_pack.gui;

import java.util.Map;

public interface ComponentWithState
{
    /**
     * Сохраняет информацию о текущем состоянии компонента словарь
     * @param state Словарь, куда следует сохранить состояние
     */
    public void saveComponentState(Map<String, String> state);

    /**
     * Восстанавливает информацию о состоянии компонента из словаря
     * @param state Словарь, откуда следует прочитать состояние
     */
    public void loadComponentState(Map<String, String> state);
}
