
# Add Group By Refactoring

Este proyecto es un trabajo para la materia Orientación a Objetos 2 de la UNLP, el cual consiste en generar un refactor sobre una consulta de SQL utilizando el patrón Visitor y Template Method junto con ANTLR (ANother Tool for Language Recognition), el lenguaje Java junto a JUnit para correr los test implemetados dentro del proyecto.


## Demo

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/Example1_Query_Transformed.png?raw=true)

# Pre-requisitos:
- Tener instalado el JDK de Java, mínimo versión 8.

## Variables de Entorno e Instalación

Para correr el proyecto a través de línea de comandos, debemos agregar algunas variables para que nuestro sistema interprete y compile el código.

`Aclaración`
 Para este ejemplo usamos la ruta: "$HOME/Desktop/antlr/repo/sql-refactoring/" para clonar el repositorio ustedes pueden tener rutas diferentes pero tengan en cuenta que les va a afectar directamente donde alojen el repo y sus nombres con respecto a las variables y configuracions, solo tienen que cambiar por las que usaron y deberia funcionarles isn problema

# Guia para crear perfil bash y dejar permanente las variables de entorno
- Creamos un archivo .bash_profile dentro de nuestro HOME y luego agregamos las variables que se situan en la siguiente seccion y deberia quedarnos asi:

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/bash_profile_example.png?raw=true)

luego que tenemos agregado el comando: $ source ~/.bash-profile

# Mac OS / Linux

`ANTLR 4`

Dentro del proyecto encontraremos la carpeta "jars" que contiene 3 archivos:

- antlr-4.13.1-complete

- hamcrest-core-1.3

- junit-4.13.2

export ANTLR_HOME="$HOME/Desktop/antlr/repo/sql-refactoring/jars/"
export ANTLR_JAR="$ANTLR_HOME/antlr-4.13.1-complete.jar"

`JUNIT`

export JUNIT_JAR="$ANTLR_HOME/junit-4.13.2-complete.jar"

`HAMREST`

export HAMREST_JAR="$ANTLR_HOME/hamrest-core-1.3.jar"

export CLASSPATH=".:$ANTLR_JAR:$CLASSPATH:$JUNIT_JAR:$HAMREST_JAR"

`ALIAS`

alias antlr4="java -jar $ANTLR_JAR"

alias grun="java org.antlr.v4.gui.TestRig

# Win

Instrucciones:
1) Crear una carpeta con algún nombre significativo (ej: JavaLib) donde colocaremos
varios archivos para que ANTLR funcione.

2) Crear los siguientes 3 archivos en la carpeta creada (ej: C:\JavaLib):

Nombre y extensión de archivo | Contenido del archivo | 
--- | --- | 
antlr4.bat | java org.antlr.v4.Tool %* |
class.bat | SET CLASSPATH=.;%CLASSPATH% | 
grun.bat | java org.antlr.v4.gui.TestRig %* |

3) Buscar en la barra de búsqueda de Windows “Editar variables de entorno” y seleccionar “Variables de entorno...”.
Nombre y extensión de archivo

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/setEnvVariables1.png?raw=true)

a) En la sección de “Variables del sistema “ agregar una nueva variable con nombre “CLASSPATH” y con valor que sea el path donde se encuentre el jar de ANTLR descargado, por ejemplo “C:\JavaLib\antlr-4.13.0-complete.jar”.

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/setEnvVariables2.png?raw=true)

b) En la sección de “Variables de usuario”, seleccionar la variable “Path” y editarla. Hay que agregar dos valores:
i) La referencia a la variable de sistema: %CLASSPATH%
ii) La dirección de la carpeta que creamos anteriormente, por ejemplo:
C:\JavaLib

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/setEnvVariables3.png?raw=true)

Para comprobar el funcionamiento, abrir el CMD y ejecutar el comando antlr4. Debería verse algo como esto:

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/checkAntlr4.png?raw=true)

Fuente: Instalacion de ANTLR4 en Windows.pdf (provisto por la cátedra de OO2)

# Compilación y Correr por línea de comandos

`Path`

1 Para poder compilar los archivos primero tenemos que ubicarnos en la carpeta raiz, en nuestro caso sql-refactoring como se ve en la imagen
 y corremos el comando:
 
 `javac -cp "jars/antlr-4.13.1-complete.jar:jars/hamcrest-core-1.3.jar:jars/junit-4.13.2.jar:" -d output ./sqlitegrammar/*.java ./*.java`

2 Luego para correr el programa, en este caso los test que tenemos dentro de GroupByRefactoringTest.java utilizamos el comando:

`java -cp "jars/antlr-4.13.1-complete.jar:jars/hamcrest-core-1.3.jar:jars/junit-4.13.2.jar:output/" org.junit.runner.JUnitCore GroupByRefactoringTest`

3 Deberiamos ver el resultado de los test como muestra en la imagen final.

![App Screenshot](https://github.com/roo2-unlp/sql-refactoring/blob/AddGroupBy/img/compile_running.png?raw=true)

