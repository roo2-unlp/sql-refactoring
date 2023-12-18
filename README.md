# Remove Alias Refactoring

Este proyecto académico, desarrollado para la asignatura de Orientación a Objetos 2 en la Universidad de La Plata, tiene como objetivo realizar un refactoring en una consulta de SQLite utilizando los patrones de diseño Visitor y Template Method. El refactoring se lleva a cabo empleando ANTLR4 (Another Tool for Language Recognition) en conjunto con el lenguaje de programación Java. Además, se utilizan las bibliotecas Hamcrest y JUnit para realizar pruebas unitarias durante el desarrollo del proyecto en Java.

## Versiones

- openjdk => 17.0.9
- antlr => 4.13.1
- junit => 4.13.2
- hamcrest-all => 1.3

Para obtener información sobre la instalación de JUnit y su utilización a través de la terminal, se puede consultar el siguiente enlace: [Instrucciones JUnit](https://hikmetcakir.medium.com/how-to-run-test-classes-in-command-line-2322da70195f)

## Variables de entorno

Para ejecutar el proyecto mediante la línea de comandos, es necesario agregar algunas variables al sistema para que interprete y compile el código. La variable CLASSPATH debe contener:

- La dirección donde se encuentra antlr4
- La dirección donde se encuentra hamcrest
- La dirección donde se encuentra junit
- La carpeta donde están las clases generadas por antlr4

### Linux/MAC OS

Dentro del archivo `~/.bashrc`, deben estar presentes estas variables:

```bash
export CLASSPATH="/usr/local/lib/antlr-4.13.1-complete.jar:/usr/local/lib/junit"
alias antlr4='java -Xmx500M -cp "/usr/local/lib/antlr-4.13.1-complete.jar:$CLASSPATH"'
alias grun='java -Xmx500M -cp "/usr/local/lib/antlr-4.13.1-complete.jar:$CLASSPATH"'
export PATH=~/.npm-global/bin:$PATH
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```


### Configuración en Windows

1. **Crear Carpeta:**
   - Crear una carpeta con algún nombre significativo (por ejemplo, `JavaLib`) donde colocaremos varios archivos para que ANTLR funcione.

2. **Crear Archivos:**
   - Crear los siguientes 3 archivos en la carpeta creada (por ejemplo, `C:\JavaLib`):
      - `antlr4.bat`: Contenido `java org.antlr.v4.Tool %*`
      - `class.bat`: Contenido `SET CLASSPATH=.;%CLASSPATH%`
      - `grun.bat`: Contenido `java org.antlr.v4.gui.TestRig %*`

3. **Variables de Entorno:**
   - Buscar en la barra de búsqueda de Windows “Editar variables de entorno” y seleccionar “Variables de entorno”.

4. **Variables del Sistema:**
   - En la sección de “Variables del sistema“, agregar una nueva variable con nombre `CLASSPATH` y con valor que sea el path donde se encuentre el JAR de ANTLR descargado (por ejemplo, `C:\JavaLib\antlr-4.13.0-complete.jar`).

5. **Variables de Usuario:**
   - En la sección de “Variables de usuario”, seleccionar la variable `Path` y editarla. Agregar dos valores:
      1. La referencia a la variable de sistema => `%CLASSPATH%`
      2. La dirección de la carpeta que creamos anteriormente => `C:\JavaLib`

6. **Verificar Funcionamiento:**
   - Para comprobar el funcionamiento, abrir el CMD y ejecutar el comando `antlr4`.


## Compilación

### Compilar Clases de ANTLR4
Para compilar las clases de ANTLR4 necesarias para el proyecto, ejecuta el siguiente comando en la terminal desde la raíz del proyecto:
`javac .\sqlitegrammar\*.java .\*.java`

Para compilar nuestro proyecto nos tendremos que ubicar en la raiz de nuestro proyecto y ejecutar este comando:
`javac *.java;`

Luego, para correr nuestro proyecto debemos ejecutar el siguiente comando:
`java org.junit.runner.JUnitCore RemoveAliasRefactoringTest`

*Nota: se puede resumir los dos pasos anteriores  en un comando:*
`javac *.java; java org.junit.runner.JUnitCore RemoveAliasRefactoringTest`


## Objetivo del proyecto:

Dada una consulta SQL y un alias a eliminar, busca dicho alias en la consulta, lo elimina y reemplaza las referencias al mismo por el nombre de la tabla o columna correspondiente.

![Ejemplo](./imagenes/image.png)

### PreCondiciones a tener en cuenta:

- La consulta debe ser válida (Parsea)
- El alias a remover debe existir en la consulta y está asociado a una tabla o columna.

### PostCondiciones a tener en cuenta:

- La consulta es válida (Parsea)
- El alias a remover ya no se encuentra en la consulta

## Implementación

### AliasCheckerVisitor
El `AliasCheckerVisitor` recorre el árbol generado por Antlr4 en busca del alias a remover. Si lo encuentra, establece en verdadero el valor de la variable booleana "aliasEncontrado" (inicialmente falsa) y guarda la referencia a la tabla/columna en "aliasReference".

### RemoveAliasVisitor
El `RemoveAliasVisitor` recorre el árbol generado por Antlr4. En cada nodo visitado, construye la consulta transformada utilizando la variable "querySeparate", que es un `StringBuilder`. Además, en los nodos donde se puede declarar un alias, este se elimina junto con la palabra clave "as". Por lo tanto, cada referencia al alias en la consulta se reemplaza por el nombre de la tabla o columna almacenado en "aliasReference".

### RemoveAliasRefactoring
`RemoveAliasRefactoring` es una subclase de `Refactoring` que define la lógica para verificar las precondiciones y postcondiciones, realizar las transformaciones y establecer el alias a remover. Utiliza el patrón Template Method.

### RemoveAliasRefactoringTest
`RemoveAliasRefactoringTest` es una colección de pruebas para verificar que, dadas las precondiciones y postcondiciones, las transformaciones se realicen correctamente.

### Conclusiones
- Se verifica como precondición que la consulta y el alias a remover se analicen correctamente mediante el `AliasCheckerVisitor`.
- Es un refactoring, ya que la consulta antes y después de la transformación tiene el mismo comportamiento, solo cambia la sintaxis.

**Aclaración:** Las consultas deben ingresarse sin el ';' al final.


