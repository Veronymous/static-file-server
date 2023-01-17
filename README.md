# static-file-server

* Files are loaded into memory.
* Files are watched and updated in the memory on changes.
* Currently only supports json files.

**Files Directory**

`/etc/static-file/server/files`

* The location can also be configured in the application file.

## Build

`mvn package`
Executable - `./target/appassembler/bin/static-file-server-application`;