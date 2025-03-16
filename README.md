# Screen sharing system

[[YouTube demo](https://www.youtube.com/watch?v=zfW8Ah0KPZM)] |
[[About project](https://miloszgilga.pl/project/screen-sharing-system)]

Secured screen sharing system created with low-level Socket API written in Java SE 1.8 with JCA, JCE and Swing UI. Uses
event-based reactive programming with observer pattern supplied by [RxJava](https://github.com/ReactiveX/RxJava)
library.

## Table of content

* [Project info](#project-info)
* [Gallery](#gallery)
* [Clone and install](#clone-and-install)
* [Build and run](#build-and-run)
* [Tech stack](#tech-stack)
* [License](#license)

## Project info

<details>
  <summary>Sequence diagram</summary><br>
  <img src=".github/diagrams/sequence.png" width="600" alt="">
</details>

### Error correction system

App supports HD resolution, so each frame is split into smaller chunks due to the maximum UDP frame size (65kb). I
decided to create simple error correction system, because UDP protocol can loose the packets which caused visible
artifacts in result video stream.

<details>
  <summary>Host state diagram</summary><br>
  <img src=".github/diagrams/host.png" width="600" alt="">
</details>

<details>
  <summary>Client state diagram</summary><br>
  <img src=".github/diagrams/client.png" width="600" alt="">
</details>

> [!NOTE]
> If the image fragments were not transmitted in the correct order, frame is not displayed in the user interface. Solves
> the problem of displaying artifacts in the image.

## Gallery

Host window
<img src=".github/host.png">

Client window
<img src=".github/client.png">

## Clone and install

To install the program on your computer, use the command below (or use the build-in GIT system in your IDE environment):

```
$ git clone https://github.com/milosz08/screen-sharing-system
```

## Build and run

1. Firstly, clean output `.bin` directory via:

```bash
$ ./mvnw clean      # for UNIX
.\mvnw.cmd clean    # for Windows
```

2. Build and install shared library via:

```bash
$ ./mvnw package install -pl lib      # for UNIX
.\mvnw.cmd package install -pl lib    # for Windows
```

3. Create executable JAR file for modules:

```bash
$ ./mvnw package -pl <module>         # for UNIX
.\mvnw.cmd package -pl <module>       # for Windows
```

where `<module>` is `client` or `host`.

4. Optionally, build all modules via:

```bash
$ ./mvnw package -pl client,host      # for UNIX
.\mvnw.cmd package -pl client,host    # for Windows
```

> [!TIP]
> All executable JAR files will be available in `.bin` directory.

5. To run host or client, type:

```bash
$ java -Xms<memory> -Xmx<memory> -jar <module>.jar
```

where:

* `<memory>`: ex. 512m or 2g
* `<module>`: ex. client or host

## Tech stack

* Java SE 1.8,
* JCA (Java Cryptography Architecture),
* JCE (Java Cryptography Extension),
* Swing UI, AWT,
* RxJava (reactive pattern),
* Imgscalr (image processing),
* Apache Commons Lang, IO (utilities),
* Logback with Slf4j api,
* JFreeChart (data transfer linear plot),
* Bcrypt (password hashing),
* Lombok (accessor annotations),
* Jackson (json processing).

## License

This project is licensed under the Apache 2.0 License.
