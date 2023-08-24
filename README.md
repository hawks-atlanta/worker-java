# Worker

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
