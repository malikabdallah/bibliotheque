package com.bookstore.projet;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {


    /*
         @Override
         public void addViewControllers(ViewControllerRegistry registry) {
          registry.addViewController("/").setViewName("home");
         }
    */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/media/**")
                .addResourceLocations("file:/C:/Users/ilyas/Pictures/bibliotheque/src/main/resources/static/media/");
        registry.addResourceHandler("/etudiant/**").addResourceLocations("file:/C:/Users/ilyas/Pictures/bibliotheque/src/main/resources/static/etudiant/");

        registry.addResourceHandler("/livre/**").addResourceLocations("file:/C:/Users/ilyas/Pictures/bibliotheque/src/main/resources/static/livre/");

        registry.addResourceHandler("/adminmedia/**").addResourceLocations("file:/C:/Users/ilyas/Pictures/bibliotheque/src/main/resources/static/adminmedia/");
    }
}
