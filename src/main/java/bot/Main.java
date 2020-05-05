package bot;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class Main {

    public static void main(String[] args) {
        new SpringApplicationBuilder().main(Main.class).sources(Configuration.class).run(args);
    }

}