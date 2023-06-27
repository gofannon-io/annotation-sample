package io.gofannon.apl;

import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import java.util.Arrays;
import java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationProcessorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    @DisplayName("check that the processor is called")
//    void check_processor_is_called() {
//        final JavaFileObject input = JavaFileObjects.forSourceString(
//                "com.example.ConfX",
//                """
//                        package com.example;
//
//                        import io.gofannon.apl.annotation.TheConf;
//
//                        @TheConf(name="confx")
//                        public interface ConfX {
//                        }
//
//                        """
//        );
//
//        Truth.assert_()
//                .about(JavaSourcesSubjectFactory.javaSources())
//                .that(Collections.singleton(input))
//                .processedWith(new ConfigurationProcessor())
//                .failsToCompile()
//                .withErrorContaining("Not yet implemented");
//    }

    @Test
    @DisplayName("shall accept @TheConf with name() argument on interface")
    void acceptConfWithNameOnInterface() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
                "com.example.ConfX",
                """
                        package com.example;
                                                
                        import io.gofannon.apl.annotation.TheConf;
                                        
                        @TheConf(name="confx")
                        public interface ConfX {
                        }
                                                
                        """
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singleton(input))
                .processedWith(new ConfigurationProcessor())
                .compilesWithoutError();
    }


    @Test
    @DisplayName("shall not accept @TheConf on class with name() argument")
    void notAcceptConfWithNameOnClass() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
                "com.example.ConfX",
                """
                        package com.example;
                                                
                        import io.gofannon.apl.annotation.TheConf;
                                        
                        @TheConf(name="confx")
                        public class ConfX {
                        }
                                                
                        """
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singleton(input))
                .processedWith(new ConfigurationProcessor())
                .failsToCompile()
                .withErrorContaining("Invalid type");
    }


    @Test
    @DisplayName("shall not accept @TheConf on interface without name() argument")
    void notAcceptConfWithoutNameOnInterface() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
                "com.example.ConfX",
                """
                        package com.example;
                                                
                        import io.gofannon.apl.annotation.TheConf;
                                        
                        @TheConf(name="")
                        public interface ConfX {
                        }
                                                
                        """
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singleton(input))
                .processedWith(new ConfigurationProcessor())
                .failsToCompile()
                .withErrorContaining("Missing name ");
    }


    @Test
    @DisplayName("shall accept @TheConfParam on String field")
    void acceptConfParamOnInterface() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
                "com.example.ConfX",
                """
                        package com.example;
                                                
                        import io.gofannon.apl.annotation.TheConf;
                        import io.gofannon.apl.annotation.TheConfParam;
                                        
                        @TheConf(name="confX")
                        public interface ConfX {
                            @TheConfParam(  defaultValue="42",
                                            description="The Description"
                                         )
                            String THRESHOLD = "confX.threshold";
                        }
                        """
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singleton(input))
                .processedWith(new ConfigurationProcessor())
                .compilesWithoutError();
    }


    @Test
    @DisplayName("shall accept @TheConfParam on String field")
    void notAcceptConfParamOnInterface() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
                "com.example.ConfX",
                """
                        package com.example;
                                                
                        import io.gofannon.apl.annotation.TheConf;
                        import io.gofannon.apl.annotation.TheConfParam;
                                        
                        @TheConf(name="confX")
                        public interface ConfX {
                            @TheConfParam(  defaultValue="42",
                                            description="The Description"
                                         )
                            int THRESHOLD = 42;
                        }
                        """
        );

        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singleton(input))
                .processedWith(new ConfigurationProcessor())
                .failsToCompile()
                .withErrorContaining("Invalid field type");
    }


    @Test
    @DisplayName("shall process a valid configuration")
    void full() {
        final JavaFileObject input = JavaFileObjects.forSourceString(
                "com.example.ConfX",
                """
                        package com.example;
                                                
                        import io.gofannon.apl.annotation.TheConf;
                        import io.gofannon.apl.annotation.TheConfParam;
                                        
                        @TheConf(name="confX")
                        public interface ConfX {
                            @TheConfParam(  defaultValue="42",
                                            description="The Description"
                                         )
                            String THRESHOLD = "confX.threshold";
                            
                            @TheConfParam( defaultValue="Lille", description="the name of a city")
                            String LOCATION = "confX.location";
                        }
                        """
        );

        ConfigurationProcessor processor = new ConfigurationProcessor();
        Truth.assert_()
                .about(JavaSourcesSubjectFactory.javaSources())
                .that(Collections.singleton(input))
                .processedWith(processor)
                .compilesWithoutError();

        assertThat(processor.getParameterList())
                .containsExactly(
                        new Parameter("confX", "confX.threshold", "42", "The Description"),
                        new Parameter("confX", "confX.location", "Lille", "the name of a city")
                );
    }

}