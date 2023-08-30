# Worker

[![codecov](https://codecov.io/gh/hawks-atlanta/worker-java/graph/badge.svg?token=FRKGQKPP2Q)](https://codecov.io/gh/hawks-atlanta/worker-java)
[![Coverage](https://github.com/hawks-atlanta/worker-java/actions/workflows/coverage.yml/badge.svg)](https://github.com/hawks-atlanta/worker-java/actions/workflows/coverage.yml)
[![Release](https://github.com/hawks-atlanta/worker-java/actions/workflows/release.yaml/badge.svg)](https://github.com/hawks-atlanta/worker-java/actions/workflows/release.yaml)
[![Tagging](https://github.com/hawks-atlanta/worker-java/actions/workflows/tagging.yaml/badge.svg)](https://github.com/hawks-atlanta/worker-java/actions/workflows/tagging.yaml)
[![Test](https://github.com/hawks-atlanta/worker-java/actions/workflows/testing.yml/badge.svg)](https://github.com/hawks-atlanta/worker-java/actions/workflows/testing.yml)

## Development

### Submodules

Fetch submodules after cloning:

```sh
git clone https://github.com/hawks-atlanta/worker-java
git submodule update --init
```

### Tools

- Have `jdk11` or newer installed.
- (Optional) Use the **gradle wrapper script** (`./gradlew`) for all `gradle` commands. For example:

    ```sh
    ./gradlew build
    ```

- (Optional) Use the provided `nix-shell` to get into a shell with all required dependecies [[install Nix](https://nixos.org/download)].

    ```sh
    nix-shell
    ```

### Run

```sh
gradle run
```

### Run tests

```sh
gradle test
```

### Format

You need to have `clang-format` installed.

```sh
./format.sh clang-check # check (doesn't write)
./format.sh clang-format # apply (writes)
```

## Coverage

|![coverage](https://codecov.io/gh/hawks-atlanta/worker-java/graphs/sunburst.svg?token=FRKGQKPP2Q)|![coverage](https://codecov.io/gh/hawks-atlanta/worker-java/graphs/tree.svg?token=FRKGQKPP2Q)|
|---|---|
