# Price Prover CLI

## UserStory
User Alice, want to exchange aUSD with aIBM. She need to submit a transaction to our exchange contract, with this info: (aUSD->aIBM, amount, price of aIBM, proof of price of aIBM, timestamp). And exchange contract determines the validity of the info and execute the exchange on valid.

In the first version, the process to fetch this info is wrapped in a CLI, we call it “Price Prover CLI”. Alice provides “aUSD->aIBM” as parameter to the CLI. The CLI does the following:

trigger tls-notary client pagesigner-cli, to obtain the HTTPS response of the price data JSON and a notary file.

preprocess the price data JSON and notary file to circuit input.

feed the input to circuit and generate a proof file.

create a transaction payload: call price contract’s submit_price method, with argument: (aIBM, price of aIBM, proof, content, timestamp)

## Usage
This CLI is built on [picocli](https://www.picocli.info).

(Optional) define an alias:

```alias priceprover='java -cp priceprover-0.0.2.jar com.abstrlabs.priceprover.PriceProver```

Usage: `priceprover [-hVv] [-fi] [-as=<asset>] [-op=<outputPath>] [COMMAND]`
```
given the stock symbol, notarize the price and generate the proof

-as, --asset           the asset name used to obtain the price data.

-fi, --firstTime       if it is first time run

-h, --help             show help message and exit.

-op, --outputPath      output path for generated files

-v, --verbose          specify multiple -v options to increase verbosity.
                       for example, `-v` - info, '-vv' - debug,'-vvv' -trace
                       
-V, --version          print version information and exit.

SubCommands:
notarize  Call pagesigner-cli and notarize the stock price
build     Parse the notary json from pagesinger, and build the circuit/input
by xjsnark
prove     Trigger libsnark and generate the proof

```
## For User
### Run Executable priceprover Across OS
1. Download executable from drive links:
    1. Mac: [link](https://drive.google.com/file/d/1Uf7qrI6eu-GpOf_YxJdiLCRjRoAvmjYn/view?usp=sharing)
    2. Linux: [link](https://drive.google.com/file/d/1GGR3HaDjnq5aQqn5vq1Y7MJ8t7MY3Hpn/view?usp=sharing)
2. Run below command to show help message
```
./priceprover -h
```
Note: Image 'priceprover' is a fallback image that requires a JDK for execution.

## For Developer
### Build
(Please install [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) if you haven't). 

Before first build, add xjsnark-1.0.jar to local maven repository:
```
mvn install:install-file -Dfile=depends/akosba/xjsnark/1.0/xjsnark-1.0.jar -DgroupId=akosba -DartifactId=xjsnark -Dversion=1.0 -Dpackaging=jar
```

To build an executable jar:

`mvn clean compile assembly:single`

### Create Executable via [GraalVM Native Image](https://picocli.info/#_graalvm_native_image) 
#### Prerequisites
* **macOS**:
1. install GraalVM
```
brew install --cask graalvm/tap/graalvm-ce-java8
```
2. install Compiler Toochain
```
mac: xcode-select --install
```
3. install native-image
```
gu install native-image
```
4. set JAVA_HOME variable to the GraalVM installation directory
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java8-21.0.0.2/Contents/Home
```
5. Point the PATH environment variable to the GraalVM bin directory:
```
export PATH=$PATH:$JAVA_HOME/bin
```

* **Linux**:
1. install GraalVM
```
wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-19.3.6/graalvm-ce-java8-linux-amd64-19.3.6.tar.gz
tar -xzf graalvm-ce-java8-linux-amd64-19.3.6.tar.gz
```
2. install Compiler Toochain
see https://www.graalvm.org/22.1/reference-manual/native-image/#prerequisites

3. install native-image
```
gu install native-image
```
4. set JAVA_HOME variable to the GraalVM installation directory
```
export JAVA_HOME=/path/to/GraalVM/graalvm-ce-java8-19.3.6
```
5. Point the PATH environment variable to the GraalVM bin directory:
```
export PATH=$PATH:$JAVA_HOME/bin
```

* **Windows**: - TBD

#### Create a Native Image with picocli
After succeed build via maven, an executable jar will be generated under target/ folder. 

Run below command to create a native image [priceprover].
```
native-image -cp picocli-4.6.3.jar --static -jar target/priceprover-0.0.3-jar-with-dependencies.jar priceprover
```

### Q&A

#### Cannot run program "./depends/libsnark/run_ppzksnark": error=13, Permission denied
Try
`chmod u+x ./depends/libsnark/run_ppzksnark`

### References
