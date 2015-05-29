package de.mszturc.shrinkwrap.deltaspike.integration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ManifestContainer;
import org.jboss.shrinkwrap.api.container.ServiceProviderContainer;
import org.jboss.shrinkwrap.api.container.WebContainer;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

/**
 * Author: MSzturc
 * Date:   27.05.2015 
 */
public class Deltaspike<T extends Archive> {

    public static <T extends Archive> Deltaspike<T> create(final Class<T> type) {
        return new Deltaspike(type);
    }

    public static <T extends Archive> Deltaspike<T> create() {
        return new Deltaspike(WebArchive.class);
    }

    private final Class<T> type;

    private final boolean core;

    private boolean beanValidation;

    private boolean containerControl;

    private boolean data;

    private boolean jpa;

    private boolean jsf;

    private boolean partialBean;

    private boolean scheduler;

    private boolean security;

    private boolean servlet;

    private boolean testControl;

    public Deltaspike(final Class<T> type) {
        this.type = type;
        this.core = true;
        this.beanValidation = false;
        this.containerControl = false;
        this.data = false;
        this.jpa = false;
        this.jsf = false;
        this.partialBean = false;
        this.scheduler = false;
        this.security = false;
        this.servlet = false;
        this.testControl = false;
    }

    public Deltaspike<T> addBeanValidationSupport() {
        this.beanValidation = true;
        return this;
    }

    public Deltaspike<T> addDataSupport() {
        this.jpa = true;
        this.partialBean = true;
        this.data = true;
        return this;
    }

    public Deltaspike<T> addJPASupport() {
        this.jpa = true;
        return this;
    }

    public Deltaspike<T> addJSFSupport() {
        this.security = true;
        this.jsf = true;
        return this;
    }

    public Deltaspike<T> addPartialBeanSupport() {
        this.partialBean = true;
        return this;
    }

    public Deltaspike<T> addSchedulerSupport() {
        this.containerControl = true;
        this.scheduler = true;
        return this;
    }

    public Deltaspike<T> addSecuritySupport() {
        this.security = true;
        return this;
    }

    public Deltaspike<T> addServletSupport() {
        this.servlet = true;
        return this;
    }

    public Deltaspike<T> addTestControlSupport() {
        this.containerControl = true;
        this.testControl = true;
        return this;
    }

    public T getDeployment() {

        T target = ShrinkWrap.create(type);
        
        //Workaround: JavaArchive implementation JavaArchiveImpl implements 
        //            LibraryContainer but throws UnsupportedOperationException 
        //            on addAsLibraries method call
        if (target instanceof LibraryContainer && !(target instanceof JavaArchive)) {
                LibraryContainer container = (LibraryContainer) target;
                container.addAsLibraries(collectDeltaspikeDependencies());
        }

        if (target instanceof ServiceProviderContainer) {
            ServiceProviderContainer container = (ServiceProviderContainer) target;
            container.addAsServiceProvider("org.apache.deltaspike.core.spi.config.ConfigSourceProvider");
        }

        if (target instanceof WebContainer) {
            WebContainer container = (WebContainer) target;
            container.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        } else if (target instanceof ManifestContainer) {
            ManifestContainer container = (ManifestContainer) target;
            container.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        }

        return target;
    }

    public Archive<?> enrich(Archive<?> archive) {
        return archive.merge(getDeployment());
    }

    private File[] collectDeltaspikeDependencies() {
        List<File> dependencies = new ArrayList<>();

        PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");

        if (core) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.core:deltaspike-core-api",
                            "org.apache.deltaspike.core:deltaspike-core-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (beanValidation) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-bean-validation-module-api",
                            "org.apache.deltaspike.modules:deltaspike-bean-validation-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (containerControl) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.cdictrl:deltaspike-cdictrl-api")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (data) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-data-module-api",
                            "org.apache.deltaspike.modules:deltaspike-data-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (jpa) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-jpa-module-api",
                            "org.apache.deltaspike.modules:deltaspike-jpa-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (jsf) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-jsf-module-api",
                            "org.apache.deltaspike.modules:deltaspike-jsf-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (partialBean) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-partial-bean-module-api",
                            "org.apache.deltaspike.modules:deltaspike-partial-bean-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (scheduler) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-scheduler-module-api",
                            "org.apache.deltaspike.modules:deltaspike-scheduler-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (security) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-security-module-api",
                            "org.apache.deltaspike.modules:deltaspike-security-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (servlet) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-servlet-module-api",
                            "org.apache.deltaspike.modules:deltaspike-servlet-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }

        if (testControl) {
            File[] modules = pom
                    .resolve("org.apache.deltaspike.modules:deltaspike-test-control-module-api",
                            "org.apache.deltaspike.modules:deltaspike-test-control-module-impl")
                    .withoutTransitivity().asFile();
            dependencies.addAll(Arrays.asList(modules));
        }
        return dependencies.toArray(new File[0]);
    }
}
