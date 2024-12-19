### Домашнее задание 2

Студент - Семенов Владислав (vliusemenov@edu.hse.ru).

В рамках домашнего задания требовалось считать изображение по ссылке и вывести его на экран. Интерфейс приложения (инернет-соединение есть):

<img src="https://github.com/user-attachments/assets/30af6f8d-e04c-484e-94f7-7af6fffe258d" width="206" height="446"/>

Интерфейс приложения, когда интернет отключен:

<img src="https://github.com/user-attachments/assets/94df1476-bed5-4509-afe0-73d7a8d54d80" width="206" height="446"/>

Приложение реализовано на Compose, поэтому чуть переписал инструментальный тест. Также добавил параметр LOADING_DELAY для задержки чтения файлов: QR-код подгружется не сразу, и нужно сделать паузу, чтобы сканер считал верное изображение, а не плейсхолдер.

Тесты на Github.Actions не прошли, хотя локально все запускается: видимо, эмулятор не подключается к интернету. Пробовал вносить правки в android.yaml, но безрезультатно.
