# try_helidon

## 環境構築

### SDKMAN! をインストール

```
$ sudo apt update
$ sudo apt install zip unzip
$ curl -s "https://get.sdkman.io" | bash
$ . ~/.sdkman/bin/sdkman-init.sh
$ sdk version

SDKMAN!
script: 5.19.0
native: 0.7.4 (linux aarch64)

$ 
```

### JDK, Maven をインストール

```
$ sdk list java
...
 GraalVM CE    |     | 24.0.1       | graalce |            | 24.0.1-graalce      
               |     | 24           | graalce |            | 24-graalce          
               |     | 23.0.2       | graalce |            | 23.0.2-graalce      
               |     | 21.0.2       | graalce |            | 21.0.2-graalce      
               |     | 17.0.9       | graalce |            | 17.0.9-graalce      
...
$ sdk install java 24.0.1-graalce
$ sdk install maven 3.9.10
$ java --version
openjdk 24.0.1 2025-04-15
OpenJDK Runtime Environment GraalVM CE 24.0.1+9.1 (build 24.0.1+9-jvmci-b01)
OpenJDK 64-Bit Server VM GraalVM CE 24.0.1+9.1 (build 24.0.1+9-jvmci-b01, mixed mode, sharing)
$ mvn --version
Apache Maven 3.9.10 (5f519b97e944483d878815739f519b2eade0a91d)
Maven home: ~/.sdkman/candidates/maven/current
Java version: 24.0.1, vendor: GraalVM Community, runtime: ~/.sdkman/candidates/java/24.0.1-graalce
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.6.87.1-microsoft-standard-wsl2", arch: "aarch64", family: "unix"
```

### Docker をインストール

```
$ curl -fsSL https://get.docker.com -o get-docker.sh
$ sudo sh get-docker.sh
$ sudo docker run hello-world
...
Hello from Docker!
...
$
```

### Homebrew で Helidon-CLI をインストール

```
$ sudo apt install build-essential procps curl file git -y
$ /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
$ echo >> ~/.bashrc
$ echo 'eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"' >> ~/.bashrc
$ eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"
$ type brew
brew is /home/linuxbrew/.linuxbrew/bin/brew
$ brew --version
Homebrew 4.5.7
$ brew install helidon
$ helidon version
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::loadLibrary has been called by org.fusesource.hawtjni.runtime.Library in an unnamed module (file:/home/linuxbrew/.linuxbrew/Cellar/helidon/3.0.6_1/libexec/libs/jansi-1.18.jar)
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

Looking up default Helidon version
build.date               2025-03-21 22:24:21 GMT
build.version            3.0.6
build.revision           ${buildNumber}
latest.helidon.version   4.2.3
default.helidon.version  4.2.3
$
```
