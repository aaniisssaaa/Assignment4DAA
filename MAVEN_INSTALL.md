# Быстрая установка Maven для Windows

## Способ 1: Через IntelliJ IDEA (БЕЗ установки Maven)

1. Откройте IntelliJ IDEA
2. File → Open → выберите папку asik4daa
3. Нажмите OK
4. IDE автоматически скачает зависимости
5. Build → Build Project (Ctrl+F9)
6. Готово! ✅

## Способ 2: Установка Maven вручную

### Шаг 1: Скачайте Maven
1. Откройте: https://maven.apache.org/download.cgi
2. Найдите раздел "Files"
3. Скачайте: apache-maven-3.9.x-bin.zip

### Шаг 2: Распакуйте
1. Распакуйте архив в: C:\Program Files\Apache\maven
2. Полный путь должен быть: C:\Program Files\Apache\maven\bin\mvn.cmd

### Шаг 3: Добавьте в PATH
1. Нажмите Win + R
2. Введите: sysdm.cpl
3. Вкладка "Дополнительно" → "Переменные среды"
4. В "Системные переменные" найдите "Path"
5. Нажмите "Изменить"
6. Добавьте: C:\Program Files\Apache\maven\bin
7. Нажмите OK везде

### Шаг 4: Проверьте установку
1. Откройте НОВЫЙ PowerShell
2. Выполните: mvn -version
3. Должно показать версию Maven

### Шаг 5: Компилируйте проект
```powershell
cd C:\Users\user\Downloads\asik4daa
mvn clean compile
mvn test
mvn exec:java -Dexec.mainClass="Main"
```

## Способ 3: Использовать только IntelliJ IDEA

Если Maven не нужен в терминале, просто используйте IntelliJ IDEA:
- Все команды можно запускать через Maven панель в IDE
- Не нужно ничего устанавливать дополнительно
- Работает "из коробки"

## ✅ Рекомендация

Используйте IntelliJ IDEA - это проще и быстрее!
Maven нужен только если вы хотите компилировать через командную строку.
