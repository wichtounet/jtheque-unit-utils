# JTheque Unit Utils #

A simple utility library to write unit test for file operations and for databases operations.

## Building ##

You need a Java 6 (or newer) environment and Maven 2.0.9 (or newer) installed.

You should now be able to do a full build of `jtheque-unit-utils`:

    $ git clone git://github.com/wichtounet/jtheque-unit-utils.git
    $ cd jtheque-unit-utils
    $ mvn clean install

To use this library in your projects, add the following to the `dependencies` section of your `pom.xml`:

    <dependency>
       <groupId>org.jtheque</groupId>
       <artifactId>org.jtheque.unit.utils</artifactId>
       <version>1.0</version>
       <scope>test</scope>
    </dependency>

## Troubleshooting ##

Please consider using [Github issues tracker](http://github.com/wichtounet/jtheque-unit-utils/issues) to submit bug reports or feature requests.

## License ##

See `LICENSE` for details.