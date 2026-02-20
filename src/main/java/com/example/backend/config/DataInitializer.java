package com.example.backend.config;

import com.example.backend.models.Category;
import com.example.backend.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {

            Category cat1 = new Category(null, "Desenvolvimento Web", null);
            Category cat2 = new Category(null, "Design Gráfico", null);
            Category cat3 = new Category(null, "Marketing Digital", null);
            Category cat4 = new Category(null, "Edição de Vídeo", null);

            categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4));

            System.out.println("Categorias iniciais inseridas no banco com sucesso!");
        }
    }
}