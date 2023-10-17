# Screen sharing system

[![](https://img.shields.io/badge/Made%20with-Java%20SE%201.8-brown.svg)](https://www.java.com/en/)&nbsp;&nbsp;
[![](https://img.shields.io/badge/Build%20with-Maven%203.9.4-1abc9c.svg)](https://maven.apache.org//)&nbsp;&nbsp;

TL;DR Protected screen sharing system created with Java SE 1.8, JSSE, JCA, JCE and Swing UI.

## Table of content

* [Clone and install](#clone-and-install)
* [Prepare configuration and run](#prepare-configuration-and-run)
* [Tech stack](#tech-stack)
* [Project status](#project-status)
* [License](#license)

<a name="clone-and-install"></a>

## Clone and install

To install the program on your computer, use the command below (or use the build-in GIT system in your IDE environment):

```
$ git clone https://github.com/Milosz08/screen-sharing-system
```

<a name="prepare-configuration-and-run"></a>

## Prepare configuration and run

1. Build and install shared library via:

```bash
$ mvn -N clean package install
```

2. Create executable JAR file for modules:

```bash
$ mvn -pl "<module>" clean assembly:assembly
```

where `<module>` is `client` or `host`.

3. Optionally, build all modules via (for UNIX system):

```bash
$ ./package
```
or for Windows system:
```cmd
.\package.bin
```

All executable JAR files will be available in `.bin` directory.

<a name="tech-stack"></a>

## Tech stack

* Java SE 1.8
* JSSE (Java Secure Socket Extension)
* JCA (Java Cryptography Architecture)
* JCE (Java Cryptography Extension)
* Swing UI, AWT

<a name="project-status"></a>

## Project status

Project is still in development.

<a name="license"></a>

## License

This application is on Apache 2.0 License.
