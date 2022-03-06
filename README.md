## Price Prover CLI
 
### UserStory
User Alice, want to exchange aUSD with aIBM. She need to submit a transaction to our exchange contract, with this info: (aUSD->aIBM, amount, price of aIBM, proof of price of aIBM, timestamp). And exchange contract determines the validity of the info and execute the exchange on valid.

In the first version, the process to fetch this info is wrapped in a CLI, we call it “Price Prover CLI”. Alice provides “aUSD->aIBM” as parameter to the CLI. The CLI does the following:

trigger tls-notary client pagesigner-cli, to obtain the HTTPS response of the price data JSON and a notary file.

preprocess the price data JSON and notary file to circuit input.

feed the input to circuit and generate a proof file.

create a transaction payload: call price contract’s submit_price method, with argument: (aIBM, price of aIBM, proof, content, timestamp)

### Usage
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


### Build
(Please install [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) if you haven't). To build an executable jar:

`mvn clean compile assembly:single`

### Q&A

#### Cannot run program "./depends/libsnark/run_ppzksnark": error=13, Permission denied
Try
`chmod u+x ./depends/libsnark/run_ppzksnark`

### References
