Graylog2 Migrator
=================

A tool to migrate data from a pre-0.20.x Graylog2 version to a new instance. It only supports streams and streamrules
for now.

## Installation

Download the JAR file from the Releases page or build with `mvn package`.

## Usage

Use the `-h` flag to show the available command line options.

```
$ java -jar graylog2-migrator-0.20.0.jar -h
  Options:
        --creator
       Value for the creator_user_id field
       Default: admin
  *     --from-db
       Source database URI (Example: mongodb://localhost/graylog2old)
        --from-version
       Graylog2 version of the source database
       Default: 0.12.0
    -h, --help
       Show usage information and exit
       Default: false
  *     --to-db
       Target database URI (Example: mongodb://localhost/graylog2new
```

### Command Line Options

* `--from-version` -- The Graylog2 version of the source database.
* `--from-db` -- The Graylog2 source database URI. This is the database of
  your **old** Graylog2 instance.
* `--to-db` -- The Graylog2 source database URI. This is the database of
  your **new** Graylog2 instance.
* `--creator` -- Use the given user id as a creator of the migrated streams.

### Example

```
$ java -jar target/graylog2-migrator-0.20.0-SNAPSHOT.jar --from-db mongodb://localhost/graylog2old --to-db mongodb://localhost/graylog2
2014-05-14 17:34:42,719 INFO : Migrating data from version 0.12.0
2014-05-14 17:34:42,722 INFO : Connecting to mongodb://localhost:27017/graylog2old
2014-05-14 17:34:42,808 INFO : Connecting to mongodb://localhost:27017/graylog2
2014-05-14 17:34:42,878 INFO : Migrating Stream object 5370cea00e77760892000009 (Oldstream)
2014-05-14 17:34:42,898 INFO : Migrating Stream rule object 5370cea30e7776089200000b
2014-05-14 17:34:42,900 INFO : Migrating Stream rule object 53721e280e777608a4000003
2014-05-14 17:34:42,902 INFO : Migrating Stream rule object 537226be0e77760b0a000003
2014-05-14 17:34:42,903 WARN : Unsupported stream rule field: Stream rule type FILENAME_LINE not supported
2014-05-14 17:34:42,903 INFO : Migrating Stream rule object 53722e890e77760f5c000005
2014-05-14 17:34:42,904 INFO : DONE
$
```
