package org.example;

import io.gofannon.apl.annotation.TheConf;
import io.gofannon.apl.annotation.TheConfParam;

@TheConf(name = "MyConfZ")
interface MyConfiguration {

    @TheConfParam(
            description = """
                    line 1
                    line 2
                    line 3
                    """,
            defaultValue = "42"
    )
    String THRESHOLD = "MyConfZ.threshold";

    @TheConfParam(
            description = """
                    line 1b
                    line 2b
                    line 3b
                    line 4b
                    """,
            defaultValue = ".properties"
    )
    String FILE_SUFFIX = "MyConfZ.file_suffix";

}
