# BookHive
## Integrantes del grupo
| Nombre                  | Correo                            | Usuario GitHub |
|-------------------------|-----------------------------------|----------------|
| Paula Fernández Pérez   | p.fernandezp.2022@alumnos.urjc.es | Paulaferpe               |
| Lucía Galán Galán       | l.galang.2019@alumnos.urjc.es     |    Luciagalan38            |
| Rubén Camacho Rodríguez | r.camacho.2022@alumnos.urjc.es    | RubenCamach0   |

## Entidades
* **Usuario**
* **Libro**
* **Reseña**
* **Pedido**
### Relaciones de entidades
* **OneToMany Book -> Review**
* **ManyToOne Purchase -> User**
* **ManyToMany Purchase -> Book**
* **ManyToOne Review -> User**
* **ManyToOne Review -> Book**
* **OneToMany User -> Review**
* **OneToMany User -> Purchase**


## Permisos de los usuarios
* **Usuario anónimo:** podrá ver los libros del catálogo, las reseñas de estos libros y añadir libros al carrito sin la posibilidad de realizar la compra o escribir reseñas.
* **Usuario registrado:** podrá realizar pedidos de libros y escribir reseña sobre los libros.
* **Usuario administrador:** tendrá control total sobre la aplicació, pudiendo añadir, editar y eliminar libros.
## Imágenes
* **Libros:** portada del libro.

## Navegación
### Pantalla de inicio
![](Home.png)
Desde esta pantalla se puede acceder a los libros de la librería, a la pantalla de inicio de sesión y registarse, a tu perfil y a tu cesta, además de poder añadir un libro a la página. 
### Pantalla de Crear cuenta
![](Register.png)
Desde esta pantalla podrás crear un nuevo usuario introducciendo tus datos.
### Pantalla de Inicio de sesión
![](Login.png)
Desde esta pantalla podrás iniciar sesión en la aplicación.
### Pantalla de un libro
![](Book.png)
En esta pantalla podrás ver la información y las reseñas del libro seleccionado, además podrás añadir una reseña, agregar el libro a tu cesta. Tambieén podrás editar y eliminar el libro. 
### Pantalla Añadir libro
![](NewBook.png)
Desde esta pantalla podrás crear y añadir un libro nuevo a la aplicación proporcionando la información necesaria sobre dicho libro. 
### Pantalla Editar libro 
![](EditBook.png)
En esta pantalla podrás editar la información sobre el libro seleccionado. 
### Pantalla nueva reseña
![](newReview.png)
En esta pantalla podrás realizar una reseña sobre el libro que has seleccionado, la cual quedará guardada.
### Pantalla de la cesta
![](basket.png)
En esta pantalla podrás ver los libros que tienes en tu cesta, con el precio de cada uno y el precio total, además podrás comprar dichos libros o volver hacia la pantalla de inicio para seguir comprando. 
### Pantalla de perfil 
![](Profile.png)
En esta pantalla podrás ver tu nombre de usuario, y acceder a tus pedidos anteriores y a las reseñas que has realizado, además tendrás la posibilidad de eliminar tu cuenta. 
### Pantalla de mis reseñas 
![](myReviews.png)
En esta pantalla podrás ver y eliminar tus reseñas. 
### Pantalla de mis pedidos 
![](myPurchases.png)
En esta pantalla podrás ver y eliminar tus pedidos. 
### Diagrama de navegación 
![](diagramaFlujo.png)
## Instrucciones de ejecución 
## Diagrama con las entidades de la base de datos 
![](DIAGRAMA.png)
## Diagrama de clases y templates
![](Diagrama2.png)
## Participación de miembros
### **Paula Fernández Pérez**
Todo lo relacionado con la entidad User, es decir, la propia entidad User, UserController, UserRepository y UserService. En cuanto a los templates, la realización createAccount.html, login.html, profile.html, users.html, errorNoSessionAddReview.html, errorNoSessionBuy.html y errorNoSessionDeletebok.html. 
* Principales commits: 
    - EntityUser :https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/d840f849462e04d9b42c35148232cf0a8425eedc 
    - User Service: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/a6a61a56f4c06d89d0dc87fe866c2d189b5f447d
    - User Controller/Create account: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/b1a875f5910f37dd630669b6aa06260698775cfa
    - User Controller login: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/dd95726192be9e92a00366815dd039c881509251
    - More errorNoSession pages: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/6abecb6ce4a6490eac43841791af4a2e2dd54715

* Ficheros importantes: 
    - UserController: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/controller/UserController.java
    - User: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/model/User.java
    - UserRepository: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/repository/UserRepository.java
    - UserService: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/service/UserService.java
    - createAccount.html: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/resources/templates/createAccount.html
### **Lucía Galán Galán**
Todo lo relacionado con la entidad Book, es decir, la propia entidad Book, BookController, BookRepository y BookService. En cuanto a los templates, la realización de book.html, editBook.html, home.html, newBook.html, errorNoSessionAddBook.html, errorNoSession.html y errorNoSessionEditbook.html. También, la gestión de imágenes para la entidad Libro.

* Principales commits: 

    - Entidad Book y BookRepository : https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/e8b3d0b1244d7b02529031adc16537f9aa10e301
    - Book Service: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/bc7e807ebdade0f90ef8b0e70516e6fd4f73200f
    - Book Controller: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/cf21beccbc391751e9bbeb25ab20eb0fec119f94
    - Book Images: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/0dd096a5fabd3ee64be956907032b2fa9e0a100f
    - Add errorNoSession pages: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/a692b81bfdd69f338c5cdd538baf247104bdc49a

* Ficheros importantes: 
    - BookController: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/controller/BookController.java
    - Book: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/model/Book.java
    - BookRepository: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/repository/BookRepository.java
    - BookService: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/service/BookService.java
    - home.html: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/resources/templates/home.html
### **Rubén Camacho Rodríguez**
Todo lo relacionado con las entidades Review y Purchase, es decir, las entidades Review y Purchase, ReviewRepository y PurchaseRepository, ReviewService y PurchaseService, ReviewController y PurchaseController. En cuanto a los templates, la realización basket.html, myPurchases.html, myReviews.html y newReview.html.

* Principales commits:
    - Entity Order + Order Repository: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/673b591424dd73104840ed3bd9b7c0581bb7d27c
    -ReviewService: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/3add2861773aeb5a83f853e5d82a1763e2745bd0
    -Change Order to Purchase: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/4a6cae40c236d3f438e0d211f23f4af0491ad953
    -Basket Controller: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/32f82d79e94651ac0e8f0b0500a3d95eebd28d2b
    -MyReviews Controller: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/29b086df2e2ab95310b3d4d21dc8a80fe83d0805

* Ficheros importantes:
    - PurchaseController: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/controller/PurchaseController.java
    - ReviewController: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/controller/ReviewController.java
    - PurchaseService: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/service/PurchaseService.java
    - ReviewService: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/java/com/example/demo/service/ReviewService.java
    - basket.html: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/blob/main/demo/src/main/resources/templates/basket.html

### Commits grupales
Algunos de los commits se realizaban tras revisiones en grupo. Estos commits principalmente son el estilo (CSS), la base de datos (SQL) y otras revisiones que se hacían para corregir diferentes errores.

Algunos ejemplos son:

* CSS: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/2068802d70c002ab4c36150ba1acfd47f4d230d7
* SQL (la contraseña que sale en este commit cambia posteriormente): https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/8fbea468d24dc3a18d6e194b51623f12d3135334
* Revisión en grupo: https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-15/commit/8fbea468d24dc3a18d6e194b51623f12d3135334

## Instrucciones de ejecución
1. Descargar el repositorio y descomprimirlo en una carpeta.
2. Descargar MySQL Configurator y MySQL Worckbench.
3. En MySQL Configurator poner Usuario: root, Password: grupo15SQL y el puerto predeterminado (3306).
4. En MySQL Worckbench añadir una conexión con el usuario y password previamente introducidos.
5. Crear un esquema llamado bookshop en la conexión anterior.
6. Ejecutar la aplicación.
7. Ir a http://localhost:8080/
### Versiones utilizadas
* Java Version: 21
* MySQL Connector: 8.0.33
* Maven Version: 4.0.0
* SpringBoot: 3.4.3
* Entorno de desarrollo: Visual Studio Code y MySQL Workbench (8.0)
* Extensions VS:
    - Spring Boot Extension Pack
    - Maven for Java
