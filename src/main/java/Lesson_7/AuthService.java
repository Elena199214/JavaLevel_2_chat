package Lesson_7;

import java.util.Optional;

/**
 * Сервис авторизации
 */
public interface AuthService {

    /**
     * запустить сервис
     */
    void start();

    /**
     * Остановить сервис
     */

    void stop();

    /**
     * Получить никнейм
     * @return
     */
    Optional<String> getNickByLoginAndPass(String login, String pass);
}
