
# Таблица котировок
Это приложение отображает таблицу с информацией по котировкам в реальном времени. Данные обновляются через WebSocket соединение. Реализовано в соответствии с требованиями тестового задания.

## Функциональность

- Обновление котировок происходит через подписку на события WebSocket.
- Отображается фиксированный список бумаг, включая их тикер, биржу, изменения в процентах, изменение в цене, последнюю цену.
- Значения подсвечиваются зеленым при положительном изменении и красным при отрицательном.
- Логотипы компаний загружаются из сети.
- Используется Jetpack Compose.

## Технологии

- **MVVM**
- **Jetpack Compose**
- **Hilt**
- **Retrofit**
- **OkHttp**
- **Coroutines**



<img src="https://github.com/user-attachments/assets/8c64aa74-21bf-4603-8bf3-7a5a3e7a0ca6" alt="screen_quote_table" width="300"/>
