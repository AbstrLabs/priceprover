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


#### trigger pagersigner-cli

This step will normalize the asset name to the ones supported by our selected API: Alphadvantage. E.g. aIBM → IBM
Generate a headers.txt that contains HTTP request to stock price API
Invoke pagersigner-cli and output the notary file.

CLI usage (for now, will be improved soon):
```
java -cp priceprover-0.0.1.jar com.abstrlabs.priceprover.PageSignerCallBack -as aIBM -op ./out
```
This is quite verbose. You can define an alias. For example:

```alias pagesigner='java -cp priceprover-0.0.1.jar com.abstrlabs.priceprover.PageSignerCallBack```

#### notary json parser
This step could parse the notary.json and get the required input java objects.

CLI usage :
```
java -cp priceprover-0.0.1.jar com.abstrlabs.priceprover.NotaryJsonParser -nf ./out/notary.json
```

### Todo
- [ ] *update the rest steps and cli usage*
- [ ] *combine submodules together*

### Build
(Please install [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) if you haven't). To build an executable jar:

`mvn clean compile assembly:single`

### Q&A

### References
