shrinkwrap-deltaspike
====================

Introduction
---------------------

The target of this project is to provide a fluent API to describe deployable archives for JavaEE based projects that using DeltaSpike CDI Extension. To reach this goal we created an Utility Class that allows you to write the following deployment descriptor: 

    
	@Deployment
    public static Archive<?> createTestArchive() throws Exception {
        return Deltaspike.create(WebArchive.class).addDataSupport().getDeployment()
                .addClass(ObjectUnderTest.class);
    }
	
Without this library using only Arquillian and Shrinkwrap with Maven resolver you have to write the following deployment descriptor to pack the same deployable:

	@Deployment
    public static Archive<?> createArchive() throws Exception {
        
        return ShrinkWrap.create(WebArchive.class).addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.apache.deltaspike.core:deltaspike-core-api",
                            "org.apache.deltaspike.core:deltaspike-core-impl",
                            "org.apache.deltaspike.modules:deltaspike-jpa-module-api",
                            "org.apache.deltaspike.modules:deltaspike-jpa-module-impl",
                            "org.apache.deltaspike.modules:deltaspike-data-module-api",
                            "org.apache.deltaspike.modules:deltaspike-data-module-impl",
                            "org.apache.deltaspike.modules:deltaspike-partial-bean-module-api",
                            "org.apache.deltaspike.modules:deltaspike-partial-bean-module-impl")
                .withoutTransitivity().asFile())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider("org.apache.deltaspike.core.spi.config.ConfigSourceProvider")
                .addClass(ObjectUnderTest.class);
    }


Usage
---------------------

To use this extension simply place the jar file into your projects classpath an your ready to start. Alternatively when you using Maven as build tool add the following dependency to your pom.xml:

    <dependency>
        <groupId>de.mszturc</groupId>
        <artifactId>shrinkwrap-deltaspike</artifactId>
        <version>1.0</version>
		<scope>test</scope>
        <type>jar</type>
    </dependency>
    

	
Technical Notes
---------------------

For the creation of the project I used the following tools & plugins:

- Java JDK 1.8.0_25-b18 x64
- Maven 3.1.1

The project was test in the following environment:

- Windows 7 x64
- Java JDK 1.8.0_25-b18 x64
- Arquillian 1.1.5
- Shrinkwrap 2.1.1
- Maven 3.1.1

On following Application Servers:

- Glassfish 4.1
- Wildfly 8.2
- TomEE 1.7.1