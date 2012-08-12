persistit-example
=========

This project is a simple Persistit example project to be used by people interested in exploring the Persistit library. 

What is Persistit?
---------

Let's ask the [project's homepage](http://www.akiban.com/akiban-persistit) for the answer:

> Akiban PersistitTM is an open source transactional Java B+ tree library. Built as the storage layer to Akiban Server, Persistit provides extremely fast, highly concurrent direct data access with ACID transactions. Benchmarks show that Persistit is significantly faster than other B+ tree engines and can even outperform native-code libraries. 

**Translation:** It's like the next generation of Berkeley DB, except it's faster and it's covered under a license that let's you do whatever you want (Eclipse Public License).

What's the License for Persistit?
----------

Eclipse Public License Version 1.0.   This really means, do whatever you want with the tool. Package it into commerical code, distribute it to your heart's delight.   But, if you make modifications to the code, you may be required to make these modifications available under certain circumstances.  The license isn't viral.  You can relax.

*Also, I am not a lawyer.* If you want a better answer, go [here](http://www.eclipse.org/legal/eplfaq.php).

Adding Persistit to a Project
---------

Persistit is available in Maven Central, so adding it to a project couldn't be easier.  Here's the 
dependency XML for your pom.xml if you are using Maven:

    <dependency>
      <groupId>com.akiban</groupId>
      <artifactId>akiban-persistit</artifactId>
      <version>3.1.5</version>
    </dependency>


What does this example do?
======

This example is a very simple example that aims to demonstrate how to:

* Load some data into Persistit
* Use segmented (or composite) keys to facilitate searches

This example uses the freely available Freebase data from Google to construct a single Persistit Tree in a single Persistit Volume.   *Eventually,* I plan to extend this example to parse more than just the person.tsv data, but, right now, this example delivers something simple: a working example of a project that uses Persistit.


How to run this example
=======================
Many the best place to start is how am I running it?   I'm running Java 7 on OSX Mountain Lion, but you should be able to run this example under anything that can run Java 6 or Java 7.    I also have about 1 GB of free memory to run this example.  If you run this application you should also have 10 minutes to waste: the example downloads a 160 MB file from Google.    The internet is very big and you should be able to waste at last 10 minutes looking at [Reddit](http://www.reddit.com).

I run this example by:

1. Importing a Maven project into [Eclipse IDE for Java Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/junor)
2. Running com.discursive.persistit.[SimpleExample](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SimpleExample.java), and making sure to pass in the "-Xmx1G" JVM option to give the heap a Gig of memory to play around in.

**How could you run this example?**   I could probably make it easier to people to run this example by generating the appropriate Maven assembly along with some shell scripts and whatnot, but I haven't done that.   Care to do that?  Go fork yourself, I'd appreciate the help.


How to comprehend this example
==============================

While classes like [DownloadDataset](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/DownloadDataset.java), [ParseData](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/ParseData.java), [SearchByName](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchByName.java), and [SearchById](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchById.java) are easy to understand, you'll be mystified by this example if you don't understand Spring.   

**How this example uses Spring…**

You should know that I'm not really using Spring in a very "Springy" way.   If you were expecting some XML you won't find it.  I'm just using Spring to scan the classpath for components with @Component annotations.   The magic happens in [SimpleExample](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SimpleExample.java).java, here:

    // Configure Spring Framework
    log.info("Configuring Spring Framework...");
    GenericApplicationContext ctx = new GenericApplicationContext();
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner( ctx );
    scanner.scan( "com.discursive.persistit" );
    ctx.refresh();
    context = ctx;

Fancy, eh?  You'll see that all the component classes like [DownloadDataset](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/DownloadDataset.java), [ParseData](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/ParseData.java), [SearchByName](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchByName.java), and [SearchById](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchById.java) all have @Component annotations at the class level with an @Autowired that points to the [PersistitManager](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/PersistitManager.java) class

**What does this example do?**

This example does a few things, here's an outline of what [SimpleExample](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SimpleExample.java) does:

1. **Initialization**
   * Uses slf4j with log4j so it automatically configures logging from a log4j.properties
   * Scans the classpath for Spring components and configures a Spring ApplicationContext
   * As a by product of initializing Spring components, the [PersistitManager](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/PersistitManager.java) is going to be initialized and a "people" volume will be created using the configuration in persistit.properties.
   * The good stuff all happens in run()
2. **Download Dataset**
   * [SimpleExample](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SimpleExample.java) calls out to [DownloadDataset](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/DownloadDataset.java)
   * This component downloads person.tsv from Freebase: [here](http://download.freebase.com/datadumps/2012-08-09/browse/people/)
   * Note that this file is 160 MB, depending on your connection to the internet this could take a minute or a few more.
3. **Parse Data**
   * The [ParseData](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/ParseData.java) parses the data and loads it into the person tree in the people volume.
   * Since this example is demonstrating the ability to search and retrieve by a person's name and by an identifier, both of these fields are used to create a composite key.
   * If your memory settings aren't set correctly, you'll run out of heap, but this load process takes about 40 seconds (on my laptop at least).   **Did you catch that? it takes about 40 seconds to load about 2.3 million records into a Persistit volume.**
4. **Search by Name**
   * The [SearchByName](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchByName.java) component just searches for a few records that have a name key that starts with a certain combination of keys.
   * In this case, the search term is hard-coded into [SearchByName](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchByName.java) (it was "Will Smith" when I wrote this).
   * You can see that the system locates a matching record in ~600 microseconds.
5. **Search by Identifer**
   * In the Freebase system every concept (every thing) has an identifier.  The [SearchById](https://github.com/tobrien/persistit-example/blob/master/src/main/java/com/discursive/persistit/SearchById.java) component just tries to locate a record by identifier.
   * Now, this is very likely due to some mistake on my part, but the KeyFilter leads with a wildcard on name and selects a single record by id.   The retrieval time in this case is about 1.3 seconds.
   * I'm sure someone is going to tell me I'm doing it wrong in this stage and that there's a much more elegant solution to this problem.