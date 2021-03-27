# set2
 
Ustawienia połączenia z bazą danych można zmienić w pliku src\main\resources\application.properties
Ścieżka do pliku wejściowego powinna zostać przekazana jako parametr linii poleceń.
Działanie aplikacji sprawdzałem w PostgreSQL. Aby skorzystać z innego DBMS trzeba dodać do niego sterownik w pom.xml.
Raczej nie powinno być problemu z poleceniami w stylu TOP zamiast LIMIT, ale nazwy kolumn powinny wyglądać tak jak w pliku z opisem zadania
(tzn. całe nazwy pisane wielkimi literami poza "Age").
Zakładam że dane wejściowe są prawidłowe i tylko Age może być nullem w tabeli customers. 