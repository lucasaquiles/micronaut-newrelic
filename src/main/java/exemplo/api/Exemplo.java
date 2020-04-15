package exemplo.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/exemplo")
public class Exemplo {

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {

        return "E ai, new relic";
    }
}
