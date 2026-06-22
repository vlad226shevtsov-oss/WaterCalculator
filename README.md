# Water Calculator

Console application that estimates water consumption, energy usage, and monetary cost for a shower or bath.
The user can choose EUR, USD, or UAH before the calculation.

## Architecture

- `domain` — consumption types, settings, and immutable report data
- `application` — calculation use case
- `config` — loading configurable assumptions from properties
- `ui` — localized console input and output
- `Main` — application composition only

## Run

```shell
mvn test
mvn package
java -cp target/classes watercalculator.Main
```

Russian interface:

```shell
java -cp target/classes watercalculator.Main --lang=ru
```

Custom settings:

```shell
java -cp target/classes watercalculator.Main --config=C:\path\calculator.properties
```

Default assumptions and example tariffs are stored in
`src/main/resources/calculator.properties`. Tariffs are stored in EUR and converted
with the configurable `exchange-rate.*` snapshot. These are configuration examples,
not universal utility prices or live exchange rates.
